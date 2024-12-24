package com.health_care.user_service.service.Impl;


import com.health_care.id.generator.Impl.UniqueIdGeneratorImpl;
import com.health_care.user_service.common.exceptions.AlreadyExistsException;
import com.health_care.user_service.common.exceptions.InvalidRequestDataException;
import com.health_care.user_service.common.exceptions.RequestCanNotProcessException;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.TempData;
import com.health_care.user_service.domain.enums.ApiResponseCode;
import com.health_care.user_service.domain.enums.GlobalFeatureCode;
import com.health_care.user_service.domain.enums.OperationType;
import com.health_care.user_service.domain.enums.ResponseMessage;
import com.health_care.user_service.domain.enums.ResponseStatusType;
import com.health_care.user_service.domain.request.RegistrationRequestTemp;
import com.health_care.user_service.domain.response.AdminCheckerMackerResponse;
import com.health_care.user_service.domain.response.TempDataResponse;
import com.health_care.user_service.repository.TempDataRepository;
import com.health_care.user_service.repository.specification.TempDataSpecifications;
import com.health_care.user_service.service.BaseService;
import com.health_care.user_service.service.IAdminCheckerMacker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingRequestValueException;

@Service
@RequiredArgsConstructor
public class AdminCheckerMackerImpl extends BaseService implements IAdminCheckerMacker {

    private final TempDataRepository tempDataRepository;
    private final UniqueIdGeneratorImpl uniqueIdGenerator;

    @Value("${unique.id.checker.macker}")
    private String checkerMacker;

    @Value("${unique.id.appointment.prefix}")
    private String appointmentPrefix;

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
            String featureCode, String requestId, String startDate, String endDate, Boolean operationType, int page, int size) {

        ApiResponse<Page<TempDataResponse>> response = new ApiResponse<>();

        Specification<TempData> spec = TempDataSpecifications.buildSpecification(featureCode, requestId, startDate, endDate, operationType, getUserIdentity(), Boolean.TRUE);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<TempData> tempDataPage = tempDataRepository.findAll(spec, pageable);

        if(tempDataPage.isEmpty()) {
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
                : checkerMacker;
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
