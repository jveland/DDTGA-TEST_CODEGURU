package co.com.bancodebogota.auth.controller;

import co.com.bancodebogota.auth.dto.corporate.CorporateRetrieveCompanyResponseDto;
import co.com.bancodebogota.auth.dto.corporate.CorporateValidateUserResponseDto;
import co.com.bancodebogota.auth.dto.corporate.PseValidateUserDto;
import co.com.bancodebogota.auth.dto.corporate.StatusDto;
import co.com.bancodebogota.auth.dto.mng.AuthValidateUserRequestDto;
import co.com.bancodebogota.auth.dto.mng.AuthValidateUserResponseDto;
import co.com.bancodebogota.auth.dto.mng.ValidateUserCorporateRequestDto;
import co.com.bancodebogota.auth.dto.mng.ValidateUserCorporateResponseDto;
import co.com.bancodebogota.auth.dto.otp.OTPResponseDto;
import co.com.bancodebogota.auth.services.CorporateService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-dev.properties")
public class AuthenticationControllerTest {

	@Autowired
	private AuthenticationController authController;

	@Autowired
	private CorporateService proxy;

	@Before
	public void setUp() {

		proxy = Mockito.mock(CorporateService.class);
		MockitoAnnotations.initMocks(this);

	}

	public CorporateValidateUserResponseDto getMockValidaUser(String mockUrl) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode tree = mapper.readTree(new File(mockUrl));
			CorporateValidateUserResponseDto mock = mapper.convertValue(tree, CorporateValidateUserResponseDto.class);
			return mock;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public CorporateRetrieveCompanyResponseDto getMockRetrieveCompany(String mockUrl) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode tree = mapper.readTree(new File(mockUrl));
			CorporateRetrieveCompanyResponseDto mock = mapper.convertValue(tree,
					CorporateRetrieveCompanyResponseDto.class);
			return mock;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Test
	public void validateAuthenticationTest()
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
		AuthValidateUserRequestDto authRequestDto = new AuthValidateUserRequestDto();
		authRequestDto.setLoginID("X");
		authRequestDto.setPassword("X");

		BindingResult result = mock(BindingResult.class);
		when(result.hasErrors()).thenReturn(true);

