package com.kakaopay.payment.domain.entity;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.kakaopay.payment.common.SeqenceGenerator;

import lombok.*;

/*
 * ù ������ �̷��� �������� ù��° ������ �򸮰�, �κ� ��� �Ǹ� �̷� �����, ��ü ��� �Ǹ� ��ü��� ������ȣ ����
 * 
 * */

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name="Payments")
public class Payments extends CommonEntity {
	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aid_seq")
	@GenericGenerator(
	        name = "aid_seq", 
	        strategy = "com.kakaopay.payment.common.SeqenceGenerator", 
	        parameters = {
	            @Parameter(name = SeqenceGenerator.INCREMENT_PARAM, value = "1"),
	            @Parameter(name = SeqenceGenerator.VALUE_PREFIX_PARAMETER, value = "APPROVAL_"),
	            @Parameter(name = SeqenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%04d") 
	            })
	@Column(name = "aid")
	private String aid;

	@Column
	@ColumnDefault("00")
	private String installment;
	
	@Column
	private String price;
	
	@Column
	private String vat;
	
	@Column 
	private String payment_type;
	
	@ManyToOne
    @JoinColumn(name = "pid")
    private Payment payment;
	
}
