package com.healthcare.tfaservice.common.handler;

import com.healthcare.tfaservice.common.exceptions.CustomRootException;
import com.healthcare.tfaservice.common.exceptions.FeignClientException;
import com.healthcare.tfaservice.common.logger.TfaServiceLogger;
import com.healthcare.tfaservice.domain.common.ApiResponse;
import com.healthcare.tfaservice.domain.enums.ResponseMessage;
import lombok.RequiredArgsConstructor;
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
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestControllerAdvice
public class TfaServiceExceptionHandler extends BaseExceptionHandler{

    private final TfaServiceLogger logger;


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


    private void dropErrorLogForArgumentNotValid(final String className, final String methodName, final String message, final Object data) {

        errorLogger.error(String.format("****Custom Jakarta Validation Error**** " +
                "\nClassName: %s | MethodName: %s | Message : %s" +
                "\nError Data: %s", className, methodName, message, data));
    }

}
