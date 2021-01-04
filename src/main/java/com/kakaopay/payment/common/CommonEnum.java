package com.kakaopay.payment.common;

import org.springframework.http.HttpStatus;

import lombok.Getter;


public class CommonEnum {
	
	public enum ErrorCode {

		EXCEPTION_PARAMETER(HttpStatus.BAD_REQUEST, "ERR0001"),
	    EXCEPTION_EXPIRATION(HttpStatus.INTERNAL_SERVER_ERROR, "ERR0002"),
	    EXCEPTION_FOUND(HttpStatus.NOT_FOUND, "ERR0003");

	    private final String code;

	    private final HttpStatus status;

	    public String getCode() {
	        return code;
	    }


	    public HttpStatus getStatus() {
	        return status;
	    }

	    ErrorCode(final HttpStatus  status, final String code) {
	        this.status = status;

	        this.code = code;
	    }
	}
	@Getter
	public enum PaymentType {
		PAYMENT("PAYMENT"),
		CANCEL("CANCEL");
		
		private String type;
		
		PaymentType(String type){
			this.type = type;
		}

	}
	public enum DataType {
		
		NUMBER{ // ����, ���� ����, ���ڸ� ����
			@Override
			public String format(String data, int length) {
				
				String formatter = "%" + length + "s" ;
				return String.format(formatter, data).replace(' ',' ');
			}
		},
		NUMBER_0{ // ����, ���� ����, ���ڸ� 0
			@Override
			public String format(String data, int length) {
				String formatter = "%" + length + "s" ;
				return String.format(formatter, data).replace(' ','0');
			}
		},
		NUMBER_L{ // ����, ���� ����, ���ڸ� ����
			@Override
			public String format(String data, int length) {
				String formatter = "%-" + length + "s" ;
				return String.format(formatter, data).replace(' ',' ');
			}
		},
		CHARACTER{ // ����, ���� ����, ���ڸ� ����
			@Override
			public String format(String data, int length) {
				String formatter = "%-" + length + "s" ;
				return String.format(formatter, data).replace(' ',' ');
			}
		};
		
		public abstract String format(String data, int length);
	}
	
}
