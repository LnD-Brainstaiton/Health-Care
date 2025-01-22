package com.healthcare.userservice.presenter.service;

import com.healthcare.userservice.common.exceptions.FeignClientException;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.request.BmdcValidationRequest;
import com.healthcare.userservice.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class BmdcClientService extends BaseService {

    private static String bmdcCsrfCookie;
    private static String bmdcSessionCookie;

    @Value("${bmdc_csrf_cookie}")
    private String bmdcCsrf;

    @Value("${bmdc_session_cookie}")
    private String bmdcSession;

    @Value("${bmdc_action_key}")
    private String bmdcActionKey;

    @Value("${bmdc_action_flag}")
    private int bmdcActionFlag;

    @Value("${bmdc_reg_student}")
    private int bmdcRegStudent;

    public String fetchCaptcha() {
        String responseBody = "";
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String url = "https://verify.bmdc.org.bd/portal/captcha?_=" + timestamp;

            HttpGet getRequest = new HttpGet(url);

            try (CloseableHttpResponse response = client.execute(getRequest)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                    responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    extractAndSetCookies(response);
                } else {
                    throw new FeignClientException(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(),
                            ResponseMessage.INTER_SERVICE_COMMUNICATION_ERROR.getResponseMessage());
                }
            }
        } catch (Exception e) {
            throw new FeignClientException(ApiResponseCode.INTER_SERVICE_COMMUNICATION_ERROR.getResponseCode(),
                    ResponseMessage.FAIL_BMDC_API_CALL.getResponseMessage());
        }
        return responseBody;
    }

    public String validateRegistration(BmdcValidationRequest request) {
        try {
            // Prepare the cookies in the header
            String cookieHeader = "bmdckyc_csrf_cookie=" + bmdcCsrfCookie + "; bmdckyc_sessions=" + bmdcSessionCookie + ";";

            // Prepare the form data
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("bmdckyc_csrf_token", bmdcCsrfCookie);
            formData.add("reg_ful_no", String.valueOf(request.getRegistrationNo()));
            formData.add("captcha_code", request.getCaptchaCode());
            formData.add("reg_student", String.valueOf(bmdcRegStudent));
            formData.add("action_key", bmdcActionKey);
            formData.add("action_flag", String.valueOf(bmdcActionFlag));

            try (CloseableHttpClient client = HttpClients.createDefault()) {

                // Send the POST request to validate the registration
                HttpPost postRequest = new HttpPost("https://verify.bmdc.org.bd/regfind");
                postRequest.setHeader("Cookie", cookieHeader);
                postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");

                // Convert form data to a URL-encoded string (for POST request body)
                String formDataString = buildFormDataString(formData);
                postRequest.setEntity(new StringEntity(formDataString));

                // Execute the request
                HttpResponse response = client.execute(postRequest);
                extractAndSetCookies(response);

                if(response.getStatusLine().getStatusCode() == HttpStatus.FORBIDDEN.value()){
                    throw new FeignClientException(String.valueOf(HttpStatus.FORBIDDEN.value()),
                            ResponseMessage.BMDC_Authentication_Failure.getResponseMessage());
                }

                if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                    String data = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    String cleanedData = data.replace("\r\n", "").replace("\n", "");
                    cleanedData = cleanedData.replace("\\\"", "\"");
                    cleanedData = cleanedData.replace("\"","");
                    return cleanedData;
                } else {
                    throw new FeignClientException(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(),
                            ResponseMessage.INTER_SERVICE_COMMUNICATION_ERROR.getResponseMessage());
                }
            }
        } catch (Exception e) {
            throw new FeignClientException(ApiResponseCode.INTER_SERVICE_COMMUNICATION_ERROR.getResponseCode(),
                    ResponseMessage.FAIL_BMDC_API_CALL.getResponseMessage());
        }
    }

    private String buildFormDataString(MultiValueMap<String, String> formData) {
        StringBuilder formDataString = new StringBuilder();
        for (String key : formData.keySet()) {
            for (String value : formData.get(key)) {
                if (!formDataString.isEmpty()) {
                    formDataString.append("&");
                }
                formDataString.append(key).append("=").append(value);
            }
        }
        return formDataString.toString();
    }

    private void extractAndSetCookies(HttpResponse response) {
        String csrfCookie = extractCookie(response, bmdcCsrf);
        String sessionCookie = extractCookie(response, bmdcSession);

        if (csrfCookie == null) {
            throw new FeignClientException(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode(),
                    ResponseMessage.COOKIE_NOT_FOUND.getResponseMessage());
        }

        bmdcCsrfCookie = csrfCookie;
        bmdcSessionCookie = sessionCookie;
    }

    private String extractCookie(HttpResponse response, String cookieName) {
        Header[] cookies = response.getHeaders("Set-Cookie");

        for (Header cookieHeader : cookies) {
            String cookieValue = cookieHeader.getValue();

            if (cookieValue.startsWith(cookieName + "=")) {
                // Split at ';' to get the cookie value before attributes
                String[] cookieParts = cookieValue.split(";");
                // Extract the cookie value (before '=')
                return cookieParts[0].split("=")[1];
            }
        }
        return null;
    }
}
