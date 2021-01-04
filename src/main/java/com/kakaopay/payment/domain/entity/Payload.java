package com.kakaopay.payment.domain.entity;

import javax.persistence.*;

import lombok.*;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Entity
@Table(name="Payload")
public class Payload extends CommonEntity {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private String id;
	
	@Column(length = 450, nullable = false)
    private String data;

}
