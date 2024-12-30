package com.health_care.user_service.service;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.request.ApproveRejectRequest;
import com.health_care.user_service.domain.request.RegistrationRequestTemp;
import com.health_care.user_service.domain.response.AdminCheckerMackerResponse;
import com.health_care.user_service.domain.response.TempDataResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.MissingRequestValueException;

public interface IAdminCheckerMacker {
    ApiResponse<AdminCheckerMackerResponse> saveTemp(RegistrationRequestTemp temp) throws MissingRequestValueException;

    ApiResponse<Page<TempDataResponse>> getTempData(String featureCode, String requestId, String startDate, String endDate, Boolean operationType, int page, int size);

    void requestCheck(ApproveRejectRequest request);
}
