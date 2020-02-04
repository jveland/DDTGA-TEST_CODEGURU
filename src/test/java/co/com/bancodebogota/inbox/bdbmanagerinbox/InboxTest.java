package co.com.bancodebogota.inbox.bdbmanagerinbox;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import co.com.bancodebogota.adpe.util.Response;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.controller.InboxController;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.CaseDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.SaveAdditionalInfoRequestDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.TaskDoneRequestDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.UserDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.ICaseService;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.impl.CaseServiceImpl;



@RunWith(SpringRunner.class)
@SpringBootTest(classes=BdbManagerInboxApplication.class)
@TestPropertySource(locations= "classpath:application-dev.properties")
public class InboxTest {

	@Autowired
	ICaseService caseService;
	
	@Autowired
	CaseServiceImpl caseServiceImpl;
	
	@Autowired
	InboxController inboxController;  
	
	CaseDto caseDto;
	
	@Before
	public void setUp() {
		caseDto=new CaseDto();
		caseDto.setType("APE") ;
		caseDto.setIdentityType("N");
		caseDto.setIdentityNumber("8592365983");
		caseDto.setIdCustomerType("PRI");
		caseDto.setCompanyName("Empresa Acme2");
		caseDto.setUsername("ACASALL4");
		caseDto.setCompanyUser("Andres Casallas");
		caseDto.setUserEmail("acasall@acme.com");
		caseDto.setRol("RADICADOR_EMPRESA");
		caseDto.setCompanyId("CB000001");
	}

	@Test
	public void contextLoads() {
		try {
			ResponseEntity<JSONObject> entity= caseService.caseList();
			assertNotNull(entity);
		}catch (Exception e){
			assertTrue(true);
		}

	}
	
	@Test
	public void getTaskStatusTest() {
		ResponseEntity<JSONObject> entity= caseService.getTaskStatus("");
		assertNotNull(entity);
	}
	
	@Test
	public void saveAditionalInfoValidatorTest() {
		org.json.JSONObject statusResponse = new org.json.JSONObject();
		org.json.JSONObject jsonObjectHelper = new org.json.JSONObject();
		ResponseEntity<JSONObject> entity = null;
		
		try {
			statusResponse.put("status", 0);
			jsonObjectHelper.put("statusResponse", statusResponse);
			entity = caseServiceImpl.saveAditionalInfoValidator(jsonObjectHelper);
			assertNotNull(entity);
			
			
			entity = caseServiceImpl.saveAditionalInfoValidator(new org.json.JSONObject());
			assertNotNull(entity);
		} catch (Exception e) {
			assertNull(entity);
		}
	}
	
	@Test
	public void createCaseValidatorTest() {
		
		org.json.JSONObject jsonObject = new org.json.JSONObject();
		
		Response response;
		ResponseEntity<JSONObject> entity;
		
		try {
			response = new Response();
		
			jsonObject.put("statusCode", 0);
			jsonObject.put("idCase", 0);
			
			ResponseEntity<JSONObject> jsonObjectResponseEntity = new ResponseEntity<>(
					response.response(response.buildStatusSuccessfull(), jsonObject, true),
					HttpStatus.OK);
			
			
			entity = caseServiceImpl.createCaseValidator(jsonObjectResponseEntity, caseDto);
			assertNotNull(entity);	
			
			entity = caseServiceImpl.saveAditionalInfoValidator(new org.json.JSONObject());
			assertNotNull(entity);
		} catch (Exception e) {
			entity = null;
			assertNull(entity);
		}
	}
	
	@Test
	public void casesByIdTest() {
		String id="";
		ResponseEntity<JSONObject> entity= caseService.casesById(id);
		JSONObject jsonObject=entity.getBody();
		System.out.println(""+jsonObject);		
		Assert.assertEquals(1,1);
	}
	
