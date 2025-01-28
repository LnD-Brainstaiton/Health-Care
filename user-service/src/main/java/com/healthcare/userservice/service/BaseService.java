package com.healthcare.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.userservice.common.exceptions.RecordNotFoundException;
import com.healthcare.userservice.common.utils.SerializationUtils;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.dto.CurrentUserContext;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.Optional;

public class BaseService {

    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    protected ObjectMapper objectMapper;
    private HttpServletRequest request;

    @Value("${http.header.auth}")
    protected String tokenHeader;

    public static final String CURRENT_USER_CONTEXT_HEADER = "CurrentContext";

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.objectMapper = mapper;
    }

    public Optional<String> getHeaderValue(String headerName) {

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attrs.getRequest();

        try {
            return Optional.ofNullable(request.getHeader(headerName));
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }

        return Optional.empty();
    }

    public Date getCurrentDate() {
        return new Date();
    }

    private String getCurrentUserContextHeaderValue() {
        Optional<String> userTokenOpt = getHeaderValue(CURRENT_USER_CONTEXT_HEADER);

        if (userTokenOpt.isEmpty())
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND);

        return userTokenOpt.get();
    }

    public Optional<String> getHeaderValueForToken(String headerName) {

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attrs.getRequest();

        try {
            return Optional.ofNullable(request.getHeader(headerName));
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }

        return Optional.empty();
    }

    public CurrentUserContext getCurrentUserContext() {
        String base64Data = getCurrentUserContextHeaderValue();
        String jsonObject = SerializationUtils.toByteArrayToString(base64Data);
        return toObject(jsonObject, CurrentUserContext.class);
    }

    public <T> T toObject(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public String getUserIdentity() {
        return getCurrentUserContext().getUserIdentity();
    }

}
