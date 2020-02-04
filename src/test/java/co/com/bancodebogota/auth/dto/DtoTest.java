package co.com.bancodebogota.auth.dto;

import co.com.bancodebogota.auth.dto.corporate.CorporateRetrieveCompanyRequestDto;
import co.com.bancodebogota.auth.dto.idm.IDMRequestDto;
import co.com.bancodebogota.auth.dto.idm.IDMResponseDto;
import co.com.bancodebogota.auth.dto.idm.Info;
import co.com.bancodebogota.auth.dto.mng.AuthValidateUserRequestDto;
import co.com.bancodebogota.auth.dto.mng.AuthValidateUserResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:application-dev.properties")
public class DtoTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void validateAuthReqDTO() {
		AuthValidateUserRequestDto authReq = new AuthValidateUserRequestDto();
		authReq.setLoginID("123456");
		authReq.setPassword("1234567");
		assertEquals("123456", authReq.getLoginID());
		assertEquals("1234567", authReq.getPassword());
	}
	
	@Test
	public void validateAuthResDTO() {
		AuthValidateUserResponseDto authRes = new AuthValidateUserResponseDto();
		authRes.setStatusCode("0");
		authRes.setStatusDesc("Test");
		authRes.setUserEmail("test@test.com");
		authRes.setUserName("test name");
		authRes.setLoginDT("201901011200");
		assertEquals("0", authRes.getStatusCode());
		assertEquals("Test", authRes.getStatusDesc());
		assertEquals("test@test.com", authRes.getUserEmail());
		assertEquals("test name", authRes.getUserName());
		assertEquals("201901011200", authRes.getLoginDT());
	}
	
	@Test
	public void validateIDMReqDTO() {
		IDMRequestDto idmReq = new IDMRequestDto();
		idmReq.setPassword("123456");
		idmReq.setUser("1234567");
		assertEquals("1234567", idmReq.getUser());
		assertEquals("123456", idmReq.getPassword());
	}
	
	@Test
	public void validateIDMResDTO() {
		IDMResponseDto idmRes = new IDMResponseDto();
		Info info = new Info();
	
		info.setEmployeeid("0");
		info.setFullName("Test Test");
		info.setMail("test@test.com");
		idmRes.setStatusDesc("test");
		info.setUserName("testuser");
		assertEquals("0", info.getEmployeeid());
		assertEquals("Test Test", info.getFullName());
		assertEquals("test@test.com", info.getMail());
		assertEquals("test", idmRes.getStatusDesc());
		assertEquals("testuser", info.getUserName());
		
	}
	
	@Test
	public void CorporateRetrieveCompanyRequestDtoTest() {
		CorporateRetrieveCompanyRequestDto dto = new CorporateRetrieveCompanyRequestDto();
		dto.setCustLoginId("X");
		dto.setUserId("Y");
		assertTrue(dto.getCustLoginId().equals("X"));
		assertTrue(dto.getUserId().equals("Y"));
		assertTrue(!dto.toString().isEmpty());
	}
	



}
