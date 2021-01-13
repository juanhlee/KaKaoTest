package com.kakaopay.payment.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
public class CardInfoModel {
	@NotNull @Size(min=10,max=16 , message = "ī�� ��ȣ Ȯ��")
	private String cardnum;
	@NotNull @Pattern(regexp="(0[1-9]|1[0-2])([0-9]{2})", message="ī�� ��ȿ�Ⱓ Ȯ��")
	private String expires;
	@NotNull @Size(min=2,max=3, message = "CVC ��ȣ Ȯ��")
    private String cvc;
	
	@Builder
	public CardInfoModel(String cardnum, String expires, String cvc ) {
		this.cardnum = cardnum;
		this.expires = expires;
		this.cvc = cvc;
	}
	
}
