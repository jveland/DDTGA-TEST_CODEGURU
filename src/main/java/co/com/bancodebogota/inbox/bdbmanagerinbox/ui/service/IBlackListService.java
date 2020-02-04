package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service;

import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.BasicInfoCustomer;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.CustomerInformationRequestDto;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBlackListService {
  public ResponseEntity<org.json.simple.JSONObject> beginCRM(CustomerInformationRequestDto customers);
  public ResponseEntity<org.json.simple.JSONObject> formatterToBlackList(JSONObject customerInformation);
  public ResponseEntity<org.json.simple.JSONObject> validateBussiness(List<BasicInfoCustomer> customers);

}
