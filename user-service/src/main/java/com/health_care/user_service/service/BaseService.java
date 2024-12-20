package com.health_care.user_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health_care.user_service.common.exceptions.UnauthorizedResourceException;
import com.health_care.user_service.domain.enums.ResponseMessage;
import com.health_care.user_service.domain.response.CurrentUserContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Optional;

@Service
public class BaseService {

    protected ObjectMapper objectMapper;
    protected HttpServletRequest httpServletRequest;

    @Autowired
    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public Optional<String> getHeaderValue(String headerName) {
        return Optional.ofNullable(httpServletRequest.getHeader(headerName));
    }

    public CurrentUserContext getCurrentUserContext() {
        String bearerToken = getBearerToken();
        String[] tokenParts = bearerToken.split("\\."); // JWT structure: header.payload.signature

        if (tokenParts.length != 3) {
            throw new UnauthorizedResourceException(ResponseMessage.AUTHENTICATION_FAILED);
        }

        String payload = decodeBase64(tokenParts[1]); // Decode the payload (second part of the JWT).
        try {
            JsonNode payloadJson = objectMapper.readTree(payload); // Parse the JSON payload.
            CurrentUserContext currentUserContext = new CurrentUserContext();

            // Extract the "sub" field and set it as the userId.
            if (payloadJson.has("sub")) {
                currentUserContext.setUserId(payloadJson.get("sub").asText());
            }

            // Optional: Extract other fields as needed.
            if (payloadJson.has("userName")) {
                currentUserContext.setUserName(payloadJson.get("userName").asText());
            }
            if (payloadJson.has("userType")) {
                currentUserContext.setUserType(payloadJson.get("userType").asText());
            }

            return currentUserContext;
        } catch (Exception ex) {
            throw new UnauthorizedResourceException(ResponseMessage.AUTHENTICATION_FAILED);
        }
    }

    private String getBearerToken() {
        Optional<String> authHeader = getHeaderValue("Authorization");
        if (authHeader.isEmpty() || !authHeader.get().startsWith("Bearer ")) {
            throw new UnauthorizedResourceException(ResponseMessage.AUTHENTICATION_FAILED);
        }
        return authHeader.get().substring(7); // Remove "Bearer " prefix
    }

    protected String decodeBase64(String encodedString) {
        return new String(Base64.getDecoder().decode(encodedString));
    }
}
