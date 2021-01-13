package com.kakaopay.payment.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.*;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceInfoModel {
	@NotNull @Min(value=100, message="100원 이상") @Max(value=1000000000, message="10억 이하")
	private Integer price;

	private Integer vat;
	@NotNull @Pattern(regexp="(0[0-9]|1[0-2])", message="할부는 00-12 까지")
	private String installment = "00";
	
	public void setVatForNull() {
		this.vat = (Integer)Math.round(price/11);
	}
}
