package com.kakaopay.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kakaopay.payment.domain.entity.Payments;

public interface PaymentsRepository extends JpaRepository <Payments, String> {

	@Query(value="SELECT MIN(DECODE  ( A.PAYMENT_TYPE, 'PAYMENT', A.TOTAL, NULL)) - NVL ( MIN(DECODE ( A.PAYMENT_TYPE, 'CANCEL', A.TOTAL, NULL)), 0 ) "
			+ "FROM ( "
			+ "SELECT PAYMENT_TYPE,  SUM(CAST(PRICE AS INT))  AS TOTAL "
			+ "FROM PAYMENTS WHERE PID = :pid  "
			+ "GROUP BY PAYMENT_TYPE) A", nativeQuery = true)
	Integer selectTotalPrice(@Param("pid") String pid);
	
	@Query(value="SELECT MIN(DECODE  ( A.PAYMENT_TYPE, 'PAYMENT', A.TOTAL, NULL)) - NVL ( MIN(DECODE ( A.PAYMENT_TYPE, 'CANCEL', A.TOTAL, NULL)) , 0 )"
			+ "FROM ( "
			+ "SELECT PAYMENT_TYPE,  SUM(CAST(VAT AS INT))  AS TOTAL "
			+ "FROM PAYMENTS WHERE PID = :pid  "
			+ "GROUP BY PAYMENT_TYPE) A", nativeQuery = true)
	Integer selectTotalVat(@Param("pid") String pid);
	
	@Query(value="SELECT COUNT(*) FROM PAYMENTS WHERE PID = :pid AND PAYMENT_TYPE='CANCEL' ", nativeQuery = true)
	Integer countCacelPayment(@Param("pid") String pid);
}
