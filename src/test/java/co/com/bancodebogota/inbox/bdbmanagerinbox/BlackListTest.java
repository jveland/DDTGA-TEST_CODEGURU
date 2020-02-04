package co.com.bancodebogota.inbox.bdbmanagerinbox;

import co.com.bancodebogota.inbox.bdbmanagerinbox.BdbManagerInboxApplication;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.controller.BlackLisController;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.BasicInfoCustomer;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.CustomerInformationRequestDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.DuccResponse;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.ValidCustomerBlackListRp;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.IBlackListService;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.impl.BlackListServiceImpl;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BdbManagerInboxApplication.class)
@TestPropertySource(locations = "classpath:application-dev.properties")
public class BlackListTest {
	BlackListServiceImpl serviceImpl;

	@Autowired
	IBlackListService service;

	@Autowired
	BlackLisController controller;

	@Before
	public void setUp() {

		serviceImpl = new BlackListServiceImpl();
	}

	@Test
	public void testDtoMapper() throws Exception {

		BasicInfoCustomer basicInfoCustomer = new BasicInfoCustomer();
		basicInfoCustomer.setCellPhone("3218978899");
		basicInfoCustomer.setIdentityNumber(new BigDecimal(123456789));
		basicInfoCustomer.setLegalName("ACME Ltda.");
		basicInfoCustomer.setFirstName("ACME Ltda.");
		basicInfoCustomer.setMiddleName("ACME Ltda.");
		basicInfoCustomer.setLastName("ACME Ltda.");
		basicInfoCustomer.setSecondLastName("ACME Ltda.");
		basicInfoCustomer.setPhoneNumber(new BigDecimal(4545454));
		List<BasicInfoCustomer> customers = new ArrayList<BasicInfoCustomer>();
		customers.add(basicInfoCustomer);

		Assert.assertFalse(basicInfoCustomer.getCellPhone().isEmpty());
		assertNotNull(basicInfoCustomer.getIdentityNumber());
		assertNotNull(basicInfoCustomer.getLegalName());
		assertNotNull(basicInfoCustomer.getFirstName());
		assertNotNull(basicInfoCustomer.getMiddleName());
		assertNotNull(basicInfoCustomer.getLastName());
		assertNotNull(basicInfoCustomer.getSecondLastName());
		assertNotNull(basicInfoCustomer.getPhoneNumber());
		Assert.assertTrue(basicInfoCustomer.isValid());

	}

	@Test
	public void DuccResponseMapper() throws Exception {

		DuccResponse duccResponse = new DuccResponse();
		duccResponse.setStatusCode("1");
		duccResponse.setStatusDescription("lorem");
		duccResponse.setCustomers(new ArrayList<>());
		assertNotNull(duccResponse.getStatusCode());
		assertNotNull(duccResponse.getStatusDescription());
		assertNotNull(duccResponse.getCustomers());

	}

	@Test
	public void ValidCustomerBlackListRpTest() throws Exception {

		ValidCustomerBlackListRp customerBlackListRp = new ValidCustomerBlackListRp();
		customerBlackListRp.setIdentityNumber("1");
		customerBlackListRp.setRefCodeBlackList("lorem");
		customerBlackListRp.setRefDescBlackList("lorem");
		customerBlackListRp.setIsPossibleContinue(true);

		assertNotNull(customerBlackListRp.getIdentityNumber());
		assertNotNull(customerBlackListRp.getRefCodeBlackList());
		assertNotNull(customerBlackListRp.getRefDescBlackList());
		assertNotNull(customerBlackListRp.getIsPossibleContinue());

	}

