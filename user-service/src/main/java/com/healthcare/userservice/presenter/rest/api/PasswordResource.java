package com.healthcare.userservice.presenter.rest.api;

import com.healthcare.userservice.common.utils.AppUtils;
import com.healthcare.userservice.common.utils.ResponseUtils;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.request.ChangePasswordRequest;
import com.healthcare.userservice.service.IPasswordService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@AllArgsConstructor
public class PasswordResource {

    private final IPasswordService passwordService;

    @PostMapping("/change/password")
    ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest request){
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, passwordService.changePassword(request));
    }
}
