package com.kakaopay.payment.domain;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

/*
 * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/messaging/handler/annotation/Payload.html#expression--
 * */
@Data
@Builder
public class PayloadModel {
	
	public String paymentType;
	
	public String pid;
	
	public String cardnum;
	@NotNull
	@Builder.Default
	public String installment = "00";

	public String expires;

	public String cvc;

	public String price;
	
	@Builder.Default
	public String vat = "";

	public String aid;

	public String encryptedCardNum;

	public String temp;
}
