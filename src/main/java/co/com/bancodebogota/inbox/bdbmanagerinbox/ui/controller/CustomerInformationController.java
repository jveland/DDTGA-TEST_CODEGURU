package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.controller;

import co.com.bancodebogota.adpe.logger.BdbLoggerUtils;
import co.com.bancodebogota.adpe.util.Response;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.CustomerInformationRequestDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.ICustomerInformationService;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.IEmployeeService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping()
public class CustomerInformationController {

	@Autowired
	IEmployeeService service;

	@Autowired
	ICustomerInformationService iCustomerInformationService;

	@PostMapping("infoCustomer/")
	public ResponseEntity<JSONObject> customerInformation(
			@Valid @RequestBody CustomerInformationRequestDto customerInformationRqDto, BindingResult bd) {
		Response response = new Response(); 
		if (bd.hasErrors()) {
			return new ResponseEntity<>(response.response(response.buildStatus(HttpStatus.BAD_REQUEST.value(), bd.getFieldError().toString()), null, false), HttpStatus.BAD_REQUEST);
		} else {
			try {
				BdbLoggerUtils.info("Inicio validación variables consulta cliente.");
				if(customerInformationRqDto.getCustomerUserName().trim().equals("")) {
					BdbLoggerUtils.info("Error al validar customerUsername.");
					return new ResponseEntity<>(response.response(response.buildStatus(HttpStatus.BAD_REQUEST.value(), "Error al validar customerUsername. No puede ser vacío. "), null, false), HttpStatus.BAD_REQUEST);
				}
				else if(customerInformationRqDto.getIdentityNumber().trim().equals("")){
					BdbLoggerUtils.info("Error al validar identityNumber.");
					return new ResponseEntity<>(response.response(response.buildStatus(HttpStatus.BAD_REQUEST.value(), "Error al validar identityNumber. No puede ser vacío. "), null, false), HttpStatus.BAD_REQUEST);
				}
				else if(customerInformationRqDto.getIdentityType().trim().equals("")){
					BdbLoggerUtils.info("Error al validar identityType.");
					return new ResponseEntity<>(response.response(response.buildStatus(HttpStatus.BAD_REQUEST.value(), "Error al validar identityType. No puede ser vacío. "), null, false), HttpStatus.BAD_REQUEST);
				}
				else {
					BdbLoggerUtils.info("Inicio consumo adaptador CRM.");
					return service.beginQueryEmployee(customerInformationRqDto,false);
				}
			} catch(Exception e) {
			  e.printStackTrace();
				BdbLoggerUtils.info("Error consumo adaptador CRM.");
				return new ResponseEntity<>(response.response(response.buildStatus(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error al consumir el adaptador de CRM." + e.getMessage()), null, false), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@PostMapping("infoCustomer/mask")
	public ResponseEntity<JSONObject> customerInformationTest(
			@Valid @RequestBody CustomerInformationRequestDto customerInformationRqDto, BindingResult bd) {
		Response response = new Response();
		if (bd.hasErrors()) {
			return new ResponseEntity<>(response.response(response.buildStatus(HttpStatus.BAD_REQUEST.value(), bd.getFieldError().toString()), null, false), HttpStatus.BAD_REQUEST);
		} else {
			try {
				BdbLoggerUtils.info("Inicio validación variables consulta cliente.");
				if(customerInformationRqDto.getCustomerUserName().trim().equals("")) {
					BdbLoggerUtils.info("Error al validar customerUsername.");
					return new ResponseEntity<>(response.response(response.buildStatus(HttpStatus.BAD_REQUEST.value(), "Error al validar customerUsername. No puede ser vacío. "), null, false), HttpStatus.BAD_REQUEST);
				}
				else if(customerInformationRqDto.getIdentityNumber().trim().equals("")){
					BdbLoggerUtils.info("Error al validar identityNumber.");
					return new ResponseEntity<>(response.response(response.buildStatus(HttpStatus.BAD_REQUEST.value(), "Error al validar identityNumber. No puede ser vacío. "), null, false), HttpStatus.BAD_REQUEST);
				}
				else if(customerInformationRqDto.getIdentityType().trim().equals("")){
					BdbLoggerUtils.info("Error al validar identityType.");
					return new ResponseEntity<>(response.response(response.buildStatus(HttpStatus.BAD_REQUEST.value(), "Error al validar identityType. No puede ser vacío. "), null, false), HttpStatus.BAD_REQUEST);
				}
				else {
					BdbLoggerUtils.info("Inicio consumo adaptador CRM.");
					return service.beginQueryEmployee(customerInformationRqDto, true);
				}
			} catch(Exception e) {
				e.printStackTrace();
				BdbLoggerUtils.info("Error consumo adaptador CRM.");
				return new ResponseEntity<>(response.response(response.buildStatus(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error al consumir el adaptador de CRM." + e.getMessage()), null, false), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}




	@GetMapping("infoCustomer/health")
	public ResponseEntity<String> health() {
		return new ResponseEntity<>("Health Ok!", HttpStatus.OK);
	}

	@PostMapping("customer")
	public ResponseEntity<JSONObject> formatterCustomer(@Valid @RequestBody JSONObject jsonObject) {
		System.out.println(jsonObject);
		return iCustomerInformationService.formatterCustomer(new org.json.JSONObject(jsonObject));
	}
}