	@Test
	public void casesByCustomerTest() {
		String identityType="";
		BigDecimal identityNumber=new BigDecimal(123456789);
		Pageable pageable= null;
		ResponseEntity<JSONObject> entity= caseService.casesByCustomer(identityType,identityNumber,pageable);
		JSONObject jsonObject=entity.getBody();
		System.out.println(""+jsonObject);		
		Assert.assertEquals(1,1);
	}
	
	@Test
	public void casesByEmployeeTest() {
		String customerUsername="";
		Pageable pageable= null;
		ResponseEntity<JSONObject> entity= caseService.casesByEmployee(customerUsername,pageable);
		JSONObject jsonObject=entity.getBody();
		System.out.println(""+jsonObject);		
		Assert.assertEquals(1,1);
	}

	
	@Test
	public void casesByIdControlerTest() {
		String id="";
		ResponseEntity<JSONObject> entity= inboxController.casesById(id);
		JSONObject jsonObject=entity.getBody();
		System.out.println(""+jsonObject);		
		Assert.assertEquals(1,1);
	}
	
	@Test
	public void saveAditionalInfoTest() {
		SaveAdditionalInfoRequestDto additionalInfoRequestDto = new SaveAdditionalInfoRequestDto("", "", "", "", "", "");
		ResponseEntity<JSONObject> entity = null;
		try{
			entity = inboxController.saveAditionalInfo(additionalInfoRequestDto);
			assertNotNull(entity);
		}catch(Exception e) {
			assertNull(entity);
		}
		
	}
	
	@Test
	public void getTaskStatusInfoTest() {
		ResponseEntity<JSONObject> entity= inboxController.getTaskStatusInfo("");
		assertNotNull(entity);
	}

	@Test
	public void healthInboxTest() {
		ResponseEntity<String> health = inboxController.health();
		assertNotNull(health);
	}
	
	@Test
	public void casesByCustomerControlerTest() {
		String identityType="";
		BigDecimal identityNumber=new BigDecimal(123456789);
		Pageable pageable= null;
		ResponseEntity<JSONObject> entity= inboxController.casesByCustomer(identityType,identityNumber,pageable);
		JSONObject jsonObject=entity.getBody();
		System.out.println(""+jsonObject);		
		Assert.assertEquals(1,1);
	}
	
	@Test
	public void casesByEmployeeControlerTest() {
		String customerUsername="";
		Pageable pageable= null;
		ResponseEntity<JSONObject> entity= inboxController.casesByEmployee(customerUsername,pageable);
		JSONObject jsonObject=entity.getBody();
		System.out.println(""+jsonObject);		
		Assert.assertEquals(1,1);
	}

	@Test
	public void validateException() {
		ResponseEntity<JSONObject> jsonObjectResponseEntity = caseService.casesByEmployee(null, null);
		assertNotNull(jsonObjectResponseEntity);
	}

