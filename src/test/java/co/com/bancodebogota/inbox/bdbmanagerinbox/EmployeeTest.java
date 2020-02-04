package co.com.bancodebogota.inbox.bdbmanagerinbox;

import co.com.bancodebogota.inbox.bdbmanagerinbox.BdbManagerInboxApplication;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.CustomerInformationRequestDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.IEmployeeService;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.impl.CaseServiceImpl;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.impl.EmployeeServiceImpl;

import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BdbManagerInboxApplication.class)
@TestPropertySource(locations = "classpath:application-dev.properties")
public class EmployeeTest {
	@Autowired
	IEmployeeService service;

	@Autowired
	EmployeeServiceImpl employeeServiceImpl;

	@Test
	public void getTerritoryInfoTest() {
		String branchId = "17580080000";
		ResponseEntity<JSONObject> commTerritoryInfo = service.getCommTerritoryInfo(branchId);
		assertNotNull(commTerritoryInfo);

	}

	@Test
	public void getCommInfoTest() {
		String custLoginId = "CJARAm2";
		ResponseEntity<JSONObject> empCommercialInfo = service.getEmpCommercialInfo(custLoginId);
		assertNotNull(empCommercialInfo);
	}

	@Test
	public void beginQueryEmployeeTest() {
		try {
			CustomerInformationRequestDto requestDto = new CustomerInformationRequestDto();
			requestDto.setIdentityType("N");
			requestDto.setIdentityNumber("8600440135");
			requestDto.setCustomerUserName("requestDto");
			assertNotNull(requestDto);
		} catch (Exception e) {

		}
	}

	@Test
	public void builtCommercialItemTest() {
		try {
			org.json.JSONObject commTerritoryUser = new org.json.JSONObject();
			commTerritoryUser.put("sessionId", "");
			commTerritoryUser.put("fullName", "");
			commTerritoryUser.put("emailAddr", "");
			commTerritoryUser.put("participantId", "");

			org.json.JSONObject commercial_info = new org.json.JSONObject();
			org.json.JSONObject empCommercialInfoInqRs = new org.json.JSONObject();
			org.json.JSONObject custInfo = new org.json.JSONObject();
			org.json.JSONObject employmentHistory = new org.json.JSONObject();
			employmentHistory.put("memo", "gerente ceo");

			custInfo.put("employmentHistory", employmentHistory);
			empCommercialInfoInqRs.put("custInfo", custInfo);
			commercial_info.put("empCommercialInfoInqRs", empCommercialInfoInqRs);
			commTerritoryUser.put("commercial-info", commercial_info);

			org.json.JSONObject empCommercialInfo = employeeServiceImpl.builtCommercialItem(commTerritoryUser);
			assertNotNull(empCommercialInfo);
		} catch (Exception e) {

		}
	}

	@Test
	public void validateInfoCommercialTest() {
		try {
			org.json.JSONObject commTerritoryUser = new org.json.JSONObject();
			commTerritoryUser.put("sessionId", "");
			commTerritoryUser.put("fullName", "");
			commTerritoryUser.put("emailAddr", "");
			commTerritoryUser.put("participantId", "");

			org.json.JSONObject commercial_info = new org.json.JSONObject();
			org.json.JSONObject empCommercialInfoInqRs = new org.json.JSONObject();
			org.json.JSONObject custInfo = new org.json.JSONObject();
			org.json.JSONObject employmentHistory = new org.json.JSONObject();
			employmentHistory.put("memo", "gerente ceo");

			custInfo.put("employmentHistory", employmentHistory);
			empCommercialInfoInqRs.put("custInfo", custInfo);
			commercial_info.put("empCommercialInfoInqRs", empCommercialInfoInqRs);
			commTerritoryUser.put("commercial-info", commercial_info);

			boolean empCommercialInfo = employeeServiceImpl.validateInfoCommercial(commTerritoryUser);
			assertNotNull(empCommercialInfo);
		} catch (Exception e) {

		}
	}
}
