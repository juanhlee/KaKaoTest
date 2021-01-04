package com.kakaopay.payment.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.kakaopay.payment.domain.CardInfoModel;
import com.kakaopay.payment.domain.PriceInfoModel;

import lombok.*;

public class RequestDto {
	
	@Getter @NoArgsConstructor
	public static class RequestPayment extends PriceInfoModel {
		@NotNull
		@Valid
		private CardInfoModel card;
		
	}
	
	@Getter @NoArgsConstructor
	public static class RequestCancel extends PriceInfoModel {
		@NotNull
		private String pid;
		
	}
	
	@Getter @NoArgsConstructor
	public static class RequestSearch {
		@NotNull
		private String pid;
		
	}
	
}
