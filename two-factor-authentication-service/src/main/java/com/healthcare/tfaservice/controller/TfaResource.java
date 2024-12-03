package com.healthcare.tfaservice.controller;

import com.healthcare.tfaservice.common.utils.AppUtils;
import com.healthcare.tfaservice.common.utils.ResponseUtils;
import com.healthcare.tfaservice.domain.common.ApiResponse;
import com.healthcare.tfaservice.domain.enums.ResponseMessage;
import com.healthcare.tfaservice.domain.request.TfaRequest;
import com.healthcare.tfaservice.domain.response.TfaResponse;
import com.healthcare.tfaservice.service.interfaces.ITfaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping(AppUtils.BASE_URL)
public class TfaResource {

    @Autowired
    ITfaService iTfaService;

    @PostMapping("/generate-otp")
    public ApiResponse<TfaResponse> generateOtp(@RequestBody TfaRequest tfaRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, iTfaService.generateOtp(tfaRequest));
    }
}
