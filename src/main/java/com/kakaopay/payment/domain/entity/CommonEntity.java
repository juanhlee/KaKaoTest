package com.kakaopay.payment.domain.entity;

import java.sql.Timestamp;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Setter;


@MappedSuperclass @Setter
public class CommonEntity {
	
	@CreationTimestamp
	private Timestamp createdAt;
	
	@UpdateTimestamp
	private Timestamp updatedAt;
}
