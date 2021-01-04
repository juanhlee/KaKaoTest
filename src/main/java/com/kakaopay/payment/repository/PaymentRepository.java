package com.kakaopay.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakaopay.payment.domain.entity.Payment;

public interface PaymentRepository extends JpaRepository <Payment, String> {

}
