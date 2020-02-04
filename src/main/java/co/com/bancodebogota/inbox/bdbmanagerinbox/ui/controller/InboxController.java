package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.controller;

import co.com.bancodebogota.adpe.logger.BdbLoggerUtils;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.CaseDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.SaveAdditionalInfoRequestDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.TaskDoneRequestDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.ICaseService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("inbox")
public class InboxController {
	private static final Log logger = LogFactory.getLog(InboxController.class);

	@Autowired // inyección
	ICaseService service;

	@GetMapping("/{id}")
	public ResponseEntity<JSONObject> casesById(@PathVariable("id") String idCase) {
		return service.casesById(idCase);
	}

	@GetMapping("customer/{identityType}/{identityNumber}")
	public ResponseEntity<JSONObject> casesByCustomer(@PathVariable("identityType") String identityType,
			@PathVariable("identityNumber") BigDecimal identityNumber, Pageable pageable) {
		logger.info("PARAMETOS DE CONSULTA " + identityType + " " + identityNumber + "\n");
		return service.casesByCustomer(identityType, identityNumber, pageable);
	}

	@GetMapping("employee/{username}")
	public ResponseEntity<JSONObject> casesByEmployee(@PathVariable("username") String customerUsername,
			Pageable pageable) {
		return service.casesByEmployee(customerUsername, pageable);
	}

	@PostMapping("/createCase")
	public ResponseEntity<JSONObject> createCase(@Valid @RequestBody CaseDto caseDto) {
		BdbLoggerUtils.json(caseDto, "RESPONSE ADPE CA");
		return service.createCase(caseDto);
	}

	@PostMapping("/taskDone")
	public ResponseEntity<JSONObject> taskDone(@Valid @RequestBody TaskDoneRequestDto taskToFinish) {
		BdbLoggerUtils.json(taskToFinish, "RESPONSE ADPE CA");
		return service.taskDone(taskToFinish);
	}

	@GetMapping("/health")
	public ResponseEntity<String> health() {
		return new ResponseEntity<>("Health Ok!", HttpStatus.OK);
	}

	// INFO CASES SERVICES
	/**
	 * 
	 * @param saveAdditionalInfoRequestDto
	 * @return
	 */
	@PostMapping("saveAditionalInfo")
	public ResponseEntity<JSONObject> saveAditionalInfo(
			@Valid @RequestBody SaveAdditionalInfoRequestDto saveAdditionalInfoRequestDto) {
		return service.saveAditionalInfo(saveAdditionalInfoRequestDto);
	}

	@GetMapping("aditionalInfo/{idCase}")
	public ResponseEntity<JSONObject> getAditionalInfo(@PathVariable("idCase") String idCase) {
		return service.getInfo(idCase);
	}

	// TASK CASES SERVICES
	@GetMapping("task/{idCase}")
	public ResponseEntity<JSONObject> getTaskStatusInfo(@PathVariable("idCase") String idCase) {
		return service.getTaskStatus(idCase);
	}

}