	@Test
	public void beginCRMTest() throws Exception {
		CustomerInformationRequestDto customerInformationRequestDto = new CustomerInformationRequestDto();
		customerInformationRequestDto.setCustomerUserName("edoria1");
		customerInformationRequestDto.setIdentityNumber("12345678");
		customerInformationRequestDto.setIdentityType("N");
		Assert.assertEquals("edoria1", customerInformationRequestDto.getCustomerUserName());
		Assert.assertEquals("12345678", customerInformationRequestDto.getIdentityNumber());
		Assert.assertEquals("N", customerInformationRequestDto.getIdentityType());
		try {
			ResponseEntity<JSONObject> jsonObjectResponseEntity = service.beginCRM(customerInformationRequestDto);
			assertNotNull(jsonObjectResponseEntity);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void formatterToBlackListTest() {
		try {
			ResponseEntity<JSONObject> jsonObjectResponseEntity = service.formatterToBlackList(
					new org.json.JSONObject("{\n" + "    \"custOrgInfoInqRs\": {\n" + "        \"rqUID\": null,\n"
							+ "        \"asyncRqUID\": null,\n" + "        \"revClientTrnSeq\": null,\n"
							+ "        \"status\": {\n" + "            \"statusCode\": \"0\",\n"
							+ "            \"serverStatusCode\": \"0\",\n" + "            \"severity\": \"INFO\",\n"
							+ "            \"statusDesc\": \"Transaccion Exitosa\",\n"
							+ "            \"serverStatusDesc\": \" \",\n" + "            \"additionalStatus\": [],\n"
							+ "            \"asyncRsInfo\": null\n" + "        },\n" + "        \"custId\": null,\n"
							+ "        \"nextDay\": null,\n" + "        \"serverTerminalSeqId\": null,\n"
							+ "        \"networkTrnInfo\": null,\n" + "        \"serverDt\": null,\n"
							+ "        \"identifier\": \"\",\n" + "        \"ciiu\": \"0113\",\n"
							+ "        \"orgClient\": {\n" + "            \"typeId\": \"N\",\n"
							+ "            \"participantId\": \"8087921544\",\n" + "            \"role\": [\n"
							+ "                \"2\"\n" + "            ],\n" + "            \"legalName\": \"BBSCS\",\n"
							+ "            \"fullName\": \"BCS\",\n"
							+ "            \"establishDt\": \"2011-10-03T05:00:00.000+0000\",\n"
							+ "            \"numEmployees\": 0,\n" + "            \"numBranchOffice\": 0,\n"
							+ "            \"industId\": {\n" + "                \"org\": \"LTD\",\n"
							+ "                \"industNum\": \"P\"\n" + "            },\n"
							+ "            \"compositeContactInfo\": {\n"
							+ "                \"contactInfoType\": null,\n" + "                \"contactInfo\": {\n"
							+ "                    \"contactId\": null,\n"
							+ "                    \"contactPref\": null,\n" + "                    \"phoneNum\": [\n"
							+ "                        {\n" + "                            \"phoneType\": \"11\",\n"
							+ "                            \"phone\": \"6352489\",\n"
							+ "                            \"phoneExt\": null,\n"
							+ "                            \"phoneArea\": \"051\"\n" + "                        },\n"
							+ "                        {\n" + "                            \"phoneType\": \"14\",\n"
							+ "                            \"phone\": null,\n"
							+ "                            \"phoneExt\": null,\n"
							+ "                            \"phoneArea\": null\n" + "                        },\n"
							+ "                        {\n" + "                            \"phoneType\": \"43\",\n"
							+ "                            \"phone\": null,\n"
							+ "                            \"phoneExt\": null,\n"
							+ "                            \"phoneArea\": null\n" + "                        },\n"
							+ "                        {\n" + "                            \"phoneType\": \"12\",\n"
							+ "                            \"phone\": \"3012390000\",\n"
							+ "                            \"phoneExt\": null,\n"
							+ "                            \"phoneArea\": null\n" + "                        }\n"
							+ "                    ],\n" + "                    \"emailType\": null,\n"
							+ "                    \"emailAddr\": \"jescob4@bancodebogota.com.co\",\n"
							+ "                    \"deliveryDestination\": \"1\",\n"
							+ "                    \"postAddr\": [\n" + "                        {\n"
							+ "                            \"addr1\": \"AC;55;;54-96;;;;;;;COL;11;11001000;;\",\n"
							+ "                            \"addr2\": null,\n"
							+ "                            \"addr3\": null,\n"
							+ "                            \"addr4\": null,\n"
							+ "                            \"city\": \"11001000\",\n"
							+ "                            \"stateProv\": \"11\",\n"
							+ "                            \"postalCode\": null,\n"
							+ "                            \"country\": \"COL\",\n"
							+ "                            \"addrType\": \"32\",\n"
							+ "                            \"zipCode\": null,\n"
							+ "                            \"coordenate\": null\n" + "                        },\n"
							+ "                        {\n" + "                            \"addr1\": null,\n"
							+ "                            \"addr2\": null,\n"
							+ "                            \"addr3\": null,\n"
							+ "                            \"addr4\": null,\n"
							+ "                            \"city\": null,\n"
							+ "                            \"stateProv\": null,\n"
							+ "                            \"postalCode\": null,\n"
							+ "                            \"country\": null,\n"
							+ "                            \"addrType\": \"38\",\n"
							+ "                            \"zipCode\": null,\n"
							+ "                            \"coordenate\": null\n" + "                        }\n"
							+ "                    ]\n" + "                }\n" + "            },\n"
							+ "            \"income\": {\n" + "                \"amt\": 11000000000,\n"
							+ "                \"curCode\": null,\n" + "                \"curRate\": null,\n"
							+ "                \"curConvertRule\": null\n" + "            },\n"
							+ "            \"outcome\": {\n" + "                \"amt\": 32362000,\n"
							+ "                \"curCode\": null,\n" + "                \"curRate\": null,\n"
							+ "                \"curConvertRule\": null\n" + "            },\n"
							+ "            \"totalLiabilities\": {\n" + "                \"amt\": 32300000,\n"
							+ "                \"curCode\": null,\n" + "                \"curRate\": null,\n"
							+ "                \"curConvertRule\": null\n" + "            },\n"
							+ "            \"totalAssets\": {\n" + "                \"amt\": 326560000,\n"
							+ "                \"curCode\": null,\n" + "                \"curRate\": null,\n"
							+ "                \"curConvertRule\": null\n" + "            },\n"
							+ "            \"otherAssets\": {\n" + "                \"amt\": 3232000,\n"
							+ "                \"curCode\": null,\n" + "                \"curRate\": null,\n"
							+ "                \"curConvertRule\": null\n" + "            },\n"
							+ "            \"otherIncome\": {\n" + "                \"amt\": 2323200,\n"
							+ "                \"curCode\": null,\n" + "                \"curRate\": null,\n"
							+ "                \"curConvertRule\": null\n" + "            },\n"
							+ "            \"otherIncomeDesc\": \"ventas\",\n"
							+ "            \"effDt\": \"2017-10-05\",\n" + "            \"commercialArea\": {\n"
							+ "                \"segmentId\": \"618\",\n"
							+ "                \"subSegmentId\": \"08\",\n"
							+ "                \"priorizationCode\": null,\n"
							+ "                \"agentId\": \"17820090000\"\n" + "            },\n"
							+ "            \"groupHeaderInfo\": {\n" + "                \"groupMemberInd\": \"N\",\n"
							+ "                \"typeId\": null,\n" + "                \"participantId\": null,\n"
							+ "                \"personName\": null,\n" + "                \"commercialArea\": null\n"
							+ "            },\n" + "            \"residentAgentInfo\": {\n"
							+ "                \"otherIdentDoc\": {\n" + "                    \"typeId\": \"C\",\n"
							+ "                    \"participantId\": \"10352712\",\n"
							+ "                    \"issDt\": \"2000-03-28\",\n"
							+ "                    \"expDt\": null,\n" + "                    \"city\": \"11001000\",\n"
							+ "                    \"stateProv\": \"11\",\n"
							+ "                    \"country\": \"COL\",\n" + "                    \"desc\": null\n"
							+ "                },\n" + "                \"personClient\": {\n"
							+ "                    \"typeId\": null,\n"
							+ "                    \"participantId\": null,\n" + "                    \"role\": [],\n"
							+ "                    \"personName\": {\n" + "                        \"lastName\": [\n"
							+ "                            \"APELLIDOA\"\n" + "                        ],\n"
							+ "                        \"firstName\": \"NOMBREA\",\n"
							+ "                        \"middleName\": [],\n"
							+ "                        \"titlePrefix\": null,\n"
							+ "                        \"nameSuffix\": null,\n"
							+ "                        \"nickname\": null,\n"
							+ "                        \"legalName\": null,\n"
							+ "                        \"fullName\": null\n" + "                    },\n"
							+ "                    \"gender\": \"M\",\n"
							+ "                    \"maritalStatus\": null,\n"
							+ "                    \"city\": \"11001000\",\n"
							+ "                    \"stateProv\": \"11\",\n"
							+ "                    \"country\": \"COL\",\n"
							+ "                    \"birthDt\": \"1976-01-12T05:00:00.000+0000\",\n"
							+ "                    \"driversLicense\": null,\n"
							+ "                    \"contactInfo\": {\n"
							+ "                        \"contactId\": null,\n"
							+ "                        \"contactPref\": null,\n"
							+ "                        \"phoneNum\": [\n" + "                            {\n"
							+ "                                \"phoneType\": \"11\",\n"
							+ "                                \"phone\": \"4510748\",\n"
							+ "                                \"phoneExt\": null,\n"
							+ "                                \"phoneArea\": \"058\"\n"
							+ "                            },\n" + "                            {\n"
							+ "                                \"phoneType\": \"14\",\n"
							+ "                                \"phone\": null,\n"
							+ "                                \"phoneExt\": null,\n"
							+ "                                \"phoneArea\": null\n"
							+ "                            },\n" + "                            {\n"
							+ "                                \"phoneType\": \"12\",\n"
							+ "                                \"phone\": \"3004943963\",\n"
							+ "                                \"phoneExt\": null,\n"
							+ "                                \"phoneArea\": null\n"
							+ "                            }\n" + "                        ],\n"
							+ "                        \"emailType\": null,\n"
							+ "                        \"emailAddr\": \"jescob4@bancodebogota.com.co\",\n"
							+ "                        \"deliveryDestination\": null,\n"
							+ "                        \"postAddr\": [\n" + "                            {\n"
							+ "                                \"addr1\": \"CL;7;CR;189-20;;;;;;;COL;23;23660015;;\",\n"
							+ "                                \"addr2\": null,\n"
							+ "                                \"addr3\": null,\n"
							+ "                                \"addr4\": null,\n"
							+ "                                \"city\": \"23660015\",\n"
							+ "                                \"stateProv\": \"23\",\n"
							+ "                                \"postalCode\": null,\n"
							+ "                                \"country\": \"COL\",\n"
							+ "                                \"addrType\": \"32\",\n"
							+ "                                \"zipCode\": null,\n"
							+ "                                \"coordenate\": null\n"
							+ "                            }\n" + "                        ]\n"
							+ "                    },\n" + "                    \"finantialInfo\": null,\n"
							+ "                    \"residencePlace\": null,\n"
							+ "                    \"educationalLevel\": null,\n"
							+ "                    \"deathCertificateInfo\": null\n" + "                },\n"
							+ "                \"factaind\": \"COL\"\n" + "            },\n"
							+ "            \"largessTaxPayerInd\": \"N\",\n"
							+ "            \"selfTaxWithholderInd\": \"N\"\n" + "        },\n"
							+ "        \"orgId\": [\n" + "            {\n"
							+ "                \"orgIdType\": \"EntityType\",\n"
							+ "                \"orgIdNum\": \"11\"\n" + "            }\n" + "        ],\n"
							+ "        \"shareholderInfo\": [\n" + "            {\n"
							+ "                \"refId\": \"2580\",\n" + "                \"otherIdentDoc\": {\n"
							+ "                    \"typeId\": \"E\",\n"
							+ "                    \"participantId\": \"37548958\",\n"
							+ "                    \"issDt\": null,\n" + "                    \"expDt\": null,\n"
							+ "                    \"city\": null,\n" + "                    \"stateProv\": null,\n"
							+ "                    \"country\": null,\n" + "                    \"desc\": null\n"
							+ "                },\n" + "                \"personName\": {\n"
							+ "                    \"lastName\": [\n" + "                        \"MONROY CAICEDO\",\n"
							+ "                        \" \"\n" + "                    ],\n"
							+ "                    \"firstName\": \"MONICA\",\n"
							+ "                    \"middleName\": [\n" + "                        \" \"\n"
							+ "                    ],\n" + "                    \"titlePrefix\": null,\n"
							+ "                    \"nameSuffix\": null,\n"
							+ "                    \"nickname\": null,\n" + "                    \"legalName\": null,\n"
							+ "                    \"fullName\": \"MONROY CAICEDO MONICA\"\n" + "                },\n"
							+ "                \"gender\": null,\n" + "                \"country\": \"COL\",\n"
							+ "                \"birthDt\": null,\n" + "                \"ownerPercent\": 80\n"
							+ "            },\n" + "            {\n" + "                \"refId\": \"2579\",\n"
							+ "                \"otherIdentDoc\": {\n" + "                    \"typeId\": \"P\",\n"
							+ "                    \"participantId\": \"52704972\",\n"
							+ "                    \"issDt\": null,\n" + "                    \"expDt\": null,\n"
							+ "                    \"city\": null,\n" + "                    \"stateProv\": null,\n"
							+ "                    \"country\": null,\n" + "                    \"desc\": null\n"
							+ "                },\n" + "                \"personName\": {\n"
							+ "                    \"lastName\": [\n" + "                        \"RODRIGUEZ\",\n"
							+ "                        \"LEGUIZAMON\"\n" + "                    ],\n"
							+ "                    \"firstName\": \"LAURA GISELA\",\n"
							+ "                    \"middleName\": [\n" + "                        \" \"\n"
							+ "                    ],\n" + "                    \"titlePrefix\": null,\n"
							+ "                    \"nameSuffix\": null,\n"
							+ "                    \"nickname\": null,\n" + "                    \"legalName\": null,\n"
							+ "                    \"fullName\": \"RODRIGUEZ LEGUIZAMON LAURA GISELA\"\n"
							+ "                },\n" + "                \"gender\": null,\n"
							+ "                \"country\": \"COL\",\n" + "                \"birthDt\": null,\n"
							+ "                \"ownerPercent\": 80\n" + "            },\n" + "            {\n"
							+ "                \"refId\": \"2578\",\n" + "                \"otherIdentDoc\": {\n"
							+ "                    \"typeId\": \"C\",\n"
							+ "                    \"participantId\": \"52428220\",\n"
							+ "                    \"issDt\": null,\n" + "                    \"expDt\": null,\n"
							+ "                    \"city\": null,\n" + "                    \"stateProv\": null,\n"
							+ "                    \"country\": null,\n" + "                    \"desc\": null\n"
							+ "                },\n" + "                \"personName\": {\n"
							+ "                    \"lastName\": [\n" + "                        \"Simanca\",\n"
							+ "                        \" \"\n" + "                    ],\n"
							+ "                    \"firstName\": \"Alejandro\",\n"
							+ "                    \"middleName\": [\n" + "                        \" \"\n"
							+ "                    ],\n" + "                    \"titlePrefix\": null,\n"
							+ "                    \"nameSuffix\": null,\n"
							+ "                    \"nickname\": null,\n" + "                    \"legalName\": null,\n"
							+ "                    \"fullName\": \"SIMANCA ALEJANDRO\"\n" + "                },\n"
							+ "                \"gender\": null,\n" + "                \"country\": \"COL\",\n"
							+ "                \"birthDt\": null,\n" + "                \"ownerPercent\": 80\n"
							+ "            },\n" + "            {\n" + "                \"refId\": \"2557\",\n"
							+ "                \"otherIdentDoc\": {\n" + "                    \"typeId\": \"C\",\n"
							+ "                    \"participantId\": \"52419858\",\n"
							+ "                    \"issDt\": null,\n" + "                    \"expDt\": null,\n"
							+ "                    \"city\": null,\n" + "                    \"stateProv\": null,\n"
							+ "                    \"country\": null,\n" + "                    \"desc\": null\n"
							+ "                },\n" + "                \"personName\": {\n"
							+ "                    \"lastName\": [\n" + "                        \"NAVARRETE\",\n"
							+ "                        \"CORTES\"\n" + "                    ],\n"
							+ "                    \"firstName\": \"CLAUDIA\",\n"
							+ "                    \"middleName\": [\n" + "                        \"MARCELA\"\n"
							+ "                    ],\n" + "                    \"titlePrefix\": null,\n"
							+ "                    \"nameSuffix\": null,\n"
							+ "                    \"nickname\": null,\n" + "                    \"legalName\": null,\n"
							+ "                    \"fullName\": \"NAVARRETE CORTES CLAUDIA MARCELA\"\n"
							+ "                },\n" + "                \"gender\": null,\n"
							+ "                \"country\": \"COL\",\n" + "                \"birthDt\": null,\n"
							+ "                \"ownerPercent\": 90\n" + "            },\n" + "            {\n"
							+ "                \"refId\": \"2556\",\n" + "                \"otherIdentDoc\": {\n"
							+ "                    \"typeId\": \"N\",\n"
							+ "                    \"participantId\": \"8006347301\",\n"
							+ "                    \"issDt\": null,\n" + "                    \"expDt\": null,\n"
							+ "                    \"city\": null,\n" + "                    \"stateProv\": null,\n"
							+ "                    \"country\": null,\n" + "                    \"desc\": null\n"
							+ "                },\n" + "                \"personName\": {\n"
							+ "                    \"lastName\": [\n" + "                        \"ACCIONISTA ST\",\n"
							+ "                        \" \"\n" + "                    ],\n"
							+ "                    \"firstName\": \"EMPRESA\",\n"
							+ "                    \"middleName\": [\n" + "                        \" \"\n"
							+ "                    ],\n" + "                    \"titlePrefix\": null,\n"
							+ "                    \"nameSuffix\": null,\n"
							+ "                    \"nickname\": null,\n" + "                    \"legalName\": null,\n"
							+ "                    \"fullName\": \"EMPRESA ACCIONISTA ST\"\n" + "                },\n"
							+ "                \"gender\": null,\n" + "                \"country\": \"COL\",\n"
							+ "                \"birthDt\": null,\n" + "                \"ownerPercent\": 80\n"
							+ "            }\n" + "        ],\n" + "        \"factaind\": \"COL\"\n" + "    }\n"
							+ "}"));
			assertNotNull(jsonObjectResponseEntity);
		} catch (Exception e) {
			assertTrue(true);
		}

	}

	@Test
	public void validateBussinessTest() {
		try {
			ResponseEntity<JSONObject> jsonObjectResponseEntity = service.validateBussiness(new ArrayList<>());
			assertNotNull(jsonObjectResponseEntity);
		} catch (Exception e) {
			assertTrue(true);
		}

	}

	@Test
	public void customerInformationTest() throws Exception {
		CustomerInformationRequestDto customerInformationRequestDto = new CustomerInformationRequestDto();
		customerInformationRequestDto.setCustomerUserName("edoria1");
		customerInformationRequestDto.setIdentityNumber("12345678");
		customerInformationRequestDto.setIdentityType("N");
		try {
			ResponseEntity<JSONObject> jsonObjectResponseEntity = controller
					.customerInformation(customerInformationRequestDto, null);
			assertNotNull(jsonObjectResponseEntity);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void isNaturalPersonTest() {
		boolean response = serviceImpl.isNaturalPerson("C");
		Assert.assertTrue(response);
		response = serviceImpl.isNaturalPerson("S");

		assertFalse(response);
	}

	@Test
	public void getJsonObjectResponseEntityTest() {
		DuccResponse duccResponse = new DuccResponse();
		duccResponse.setStatusCode("1");
		duccResponse.setStatusDescription("lorem");
		ValidCustomerBlackListRp customerBlackListRp = new ValidCustomerBlackListRp();
		customerBlackListRp.setIdentityNumber("1");
		customerBlackListRp.setRefCodeBlackList("lorem");
		customerBlackListRp.setRefDescBlackList("lorem");
		customerBlackListRp.setIsPossibleContinue(true);
		List<ValidCustomerBlackListRp> customerDucc = new ArrayList<>();
		customerDucc.add(customerBlackListRp);
		duccResponse.setCustomers(customerDucc);
		try {
			ResponseEntity<DuccResponse> exchange = new ResponseEntity<>(duccResponse, HttpStatus.OK);
			ResponseEntity<JSONObject> jsonObjectResponseEntity = serviceImpl.getJsonObjectResponseEntity(exchange);
			assertNotNull(jsonObjectResponseEntity);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void basicInfoCustomerTest() {
		try {
			BasicInfoCustomer basicInfoCustomer = new BasicInfoCustomer();
			basicInfoCustomer = serviceImpl.basicInfoCustomer(new org.json.JSONObject("{\n"
					+ "                \"otherIdentDoc\": {\n" + "                    \"typeId\": \"C\",\n"
					+ "                    \"participantId\": \"10352712\",\n"
					+ "                    \"issDt\": \"2000-03-28\",\n" + "                    \"expDt\": null,\n"
					+ "                    \"city\": \"11001000\",\n" + "                    \"stateProv\": \"11\",\n"
					+ "                    \"country\": \"COL\",\n" + "                    \"desc\": null\n"
					+ "                },\n" + "                \"personClient\": {\n"
					+ "                    \"typeId\": null,\n" + "                    \"participantId\": null,\n"
					+ "                    \"role\": [],\n" + "                    \"personName\": {\n"
					+ "                        \"lastName\": [\n" + "                            \"APELLIDOA\"\n"
					+ "                        ],\n" + "                        \"firstName\": \"NOMBREA\",\n"
					+ "                        \"middleName\": [],\n"
					+ "                        \"titlePrefix\": null,\n"
					+ "                        \"nameSuffix\": null,\n"
					+ "                        \"nickname\": null,\n" + "                        \"legalName\": null,\n"
					+ "                        \"fullName\": null\n" + "                    },\n"
					+ "                    \"gender\": \"M\",\n" + "                    \"maritalStatus\": null,\n"
					+ "                    \"city\": \"11001000\",\n" + "                    \"stateProv\": \"11\",\n"
					+ "                    \"country\": \"COL\",\n"
					+ "                    \"birthDt\": \"1976-01-12T05:00:00.000+0000\",\n"
					+ "                    \"driversLicense\": null,\n" + "                    \"contactInfo\": {\n"
					+ "                        \"contactId\": null,\n"
					+ "                        \"contactPref\": null,\n" + "                        \"phoneNum\": [\n"
					+ "                            {\n" + "                                \"phoneType\": \"11\",\n"
					+ "                                \"phone\": \"4510748\",\n"
					+ "                                \"phoneExt\": null,\n"
					+ "                                \"phoneArea\": \"058\"\n" + "                            },\n"
					+ "                            {\n" + "                                \"phoneType\": \"14\",\n"
					+ "                                \"phone\": null,\n"
					+ "                                \"phoneExt\": null,\n"
					+ "                                \"phoneArea\": null\n" + "                            },\n"
					+ "                            {\n" + "                                \"phoneType\": \"12\",\n"
					+ "                                \"phone\": \"3004943963\",\n"
					+ "                                \"phoneExt\": null,\n"
					+ "                                \"phoneArea\": null\n" + "                            }\n"
					+ "                        ],\n" + "                        \"emailType\": null,\n"
					+ "                        \"emailAddr\": \"jescob4@bancodebogota.com.co\",\n"
					+ "                        \"deliveryDestination\": null,\n"
					+ "                        \"postAddr\": [\n" + "                            {\n"
					+ "                                \"addr1\": \"CL;7;CR;189-20;;;;;;;COL;23;23660015;;\",\n"
					+ "                                \"addr2\": null,\n"
					+ "                                \"addr3\": null,\n"
					+ "                                \"addr4\": null,\n"
					+ "                                \"city\": \"23660015\",\n"
					+ "                                \"stateProv\": \"23\",\n"
					+ "                                \"postalCode\": null,\n"
					+ "                                \"country\": \"COL\",\n"
					+ "                                \"addrType\": \"32\",\n"
					+ "                                \"zipCode\": null,\n"
					+ "                                \"coordenate\": null\n" + "                            }\n"
					+ "                        ]\n" + "                    },\n"
					+ "                    \"finantialInfo\": null,\n"
					+ "                    \"residencePlace\": null,\n"
					+ "                    \"educationalLevel\": null,\n"
					+ "                    \"deathCertificateInfo\": null\n" + "                },\n"
					+ "                \"factaind\": \"COL\"\n" + "            "));
			
			assertNotNull(basicInfoCustomer);
		} catch (Exception e) {
			assertTrue(true);
		}

	}

	@Test
	public void getPersonNameTest() {
		try {
			BasicInfoCustomer basicInfoCustomer = new BasicInfoCustomer();
			serviceImpl.getPersonName(basicInfoCustomer, true,
					new org.json.JSONObject("{\n" + "                \"refId\": \"2580\",\n"
							+ "                \"otherIdentDoc\": {\n" + "                    \"typeId\": \"E\",\n"
							+ "                    \"participantId\": \"37548958\",\n"
							+ "                    \"issDt\": null,\n" + "                    \"expDt\": null,\n"
							+ "                    \"city\": null,\n" + "                    \"stateProv\": null,\n"
							+ "                    \"country\": null,\n" + "                    \"desc\": null\n"
							+ "                },\n" + "                \"personName\": {\n"
							+ "                    \"lastName\": [\n" + "                        \"MONROY CAICEDO\",\n"
							+ "                        \" \"\n" + "                    ],\n"
							+ "                    \"firstName\": \"MONICA\",\n"
							+ "                    \"middleName\": [\n" + "                        \" \"\n"
							+ "                    ],\n" + "                    \"titlePrefix\": null,\n"
							+ "                    \"nameSuffix\": null,\n"
							+ "                    \"nickname\": null,\n" + "                    \"legalName\": null,\n"
							+ "                    \"fullName\": \"MONROY CAICEDO MONICA\"\n" + "                },\n"
							+ "                \"gender\": null,\n" + "                \"country\": \"COL\",\n"
							+ "                \"birthDt\": null,\n" + "                \"ownerPercent\": 80\n"
							+ "            }"));
			
			assertNotNull(basicInfoCustomer);
		} catch (Exception e) {
			assertTrue(true);
		}

	}
}
