package com.health_care.user_service.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.health_care.id.generator.Impl.UniqueIdGeneratorImpl;
import com.health_care.user_service.common.exceptions.AlreadyExistsException;
import com.health_care.user_service.common.exceptions.RequestCanNotProcessException;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.Notification;
import com.health_care.user_service.domain.enums.ApiResponseCode;
import com.health_care.user_service.domain.enums.OperationType;
import com.health_care.user_service.domain.enums.ResponseMessage;
import com.health_care.user_service.domain.enums.ResponseStatusType;
import com.health_care.user_service.domain.request.RegistrationRequestTemp;
import com.health_care.user_service.domain.response.AdminCheckerMackerResponse;
import com.health_care.user_service.repository.NotificationRepository;
import com.health_care.user_service.service.BaseService;
import com.health_care.user_service.service.IAdminCheckerMacker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingRequestValueException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminCheckerMackerImpl extends BaseService implements IAdminCheckerMacker {

    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;
    private final UniqueIdGeneratorImpl uniqueIdGenerator;

    @Value("${url.create.admin}")
    private String createAdminUrl;

    @Value("${url.update.admin}")
    private String updateAdminUrl;

    @Value("${url.create.doctor}")
    private String createDoctorUrl;

    @Value("${url.update.doctor}")
    private String updateDoctorUrl;

    @Value("${unique.id.checker.macker}")
    private String checkerMacker;

    @Override
    public ApiResponse<AdminCheckerMackerResponse> saveTemp(RegistrationRequestTemp temp) throws MissingRequestValueException {

        ApiResponse<AdminCheckerMackerResponse> response = new ApiResponse<>();
        AdminCheckerMackerResponse tempResponse = new AdminCheckerMackerResponse();

        if(temp.getOperationType().equals(OperationType.CREATE.getText())){
            String data = "";
            try {
                data = objectMapper.writeValueAsString(temp.getData());
            } catch (Exception e) {
               throw new RequestCanNotProcessException(ResponseMessage.REQUEST_CAN_NOT_PROCESS);
            }

            checkDuplicateRequest(temp, data);

            Notification notification = Notification.builder()
                    .featureCode(temp.getFeatureCode())
                    .requestId(uniqueIdGenerator.generateUniqueIdWithPrefix(checkerMacker))
                    .requestUrl(temp.getRequestUrl())
                    .message(temp.getMessage())
                    .data(data)
                    .operationType(OperationType.CREATE.getText())
                    .makerId(getCurrentUserContext().getUserId())
                    .checkerResponse(ResponseStatusType.PENDING.getCode())
                    .isActive(Boolean.TRUE)
                    .build();

            notificationRepository.save(notification);

            tempResponse.setOperationType(OperationType.CREATE.getText());
            tempResponse.setRequestId(notification.getRequestId());
            tempResponse.setFeatureCode(temp.getFeatureCode());

            response.setData(tempResponse);
            response.setResponseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode());
            response.setResponseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage());

            return response;

        } else if(temp.getOperationType().equals(OperationType.UPDATE.getText())){

        }else{
            throw new MissingRequestValueException("Invalid operation type");
        }
        return null;
    }

    private void checkDuplicateRequest(RegistrationRequestTemp temp, String data) {
        Optional<Notification> tempData = notificationRepository.findTopByFeatureCodeAndDataAndCheckerResponseAndOperationTypeAndRequestUrl(
                temp.getFeatureCode(),
                data,
                ResponseStatusType.PENDING.getCode(),
                temp.getOperationType(),
                createAdminUrl);

        if(tempData.isPresent()) {
            throw new AlreadyExistsException(ResponseMessage.ALREADY_REQUEST_PENDING);
        }
    }


}
