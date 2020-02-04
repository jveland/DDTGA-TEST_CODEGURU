package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.impl;

import co.com.bancodebogota.RestExchange;
import co.com.bancodebogota.adpe.logger.BdbLoggerUtils;
import co.com.bancodebogota.adpe.util.Response;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.IInformationMaskService;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static co.com.bancodebogota.adpe.util.Constant.MSN_OPERATION_BEGIN;
import static co.com.bancodebogota.adpe.util.K.*;
import static co.com.bancodebogota.adpe.util.Utils.padLeft;
import static co.com.bancodebogota.adpe.util.Utils.padRight;

@Service
public class InformationMaskServiceImpl  implements IInformationMaskService {

  @Value("${adapter.param.endpoint}")
  public String paramEndpoint;

  private RestExchange restExchange;

  Response response;

  public InformationMaskServiceImpl() {
    restExchange = new RestExchange();
    response = new Response();
  }

  @Override
  public ResponseEntity<JSONObject> consumeServices(String nameEntity, boolean isAll, String idEntity) {
    try {
      BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
      if(!nameEntity.isEmpty()) {
          ResponseEntity<JSONObject> exchange;
          if(isAll){
            exchange=restExchange.exchange(paramEndpoint+"/all/"+nameEntity, null, HttpMethod.GET, JSONObject.class);
          }else{
            exchange=restExchange.exchange(paramEndpoint+"/byId/"+nameEntity+"/"+idEntity, null, HttpMethod.GET, JSONObject.class);
          }
          return exchange;
      }
      BdbLoggerUtils.info("Problemas conlos parametros de consulta".toUpperCase());
      return new ResponseEntity<>(response.response(response.buildStatusSuccessfull(), new ArrayList<>(), true), HttpStatus.OK);
    }catch (Exception e){
      return response.buildObjectResponseEntity(e, 2, "PARAM ADAPTER", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public org.json.JSONObject beginMapperData(org.json.JSONObject jsonObject) {
    if(jsonObject.has("custOrgInfoInqRs")&& jsonObject.getJSONObject("custOrgInfoInqRs").has("orgClient")){
      org.json.JSONObject orgClient =  jsonObject.getJSONObject("custOrgInfoInqRs").getJSONObject("orgClient");

      if(orgClient.has("compositeContactInfo") && orgClient.getJSONObject("compositeContactInfo").has("contactInfo")){
        org.json.JSONObject contactInfo = orgClient.getJSONObject("compositeContactInfo").getJSONObject("contactInfo");
        // Begin format fields from  business
        // TODO MAIL BUSINESS, only uncomments
        String emailAddr = contactInfo.has("emailAddr") && contactInfo.getString("emailAddr").length() > 0 ? contactInfo.getString("emailAddr") : "";
        if(!emailAddr.isEmpty()){
         /* uncomments
          emailAddr=emailMask(emailAddr);
          contactInfo.put("emailAddr", emailAddr);*/
        }
        // postAddr BUSINESS
        org.json.JSONObject postAddrArray = contactInfo.has("postAddr") && contactInfo.getJSONArray("postAddr").length() > 0 ? contactInfo.getJSONArray("postAddr").getJSONObject(0) : null;
        if(postAddrArray!=null){
          org.json.JSONObject calculatePostAddr=calculatePostAddr(postAddrArray);
          contactInfo.put("postAddr", calculatePostAddr);
        }

        // TODO phoneNum BUSINESS
        JSONArray phoneNumArray = contactInfo.has("phoneNum") && contactInfo.getJSONArray("phoneNum").length() > 0 ? contactInfo.getJSONArray("phoneNum") : null;
        if(phoneNumArray!=null && phoneNumArray.length()>0){
          /* uncomments
          phoneNumArray=phoneNumMask(phoneNumArray);
          contactInfo.put("phoneNum", phoneNumArray);
          */
        }

        // End format fields from business
        orgClient.getJSONObject("compositeContactInfo").put("contactInfo",contactInfo);
      }

      if(orgClient.has("residentAgentInfo") && orgClient.getJSONObject("residentAgentInfo").has("personClient")
          && orgClient.getJSONObject("residentAgentInfo").getJSONObject("personClient").has("contactInfo")){
        org.json.JSONObject contactInfo = orgClient.getJSONObject("residentAgentInfo").getJSONObject("personClient").getJSONObject("contactInfo");
        // Begin format fields from person

        // MAIL PERSON
        String emailAddr = contactInfo.has("emailAddr") && contactInfo.getString("emailAddr").length() > 0 ? contactInfo.getString("emailAddr") : "";
        if(!emailAddr.isEmpty()){
          emailAddr=emailMask(emailAddr);
          contactInfo.put("emailAddr", emailAddr);

        }

        // postAddr PERSON [NONE]
        org.json.JSONObject postAddrArray = contactInfo.has("postAddr") && contactInfo.getJSONArray("postAddr").length() > 0 ? contactInfo.getJSONArray("postAddr").getJSONObject(0) : null;
        if(postAddrArray!=null){
          org.json.JSONObject calculatePostAddr=calculatePostAddr(postAddrArray);
          contactInfo.put("postAddr", calculatePostAddr);
        }

        // phoneNum BUSINESS
        JSONArray phoneNumArray = contactInfo.has("phoneNum") && contactInfo.getJSONArray("phoneNum").length() > 0 ? contactInfo.getJSONArray("phoneNum") : null;
        if(phoneNumArray!=null && phoneNumArray.length()>0){
          phoneNumArray=phoneNumMask(phoneNumArray);
          contactInfo.put("phoneNum", phoneNumArray);
        }

        // End format fields from person
        orgClient.getJSONObject("residentAgentInfo").getJSONObject("personClient").put("contactInfo",contactInfo);
      }
      jsonObject.getJSONObject("custOrgInfoInqRs").put("orgClient", orgClient);
    }
    if(jsonObject.has("custOrgInfoInqRs")&& jsonObject.getJSONObject("custOrgInfoInqRs").has("ciiu")){
      String ciiuStr = jsonObject.getJSONObject("custOrgInfoInqRs").getString("ciiu");
      org.json.JSONObject calculateCiiuObject = calculateCiiu(ciiuStr);
      if(calculateCiiuObject!=null && !calculateCiiuObject.isEmpty()){
        jsonObject.getJSONObject("custOrgInfoInqRs").put("ciiu", calculateCiiuObject);
      }

    }

    return jsonObject;
  }

  private org.json.JSONObject calculateCiiu(String ciiuStr) {

    org.json.JSONObject calculateCiiu = new org.json.JSONObject();

    ResponseEntity<JSONObject> jsonObjectResponseEntity = consumeServices(TBL_BDB_TYPOLOGY, false, ciiuStr);
    if (jsonObjectResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
      org.json.JSONObject viaObj = new org.json.JSONObject(jsonObjectResponseEntity.getBody());
      String value = viaObj.has("data") && viaObj.getJSONArray("data").length() > 0 ? viaObj.getJSONArray("data").getJSONObject(0).getString("description") : "";
      calculateCiiu.put("code", ciiuStr);
      calculateCiiu.put("value",value );
      return calculateCiiu;
    }
    return null;
  }

  private JSONArray phoneNumMask(JSONArray phoneNumArray) {
    JSONArray phoneNumArrayMask = new JSONArray();
    String phoneMask = "";
    for (int i = 0; i < phoneNumArray.length(); i++) {
      phoneMask = "";
      if(!phoneNumArray.isNull(i) &&!phoneNumArray.isEmpty()) {

        org.json.JSONObject phoneObject = phoneNumArray.getJSONObject(i);


      // phone
      if(phoneObject.has("phoneType") && phoneObject.getString("phoneType").equals("11") && phoneObject.has("phone") && !phoneObject.getString("phone").isEmpty()){
        String phone = phoneObject.getString("phone");
        if(!phone.isEmpty()){
          phoneMask =  phone.substring(0, 1)+padLeft(phone.substring(phone.length() - 2), 6,'X');
          phoneObject.put("phone",phoneMask);
          phoneNumArrayMask.put(phoneObject);
        }
      }
      // mobile
      if(phoneObject.has("phoneType") && phoneObject.getString("phoneType").equals("12") && phoneObject.has("phone") && !phoneObject.getString("phone").isEmpty()){
        String phone = phoneObject.getString("phone");
        if(!phone.isEmpty()){
          phoneMask =  phone.substring(0, 2)+padLeft(phone.substring(phone.length() - 2), 8,'X');
          phoneObject.put("phone",phoneMask);
          phoneNumArrayMask.put(phoneObject);
        }
      }

      }
    }
    return phoneNumArrayMask;
  }



  private org.json.JSONObject calculatePostAddr(org.json.JSONObject postAddrArray) {
    String country_name = "";
    String city_name = "";
    String full_postAddr = "";

    // COUNTRY
    if (postAddrArray.has("country")&&!postAddrArray.getString("country").isEmpty()){
      String country = postAddrArray.getString("country");
      ResponseEntity<JSONObject> jsonObjectResponseEntity = consumeServices(TBL_BDB_COUNTRY_ADMIN_UNIT, false, country);
      if(jsonObjectResponseEntity.getStatusCode().equals(HttpStatus.OK)){
        org.json.JSONObject countryObj = new org.json.JSONObject(jsonObjectResponseEntity.getBody());
        country_name = countryObj.has("data") && countryObj.getJSONArray("data").length() > 0 ? countryObj.getJSONArray("data").getJSONObject(0).getString("name") : "";
        if (!country_name.isEmpty()){
          org.json.JSONObject country_format = new org.json.JSONObject();
          country_format.put("code", country);
          country_format.put("value",country_name );
          postAddrArray.put("country",country_format);
        }

      }
    }

    //City
    if (postAddrArray.has("city")&&!postAddrArray.getString("city").isEmpty()){
      String city = postAddrArray.getString("city");
      ResponseEntity<JSONObject> jsonObjectResponseEntity = consumeServices(TBL_DANE_CODES, false, city);
      if(jsonObjectResponseEntity.getStatusCode().equals(HttpStatus.OK)){
        org.json.JSONObject countryObj = new org.json.JSONObject(jsonObjectResponseEntity.getBody());
        city_name = countryObj.has("data") && countryObj.getJSONArray("data").length() > 0 ? countryObj.getJSONArray("data").getJSONObject(0).getString("description") : "";
        if (!city_name.isEmpty()){
          org.json.JSONObject city_format = new org.json.JSONObject();
          city_format.put("code", city);
          city_format.put("value",city_name );
          postAddrArray.put("city",city_format);
        }

      }

    }
    // Address
    if (postAddrArray.has("addr1")&&!postAddrArray.getString("addr1").isEmpty()){
      String smallName;
      String addr1 = postAddrArray.getString("addr1");
      String[] split = addr1.split(";");
      if (split.length > 9) {
        for (int i = 0; i < 10; i++) {
          smallName = " ";
          String via = split[i];
          if ((i % 2 == 0)&& !via.isEmpty() ) {
            ResponseEntity<JSONObject> jsonObjectResponseEntity = consumeServices(TBL_TYPE_WAY_I, false, via);
            if (jsonObjectResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
              org.json.JSONObject viaObj = new org.json.JSONObject(jsonObjectResponseEntity.getBody());
              smallName = viaObj.has("data") && viaObj.getJSONArray("data").length() > 0 ? viaObj.getJSONArray("data").getJSONObject(0).getString("smallName")+" " : "";

              if(smallName.isEmpty()){
                jsonObjectResponseEntity = consumeServices(TBL_TYPE_WAY_II, false, via);
                if (jsonObjectResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                  viaObj = new org.json.JSONObject(jsonObjectResponseEntity.getBody());
                  smallName = viaObj.has("data") && viaObj.getJSONArray("data").length() > 0 ? viaObj.getJSONArray("data").getJSONObject(0).getString("smallName")+" " : "";
                }
                }
            }
            full_postAddr += smallName;
          } else {
            if (!via.isEmpty()) {
              full_postAddr += via+" ";
            }

          }
        }
        full_postAddr += ", " + country_name + ", " + city_name;
        postAddrArray.put("fullDirection", full_postAddr);
      }
    }



    return postAddrArray;
  }

  private String emailMask(String emailAddr) {
    String[] name = emailAddr.split("@");
    if(name.length>0){
      String firstPartOld = name[0];
      String secondPartOld = name[1];
      if (!firstPartOld.isEmpty() &&!secondPartOld.isEmpty()) {

        int limitDomain = secondPartOld.indexOf(".");
        String domain = secondPartOld.substring(0, limitDomain);

        if(!domain.isEmpty()){

          String tlsStr = secondPartOld.substring(limitDomain);
          String firstPart = padRight(firstPartOld.substring(0, 2), firstPartOld.length() - 2, 'X');
          String secondPart = padLeft(tlsStr, secondPartOld.length(), 'X');

          emailAddr = firstPart + "@" + secondPart;
        }
      }

    }
    return emailAddr;
  }

}
