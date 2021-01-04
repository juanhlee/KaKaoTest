package com.kakaopay.payment.dto;

import org.springframework.http.HttpStatus;

import lombok.*;

@Builder @Getter
public class ApiResponseDto {
	private String code;
	private String message;
	private HttpStatus httpstatus;
	

}
