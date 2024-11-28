package com.health_care.user_service.controller;

import com.health_care.user_service.common.utils.AppUtils;
import com.health_care.user_service.common.utils.ResponseUtils;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.dto.Demo;
import com.health_care.user_service.domain.enums.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppUtils.BASE_URL)
public class DemoResource {

    @GetMapping("/demo")
    public ApiResponse<Demo> getDemo(){

        //throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, new Demo());
    }
}
