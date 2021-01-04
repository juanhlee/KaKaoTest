package com.kakaopay.payment.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.*;

import com.kakaopay.payment.config.ApiException;
import com.kakaopay.payment.dto.RequestDto.*;
import com.kakaopay.payment.dto.ResponseDto.*;
import com.kakaopay.payment.service.PaymentService;

import com.kakaopay.payment.common.CommonEnum;


@RestController
@RequestMapping(value = "/kakaopay/payment/v1")
public class PaymentController {
	
	@Autowired
	PaymentService paymentService;
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="", dataType = "", paramType="", value=""),
		@ApiImplicitParam(name="", dataType = "", paramType="", value=""),
		@ApiImplicitParam(name="", dataType = "", paramType="", value="")
	})
	@PostMapping("/")
	public ResponsePayment payment(@RequestBody @Valid RequestPayment request, Errors  errors) throws Exception {
		
		if (errors.hasErrors()) {
			throw new ApiException( CommonEnum.ErrorCode.EXCEPTION_PARAMETER , "[" + errors.getFieldError().getField() +"] " + errors.getFieldError().getDefaultMessage() );
		}
		
		return paymentService.doPayment(request);
	}
	
	//@PutMapping("/{pid}/{price}") - Binding a Hierarchy of Objects 
	@PutMapping("/")
	public ResponseCancel cancel(@RequestBody @Valid RequestCancel request, Errors  errors) throws Exception {
		if (errors.hasErrors()) {
			throw new ApiException( CommonEnum.ErrorCode.EXCEPTION_PARAMETER , "[" + errors.getFieldError().getField() +"] " + errors.getFieldError().getDefaultMessage() );
		}

		return paymentService.doCancel(request);
	}
	
	@GetMapping("/{pid}")
	public ResponseSearch search(@Valid @PathVariable("pid")  String pid ) throws Exception {


		return paymentService.doSearch(pid);
	}
}
