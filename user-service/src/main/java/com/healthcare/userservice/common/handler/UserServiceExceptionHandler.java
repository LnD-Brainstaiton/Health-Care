package com.healthcare.userservice.common.handler;

import com.healthcare.userservice.common.exceptions.CustomRootException;
import com.healthcare.userservice.common.exceptions.FeignClientException;
import com.healthcare.userservice.common.exceptions.PreValidationException;
import com.healthcare.userservice.common.logger.UserServiceLogger;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestControllerAdvice
public class UserServiceExceptionHandler extends BaseExceptionHandler {

    private final UserServiceLogger logger;

//    @ExceptionHandler({FeignException.class})
//    public ResponseEntity<ApiResponse<Void>> handleFeignException(FeignException ex) {
//        logger.error(ex.getLocalizedMessage(), ex);
//        String message = processFeignExceptionMessage(ex.status(), ex.contentUTF8());
//        ApiResponse<Void> apiResponse =
//                buildApiResponse(ResponseMessage.INTER_SERVICE_COMMUNICATION_ERROR.getResponseCode(), message);
//        HttpStatus httpStatus = ex.status() == HttpStatus.BAD_REQUEST.value() ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
//        return new ResponseEntity<>(apiResponse, httpStatus);
//    }

    @ExceptionHandler(PreValidationException.class)
    public final ResponseEntity<ApiResponse<Void>> handlePreValidationException(PreValidationException ex) {
        errorLogger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Void> apiResponse = buildApiResponse(ex.getMessageCode(), ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public final ResponseEntity<ApiResponse<Void>> handleResponseStatusException(ResponseStatusException ex) {
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .responseCode(String.valueOf(ex.getStatusCode().value())) // Use the HTTP status code
                .responseMessage(ex.getReason()) // The reason passed when throwing the exception
                .build();
        return new ResponseEntity<>(apiResponse, ex.getStatusCode());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
        logger.error("Authentication failed: " + ex.getLocalizedMessage(), ex);

        // Create an appropriate response message
        ApiResponse<Void> apiResponse = buildApiResponse(
                ResponseMessage.AUTHENTICATION_FAILED.getResponseCode(),
                ResponseMessage.AUTHENTICATION_FAILED.getResponseMessage()
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

//    @ExceptionHandler(DataAccessException.class)
//    public final ResponseEntity<ApiResponse<Void>> handleDBException(DataAccessException ex) {
//        errorLogger.error(ex.getLocalizedMessage(), ex);
//        String rootCause = Objects.nonNull(ex.getRootCause()) ? ex.getRootCause().toString() : "";
//        errorLogger.error("Root Cause: " + rootCause);
//        ApiResponse<Void> apiResponse = buildApiResponse(ResponseMessage.DATABASE_EXCEPTION.getResponseCode(),
//                getMessage(ResponseMessage.DATABASE_EXCEPTION.getResponseMessage()));
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }

    @ExceptionHandler(CustomRootException.class)
    public final ResponseEntity<ApiResponse<Void>> handleCustomException(CustomRootException ex) {
        errorLogger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Void> apiResponse = buildApiResponse(ex.getMessageCode(), ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiResponse<Void>> commonException(Exception ex) {
        errorLogger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Void> apiResponse = buildApiResponse(ResponseMessage.INTERNAL_SERVICE_EXCEPTION.getResponseCode()
                , ResponseMessage.INTERNAL_SERVICE_EXCEPTION.getResponseMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> collect = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (oldValue, newValue) -> newValue));

        String message = ResponseMessage.INVALID_REQUEST_DATA.getResponseMessage();

        dropErrorLogForArgumentNotValid(ex.getParameter().getDeclaringClass().getName(),
                Objects.isNull(ex.getParameter().getMethod()) ? "" : ex.getParameter().getMethod().getName(),
                message,
                collect);

        ApiResponse<Object> apiResponse = buildApiResponse(ResponseMessage.INVALID_REQUEST_DATA.getResponseCode(), message, collect);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignClientException.class)
    public final ResponseEntity<ApiResponse<Void>> handleFeignClientException(FeignClientException ex) {
        errorLogger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Void> apiResponse = buildApiResponse(ex.getMessageCode(), ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


//    private String getMessage(String messageKey) {
//        String message = StringUtils.EMPTY;
//
//        try {
//            message = localeMessageService.getLocalMessage(messageKey);
//        } catch (Exception ex) {
//            logger.error(ex.getLocalizedMessage(), ex);
//        }
//
//        return StringUtils.isNotBlank(message) ? message : messageKey;
//    }

    private void dropErrorLogForArgumentNotValid(final String className, final String methodName, final String message, final Object data) {

        errorLogger.error(String.format("****Custom Jakarta Validation Error**** " +
                "\nClassName: %s | MethodName: %s | Message : %s" +
                "\nError Data: %s", className, methodName, message, data));
    }

}
