package com.healthcare.tfaservice.common.handler;

import com.healthcare.tfaservice.common.exceptions.CustomRootException;
import com.healthcare.tfaservice.common.logger.TfaServiceLogger;
import com.healthcare.tfaservice.domain.common.ApiResponse;
import com.healthcare.tfaservice.domain.enums.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class TfaServiceExceptionHandler extends BaseExceptionHandler {

    private final TfaServiceLogger logger;
    private final MessageSource messageSource;

    @ExceptionHandler(CustomRootException.class)
    public final ResponseEntity<ApiResponse<Void>> handleCustomException(CustomRootException ex) {
        logger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Void> apiResponse = buildApiResponse(ex.getMessageCode(), ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiResponse<Void>> commonException(Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Void> apiResponse = buildApiResponse(
                ResponseMessage.INTERNAL_SERVICE_EXCEPTION.getResponseCode(),
                ResponseMessage.INTERNAL_SERVICE_EXCEPTION.getResponseMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 status for unexpected exceptions
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        // Collecting validation errors from the MethodArgumentNotValidException
        Map<String, String> errors = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, fieldError -> getValidationMessage(fieldError.getDefaultMessage())));

        // Create the error response body with validation message
        ApiResponse<Object> apiResponse = buildApiResponse(
                ResponseMessage.INVALID_REQUEST_DATA.getResponseCode(),
                ResponseMessage.INVALID_REQUEST_DATA.getResponseMessage(),
                errors);

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST); // Returning 400 Bad Request
    }


    private String getValidationMessage(String defaultMessage) {
        try {
            // Fetch the localized message
            String message = messageSource.getMessage(defaultMessage, null, LocaleContextHolder.getLocale());
            return message != null ? message : defaultMessage; // Fallback to default if the key is not found
        } catch (Exception e) {
            logger.error("Message not found for key: " + defaultMessage, e);
            return defaultMessage;  // Fallback to the original key if no message is found
        }
    }
}
