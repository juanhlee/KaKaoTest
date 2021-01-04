package com.kakaopay.payment.service;

import com.kakaopay.payment.dto.RequestDto.*;
import com.kakaopay.payment.dto.ResponseDto.*;

public interface PaymentService {
	public ResponsePayment doPayment(RequestPayment req) throws Exception;
	
	public ResponseCancel doCancel(RequestCancel req) throws Exception;
	
	public ResponseSearch doSearch(String req) throws Exception ;
	
}
