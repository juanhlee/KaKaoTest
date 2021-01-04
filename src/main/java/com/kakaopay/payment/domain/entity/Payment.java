package com.kakaopay.payment.domain.entity;

import com.kakaopay.payment.common.SeqenceGenerator;


import lombok.*;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name="Payment")
public class Payment extends CommonEntity {
	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pid_seq")
	@GenericGenerator(
	        name = "pid_seq", 
	        strategy = "com.kakaopay.payment.common.SeqenceGenerator", 
	        parameters = {
	            @Parameter(name = SeqenceGenerator.INCREMENT_PARAM, value = "1"),
	            @Parameter(name = SeqenceGenerator.VALUE_PREFIX_PARAMETER, value = "PAYMENT_"),
	            @Parameter(name = SeqenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%04d") 
	            })
	@Column(name="pid")
	private String pid;
	
	@Column
	private String encyptedCardNum;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="pid")
	@Fetch(value=FetchMode.SELECT)
	private List<Payments> PaymentsList = new ArrayList<Payments>();
	
	public void addPayments(Payments payments) {
		PaymentsList.add(payments);
		payments.setPayment(this);
	}
}
