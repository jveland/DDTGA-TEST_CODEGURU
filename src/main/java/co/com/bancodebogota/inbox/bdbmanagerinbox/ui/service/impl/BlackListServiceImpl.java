package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.impl;

import co.com.bancodebogota.RestExchange;
import co.com.bancodebogota.adpe.logger.BdbLoggerUtils;
import co.com.bancodebogota.adpe.util.Response;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.BasicInfoCustomer;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.CustomerInformationRequestDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.DuccResponse;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.ValidCustomerBlackListRp;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.IBlackListService;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.ICustomerInformationService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static co.com.bancodebogota.adpe.util.Constant.MSN_OPERATION_BEGIN;


@Service
public class BlackListServiceImpl implements IBlackListService {

	@Autowired
	ICustomerInformationService iCustomerInformationService;


	@Value("${adapter.blacklist.endpoint}")
	public String blacklistEndpoint;

	private RestExchange restExchange;


	private Response response;

	public BlackListServiceImpl() {
		this.response = new Response();
		this.restExchange = new RestExchange();
	}

	@Override
	public ResponseEntity<org.json.simple.JSONObject> beginCRM(CustomerInformationRequestDto customers) {

		ResponseEntity<org.json.simple.JSONObject> jsonObjectResponseEntity = iCustomerInformationService.infoByCustomer(customers);
		
		try {
			JSONObject empty = new JSONObject(jsonObjectResponseEntity.getBody());
			return formatterToBlackList(empty);
		} catch(Exception e) {
			return buildObjectResponseEntity(e, 2);
		}
		
	}




	@Override
	public ResponseEntity<org.json.simple.JSONObject> formatterToBlackList(JSONObject customerInformation) {
	BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());


		BasicInfoCustomer basicInfoCustomer = new BasicInfoCustomer();
		List<BasicInfoCustomer> basicInfoCustomerList = new ArrayList<>();

