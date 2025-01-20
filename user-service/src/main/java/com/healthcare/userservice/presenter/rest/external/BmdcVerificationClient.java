package com.healthcare.userservice.presenter.rest.external;

import com.healthcare.userservice.domain.request.DoctorRegistrationValidationRequest;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "captchaClient", url = "https://verify.bmdc.org.bd")
public interface BmdcVerificationClient {

    @GetMapping("/portal/captcha")
    Response getCaptcha(@RequestParam("_") String timestamp);

    @PostMapping(value = "/regfind", consumes = "application/x-www-form-urlencoded")
    Response validateRegistration(@RequestHeader("Cookie") String cookie,
                                  @RequestBody DoctorRegistrationValidationRequest request);
}
