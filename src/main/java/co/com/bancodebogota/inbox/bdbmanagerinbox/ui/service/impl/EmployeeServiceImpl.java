package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.impl;

import co.com.bancodebogota.RestExchange;
import co.com.bancodebogota.adpe.logger.BdbLoggerUtils;
import co.com.bancodebogota.adpe.util.Response;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.CustomerInformationRequestDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.ICustomerInformationService;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.IEmployeeService;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.IInformationMaskService;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static co.com.bancodebogota.adpe.util.Constant.MSN_OPERATION_BEGIN;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

  @Value("${adapter.employee.endpoint}")
  public String employeeEndpoint;

  private RestExchange restExchange;

  Response response;

  @Autowired
  ICustomerInformationService iCustomerInformationService;

  @Autowired
  IInformationMaskService iInformationMaskService;

  public EmployeeServiceImpl() {
    restExchange = new RestExchange();
    response = new Response();
  }
  @Override
  public ResponseEntity<JSONObject> beginQueryEmployee(CustomerInformationRequestDto input, boolean test) {
    org.json.JSONObject objConsult;

    ResponseEntity<JSONObject> jsonObjectResponseEntity = iCustomerInformationService.infoByCustomer(input);
    List<org.json.JSONObject> basicInfoCustomer;
    if(jsonObjectResponseEntity.getStatusCode().equals(HttpStatus.OK) && jsonObjectResponseEntity.getBody()!=null){
      objConsult = new org.json.JSONObject(jsonObjectResponseEntity.getBody());
      if(  objConsult.has("custOrgInfoInqRs")){
        org.json.JSONObject custOrgInfoInqRs = objConsult.getJSONObject("custOrgInfoInqRs");
        if(custOrgInfoInqRs.has("orgClient")){
          org.json.JSONObject orgClient = custOrgInfoInqRs.getJSONObject("orgClient");
          if (orgClient.has("commercialArea")){
            org.json.JSONObject commercialArea = orgClient.getJSONObject("commercialArea");
            if(commercialArea.has("agentId")){
              String agentId = commercialArea.getString("agentId");
              ResponseEntity<JSONObject> commTerritoryInfo = getCommTerritoryInfo(agentId);
              if(commTerritoryInfo.getStatusCode().equals(HttpStatus.OK)){

                basicInfoCustomer= buildCommercialList(new org.json.JSONObject(commTerritoryInfo.getBody()));
                if(basicInfoCustomer.size()>0){
                  try {
                     objConsult.put("commInfo", basicInfoCustomer);

                    if(test){
                      objConsult = iInformationMaskService.beginMapperData(objConsult);
                    }
                    JSONObject jsonObject = new JSONObject(objConsult.toMap());
                    return new ResponseEntity<>(response.response(response.buildStatusSuccessfull(), jsonObject, true),HttpStatus.OK);
                  } catch (Exception e) {
                    return response.buildObjectResponseEntity(e,2, "EMPLOYEE ",HttpStatus.INTERNAL_SERVER_ERROR);
                  }

                }
              }
                return getResponseCRM(3, "No se tiene asignación comercial.", HttpStatus.NOT_FOUND);

            }

          }

        }
      }
    }
    return getResponseCRM(1, "No encontró agentId en CRM", HttpStatus.NOT_FOUND);
  }

  private List<org.json.JSONObject> buildCommercialList(org.json.JSONObject jsonObjterritoryInfo) {
    List<org.json.JSONObject> basicInfoCustomer = new ArrayList<>();
    if(jsonObjterritoryInfo.has("data") && jsonObjterritoryInfo.getJSONObject("data").has("commTerritoryInfoRs") && jsonObjterritoryInfo.getJSONObject("data").getJSONObject("commTerritoryInfoRs").has("commTerritoryUser")){

      JSONArray jsonCommTerritoryUserArray = jsonObjterritoryInfo.getJSONObject("data").getJSONObject("commTerritoryInfoRs").getJSONArray("commTerritoryUser");
      if(jsonCommTerritoryUserArray.length()>0){
        basicInfoCustomer = new ArrayList<>();
        for (int i = 0; i < jsonCommTerritoryUserArray.length(); i++) {
          org.json.JSONObject commTerritoryUser =jsonCommTerritoryUserArray.getJSONObject(i);
          if(commTerritoryUser.has("sessionId") && commTerritoryUser.getString("sessionId")!=null &&  !commTerritoryUser.getString("sessionId").isEmpty()){

            String sessionId = commTerritoryUser.getString("sessionId");
            ResponseEntity<JSONObject> empCommercialInfo = getEmpCommercialInfo(sessionId);
            if(empCommercialInfo.getStatusCode().equals(HttpStatus.OK)){
              org.json.JSONObject jsonEmpCommercialInfo= new org.json.JSONObject( empCommercialInfo.getBody());
              if(jsonEmpCommercialInfo.has("statusResponse") && jsonEmpCommercialInfo.getJSONObject("statusResponse").has("status") && jsonEmpCommercialInfo.getJSONObject("statusResponse").getInt("status")==0 && jsonEmpCommercialInfo.has("data")) {
                commTerritoryUser.put("commercial-info", jsonEmpCommercialInfo.get("data"));
                org.json.JSONObject basicInfo = builtCommercialItem(commTerritoryUser);

                if(basicInfo!=null){
                  basicInfoCustomer.add(basicInfo);
                }

              }

            }
          }
        }

      }
    }
    return basicInfoCustomer;
  }

  public org.json.JSONObject builtCommercialItem(org.json.JSONObject commTerritoryUser) {
    if(validateInfoCommercial(commTerritoryUser)){
      org.json.JSONObject user = new org.json.JSONObject();
      user.put("username", commTerritoryUser.get("sessionId"));
      user.put("name", commTerritoryUser.get("fullName"));
      user.put("email", commTerritoryUser.get("emailAddr"));
      user.put("rol", commTerritoryUser.getJSONObject("commercial-info").getJSONObject("empCommercialInfoInqRs").getJSONObject("custInfo").getJSONObject("employmentHistory").getString("memo"));
      user.put("documentID", commTerritoryUser.get("participantId"));
      return user;
    }
    return null;
  }
  public boolean  validateInfoCommercial(org.json.JSONObject commTerritoryUser){
    if(commTerritoryUser.has("sessionId") && commTerritoryUser.has("fullName") && commTerritoryUser.has("emailAddr") && commTerritoryUser.has("participantId")
    && commTerritoryUser.has("commercial-info") && commTerritoryUser.getJSONObject("commercial-info").has("empCommercialInfoInqRs")
        && commTerritoryUser.getJSONObject("commercial-info").getJSONObject("empCommercialInfoInqRs").has("custInfo")
        && commTerritoryUser.getJSONObject("commercial-info").getJSONObject("empCommercialInfoInqRs").getJSONObject("custInfo").has("employmentHistory")
        && commTerritoryUser.getJSONObject("commercial-info").getJSONObject("empCommercialInfoInqRs").getJSONObject("custInfo").getJSONObject("employmentHistory").has("memo")){
      String memo = commTerritoryUser.getJSONObject("commercial-info").getJSONObject("empCommercialInfoInqRs").getJSONObject("custInfo").getJSONObject("employmentHistory").getString("memo");
      if(!memo.isEmpty()){
        String memoTrim = memo.trim().toLowerCase();
        if (memoTrim.equals("gerente ceo") || memoTrim.equals("ejecutivo ceo")){
          return true;
        }
      }

    }
    return false;
  }

  @Override
  public ResponseEntity<JSONObject> getCommTerritoryInfo(String branchId) {
    BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
    try {
      BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
      BdbLoggerUtils.info("INPUT REQUEST "+ branchId.toString());
      return restExchange.exchange(employeeEndpoint + "/territory-info?branchId="+branchId,null, HttpMethod.GET, JSONObject.class);
    } catch (Exception e) {
      return buildObjectResponseEntity(e, HttpStatus.BAD_REQUEST.getReasonPhrase(),2);
    }
  }
  @Override
  public ResponseEntity<JSONObject> getEmpCommercialInfo(String custLoginId) {
    BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
    try {
      BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
      BdbLoggerUtils.info("INPUT REQUEST "+ custLoginId.toString());
      return restExchange.exchange(employeeEndpoint + "/commercial-info?custLoginId="+custLoginId,null, HttpMethod.GET, JSONObject.class);
    } catch (Exception e) {
      return buildObjectResponseEntity(e, HttpStatus.BAD_REQUEST.getReasonPhrase(),2);
    }
  }
  private ResponseEntity<JSONObject> buildObjectResponseEntity(Exception e, String message, int status) {
    BdbLoggerUtils.info("ERROR CONSUME CRM ADAPTER");
    BdbLoggerUtils.info(e.getMessage());
    return new ResponseEntity<>(response.response(response.buildStatus(status, message), null, false),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }





  private ResponseEntity<org.json.simple.JSONObject> getResponseCRM(int status,String desc, HttpStatus httpStatus) {
    return new ResponseEntity<>(response.response(response.buildStatus(status, desc ), null, false), httpStatus);
  }
}
