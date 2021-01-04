package com.kakaopay.payment.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.kakaopay.payment.domain.CardInfoModel;


import lombok.*;

public class ResponseDto {
	
	@Getter @Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ResponsePayment {
		@NotNull
		private String pid;
		@NotNull
		private String data;
	}
	
	@Getter @Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ResponseCancel {
		@NotNull
		private String pid;
		@NotNull
		private String data;
		
	}
	
	@Getter @Setter @Builder
	public static class ResponseSearch {
		@NotNull
		private String pid;
		@NotNull
		@Valid
		private CardInfoModel cardinfo;
		@NotNull
		private String payment_status;
		@NotNull
		private String current_price;
		@NotNull
		private String current_vat;
		
		List<ResponseForPayments> payment_list;
		
	}
	@Getter @Setter
	@Builder
	public static class ResponseForPayments {

		private String aid;

		private String installment;
		
		private String price;
		
		private String vat;
		
		private String payment_type;
	}
}
