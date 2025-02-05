package com.healthcare.userservice.service.Impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.health_care.id.generator.Impl.UniqueIdGeneratorImpl;
import com.healthcare.userservice.common.exceptions.AlreadyExistsException;
import com.healthcare.userservice.common.exceptions.InvalidRequestDataException;
import com.healthcare.userservice.common.exceptions.RecordNotFoundException;
import com.healthcare.userservice.common.exceptions.RequestCanNotProcessException;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.entity.TempData;
import com.healthcare.userservice.domain.enums.*;
import com.healthcare.userservice.domain.request.ApproveRejectRequest;
import com.healthcare.userservice.domain.request.RegistrationRequestTemp;
import com.healthcare.userservice.domain.response.AdminCheckerMackerResponse;
import com.healthcare.userservice.domain.response.TempDataResponse;
import com.healthcare.userservice.repository.TempDataRepository;
import com.healthcare.userservice.repository.specification.TempDataSpecifications;
import com.healthcare.userservice.service.BaseService;
import com.healthcare.userservice.service.IAdminCheckerMaker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminCheckerMakerImpl extends BaseService implements IAdminCheckerMaker {

    private final TempDataRepository tempDataRepository;
    private final UniqueIdGeneratorImpl uniqueIdGenerator;

    private static final Logger logger = LoggerFactory.getLogger(AdminCheckerMakerImpl.class);

    @Value("${unique.id.checker.macker}")
    private String checkerMaker;

    @Value("${unique.id.appointment.prefix}")
    private String appointmentPrefix;

    @Value("${host.url}")
    private String host;

    @Override
    public ApiResponse<AdminCheckerMackerResponse> saveTemp(RegistrationRequestTemp temp) throws MissingRequestValueException {
        validateRequest(temp);

        String data = convertDataToString(temp);
        OperationType operationType = OperationType.fromString(temp.getOperationType());

        return switch (operationType) {
            case CREATE -> handleCreateOperation(temp, data);
            case UPDATE -> handleUpdateOperation(temp, data);
            default -> throw new MissingRequestValueException("Invalid operation type: " + temp.getOperationType());
        };
    }

    @Override
    public ApiResponse<Page<TempDataResponse>> getTempData(
            String featureCode, String requestId, String checkerResponse, String startDate, String endDate, Boolean operationType, int page, int size) {

        ApiResponse<Page<TempDataResponse>> response = new ApiResponse<>();

        Specification<TempData> spec = TempDataSpecifications.buildSpecification(featureCode, requestId,checkerResponse, startDate, endDate, operationType, getUserIdentity(), Boolean.TRUE);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<TempData> tempDataPage = tempDataRepository.findAll(spec, pageable);

        if (tempDataPage.isEmpty()) {
            response.setResponseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode());
            response.setResponseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage());
            return response;
        }

        Page<TempDataResponse> tempDataResponsePage = tempDataPage.map(this::mapToTempDataResponse);

        response.setData(tempDataResponsePage);
        response.setResponseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage());
        response.setResponseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode());

        return response;
    }

    @Override
    public void requestCheck(ApproveRejectRequest request) {
        String checkerId = getUserIdentity();

        validateCheckerData(request);

        Optional<TempData> tempData = tempDataRepository.findByRequestIdAndCheckerResponse(request.getRequestId(), ResponseStatusType.PENDING.getCode());
        if (tempData.isEmpty()) {
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND);
        }
        TempData updatedTempData = tempData.get();
        updatedTempData.setUpdatedAt(LocalDateTime.now());
        updatedTempData.setUpdatedBy(checkerId);

        if (request.getStatus().equals(ResponseStatusType.ACCEPTED.getText())) {
            processData(updatedTempData);
            updatedTempData.setCheckerResponse(ResponseStatusType.ACCEPTED.getCode());
            updatedTempData.setIsActive(Boolean.FALSE);
            tempDataRepository.save(updatedTempData);
        } else {
            updatedTempData.setCheckerResponse(ResponseStatusType.REJECTED.getCode());
            updatedTempData.setMessage(request.getMessage());
            updatedTempData.setMakerId(checkerId);
            tempDataRepository.save(updatedTempData);
        }
    }

    @Override
    public void close(String requestId) {

        Optional<TempData> tempData = tempDataRepository.findByRequestIdAndIsActive(requestId, Boolean.TRUE);
        if (tempData.isEmpty()) {
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND);
        }
        TempData updatedTempData= tempData.get();
        updatedTempData.setIsActive(Boolean.FALSE);
        updatedTempData.setUpdatedBy(getUserIdentity());
        updatedTempData.setCheckerId(getUserIdentity());
        tempDataRepository.save(updatedTempData);
    }

    private void processData(TempData tempData) {

        if (getHeaderValueForToken(tokenHeader).isEmpty())
            throw new RequestCanNotProcessException(ResponseMessage.REQUEST_CAN_NOT_PROCESS);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add(tokenHeader, getHeaderValueForToken(tokenHeader).get());

        Object object = null;
        try {
            if (tempData.getData() != null) {
                JsonNode rootNode = objectMapper.readTree(tempData.getData()); // Read JSON as a generic tree
                if (rootNode.isTextual()) {
                    rootNode = objectMapper.readTree(rootNode.asText()); // Parse the inner JSON
                }
                if (rootNode.isObject()) { // Ensure the root node is an object
                    ObjectNode objectNode = (ObjectNode) rootNode;
                    if (GlobalFeatureCode.APPOINTMENT.getText().equals(tempData.getFeatureCode())) {
                        objectNode.put("appointmentNo", tempData.getRequestId());
                    }
                    object = objectMapper.treeToValue(objectNode, Object.class); // Convert to your desired object class
                } else {
                    logger.error("Expected JSON object, but got: {}", rootNode.getNodeType());
                    throw new IllegalArgumentException("Invalid JSON structure: not an object");
                }
            }
        } catch (Exception e) {
            logger.error("Error processing request: {}", e.getMessage(), e);
            throw new RequestCanNotProcessException(ResponseMessage.REQUEST_CAN_NOT_PROCESS);
        }

        HttpEntity<Object> requestEntity = new HttpEntity<>(object, headers);

        restTemplateCall(tempData, HttpMethod.POST, requestEntity);
    }

    private void restTemplateCall(TempData tempData, HttpMethod method, HttpEntity<Object> requestEntity) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ApiResponse> response;

        try {
            String API = host.concat(tempData.getRequestUrl());
            logger.trace("Rest Call API : " + API);
            response = restTemplate.exchange(API, method, requestEntity, ApiResponse.class);
            logger.trace("Rest Call Response message :" + response.getBody().getResponseMessage());
        } catch (HttpClientErrorException ex) {
            logger.error(ex.getMessage());
            logger.error("Rest Root Cause", ex);
            throw new RequestCanNotProcessException(ResponseMessage.REQUEST_CAN_NOT_PROCESS);
        }
    }

    private void validateCheckerData(ApproveRejectRequest request) {
        if (Objects.isNull(request)
                || request.getFeatureCode() == null
                || request.getStatus() == null
                || request.getRequestId() == null) {
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
    }

    private TempDataResponse mapToTempDataResponse(TempData tempData) {
        return TempDataResponse.builder()
                .featureCode(tempData.getFeatureCode())
                .operationType(tempData.getOperationType())
                .message(tempData.getMessage())
                .requestUrl(tempData.getRequestUrl())
                .data(tempData.getData())
                .requestId(tempData.getRequestId())
                .status(tempData.getCheckerResponse().equals(ResponseStatusType.PENDING.getCode()) ? ResponseStatusType.PENDING.getText() : ResponseStatusType.REJECTED.getText())
                .build();
    }

    private void validateRequest(RegistrationRequestTemp temp) {
        if (temp == null || temp.getFeatureCode() == null || temp.getOperationType() == null) {
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
    }

    private String convertDataToString(RegistrationRequestTemp temp) {
        try {
            return objectMapper.writeValueAsString(temp.getData());
        } catch (Exception e) {
            throw new RequestCanNotProcessException(ResponseMessage.REQUEST_CAN_NOT_PROCESS);
        }
    }

    private ApiResponse<AdminCheckerMackerResponse> handleCreateOperation(RegistrationRequestTemp temp, String data) {
        checkDuplicateRequest(temp, data);

        TempData tempData = buildTempData(temp, data, OperationType.CREATE.getText());
        assignRequestId(temp, tempData);

        tempDataRepository.save(tempData);

        return buildApiResponse(tempData, OperationType.CREATE);
    }

    private ApiResponse<AdminCheckerMackerResponse> handleUpdateOperation(RegistrationRequestTemp temp, String data) {
        TempData existingData = checkRequestForUpdate(temp);

        existingData.setMakerId(getUserIdentity());
        existingData.setData(data);
        existingData.setCheckerResponse(ResponseStatusType.PENDING.getCode());
        existingData.setMessage(temp.getMessage());

        tempDataRepository.save(existingData);

        return buildApiResponse(existingData, OperationType.UPDATE);
    }

    private TempData buildTempData(RegistrationRequestTemp temp, String data, String operationType) {
        return TempData.builder()
                .featureCode(temp.getFeatureCode())
                .requestUrl(temp.getRequestUrl())
                .message(temp.getMessage())
                .data(data)
                .operationType(operationType)
                .makerId(getUserIdentity())
                .checkerResponse(ResponseStatusType.PENDING.getCode())
                .isActive(Boolean.TRUE)
                .build();
    }

    private void assignRequestId(RegistrationRequestTemp temp, TempData tempData) {
        String prefix = temp.getFeatureCode().equals(GlobalFeatureCode.APPOINTMENT.getText())
                ? appointmentPrefix
                : checkerMaker;
        tempData.setRequestId(uniqueIdGenerator.generateUniqueIdWithPrefix(prefix));
    }

    private void checkDuplicateRequest(RegistrationRequestTemp temp, String data) {
        boolean exists = tempDataRepository.existsByFeatureCodeAndDataAndCheckerResponseAndOperationTypeAndRequestUrl(
                temp.getFeatureCode(),
                data,
                ResponseStatusType.PENDING.getCode(),
                temp.getOperationType(),
                temp.getRequestUrl()
        );
        if (exists) {
            throw new AlreadyExistsException(ResponseMessage.ALREADY_REQUEST_PENDING);
        }
    }

    private TempData checkRequestForUpdate(RegistrationRequestTemp temp) {
        return tempDataRepository.findTopByRequestIdAndIsActive(temp.getRequestId(), Boolean.TRUE)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA));
    }

    private ApiResponse<AdminCheckerMackerResponse> buildApiResponse(TempData tempData, OperationType operationType) {
        AdminCheckerMackerResponse response = AdminCheckerMackerResponse.builder()
                .operationType(operationType.getText())
                .requestId(tempData.getRequestId())
                .featureCode(tempData.getFeatureCode())
                .build();

        return ApiResponse.<AdminCheckerMackerResponse>builder()
                .data(response)
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }
}
