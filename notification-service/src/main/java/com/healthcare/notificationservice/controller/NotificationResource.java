package com.healthcare.notificationservice.controller;

import com.healthcare.notificationservice.common.utils.AppUtils;
import com.healthcare.notificationservice.common.utils.ResponseUtils;
import com.healthcare.notificationservice.domain.common.ApiResponse;
import com.healthcare.notificationservice.domain.dto.ReceiverDto;
import com.healthcare.notificationservice.domain.enums.ResponseMessage;
import com.healthcare.notificationservice.service.interfaces.INotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppUtils.BASE_URL)
public class NotificationResource {

    @Autowired
    INotificationService notificationService;

    @PostMapping("/send-notification")
    public ApiResponse<Boolean> generateOtp(@Valid @RequestBody ReceiverDto receiverDto) {
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, notificationService.sendNotification());
    }

}
