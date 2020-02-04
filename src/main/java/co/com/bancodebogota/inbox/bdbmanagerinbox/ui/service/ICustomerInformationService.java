package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service;

import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.CustomerInformationRequestDto;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;


public interface ICustomerInformationService {
	public ResponseEntity<JSONObject> infoByCustomer(CustomerInformationRequestDto input);

	ResponseEntity<JSONObject> formatterCustomer(org.json.JSONObject jsonObject);

	String validateCustomerType(int customerType);
}
