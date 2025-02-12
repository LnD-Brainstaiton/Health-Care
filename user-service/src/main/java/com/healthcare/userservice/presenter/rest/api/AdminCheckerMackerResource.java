package com.healthcare.userservice.presenter.rest.api;

import com.healthcare.userservice.common.utils.AppUtils;
import com.healthcare.userservice.common.utils.ResponseUtils;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.request.ApproveRejectRequest;
import com.healthcare.userservice.domain.request.RegistrationRequestTemp;
import com.healthcare.userservice.domain.response.AdminCheckerMackerResponse;
import com.healthcare.userservice.domain.response.TempDataResponse;
import com.healthcare.userservice.service.IAdminCheckerMaker;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@AllArgsConstructor
public class AdminCheckerMackerResource {

    private final IAdminCheckerMaker iAdminCheckerMacker;

    @PostMapping("/admin/temp/request")
    public ApiResponse<AdminCheckerMackerResponse> saveTemp(@RequestBody RegistrationRequestTemp temp) throws MissingRequestValueException {
        return iAdminCheckerMacker.saveTemp(temp);
    }

    @PostMapping("admin/request/check")
    public ApiResponse<Void> requestCheck(@RequestBody ApproveRejectRequest request) {
        iAdminCheckerMacker.requestCheck(request);
        return ResponseUtils.createResponseObject(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode(), ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage());
    }

    @GetMapping("/admin/tempdata")
    public ApiResponse<Page<TempDataResponse>> getTempData(
            @RequestParam(required = false) String featureCode,
            @RequestParam(required = false) String requestId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String checkerResponse,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Boolean operationType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return iAdminCheckerMacker.getTempData(featureCode, requestId, checkerResponse, startDate, endDate, operationType, page, size);
    }

    @PostMapping("admin/close/request")
    public ApiResponse<Void> close(@RequestParam String requestId){
        iAdminCheckerMacker.close(requestId);
        return ResponseUtils.createResponseObject(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode(), ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage());
    }
}
