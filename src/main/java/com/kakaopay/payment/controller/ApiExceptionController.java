package com.kakaopay.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.payment.config.ApiException;
import com.kakaopay.payment.dto.ApiResponseDto;

@ControllerAdvice
@RestController
public class ApiExceptionController {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ApiException.class)
    public ApiResponseDto handleBaseException(ApiException errorObj){

        return ApiResponseDto
        		.builder()
        		.code(errorObj.getCode().getCode())
        		.message(errorObj.getMessage())
        		.httpstatus(errorObj.getHttpstatus())
        		.build();
    }

    @ExceptionHandler(value = Exception.class)
    public String handleException(Exception e){
    	return e.getMessage();
    
    }
    
}