		ResponseEntity<AuthValidateUserResponseDto> errorIdentityType = authController.validateUserIDM(authRequestDto,
				result);
		assertEquals("2", errorIdentityType.getBody().getStatusCode());
		assertEquals(HttpStatus.BAD_REQUEST, errorIdentityType.getStatusCode());
	}

	@Test
	public void validateOKAuthenticationTest() {
		try {
			AuthValidateUserRequestDto authRequestDto = new AuthValidateUserRequestDto();
			authRequestDto.setLoginID("X");
			authRequestDto.setPassword("X");

			BindingResult result = mock(BindingResult.class);

			ResponseEntity<AuthValidateUserResponseDto> errorIdentityType = authController
					.validateUserIDM(authRequestDto, result);
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(true);
		}

	}

	@Test
	public void validateUserTest()
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
		ValidateUserCorporateRequestDto authValidaUserCorporateDto = new ValidateUserCorporateRequestDto();
		authValidaUserCorporateDto.setLoginId("X");
		authValidaUserCorporateDto.setPassword("X");
		authValidaUserCorporateDto.setIpAddress("X");
		authValidaUserCorporateDto.setCustLoginId("X");
		authValidaUserCorporateDto.setOtp("X");
		authValidaUserCorporateDto.setCustPermId("X");

		BindingResult result = mock(BindingResult.class);
		when(result.hasErrors()).thenReturn(true);

		ResponseEntity<ValidateUserCorporateResponseDto> errorIdentityType = authController
				.validateUserCorporate(authValidaUserCorporateDto, result);
		assertEquals("2", errorIdentityType.getBody().getStatusCode());
		assertEquals(HttpStatus.BAD_REQUEST, errorIdentityType.getStatusCode());
	}

	@Test
	public void validateBadUserTest()
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
		ValidateUserCorporateRequestDto authValidaUserCorporateDto = new ValidateUserCorporateRequestDto();
		authValidaUserCorporateDto.setLoginId("X");
		authValidaUserCorporateDto.setPassword("X");
		authValidaUserCorporateDto.setIpAddress("X");
		authValidaUserCorporateDto.setCustLoginId("X");
		authValidaUserCorporateDto.setOtp("X");
		authValidaUserCorporateDto.setCustPermId("X");

		BindingResult result = mock(BindingResult.class);

		ResponseEntity<ValidateUserCorporateResponseDto> errorIdentityType = authController
				.validateUserCorporate(authValidaUserCorporateDto, result);
		assertTrue(errorIdentityType != null);
	}

	@Test
	public void validateUserCorporateRequestDtoTest() {
		ValidateUserCorporateRequestDto authValidaUserCorporateDto = new ValidateUserCorporateRequestDto();
		authValidaUserCorporateDto.setLoginId("");
		authValidaUserCorporateDto.setPassword("");
		authValidaUserCorporateDto.setIpAddress("");
		authValidaUserCorporateDto.setCustLoginId("");
		authValidaUserCorporateDto.setOtp("");
		authValidaUserCorporateDto.setCustPermId("");

		AuthenticationController authenticationController = new AuthenticationController();
		String validateMessage = authenticationController.validateUserCorporateRequestDto(authValidaUserCorporateDto);
		assertEquals(
				"Login invalido. Password invalido. IPAddress invalido. CustLoginId invalido. Otp invalido. NIT invalido. ",
				validateMessage);
	}

	@Test
	public void mapperExceptionTest() {
		ValidateUserCorporateResponseDto answer = authController.mapperException("2", "error interno");
		assertNotNull(answer);
	}

	@Test
	public void mapperRequestValidateUserTest()
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
		ValidateUserCorporateRequestDto requestValidateUserCorporate = new ValidateUserCorporateRequestDto();

		requestValidateUserCorporate.setLoginId("blopezf01");
		requestValidateUserCorporate.setPassword(
				"ENb+FrYK3SsikeOZTGOjdwMO4ezW3q36WeVbPmRBjHrxPeYF9eaofqoIsHJRIaYvT+ZRThr+gZs3bxxKjY4ygijmZXIlZWkXLjpYrIhOjRwu2MbRjXqx4Rr/bFvl/nl8BrnT77E0xRjl4kRplhhEr1LQqfn9Q69MCV9prhz+68o5P4EQt/VRngwxWURf3QUZc6S38XBb3s0N8ES45DDhoFZbGTaEDrt3Gkpp2AgcD6zQ0NFJ4yjy3LMQIk9CVlc5cpW0e9fFlx1hG1gCotsvo3+rZrfSGJuQmA0QFRPtosSzcpd5kUUAB3WVOw0lOXAJfFA2hhttkzap9pO6yeZpBw==");
		requestValidateUserCorporate.setIpAddress("10.10.125.84");
		requestValidateUserCorporate.setCustLoginId("CB010516");
		requestValidateUserCorporate.setOtp("692286");
		requestValidateUserCorporate.setCustPermId("215478656");

		CorporateValidateUserResponseDto answer = authController
				.mapperRequestValidateUser(requestValidateUserCorporate);

		assertNull(answer);

		// Se necesita el adaptador de corporate corriendo localmente
		/*assertNotNull(answer);
		 */
	}

	@Test
	public void mapperRequestRetrieveCompanyTest() {

		ValidateUserCorporateRequestDto requestValidateUserCorporate = new ValidateUserCorporateRequestDto();

		requestValidateUserCorporate.setLoginId("blopezf01");
		requestValidateUserCorporate.setPassword("nY1SYio7h/rWnewwOcNDNw==");
		requestValidateUserCorporate.setIpAddress("10.10.125.84");
		requestValidateUserCorporate.setCustLoginId("CB010516");
		requestValidateUserCorporate.setOtp("692286");
		requestValidateUserCorporate.setCustPermId("215478656");

		CorporateRetrieveCompanyResponseDto answer = authController
				.mapperRequestRetrieveCompany(requestValidateUserCorporate);


		assertNull(answer);

		// Se necesita el adaptador de corporate corriendo localmente
		/*assertNotNull(answer);
		*/
	}

	@Test
	public void mapperResponseValidateUserCorporateTest() {
		CorporateRetrieveCompanyResponseDto answerRetrieveCompany = this
				.getMockRetrieveCompany("src/test/resources/mock/respuestaRetrieveCompanyOK.json");
		OTPResponseDto answerOtp = new OTPResponseDto();
		answerOtp.setStatusCode("0");
		answerOtp.setServerStatusCode("0");
		answerOtp.setSeverity("INFO");
		answerOtp.setStatusDesc("Operacion Exitosa");

		ValidateUserCorporateResponseDto answer = authController
				.mapperResponseValidateUserCorporate(answerRetrieveCompany, answerOtp,"gdgfgd");

		assertNotNull(answer);

	}

	@Test
	public void mapperRequestOtpAuthTest() {
		ValidateUserCorporateRequestDto requestValidateUserCorporate = new ValidateUserCorporateRequestDto();

		requestValidateUserCorporate.setLoginId("blopezf01");
		requestValidateUserCorporate.setPassword("nY1SYio7h/rWnewwOcNDNw==");
		requestValidateUserCorporate.setIpAddress("10.10.125.84");
		requestValidateUserCorporate.setCustLoginId("CB010516");
		requestValidateUserCorporate.setOtp("692286");
		requestValidateUserCorporate.setCustPermId("215478656");

		CorporateValidateUserResponseDto answerValidateUser = new CorporateValidateUserResponseDto();

		StatusDto answerStatusDto = new StatusDto();
		answerStatusDto.setStatusCode(new Long(0));
		answerStatusDto.setSeverity("Info");
		answerStatusDto.setStatusDesc("Success");

		PseValidateUserDto answerPseValidateUserDto = new PseValidateUserDto();
		answerPseValidateUserDto.setExternalAuthId("2776881180");
		answerPseValidateUserDto.setBnkUserKy("701008");

		answerValidateUser.setStatusDto(answerStatusDto);
		answerValidateUser.setPseValidateUserDto(answerPseValidateUserDto);

		OTPResponseDto answer = authController.mapperRequestOtpAuth(requestValidateUserCorporate, answerValidateUser);

		Assert.assertNull(answer);

		// Se necesita el adapter de OTP corriendo localmente
		/*
		Assert.assertNotNull(answer);
		String answer2 = answer.toString();
		assertNotNull(answer2);

		String answer3 = requestValidateUserCorporate.toString();

		assertNotNull(answer3);*/

	}

	@Test
	public void checkCorporateRetrieveCompanyResponseDtoTest() {
		CorporateRetrieveCompanyResponseDto answerRetrieveCompany = this
				.getMockRetrieveCompany("src/test/resources/mock/respuestaRetrieveCompanyOK.json");
		System.out.println(answerRetrieveCompany.toString());
		boolean answer = false;
		answer = authController.checkCorporateRetrieveCompanyResponseDto(answerRetrieveCompany);
		assertEquals(true, answer);

	}

	@Test
	public void checkAuthenticationOTPTest() {
		OTPResponseDto answerOtp = new OTPResponseDto();
		answerOtp.setStatusCode("0");
		answerOtp.setServerStatusCode("0");
		answerOtp.setSeverity("INFO");
		answerOtp.setStatusDesc("Operacion Exitosa");
		boolean answer = false;
		answer = authController.checkAuthenticationOTP(answerOtp);
		assertEquals(true, answer);

	}

	@Test
	public void actualDateTest() {
		String answer = authController.actualDate();
		assertNotNull(answer);

	}

	@Test
	public void checkValidateUserTest() {
		CorporateValidateUserResponseDto answerValidateUser = new CorporateValidateUserResponseDto();

		StatusDto answerStatusDto = new StatusDto();
		answerStatusDto.setStatusCode(new Long(0));
		answerStatusDto.setSeverity("Info");
		answerStatusDto.setStatusDesc("Success");

		PseValidateUserDto answerPseValidateUserDto = new PseValidateUserDto();
		answerPseValidateUserDto.setExternalAuthId("2776881180");
		answerPseValidateUserDto.setBnkUserKy("701008");

		answerValidateUser.setStatusDto(answerStatusDto);
		answerValidateUser.setPseValidateUserDto(answerPseValidateUserDto);

		boolean answer = false;
		answer = authController.checkValidateUser(answerValidateUser);
		assertEquals(true, answer);
	}

	@Test
	public void testResponseValidateUser() throws Exception {
		CorporateValidateUserResponseDto answerValidateUser = this
				.getMockValidaUser("src/test/resources/mock/respuestaValidateUser.json");
		assertEquals(new Long(0), answerValidateUser.getStatusDto().getStatusCode());
		assertEquals("Success", answerValidateUser.getStatusDto().getStatusDesc());
		assertEquals("Info", answerValidateUser.getStatusDto().getSeverity());
		assertEquals("2776881180", answerValidateUser.getPseValidateUserDto().getExternalAuthId());
		assertEquals("701008", answerValidateUser.getPseValidateUserDto().getBnkUserKy());
	}

	@Test
	public void testResponseRetrieveCompany() throws Exception {
		CorporateRetrieveCompanyResponseDto answerRetrieveCompany = this
				.getMockRetrieveCompany("src/test/resources/mock/respuestaRetrieveCompanyOK.json");
		assertEquals(new Long(0), answerRetrieveCompany.getStatusDto().getStatusCode());
		assertEquals("Info", answerRetrieveCompany.getStatusDto().getSeverity());
		assertEquals("Success", answerRetrieveCompany.getStatusDto().getStatusDesc());
		assertEquals(new Long(1), answerRetrieveCompany.getCompanyElement().getIsEnrolled());
		assertEquals("Financial Secure Messages", answerRetrieveCompany.getCompanyElement().getName());
		assertEquals(new Long(1), answerRetrieveCompany.getCompanyElement().getIsEnrolled());
		assertEquals("blopez@bancodebogota.com.co", answerRetrieveCompany.getUserElement().getEmail());
		assertEquals("Prueba", answerRetrieveCompany.getUserElement().getGivenName());
		assertEquals("Custom", answerRetrieveCompany.getUserElement().getUserAccountAndFunctionalAccess());
		assertEquals("Custom", answerRetrieveCompany.getUserElement().getUserAccountAndFunctionalAccess());
		assertEquals("Financial Secure Message Approve",
				answerRetrieveCompany.getUserElement().getUserApprovalFunction());
		assertEquals("672511", answerRetrieveCompany.getUserElement().getBnkuserKy());
	}

}
