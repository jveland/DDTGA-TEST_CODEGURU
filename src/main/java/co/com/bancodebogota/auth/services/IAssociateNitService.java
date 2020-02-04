package co.com.bancodebogota.auth.services;

import co.com.bancodebogota.auth.dto.associateNitDTO.AssociateNitRequest;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

public interface IAssociateNitService {
  public ResponseEntity<JSONObject> findNitAssociate(String company, String userName);
  public ResponseEntity<JSONObject> saveNitAssociate(AssociateNitRequest request);
}
