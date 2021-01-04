package com.kakaopay.payment.config;

import org.springframework.http.HttpStatus;

import com.kakaopay.payment.common.CommonEnum.ErrorCode;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
public class ApiException extends RuntimeException  {
	
	private static final long serialVersionUID = 1L;
	
	private ErrorCode code;
	private HttpStatus httpstatus;
	
	  public ApiException(ErrorCode errorCode, String defaultMessage){
	        super(defaultMessage);
	        this.code = errorCode;
	        this.httpstatus = errorCode.getStatus();
	        
	    }
	  
}
