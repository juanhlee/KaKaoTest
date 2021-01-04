package com.kakaopay.payment.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.*;


@Getter @Setter
@NoArgsConstructor
public class PriceInfoModel {
	@NotNull @Min(value=100, message="100�� �̻�") @Max(value=1000000000, message="10�� ����")
	private Integer price;

	private Integer vat;
	@NotNull @Pattern(regexp="(0[0-9]|1[0-2])", message="�Һδ� 00-12 ����")
	private String installment = "00";
	
	// Setting actual price Vat when getting Vat
	public Integer getVat() {
		if ( this.vat == null && this.price != null) {
			this.vat = (Integer)Math.round(price/11);
			return null;
		}
		else return vat;
	}

}