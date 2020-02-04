package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service;

import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.CustomerInformationRequestDto;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

public interface IEmployeeService {
  ResponseEntity<JSONObject>  getCommTerritoryInfo(String branchId);

  ResponseEntity<JSONObject> getEmpCommercialInfo(String custLoginId);

  ResponseEntity<JSONObject> beginQueryEmployee(CustomerInformationRequestDto requestDto, boolean b);


}

