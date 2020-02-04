package co.com.bancodebogota.inbox.bdbmanagerinbox;

import co.com.bancodebogota.inbox.bdbmanagerinbox.BdbManagerInboxApplication;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.controller.CustomerInformationController;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.CustomerInformationRequestDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.ICustomerInformationService;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BdbManagerInboxApplication.class)
@TestPropertySource(locations = "classpath:application-dev.properties")
public class CustomerTest {

	@Autowired
	ICustomerInformationService service;

	@Autowired
	CustomerInformationController controller;

	@Before
	public void setUp() {

	}

	@Test
	public void validateCustomerTypeTest() {
		assertEquals("PRI", service.validateCustomerType(614));
		assertEquals("PRI", service.validateCustomerType(615));
		assertEquals("OFI", service.validateCustomerType(617));
		assertEquals("FIN", service.validateCustomerType(618));
		assertEquals("SOC", service.validateCustomerType(1559));
	}

	@Test
	public void customerTest() {
		ResponseEntity<JSONObject> entity = controller.formatterCustomer(new JSONObject());
		JSONObject jsonObject = entity.getBody();
		assertNotNull(jsonObject);
	}

	@Test
	public void customerCreateTest() {

		ResponseEntity<JSONObject> entity = null;

		try {
			org.json.JSONObject commercialArea = new org.json.JSONObject();

			commercialArea.put("segmentId", 614);

			org.json.JSONObject orgClient = new org.json.JSONObject();
			orgClient.put("typeId", "");
			orgClient.put("participantId", "");
			orgClient.put("commercialArea", "");
			orgClient.put("legalName", "");
			orgClient.put("commercialArea", commercialArea);

			org.json.JSONObject custOrgInfoInqRs = new org.json.JSONObject();
			custOrgInfoInqRs.put("orgClient", orgClient);

			org.json.JSONObject jsonObject = new org.json.JSONObject();
			jsonObject.put("custOrgInfoInqRs", custOrgInfoInqRs);

			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject input = null;
			try {
				input = (JSONObject) parser.parse(jsonObject.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			entity = controller.formatterCustomer(input);
			assertNotNull(entity);
		} catch (JSONException e) {
			assertNull(entity);
		}
	}

	@Test
	public void customerInfoBadRequest() {
		CustomerInformationRequestDto customerInformationRequestDto = new CustomerInformationRequestDto();
		customerInformationRequestDto.setCustomerUserName("edoria1");
		customerInformationRequestDto.setIdentityNumber("12345678");
		customerInformationRequestDto.setIdentityType("N");
		ResponseEntity<JSONObject> entity = service.infoByCustomer(customerInformationRequestDto);
		Assert.assertEquals("edoria1", customerInformationRequestDto.getCustomerUserName());
		Assert.assertEquals("12345678", customerInformationRequestDto.getIdentityNumber());
		Assert.assertEquals("N", customerInformationRequestDto.getIdentityType());
		assertNotNull(customerInformationRequestDto.toString());
		assertNotNull(entity);

	}

	@Test
	public void customerInformationTest() {
		CustomerInformationRequestDto customerInformationRequestDto = new CustomerInformationRequestDto();
		customerInformationRequestDto.setCustomerUserName("edoria1");
		customerInformationRequestDto.setIdentityNumber("12345678");
		customerInformationRequestDto.setIdentityType("N");

		BindingResult bd = mock(BindingResult.class);
		ResponseEntity<JSONObject> entity = controller.customerInformation(customerInformationRequestDto, bd);
		Assert.assertEquals("edoria1", customerInformationRequestDto.getCustomerUserName());
		Assert.assertEquals("12345678", customerInformationRequestDto.getIdentityNumber());
		Assert.assertEquals("N", customerInformationRequestDto.getIdentityType());
		assertNotNull(customerInformationRequestDto.toString());
		assertNotNull(entity);

	}

	@Test
	public void customerInformationMaskTest() {
		CustomerInformationRequestDto customerInformationRequestDto = new CustomerInformationRequestDto();
		customerInformationRequestDto.setCustomerUserName("edoria1");
		customerInformationRequestDto.setIdentityNumber("12345678");
		customerInformationRequestDto.setIdentityType("N");

		BindingResult bd = mock(BindingResult.class);
		ResponseEntity<JSONObject> entity = controller.customerInformationTest(customerInformationRequestDto, bd);
		assertNotNull(customerInformationRequestDto.toString());
		assertNotNull(entity);

	}

	@Test
	public void customerInformationExceptionTest() {
		CustomerInformationRequestDto customerInformationRequestDto = new CustomerInformationRequestDto();
		customerInformationRequestDto.setCustomerUserName(null);
		customerInformationRequestDto.setIdentityNumber("12345678");
		customerInformationRequestDto.setIdentityType("N");

		BindingResult bd = mock(BindingResult.class);
		ResponseEntity<JSONObject> entity = controller.customerInformation(customerInformationRequestDto, bd);
		assertNotNull(entity);

		bd = mock(BindingResult.class);
		entity = controller.customerInformation(customerInformationRequestDto, bd);
		assertNotNull(entity);

	}

	@Test
	public void customerInformationSpaceUsernameTest() {
		CustomerInformationRequestDto customerInformationRequestDto = new CustomerInformationRequestDto();
		customerInformationRequestDto.setCustomerUserName("       ");
		customerInformationRequestDto.setIdentityNumber("12345678");
		customerInformationRequestDto.setIdentityType("N");

		BindingResult bd = mock(BindingResult.class);
		ResponseEntity<JSONObject> entity = controller.customerInformation(customerInformationRequestDto, bd);
		assertTrue(entity.hasBody());

	}

	@Test
	public void customerInformationSpaceIdentitySpaceTest() {
		CustomerInformationRequestDto customerInformationRequestDto = new CustomerInformationRequestDto();
		customerInformationRequestDto.setCustomerUserName("eerere");
		customerInformationRequestDto.setIdentityNumber("    ");
		customerInformationRequestDto.setIdentityType("N");

		BindingResult bd = mock(BindingResult.class);
		ResponseEntity<JSONObject> entity = controller.customerInformation(customerInformationRequestDto, bd);
		assertTrue(entity.hasBody());

	}

	@Test
	public void customerInformationSpaceIdentityNumberSpaceTest() {
		CustomerInformationRequestDto customerInformationRequestDto = new CustomerInformationRequestDto();
		customerInformationRequestDto.setCustomerUserName("eerere");
		customerInformationRequestDto.setIdentityNumber("12312312");
		customerInformationRequestDto.setIdentityType(" ");

		BindingResult bd = mock(BindingResult.class);
		ResponseEntity<JSONObject> entity = controller.customerInformation(customerInformationRequestDto, bd);
		assertTrue(entity.hasBody());

	}

	@Test
	public void customerInformationExceptionMaskTest() {
		CustomerInformationRequestDto customerInformationRequestDto = new CustomerInformationRequestDto();
		customerInformationRequestDto.setCustomerUserName(null);
		customerInformationRequestDto.setIdentityNumber("12345678");
		customerInformationRequestDto.setIdentityType("N");

		BindingResult bd = mock(BindingResult.class);
		ResponseEntity<JSONObject> entity = controller.customerInformation(customerInformationRequestDto, bd);
		assertNotNull(entity);

		bd = mock(BindingResult.class);
		entity = controller.customerInformationTest(customerInformationRequestDto, bd);
		assertNotNull(entity);

	}

	@Test
	public void customerInformationSpaceUsernameMaskTest() {
		CustomerInformationRequestDto customerInformationRequestDto = new CustomerInformationRequestDto();
		customerInformationRequestDto.setCustomerUserName("       ");
		customerInformationRequestDto.setIdentityNumber("12345678");
		customerInformationRequestDto.setIdentityType("N");

		BindingResult bd = mock(BindingResult.class);
		ResponseEntity<JSONObject> entity = controller.customerInformationTest(customerInformationRequestDto, bd);
		assertTrue(entity.hasBody());

	}

	@Test
	public void customerInformationSpaceIdentitySpaceMaskTest() {
		CustomerInformationRequestDto customerInformationRequestDto = new CustomerInformationRequestDto();
		customerInformationRequestDto.setCustomerUserName("eerere");
		customerInformationRequestDto.setIdentityNumber("    ");
		customerInformationRequestDto.setIdentityType("N");

		BindingResult bd = mock(BindingResult.class);
		ResponseEntity<JSONObject> entity = controller.customerInformationTest(customerInformationRequestDto, bd);
		assertTrue(entity.hasBody());

	}

	@Test
	public void customerInformationSpaceIdentityNumberSpaceMaskTest() {
		CustomerInformationRequestDto customerInformationRequestDto = new CustomerInformationRequestDto();
		customerInformationRequestDto.setCustomerUserName("eerere");
		customerInformationRequestDto.setIdentityNumber("12312312");
		customerInformationRequestDto.setIdentityType(" ");

		BindingResult bd = mock(BindingResult.class);
		ResponseEntity<JSONObject> entity = controller.customerInformationTest(customerInformationRequestDto, bd);
		assertTrue(entity.hasBody());

	}

	@Test
	public void healthControlerTest() {
		ResponseEntity<String> entity = controller.health();
		String jsonObject = entity.getBody();
		System.out.println("" + jsonObject);
		Assert.assertEquals("Health Ok!", jsonObject);
	}

}
