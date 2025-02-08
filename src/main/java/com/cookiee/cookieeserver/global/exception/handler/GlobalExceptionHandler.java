package com.cookiee.cookieeserver.global.exception.handler;

import com.cookiee.cookieeserver.global.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(GeneralException.class)
//    public ResponseEntity<ErrorResponse> handleGeneralException(GeneralException ex) {
//        ErrorResponse errorResponse = new ErrorResponse(
//        ) {
//            @Override
//            public HttpStatusCode getStatusCode() {
//                return null;
//            }
//
//            @Override
//            public ProblemDetail getBody() {
//                return null;
//            }
//        };
//        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//    }
//}
