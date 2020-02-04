package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.impl;

import co.com.bancodebogota.RestExchange;
import co.com.bancodebogota.adpe.logger.BdbLoggerUtils;
import co.com.bancodebogota.adpe.util.Response;
import co.com.bancodebogota.crypto.TripleDES;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.CustomerInformationRequestDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.JuridicalCustomerInformationRqDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.ICustomerInformationService;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.IEmployeeService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static co.com.bancodebogota.adpe.util.Constant.MSN_OPERATION_BEGIN;

@Service
public class CustomerInformationServiceImpl implements ICustomerInformationService {

	@Value("${adapter.crm.endpoint}")
	public String crmEndpoint;

	@Value("${adapter.customer.endpoint}")
	public String customerEndpoint;

	@Value("${adapter.publicKey}")
	public String publicKey;

	private RestExchange restExchange;

	Response response;
	TripleDES tripleDES;

	@Autowired
	IEmployeeService iEmployeeService;

	public CustomerInformationServiceImpl() {
		restExchange = new RestExchange();
		response = new Response();

	}

	@Override
	public ResponseEntity<JSONObject> infoByCustomer(CustomerInformationRequestDto input) {
		BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
		try {
			BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
			BdbLoggerUtils.info("INPUT REQUEST " + input.toString());
			JuridicalCustomerInformationRqDto request = new JuridicalCustomerInformationRqDto();
			request.setTypeID(input.getIdentityType());
			request.setIdentificationNumber(input.getIdentityNumber());
			ResponseEntity<JSONObject> exchange = restExchange.exchange(crmEndpoint, request, HttpMethod.POST,
					JSONObject.class);
			return exchange;
		} catch (Exception e) {
			return buildObjectResponseEntity(e, HttpStatus.BAD_REQUEST.getReasonPhrase(), 2);
		}
	}

	@Override
	public ResponseEntity<JSONObject> formatterCustomer(org.json.JSONObject jsonObject) {

		try {
			BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
			BdbLoggerUtils.info("INPUT REQUEST " + jsonObject.toString());
			return mapperRequest(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			return buildObjectResponseEntity(e, HttpStatus.BAD_REQUEST.getReasonPhrase(), 2);
		}

	}

	private ResponseEntity<JSONObject> mapperRequest(org.json.JSONObject jsonObject) {
		if (jsonObject.has("custOrgInfoInqRs") && jsonObject.getJSONObject("custOrgInfoInqRs").has("orgClient")
				&& jsonObject.getJSONObject("custOrgInfoInqRs").getJSONObject("orgClient").has("typeId")
				&& jsonObject.getJSONObject("custOrgInfoInqRs").getJSONObject("orgClient").has("participantId")
				&& jsonObject.getJSONObject("custOrgInfoInqRs").getJSONObject("orgClient").has("commercialArea")
				&& jsonObject.getJSONObject("custOrgInfoInqRs").getJSONObject("orgClient").has("legalName")
				&& jsonObject.getJSONObject("custOrgInfoInqRs").getJSONObject("orgClient")
						.getJSONObject("commercialArea").has("segmentId")) {

			int segmentId = jsonObject.getJSONObject("custOrgInfoInqRs").getJSONObject("orgClient")
					.getJSONObject("commercialArea").getInt("segmentId");
			String segmentIdStr = validateCustomerType(segmentId);
			if (segmentIdStr != null && !segmentIdStr.isEmpty()) {
				String typeId = jsonObject.getJSONObject("custOrgInfoInqRs").getJSONObject("orgClient")
						.getString("typeId");
				String participantId = jsonObject.getJSONObject("custOrgInfoInqRs").getJSONObject("orgClient")
						.getString("participantId");
				String legalName = jsonObject.getJSONObject("custOrgInfoInqRs").getJSONObject("orgClient")
						.getString("legalName");
				JSONObject jsonObjCustomerRq = new JSONObject();

				jsonObjCustomerRq.put("identityType", typeId);
				jsonObjCustomerRq.put("identityNumber", participantId);
				jsonObjCustomerRq.put("idCustomerType", segmentIdStr);
				jsonObjCustomerRq.put("name", legalName);
				try {
					BdbLoggerUtils.info("INICIO CIFRADO 3DES REQUEST ");
					tripleDES = new TripleDES(publicKey);
					String harden = tripleDES.harden(jsonObject.toString());
					BdbLoggerUtils.info("FINALIZO CIFRADO 3DES REQUEST " + harden);
					jsonObjCustomerRq.put("payload", harden);

					return customerCreate(jsonObjCustomerRq);
				} catch (Exception e) {
					e.printStackTrace();
					return response.buildObjectResponseEntity(e, 3, "CLIENTE LOCAL ADAPTER",
							HttpStatus.INTERNAL_SERVER_ERROR);
				} 

			}
		}
		return new ResponseEntity<>(
				response.response(response.buildStatus(1, "Datos del cliente incompleto"), null, false),
				HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<JSONObject> customerCreate(JSONObject jsonObjCustomerRq) {
		try {
			BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
			BdbLoggerUtils.info("INPUT REQUEST TO LOCAL CUSTOMER ADAPTER" + jsonObjCustomerRq.toString());
			ResponseEntity<JSONObject> exchange = restExchange.exchange(customerEndpoint, jsonObjCustomerRq,
					HttpMethod.POST, JSONObject.class);
			return exchange;
		} catch (Exception e) {
			return response.buildObjectResponseEntity(e, 1, "Customer Adapter from DB",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public String validateCustomerType(int customerType) {
		switch (customerType) {
		case 614:
			return "PRI";
		case 615:
			return "PRI";
		case 617:
			return "OFI";
		case 618:
			return "FIN";
		case 1559:
			return "SOC";
		default:
			return null;
		}
	}

	private ResponseEntity<JSONObject> buildObjectResponseEntity(Exception e, String message, int status) {
		BdbLoggerUtils.info("ERROR CONSUME CRM ADAPTER");
		BdbLoggerUtils.info(e.getMessage());
		return new ResponseEntity<org.json.simple.JSONObject>(
				response.response(response.buildStatus(status, message), null, false),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