		if(customerInformation.has("custOrgInfoInqRs") && customerInformation.get("custOrgInfoInqRs")!=null){//  1. step

			JSONObject	custOrgInfoinqRs =customerInformation.getJSONObject("custOrgInfoInqRs");

			BdbLoggerUtils.info("custOrgInfoInqRs  "+custOrgInfoinqRs);


			// 1.1. step
			if(custOrgInfoinqRs.has("orgClient")&&custOrgInfoinqRs.get("orgClient")!=null){
        JSONObject orgClient = (JSONObject) custOrgInfoinqRs.get("orgClient");

        // Bussiness
				BigDecimal participantId =  orgClient.has("participantId")&&orgClient.get("participantId")!=null? new BigDecimal( orgClient.get("participantId").toString()):new BigDecimal(0);
				String bussinessName = orgClient.has("legalName") && (String) orgClient.get("legalName")!=null?(String) orgClient.get("legalName"):"";
				basicInfoCustomer.setLegalName(bussinessName);
				basicInfoCustomer.setIdentityNumber(participantId);
				basicInfoCustomerList.add(basicInfoCustomer);

				// legal representation
				if( orgClient.has("residentAgentInfo") && orgClient.get("residentAgentInfo")!=null){
					JSONObject residentAgentInfoJSON = (JSONObject) orgClient.get("residentAgentInfo");
					basicInfoCustomer = basicInfoCustomer(residentAgentInfoJSON);
					if(basicInfoCustomer!=null){
						basicInfoCustomerList.add(basicInfoCustomer);
					}


				}

			}else{
				return getResponseCRM("Nit. No existe en CRM");
			}

			// 1.2. step
			if(custOrgInfoinqRs.has("shareholderInfo") && custOrgInfoinqRs.get("shareholderInfo")!=null){

				try {
				JSONArray shareholderInfoArr = custOrgInfoinqRs.getJSONArray("shareholderInfo");

				for (int i = 0; i < shareholderInfoArr.length(); i++) {
					JSONObject shareholderInfo = (JSONObject) shareholderInfoArr.get(i);

					basicInfoCustomer = basicInfoCustomer(shareholderInfo);
					if(basicInfoCustomer!=null){
						basicInfoCustomerList.add(basicInfoCustomer);
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			}

		}else{
			return getResponseCRM("Nit. No existe en CRM");
		}

		return validateBussiness(basicInfoCustomerList);
	}
	public  BasicInfoCustomer basicInfoCustomer (JSONObject scanBasicInfo){
		BasicInfoCustomer basicInfoCustomer = new BasicInfoCustomer();

		if(scanBasicInfo.has("otherIdentDoc") && scanBasicInfo.get("otherIdentDoc") != null){ // id of person client or legal representation
			JSONObject otherIdentDoc = (JSONObject) scanBasicInfo.get("otherIdentDoc");
			basicInfoCustomer = new BasicInfoCustomer();

			boolean isNaturalPerson;
			if(otherIdentDoc.has("typeId")&& otherIdentDoc.get("typeId")!=null && isNaturalPerson((String)otherIdentDoc.get("typeId"))){ //Natural person

				isNaturalPerson = true;
			}else{
				isNaturalPerson = false;
			}
			BigDecimal participantId_ =  otherIdentDoc.has("participantId") && otherIdentDoc.get("participantId")!=null? new BigDecimal( otherIdentDoc.get("participantId").toString()):new BigDecimal(0);


			if(scanBasicInfo.has("personClient") && scanBasicInfo.get("personClient")!=null){ // person client or legal representation
				JSONObject personClientJSON = (JSONObject) scanBasicInfo.get("personClient");

				BdbLoggerUtils.json(personClientJSON, "BUSSINESS: ");

				getPersonName(basicInfoCustomer, isNaturalPerson, personClientJSON);

			}else{
				getPersonName(basicInfoCustomer, isNaturalPerson, scanBasicInfo);
			}
			basicInfoCustomer.setIdentityNumber(participantId_);


		}

		return basicInfoCustomer;
	}

	public void getPersonName(BasicInfoCustomer basicInfoCustomer, boolean isNaturalPerson, JSONObject personClientJSON) {
		if(personClientJSON.has("personName")){
			JSONObject personNameJSON = (JSONObject) personClientJSON.get("personName");

			if(isNaturalPerson){

				JSONArray lastNameArr = personNameJSON.getJSONArray("lastName");
				if(lastNameArr.length()>0){
					String lastName = (String)lastNameArr.get(0);
					basicInfoCustomer.setLastName(lastName);
				}else{
					//return getResponseCRM("El apellido del reresentante legal no existe en CRM");
				}
				String firstName = personNameJSON.get("firstName").toString();
				basicInfoCustomer.setFirstName(firstName);

			}else{
				String legalName =  personNameJSON.has("legalName")?personNameJSON.get("legalName").toString(): personNameJSON.has("fullName")?personNameJSON.get("fullName").toString(): "Sin nombre";
				basicInfoCustomer.setLegalName(legalName);

			}
		}
	}


	public boolean isNaturalPerson(String typeId) {
		if(typeId.equals("C")  || typeId.equals("E") || typeId.equals("L") || typeId.equals("P") || typeId.equals("R") ||  typeId.equals("T") ){
			return true;
		}else{
			return false;
		}
	}

	private ResponseEntity<org.json.simple.JSONObject> getResponseCRM(String desc) {
		return new ResponseEntity<>(response.response(response.buildStatus(1, desc ), null, false), HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<org.json.simple.JSONObject> validateBussiness(List<BasicInfoCustomer> customers) {
		try {
			BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
			BdbLoggerUtils.info("RESPONSE BLACKLIST ");
			ResponseEntity<DuccResponse> exchange = restExchange.exchange(blacklistEndpoint + "/validate-customer" ,customers, HttpMethod.POST, DuccResponse.class);
			return getJsonObjectResponseEntity(exchange);
		} catch (Exception e) {
			return buildObjectResponseEntity(e,2);
		}
	}

	public ResponseEntity<org.json.simple.JSONObject> getJsonObjectResponseEntity(ResponseEntity<DuccResponse> exchange) {
		ResponseEntity<org.json.simple.JSONObject> responseService;
		HttpStatus statusCode = exchange.getStatusCode();
		DuccResponse body = exchange.getBody();
		if (statusCode.equals(HttpStatus.OK)) {
			if( body!=null && body.getCustomers()!=null){
				List<ValidCustomerBlackListRp> customers = body.getCustomers();

				for (ValidCustomerBlackListRp customerBlackListRp:customers) {

					if(!customerBlackListRp.getIsPossibleContinue()){
						return new ResponseEntity<>(response.response(response.buildStatus(1, "El caso no puede ser creado por este medio, comuniquese con su comercial."), null, false), HttpStatus.OK);
					}
				}
				return new ResponseEntity<>(response.response(response.buildStatus(0, "continue"), null, false), HttpStatus.OK);
			}else{
				return new ResponseEntity<>(response.response(response.buildStatus(1, "El caso no puede ser creado por este medio, comuniquese con su comercial."), null, false), HttpStatus.OK);
			}

		} else if (statusCode.equals(HttpStatus.CONFLICT)) {
			responseService = new ResponseEntity<>(HttpStatus.CONFLICT);
		} else {
			responseService = new ResponseEntity<>(statusCode);
		}
		BdbLoggerUtils.info("FINISH CONSUME BLACKLIST ADAPTER");
		return responseService;
	}

	public ResponseEntity<org.json.simple.JSONObject> buildObjectResponseEntity(Exception e,int status) {
		BdbLoggerUtils.error("ERROR CONSUME BLACKLIST ADAPTER");
		e.printStackTrace();
		return new ResponseEntity<org.json.simple.JSONObject>(response.response(response.buildStatus(2, e.getMessage()), null, false), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
