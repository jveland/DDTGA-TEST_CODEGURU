package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

public interface IInformationMaskService {
  ResponseEntity<JSONObject> consumeServices(String nameEntity, boolean isAll, String idEntity);
  org.json.JSONObject beginMapperData(org.json.JSONObject jsonObject);


}