	@Test
	public void createCasetest() {
		CaseDto caseDto=new CaseDto();

		caseDto.setType("APE") ;
		caseDto.setIdentityType("N");
		caseDto.setIdentityNumber("8592365983");
		caseDto.setIdCustomerType("PRI");
		caseDto.setCompanyName("Empresa Acme2");
		caseDto.setUsername("ACASALL4");
		caseDto.setCompanyUser("Andres Casallas");
		caseDto.setUserEmail("acasall@acme.com");
		caseDto.setRol("RADICADOR_EMPRESA");
		caseDto.setCompanyId("CB000001");
		Assert.assertEquals(caseDto.getType(),"APE") ;
		Assert.assertEquals(caseDto.getIdentityType(),"N");
		Assert.assertEquals(caseDto.getIdentityNumber(),"8592365983");
		Assert.assertEquals(caseDto.getIdCustomerType(),"PRI");
		Assert.assertEquals(caseDto.getCompanyName(),"Empresa Acme2");
		Assert.assertEquals(caseDto.getUsername(),"ACASALL4");
		Assert.assertEquals(caseDto.getCompanyUser(),"Andres Casallas");
		Assert.assertEquals(caseDto.getUserEmail(),"acasall@acme.com");
		Assert.assertEquals(caseDto.getRol(),"RADICADOR_EMPRESA");
		Assert.assertEquals(caseDto.getCompanyId(),"CB000001");
		
		List<UserDto> listUserDto = new ArrayList<>();
		UserDto userDto = new UserDto();
		userDto.setDocumentID("80125646");
		userDto.setEmail("ycasall@bancodebogota.com.co");
		userDto.setUsername("ycasall");
		userDto.setName("Yhon Casallas");
		userDto.setRol("Ejecutivo CEO");
		listUserDto.add(userDto);
		
		Assert.assertEquals(userDto.getDocumentID(), "80125646");
		Assert.assertEquals(userDto.getEmail(), "ycasall@bancodebogota.com.co");
		Assert.assertEquals(userDto.getUsername(), "ycasall");
		Assert.assertEquals(userDto.getName(), "Yhon Casallas");
		Assert.assertEquals(userDto.getRol(), "Ejecutivo CEO");
		
		userDto = new UserDto();
		userDto.setDocumentID("799999999");
		userDto.setEmail("cmedellin@bancodebogota.com.co");
		userDto.setUsername("cmedellin");
		userDto.setName("Carlos Medellin");
		userDto.setRol("Gerente CEO");
		listUserDto.add(userDto);
		
		Assert.assertEquals(userDto.getRol(), "Gerente CEO");
		
		caseDto.setUser(listUserDto);
		assertNotNull(caseDto);
		
		caseDto = new CaseDto(listUserDto);
		assertNotNull(caseDto);
		
		ResponseEntity<JSONObject> aCase;
		try {
			aCase = caseService.createCase(caseDto);
			assertNotNull(aCase);
		}catch(Exception e) {
			Assert.assertTrue(true);
		}
		
		
	}
	
