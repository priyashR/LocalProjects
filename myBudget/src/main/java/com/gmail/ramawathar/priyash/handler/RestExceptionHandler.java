package com.gmail.ramawathar.priyash.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.gmail.ramawathar.priyash.error.ErrorDetail;
import com.gmail.ramawathar.priyash.error.ValidationError;
import com.gmail.ramawathar.priyash.exception.ResourceNotFoundException;

@ControllerAdvice
public class RestExceptionHandler {
	
	 @ExceptionHandler(MethodArgumentNotValidException.class)
     public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException manve, HttpServletRequest request) {

             ErrorDetail errorDetail = new ErrorDetail();
             // Populate errorDetail instance
             errorDetail.setTimeStamp(new Date().getTime());
             errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
             String requestPath = (String) request.getAttribute("javax.servlet.error.request_uri");
             if(requestPath == null) {
                     requestPath = request.getRequestURI();
             }
             errorDetail.setTitle("Validation Failed");
             errorDetail.setDetail("Input validation failed");
             errorDetail.setDeveloperMessage(manve.getClass().getName());

             // Create ValidationError instances
             List<FieldError> fieldErrors =  manve.getBindingResult().getFieldErrors();
             for(FieldError fe : fieldErrors) {
                     List<ValidationError> validationErrorList = errorDetail.getErrors().get(fe.getField());
                     if(validationErrorList == null) {
                             validationErrorList = new ArrayList<ValidationError>();
                             errorDetail.getErrors().put(fe.getField(), validationErrorList);
                     }
                     ValidationError validationError = new ValidationError();
                     validationError.setCode(fe.getCode());
                     validationError.setMessage(fe.getDefaultMessage());
                     validationErrorList.add(validationError);
             }

             return new ResponseEntity<>(errorDetail, null, HttpStatus. BAD_REQUEST);
                }
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rnfe, HttpServletRequest request) {

                ErrorDetail errorDetail = new ErrorDetail();
                errorDetail.setTimeStamp(new Date().getTime());
                errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
                errorDetail.setTitle("Resource Not Found");
                errorDetail.setDetail(rnfe.getMessage());
                errorDetail.setDeveloperMessage(rnfe.getClass().getName());

                return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
        }

}