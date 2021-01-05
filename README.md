# KAKAO과제 - 카드 결제/취소/조회 REST API

  #### 개발 환경
  -  OS : Window10 / IDE : STS4 / DBMS : H2 / Spring Boot, JPA, Gradle..

## 프로젝트 목적
-  사용자로 부터 카드 결제/취소/조회 요청을 받아 카드사가 요청하는 데이터 명세에 결제 및 취소 데이터를 전송하는 API 개발
-  유니크한 관리자 번호로 데이터들을 관리할 수 있는 시스템 개발
-  명세와 제약조건에 맞는 정확한 데이터를 안전 ( 동시성 제어 ) 하게 전달


## 요구 사항 Check List
	[V] 결제/부분취소/조회 API 
	[V] 부분취소 및 부가가치세 로직 구현
	[V] 카드 암/복호화
	[V] 트랜잭션 데이터 관리 
	[-] 멀티쓰레드 방어 코드 ( 테스트를 진행하지 않음 )
	[X] 단위 테스트
	
----------------------------------------

## 데이터 설계

![20210104](https://user-images.githubusercontent.com/76725143/103522550-e30d0980-4ebd-11eb-8ccd-65f76cd170a6.png)

	-1:N 양방향 맵핑을 통하여 암호화된 카드와 결제 혹은 취소 정보들을 가지고있는 구조
	-Payload는 외부에 나가는 데이터로 간주하여 따로 맵핑관계를 두진 않음
	-Seqeunce generating을 통하여 관리번호는 PAYMENT210104XXX... 결제 번호는 APROVAL210104XXX... 로 관리함
	-CRUD에 있어 JPA Hibernate를 쓰고 복잡한 것은 직접 쿼리를 썼음
![image](https://user-images.githubusercontent.com/76725143/103523784-bd80ff80-4ebf-11eb-8152-8590c2cf684a.png)


## API 설계

결제, 취소 , 조회에 대한 API 설계

	POST	/kakaopay/payment/v1/
	PUT	/kakaopay/payment/v1/
	GET	/kakaopay/payment/v1/{pid} 
	
   ####	1.결제 예시
	POST	/kakaopay/payment/v1/

   입력 값 : 
   
   	
	{"card":{"cardnum":"41111111111","expires":"0199","cvc":"111"},"price":"1000","vat":"55","installment":"11"}

   응답 값 :
   
	{
		"pid": "PAYMENT_2101040003",
		"data": " 446PAYMENT PAYMENT_2101040003 41111111111 110199111 10000000000055null cSSWv9ybMJvlHSCUScBkSNOeJWH22QH7QNh6jxYkPkA= "
	}   		
   ####	2.취소 예시
	PUT	/kakaopay/payment/v1/

   입력 값 :
   	
	{"pid":"PAYMENT_2101040003","price":"1000","vat":""}
	
   응답 값 : 
   	
	{
		"pid": "PAYMENT_2101040003",
		"data": " 446CANCEL APPROVAL_2101040005 41111111111 000199111 10000000000055PAYMENT_2101040003 cSSWv9ybMJvlHSCUScBkSNOeJWH22QH7QNh6jxYkPkA= "
	}
   ####	3.조회 예시
	GET	/kakaopay/payment/v1/{pid}

   입력 값 :
   	
	GET	/kakaopay/payment/v1/PAYMENT_2101040003
	
   응답 값 : 
   	
	{
	"pid": "PAYMENT_2101040003",
	"cardinfo": {
	"cardnum": "411111**111",
	"expires": "0199",
	"cvc": "111"
	},
	"payment_status": "취소 완료",
	"current_price": "0",
	"current_vat": "0",
	"payment_list": [
	  {
	"aid": "APPROVAL_2101040004",
	"installment": "11",
	"price": "1000",
	"vat": "55",
	"payment_type": "PAYMENT"
	},
	  {
	"aid": "APPROVAL_2101040005",
	"installment": "00",
	"price": "1000",
	"vat": "55",
	"payment_type": "CANCEL"
	}
	],
	}
	---------------------------
	{
	"pid": "PAYMENT_2101040002",
	"cardinfo": {
	"cardnum": "411111**111",
	"expires": "0199",
	"cvc": "111"
	},
	"payment_status": "취소 2건",
	"current_price": "200",
	"current_vat": "88",
	"payment_list": [
	  {
	"aid": "APPROVAL_2101040002",
	"installment": "11",
	"price": "1000",
	"vat": "90",
	"payment_type": "PAYMENT"
	},
	  {
	"aid": "APPROVAL_2101040003",
	"installment": "00",
	"price": "400",
	"vat": "1",
	"payment_type": "CANCEL"
	},
	  {
	"aid": "APPROVAL_2101040004",
	"installment": "00",
	"price": "400",
	"vat": "1",
	"payment_type": "CANCEL"
	}
	],
	}

에러에 대하여 간단한 메세징 처리를 할수있도록 구현

![image](https://user-images.githubusercontent.com/76725143/103525689-01293880-4ec3-11eb-8a6a-8d21f7546399.png)

![image](https://user-images.githubusercontent.com/76725143/103526107-b8be4a80-4ec3-11eb-90d2-8be9e52415b7.png)



## To-Do List
- 에러 메세징 조금 더 깔끔하고 구체적인 정보 처리 ( 몇몇 데이터들은 커스터마이징하지않은 Exception을 그래로 송출
- Select for Update Locking 코드 방어 테스트
- Swagger를 이용하여 Api document 만들고 Hateoas를 만족시켜 Restful하게 변경
- 단위 테스트를 통하여 조금더 안정적인 코드 구현
- 주석 처리
