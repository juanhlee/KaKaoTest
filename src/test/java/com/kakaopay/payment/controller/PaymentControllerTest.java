package com.kakaopay.payment.controller;

import java.nio.charset.Charset;
import java.util.HashMap;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.payment.Application;
import com.kakaopay.payment.controller.PaymentController;
import com.kakaopay.payment.domain.CardInfoModel;
import com.kakaopay.payment.dto.RequestDto.RequestPayment;
import com.kakaopay.payment.service.PaymentService;
import com.kakaopay.payment.service.PaymentServiceImpl;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PaymentService service;
	
	 @Bean
     public PaymentService getPersonService() {
         return mock(PaymentService.class);
     }

	public static String ServerConstant = "http://localhost:8080/kakaopay/payment/v1/";
	

	@Before
	public void setUp() throws Exception {
		System.out.println("BEFORE METHOD RUN@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		//HashMap<String, Object> sendMap = new HashMap<String, Object>();
		this.mockMvc = MockMvcBuilders.standaloneSetup(new PaymentController(service)).build();
		
		ObjectMapper objMap = new ObjectMapper();
		String contents = objMap.writeValueAsString(RequestPayment.builder()		
						.card( CardInfoModel.builder().cardnum("4111111111111111").expires("0199").cvc("111").build() )
						.price(11000)
						.vat(1000)
						.installment("00")
						.build());
		System.out.println("1BEFORE METHOD RUN@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		
		mockMvc.perform(
				post(ServerConstant)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(contents))
		.andDo(MockMvcResultHandlers.print());

		System.out.println("2BEFORE METHOD RUN@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	}
	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void 결제테스트() throws Exception, JSONException  {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		mockMvc.perform(post(ServerConstant))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string("HELLO"));

	}
	
}
