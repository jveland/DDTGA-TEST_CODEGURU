package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service;

import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.CaseDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.SaveAdditionalInfoRequestDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.TaskDoneRequestDto;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface ICaseService {
	public ResponseEntity<JSONObject> caseList();

	public ResponseEntity<JSONObject> casesById(String idCase);

	public ResponseEntity<JSONObject> casesByCustomer(String idCase, BigDecimal identityNumber, Pageable pageable);

	public ResponseEntity<JSONObject> casesByEmployee(String customerUsername, Pageable pageable);

	public ResponseEntity<JSONObject> createCase(CaseDto caseDto);

	public ResponseEntity<JSONObject> taskDone(TaskDoneRequestDto taskToFinish);

	public ResponseEntity<JSONObject> saveAditionalInfo(SaveAdditionalInfoRequestDto saveAdditionalInfoRequestDto);

	public ResponseEntity<JSONObject> getInfo(String saveAdditionalInfoRequestDto);
	
	public ResponseEntity<JSONObject> getTaskStatus(String taskId);

}
