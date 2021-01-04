package com.kakaopay.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakaopay.payment.domain.entity.Payload;

public interface PayloadRepository extends JpaRepository <Payload, String>{

}
