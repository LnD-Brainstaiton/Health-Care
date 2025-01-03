package com.healthcare.userservice.presenter.rest.external;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.presenter.rest.event.NotificationEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "NOTIFICATION-SERVICE", path = "/api/v1/notification")
public interface NotificationFeignClient {
    @PostMapping("/send-notification")
    ApiResponse<Boolean> sendNotification(NotificationEvent notificationEvent);
}