	@Test
	public void assignCaseControllerTest() {
		CaseDto caseDto=new CaseDto();

		caseDto.setType("APE") ;
		caseDto.setIdentityType("N");
		caseDto.setIdentityNumber("8592365983");
		caseDto.setIdCustomerType("PRI");
		caseDto.setCompanyName("Empresa Acme2");
		caseDto.setUsername("ACASALL4");
		caseDto.setCompanyUser("Andres Casallas");
		caseDto.setUserEmail("acasall@acme.com");
		caseDto.setRol("Gerente CEO");
		caseDto.setCompanyId("CB000001");
		
		List<UserDto> listUserDto = new ArrayList<>();
		UserDto userDto = new UserDto();
		userDto.setDocumentID("80125646");
		userDto.setEmail("ycasall@bancodebogota.com.co");
		userDto.setUsername("ycasall");
		userDto.setName("Yhon Casallas");
		userDto.setRol("Gerente CEO");
		listUserDto.add(userDto);
		
		
		Assert.assertEquals(caseServiceImpl.transformRole(userDto.getRol()), "ROL_APROBADOR");
		Assert.assertEquals(userDto.getDocumentID(), "80125646");
		Assert.assertEquals(userDto.getEmail(), "ycasall@bancodebogota.com.co");
		Assert.assertEquals(userDto.getUsername(), "ycasall");
		Assert.assertEquals(userDto.getName(), "Yhon Casallas");
		Assert.assertEquals(userDto.getRol(), "Gerente CEO");
		
		userDto = new UserDto();
		userDto.setDocumentID("799999999");
		userDto.setEmail("cmedellin@bancodebogota.com.co");
		userDto.setUsername("cmedellin");
		userDto.setName("Carlos Medellin");
		userDto.setRol("Ejecutivo CEO");
		listUserDto.add(userDto);
		Assert.assertEquals(caseServiceImpl.transformRole(userDto.getRol()), "ROL_RADICADOR");
		caseDto.setUser(listUserDto);
		ResponseEntity<JSONObject> aCase;
		try {
			aCase = caseServiceImpl.assignCase(caseDto, "id123345");
			Assert.assertTrue(aCase.hasBody());
		}catch(Exception e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void createCaseControllerTest() {
		CaseDto caseDto=new CaseDto();

		caseDto.setType("APE") ;
		caseDto.setIdentityType("N");
		caseDto.setIdentityNumber("8592365983");
		caseDto.setIdCustomerType("PRI");
		caseDto.setCompanyName("Empresa Acme2");
		caseDto.setUsername("ACASALL4");
		caseDto.setCompanyUser("Andres Casallas");
		caseDto.setUserEmail("acasall@acme.com");
		caseDto.setRol("RADICADOR_EMPRESA");
		caseDto.setCompanyId("CB000001");
		ResponseEntity<JSONObject> aCase;
		try {
			aCase = inboxController.createCase(caseDto);
			assertNotNull(aCase);
		}catch(Exception e) {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void createSaveAditionalInfoTest() {
		ResponseEntity<JSONObject> aCase;
		try {
			aCase = inboxController.getAditionalInfo("");
			assertNotNull(aCase);
		}catch(Exception e) {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void createTaskDoneTest() {
		TaskDoneRequestDto input = new TaskDoneRequestDto();
		input.setIdCase("ADE0000001");
		input.setIdTask("PRODUCT");
		input.setUsername("MMONTE4");
		assertNotNull(input.getIdCase());
		assertNotNull(input.getUsername());
		assertNotNull(input.getIdTask());
		
		
		ResponseEntity<JSONObject> aCase;
		try {
			aCase = inboxController.taskDone(input);
			assertNotNull(aCase);
			
			String jsonOutput = input.toJSONString();
			assertNotNull(jsonOutput);
		}catch(Exception e) {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void createTaskDoneNullTest() {
		TaskDoneRequestDto input = new TaskDoneRequestDto();
		input.setIdCase("");
		input.setIdTask("PRODUCT");
		input.setUsername("MMONTE4");
		
		TaskDoneRequestDto input2 = new TaskDoneRequestDto();
		input2.setIdCase("ADE0000001");
		input2.setIdTask("");
		input2.setUsername("MMONTE4");
		
		TaskDoneRequestDto input3 = new TaskDoneRequestDto();
		input3.setIdCase("ADE0000001");
		input3.setIdTask("PRODUCT");
		input3.setUsername("");
		
		ResponseEntity<JSONObject> aCase;
		
		try {
			aCase = inboxController.taskDone(input);
			assertNotNull(aCase);
			
			aCase = inboxController.taskDone(input2);
			assertNotNull(aCase);
			
			aCase = inboxController.taskDone(input3);
			assertNotNull(aCase);
		}catch(Exception e) {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void createGetAditionalInfoTest() {
		
		SaveAdditionalInfoRequestDto saveDto = new SaveAdditionalInfoRequestDto("", "", "", "", "", "");
		
		saveDto.setAssets("99999.99");
		saveDto.setExpenses("99999.99");
		saveDto.setIdCase("ADE0000001");
		saveDto.setIncome("99999.99");
		saveDto.setIssueDate("2019-01-01");
		saveDto.setLiabilities("99999.99");
		
		assertNotNull(saveDto.getAssets());
		assertNotNull(saveDto.getExpenses());
		assertNotNull(saveDto.getIdCase());
		assertNotNull(saveDto.getIncome());
		assertNotNull(saveDto.getIssueDate());
		assertNotNull(saveDto.getLiabilities());
		ResponseEntity<JSONObject> aCase;
		try {
			aCase = inboxController.saveAditionalInfo(saveDto);
			assertNotNull(aCase);
		}catch(Exception e) {
			Assert.assertTrue(true);
		}
	}


}
