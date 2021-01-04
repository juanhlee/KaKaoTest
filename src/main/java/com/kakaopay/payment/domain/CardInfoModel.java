package com.kakaopay.payment.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardInfoModel {
	@NotNull @Size(min=10,max=16 , message = "카드 번호 확인")
	private String cardnum;
	@NotNull @Pattern(regexp="(0[1-9]|1[0-2])([0-9]{2})", message="카드 유효기간 확인")
	private String expires;
	@NotNull @Size(min=2,max=3, message = "CVC 번호 확인")
    private String cvc;
	
}
