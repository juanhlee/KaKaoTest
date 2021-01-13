package com.kakaopay.payment.common;


import com.kakaopay.payment.common.CommonEnum.DataType;
import com.kakaopay.payment.config.ApiException;
import com.kakaopay.payment.domain.CardInfoModel;
import com.kakaopay.payment.domain.PayloadModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonUtil {

	private static final String DELIMITER = "|";
	private static final String MASKEDLETTER = "*";
	private static final int STARTMASKED = 6;
	private static final int ENDMASKED = 3;
	
	@Autowired
	AES256Util aes256util;

	/*
	 * Payload Converter from model to String data
	 * */
	public String payloadConverter (PayloadModel model ) {
		String result= 
		DataType.CHARACTER.format(model.getPaymentType(), 10)
		+DataType.CHARACTER.format(model.getPid(), 20)
		+DataType.NUMBER_L.format(model.getCardnum(), 20)
		+DataType.NUMBER_0.format(model.getInstallment(), 2)
		+DataType.NUMBER_L.format(model.getExpires(), 4)
		+DataType.NUMBER_L.format(model.getCvc(), 3)
		+DataType.NUMBER.format(model.getPrice(), 10)
		+DataType.NUMBER_0.format(model.getVat(), 10)
		+DataType.CHARACTER.format(nullCatch(model.getAid()), 20)
		+DataType.CHARACTER.format(model.getEncryptedCardNum(), 300)
		+DataType.CHARACTER.format(nullCatch(model.getTemp()), 47);
		
		result = DataType.NUMBER.format(String.valueOf(result.length()),4) + result;

		return result;
	}
	public static String nullCatch (String str) {

		if ( "null".equals(str) || "".equals(str) || str == null) {
			return "";
		}
		return str;
	}
	
	public String getEncryptedCardNumber(CardInfoModel card) {
		String result ="";
		try {
			result = aes256util.encrypt( 
					new StringBuilder()
					.append(card.getCardnum())
					.append(DELIMITER)
					.append(card.getExpires())
					.append(DELIMITER)
					.append(card.getCvc())
					.toString()
					);
		} catch (Exception e)  {
			throw new ApiException( CommonEnum.ErrorCode.EXCEPTION_EXPIRATION , e.getMessage() );
		}
		return result ;
	}
	
	public CardInfoModel getDecryptedCardNumber(String str) {
		String result = "";
		try {
			result = aes256util.decrypt(str);
			String[] strArray = result.split("\\|");
			return CardInfoModel.builder().cardnum(strArray[0]).expires(strArray[1]).cvc(strArray[2]).build();
			//return new CardInfoModel(strArray[0], strArray[1], strArray[2]);
			
		} catch (Exception e)  {
			throw new ApiException( CommonEnum.ErrorCode.EXCEPTION_EXPIRATION , e.getMessage() );
			
		}


	}
	
	public String getMaskedCardNumber(String str) {
		String result = str;
		try {
		    int maskedLength = str.length() - (STARTMASKED + ENDMASKED);
		    StringBuilder sb = new StringBuilder();
		    
		    for (int i = 0; i < maskedLength; i++) {
		        sb.append(MASKEDLETTER);
		    }
		    result = result.substring(0, STARTMASKED) + sb + result.substring(result.length() - ENDMASKED, result.length());

		} catch (Exception e)  {
			result = str;
			throw new ApiException( CommonEnum.ErrorCode.EXCEPTION_EXPIRATION , e.getMessage() );
		}
		return result;
	}

	
}
