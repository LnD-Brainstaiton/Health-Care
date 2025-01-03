package com.health_care.user_service.controller;
import com.health_care.user_service.common.utils.AppUtils;
import com.health_care.user_service.common.utils.ResponseUtils;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.enums.ApiResponseCode;
import com.health_care.user_service.domain.enums.ResponseMessage;
import com.health_care.user_service.domain.request.ApproveRejectRequest;
import com.health_care.user_service.domain.request.RegistrationRequestTemp;
import com.health_care.user_service.domain.response.AdminCheckerMackerResponse;
import com.health_care.user_service.domain.response.TempDataResponse;
import com.health_care.user_service.service.IAdminCheckerMacker;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@AllArgsConstructor
public class AdminCheckerMackerResource {

    private final IAdminCheckerMacker iAdminCheckerMacker;

    @PostMapping("/admin/temp/request")
    public ApiResponse<AdminCheckerMackerResponse> saveTemp(@RequestBody RegistrationRequestTemp temp) throws MissingRequestValueException {
        ApiResponse<AdminCheckerMackerResponse> response = iAdminCheckerMacker.saveTemp(temp);
        return response;
    }

    @PostMapping("admin/request/check")
    public ApiResponse<Void> requestCheck(@RequestBody ApproveRejectRequest request){
        iAdminCheckerMacker.requestCheck(request);
        return ResponseUtils.createResponseObject(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode(), ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage());
    }

    @GetMapping("/admin/tempdata")
    public ApiResponse<Page<TempDataResponse>> getTempData(
            @RequestParam(required = false) String featureCode,
            @RequestParam(required = false) String requestId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Boolean operationType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return iAdminCheckerMacker.getTempData(featureCode, requestId, startDate, endDate, operationType, page, size);
    }

}
