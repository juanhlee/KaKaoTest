package com.kakaopay.payment.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.LockModeType;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopay.payment.dto.RequestDto.*;
import com.kakaopay.payment.dto.ResponseDto.*;
import com.kakaopay.payment.repository.PayloadRepository;
import com.kakaopay.payment.repository.PaymentRepository;
import com.kakaopay.payment.repository.PaymentsRepository;
import com.kakaopay.payment.common.CommonUtil;
import com.kakaopay.payment.config.ApiException;
import com.kakaopay.payment.common.AES256Util;
import com.kakaopay.payment.common.CommonEnum;
import com.kakaopay.payment.domain.CardInfoModel;
import com.kakaopay.payment.domain.PayloadModel;
import com.kakaopay.payment.domain.entity.Payload;
import com.kakaopay.payment.domain.entity.Payment;
import com.kakaopay.payment.domain.entity.Payments;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	PaymentRepository paymentRepository;
	
	@Autowired
	PaymentsRepository paymentsRepository;

	@Autowired
	PayloadRepository payloadRepository;
	
	@Autowired
	AES256Util aes256util;
	
	@Autowired
	CommonUtil commonUtil;

	@Autowired
    ModelMapper modelMapper;
	
	private static final String PAYMENT="PAYMENT";
	private static final String CANCEL="CANCEL";
	
	@Override
	@Transactional
	@Lock(value = LockModeType.PESSIMISTIC_WRITE) 
	public ResponsePayment doPayment(RequestPayment req) throws Exception {
		
		try {
			Payment payment = new Payment(); 
			
			if ( req.getVat() == null )	req.setVatForNull();
			Payments payments = modelMapper.map(req, Payments.class);
			
			//Payload�� ������ �����ͺ��̽����� Ecrypted ī�� ������ ����
			CardInfoModel cardInfo = modelMapper.map(req.getCard(), CardInfoModel.class);
			String encryptedCardInfo = commonUtil.getEncryptedCardNumber(cardInfo) ;
			payment.setEncyptedCardNum(encryptedCardInfo);
		
			payments.setPayment_type(PAYMENT);
			payment.addPayments(payments);
			payment = paymentRepository.save(payment);

			
			ResponsePayment res = modelMapper.map(payloadRepository.save(
					Payload
					.builder()
					.data(commonUtil.payloadConverter (
							PayloadModel
							.builder()
							.paymentType(PAYMENT)
							.pid(payment.getPid())
							.cardnum(req.getCard().getCardnum())
							.installment(req.getInstallment())
							.expires(req.getCard().getExpires())
							.cvc(req.getCard().getCvc())
							.price(String.valueOf(req.getPrice()))
							.vat(String.valueOf(req.getVat()))
							.aid(null)
							.encryptedCardNum(encryptedCardInfo)
							.temp(null)
							.build())
							)
					.build()
					), ResponsePayment.class);
			res.setPid(payment.getPid());
			
			return res;

		} catch (Exception e) {
			throw new ApiException(CommonEnum.ErrorCode.EXCEPTION_EXPIRATION, e.getMessage());
		} finally {
			
		}

		
	}
	
	@Override
	@Transactional
	@Lock(value = LockModeType.PESSIMISTIC_WRITE) 
	public ResponseCancel doCancel(RequestCancel req) throws Exception {
		try {

			Payment payment = paymentRepository.findById(req.getPid()).orElseThrow(()->new ApiException(CommonEnum.ErrorCode.EXCEPTION_EXPIRATION, "id�� ã�� �� �����ϴ�."));
			
			Integer totalPrice = paymentsRepository.selectTotalPrice(req.getPid());
			Integer totalVat = paymentsRepository.selectTotalVat(req.getPid());


			// ��� �ݾ��� ���� ���� �ݾװ� ������ ��, 
			if ( totalPrice - req.getPrice() == 0 ) {
				// Vat �� Null �϶�,
				if ( req.getVat() == null ) {
					req.setVat(totalVat);
				} else {
					// ��� �ݾ��� ���� Vat �ݾ׺��� ���� ��,
					if ( totalVat > req.getVat() ) {
						throw new ApiException(CommonEnum.ErrorCode.EXCEPTION_EXPIRATION, "�ΰ����ݾ��� ���� ����� �� �����ϴ�.");
					} else if ( totalVat < req.getVat()){
						throw new ApiException(CommonEnum.ErrorCode.EXCEPTION_EXPIRATION, "���� �ΰ����ݾ׺��� ��� �ΰ����� �� Ŀ ����� �� �����ϴ�.");
					}
				}
			} else if ( totalPrice > req.getPrice() && req.getVat() == null){
				req.setVatForNull();
			}

			Payments payments = modelMapper.map(req, Payments.class);
			if  (totalVat < req.getVat() ) throw new ApiException(CommonEnum.ErrorCode.EXCEPTION_EXPIRATION, "�ΰ����ݾ׺��� ��� �ΰ����� �� Ŀ ����� �� �����ϴ�.");
			else if (totalPrice <= 0 && totalVat <= 0 ) throw new ApiException(CommonEnum.ErrorCode.EXCEPTION_EXPIRATION, "������ �̹� ��ҵǾ����ϴ�.");
			else if (totalPrice < req.getPrice() ) throw new ApiException(CommonEnum.ErrorCode.EXCEPTION_EXPIRATION, "���� �����ݾ׺��� ��� �����ݾ��� �� Ŀ ����� �� �����ϴ�.");
			else if ( totalPrice > 0 && totalVat > 0 &&  totalPrice >= req.getPrice() && totalVat >= req.getVat() ) {
				payment.setUpdatedAt(null);

				payments.setPrice(String.valueOf(req.getPrice()));
				
				payments.setVat(String.valueOf(req.getVat()));
				payments.setPayment_type(CANCEL);

				payment.addPayments(payments);
				
				payment = paymentRepository.save(payment);
				
				List<Payments> paymentsList = payment.getPaymentsList();
				CardInfoModel cardInfo = modelMapper.map(commonUtil.getDecryptedCardNumber(payment.getEncyptedCardNum()), CardInfoModel.class);


				ResponseCancel res = modelMapper.map(payloadRepository.save(
						Payload
						.builder()
						.data(commonUtil.payloadConverter (
								PayloadModel
								.builder()
								.paymentType(CANCEL)
								.pid(paymentsList.get(paymentsList.size() - 1 ).getAid())
								.cardnum(cardInfo.getCardnum())
								.installment("00")
								.expires(cardInfo.getExpires())
								.cvc(cardInfo.getCvc())
								.price(String.valueOf(req.getPrice()))
								.vat(String.valueOf(req.getVat()))
								.aid(payment.getPid())
								.encryptedCardNum(payment.getEncyptedCardNum())
								.temp(null)
								.build())
								)
						.build()						
						), ResponseCancel.class);
				res.setPid(req.getPid());
				return res;
			} else {
				throw new ApiException(CommonEnum.ErrorCode.EXCEPTION_EXPIRATION, "����ġ ���� ����" );
			}

		} catch (Exception e) {
			throw new ApiException(CommonEnum.ErrorCode.EXCEPTION_EXPIRATION, e.getMessage());
		} finally {
			
		}

	}
	
	@Override
	@Transactional
	@Lock(value = LockModeType.PESSIMISTIC_WRITE) 
	public ResponseSearch doSearch(String req) throws Exception  {
		try {
			Payment payment = paymentRepository.findById(req).orElseThrow(()->new ApiException(CommonEnum.ErrorCode.EXCEPTION_EXPIRATION, "id�� ã�� �� �����ϴ�."));

			Integer totalPrice = paymentsRepository.selectTotalPrice(req);
			Integer totalVat = paymentsRepository.selectTotalVat(req);
			Integer cancelCount = paymentsRepository.countCacelPayment(req);
			
			String paymentStatus = "";
			if ( totalPrice > 0 && cancelCount == 0 ) paymentStatus = totalPrice + "�� ����";
			else if ( totalPrice > 0 && cancelCount > 0 ) paymentStatus = "��� " + cancelCount + "��" ;
			else if ( totalPrice == 0 ) paymentStatus = "��� �Ϸ�" ;

			CardInfoModel cardInfo = modelMapper.map(commonUtil.getDecryptedCardNumber(payment.getEncyptedCardNum()), CardInfoModel.class);
			cardInfo.setCardnum(commonUtil.getMaskedCardNumber(cardInfo.getCardnum()));
			List<Payments> arrayList = payment.getPaymentsList();
			List<ResponseForPayments> responseList = new ArrayList<ResponseForPayments>();
			
			for (Payments payments : arrayList ) {
				responseList.add(ResponseForPayments.builder()
						.aid(payments.getAid())
						.installment(payments.getInstallment())
						.price(payments.getPrice())
						.vat(payments.getVat())
						.payment_type(payments.getPayment_type())
						.build());
			}

			return ResponseSearch.builder()
					.pid(req)
					.cardinfo(cardInfo)
					.payment_status(paymentStatus)
					.payment_list(responseList)
					.current_price(String.valueOf(totalPrice))
					.current_vat(String.valueOf(totalVat))
					.build();
		} catch (Exception e) {
			throw new ApiException(CommonEnum.ErrorCode.EXCEPTION_EXPIRATION, e.getMessage());
			
		} finally {
			
		}

		
	}
}
