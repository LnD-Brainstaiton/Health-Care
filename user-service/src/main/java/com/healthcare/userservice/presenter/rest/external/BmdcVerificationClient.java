package com.healthcare.userservice.presenter.rest.external;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "captchaClient", url = "https://verify.bmdc.org.bd")
public interface BmdcVerificationClient {
    @GetMapping("/portal/captcha")
    Response getCaptcha(@RequestParam("_") String timestamp);
}
