package com.kakaopay.payment.service;

import java.nio.charset.Charset;
import java.util.HashMap;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.payment.domain.CardInfoModel;
import com.kakaopay.payment.domain.PriceInfoModel;
import com.kakaopay.payment.dto.RequestDto;
import com.kakaopay.payment.dto.RequestDto.RequestPayment;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


@SpringBootTest
@AutoConfigureMockMvc
public class PaymentServiceTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PaymentService service;
	
	public static String ServerConstant = "http://localhost:8080/kakaopay/payment/v1";
	

	@Before
	public void setUp() throws Exception {
	
		//HashMap<String, Object> sendMap = new HashMap<String, Object>();

		ObjectMapper objMap = new ObjectMapper();
		String contents = objMap.writeValueAsString(RequestPayment.builder()		
						.card( CardInfoModel.builder().cardnum("4111111111111111").expires("0199").cvc("111").build() )
						.price(11000)
						.vat(1000)
						.installment(null)
						.build());
		
		mockMvc.perform(
				post(ServerConstant)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(contents))
		.andDo(MockMvcResultHandlers.print());

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
