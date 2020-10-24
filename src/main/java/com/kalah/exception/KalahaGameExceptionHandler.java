package com.kalah.exception;


import com.kalah.model.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import static com.kalah.constant.KalahaGameConstant.*;

/**
 * Exception Handler for Kalaha Game API
 *
 * @author Proma Chowdhury
 * @version 1.0
 */

@ControllerAdvice
@Slf4j
public class KalahaGameExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     * To handle custom KalahGameException
     *
     * @param ex       Exception thrown by the Kalah game application
     * @param request    WebRequest
     * @return ResponseEntity Json containing error details
     */

    @ExceptionHandler(KalahaGameException.class)
    public ResponseEntity<?> handleKalahaGameException(KalahaGameException ex,
                                            WebRequest request)  {

        ErrorDetails errorDetails = buildErrorDetails(LocalDateTime.now().toString(),
                ex.getMessage(), request.getDescription(false)
                , ex.getErrorCode());
        return new ResponseEntity<>(errorDetails, ex.getHttpStatus());
    }



    /**
     * To handle handleNoHandlerFoundException
     *
     * @param ex       NoHandlerFoundException
     * @param headers        HttpHeaders
     * @param status     HttpStatus
     * @param request    WebRequest
     * @return ResponseEntity Json containing error details
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        ErrorDetails errorDetails = buildErrorDetails(LocalDateTime.now().toString(),
                errorMessage, request.getDescription(false)
                , HANDLER_NOT_FOUND_ERROR_CODE);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

    }

    /**
     * To handle handle HttpRequestMethodNotSupported Exception
     *
     * @param ex       handleHttpRequestMethodNotSupported
     * @param headers        HttpHeaders
     * @param status     HttpStatus
     * @param request    WebRequest
     * @return ResponseEntity Json containing error details
     */

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {


        String errorMessage =  ex.getMethod() + " please use " +
                ex.getSupportedMethods()[0];

        ErrorDetails errorDetails = buildErrorDetails(LocalDateTime.now().toString(),
                errorMessage, request.getDescription(false)
                ,METHOD_NOT_ALLOWED_ERROR_CODE);

        return new ResponseEntity<>(errorDetails,HttpStatus.METHOD_NOT_ALLOWED);

    }

    /**
     * To handle ConstraintViolationException
     *
     * @param ex       ConstraintViolationException
     * @param request    WebRequest
     * @return ResponseEntity Json containing error details
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {

        ErrorDetails errorDetails = buildErrorDetails(LocalDateTime.now().toString(), ex.getMessage(), request.getDescription(false)
        ,CONSTRAINT_VIOLATION_EXCEPTION_ERROR_CODE);
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public HttpEntity<ErrorDetails> handleGlobalException(Throwable t, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder().timestamp(LocalDateTime.now().toString()).message(t.getMessage())
                .requestUri(request.getDescription(false)).build();
        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public HttpEntity<ErrorDetails> handleGlobalException(HttpServerErrorException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder().timestamp(LocalDateTime.now().toString()).message(ex.getMessage())
                .requestUri(request.getDescription(false)).build();
        return new ResponseEntity<>(errorDetails, ex.getStatusCode());
    }



    @ExceptionHandler(IllegalArgumentException.class)
    public final HttpEntity<ErrorDetails> handleIllegalArgumentRequest(IllegalArgumentException ex,
                                                                       WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder().timestamp(LocalDateTime.now().toString()).message(ex.getMessage())
                .requestUri(request.getDescription(false)).build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InterruptedException.class)
    public final ResponseEntity<ErrorDetails> handleInterruptedException(InterruptedException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder().timestamp(LocalDateTime.now().toString()).message(ex.getMessage())
                .requestUri(request.getDescription(false)).build();

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExecutionException.class)
    public final ResponseEntity<ErrorDetails> handleExecutionException(ExecutionException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder().timestamp(LocalDateTime.now().toString()).message(ex.getMessage())
                .requestUri(request.getDescription(false)).build();

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorDetails buildErrorDetails(String timeStamp, String errorMessage, String requestURI, String errorCode) {

        return ErrorDetails.builder().timestamp(timeStamp)
                .message(errorMessage).requestUri(requestURI).errorCode(errorCode)
                .build();
    }
}
