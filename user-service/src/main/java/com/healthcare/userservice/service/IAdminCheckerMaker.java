package com.healthcare.userservice.service;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.request.ApproveRejectRequest;
import com.healthcare.userservice.domain.request.RegistrationRequestTemp;
import com.healthcare.userservice.domain.response.AdminCheckerMackerResponse;
import com.healthcare.userservice.domain.response.TempDataResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.MissingRequestValueException;

public interface IAdminCheckerMaker {

    ApiResponse<AdminCheckerMackerResponse> saveTemp(RegistrationRequestTemp temp) throws MissingRequestValueException;

    ApiResponse<Page<TempDataResponse>> getTempData(String featureCode, String requestId, String checkerResponse, String startDate, String endDate, Boolean operationType, int page, int size);

    void requestCheck(ApproveRejectRequest request);

    void close(String requestId);
}
