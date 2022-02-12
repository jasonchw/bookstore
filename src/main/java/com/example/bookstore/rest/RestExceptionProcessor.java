package com.example.bookstore.rest;

import com.example.bookstore.lang.ServiceException;
import com.example.bookstore.payload.ErrorResponse;
import com.example.bookstore.payload.ImmutableErrorResponse;
import com.example.bookstore.payload.ImmutableResult;
import com.example.bookstore.payload.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(annotations = {RestController.class})
public class RestExceptionProcessor extends ResponseEntityExceptionHandler {
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseEntity<Result<ErrorResponse>> handleServiceException(Exception e) {
        logger.error("Error: " + e.getMessage(), e);

        ErrorResponse errorResponse = ImmutableErrorResponse.builder()
                .errorCode("UNKNOWN")
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity.internalServerError()
                .body(
                        ImmutableResult.<ErrorResponse>builder()
                                .status(Result.Status.FAILURE)
                                .payload(errorResponse)
                                .build()
                );
    }

    @ExceptionHandler({ServiceException.class})
    @ResponseBody
    public ResponseEntity<Result<ErrorResponse>> handleServiceException(ServiceException e) {
        logger.error("Error: " + e.getMessage(), e);

        ErrorResponse errorResponse = ImmutableErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity.internalServerError()
                .body(
                        ImmutableResult.<ErrorResponse>builder()
                                .status(Result.Status.FAILURE)
                                .payload(errorResponse)
                                .build()
                );
    }
}
