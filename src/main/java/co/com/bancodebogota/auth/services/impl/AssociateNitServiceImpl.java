package co.com.bancodebogota.auth.services.impl;

import co.com.bancodebogota.adpe.logger.BdbLoggerUtils;
import co.com.bancodebogota.adpe.logger.RestExchange;
import co.com.bancodebogota.adpe.util.Response;
import co.com.bancodebogota.auth.dto.associateNitDTO.AssociateNitRequest;
import co.com.bancodebogota.auth.services.IAssociateNitService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import static co.com.bancodebogota.adpe.util.Constant.MSN_OPERATION_BEGIN;
import static co.com.bancodebogota.adpe.util.Constant.MSN_OPERATION_DB_BAD;

@Service
public class AssociateNitServiceImpl implements IAssociateNitService {

  private static final Log logger = LogFactory.getLog(AssociateNitServiceImpl.class);

  @Value("${adapter.adpe.validNit}")
  public String adapterCasesNit;

  Response response;

  private RestExchange restExchange;

  public AssociateNitServiceImpl() {
    restExchange = new RestExchange();
    response = new Response();
  }

  @Override
  public ResponseEntity<JSONObject> findNitAssociate(String company, String userName) {
    logger.info(MSN_OPERATION_BEGIN.toUpperCase());
    try{
      BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
      BdbLoggerUtils.info("FIND NIT ASSOCIATE WITH INFO: { CB: " + company + ", userName: " + userName + " }");
      ResponseEntity<JSONObject> exchange = restExchange.exchange(adapterCasesNit + "/customer/idByUser/" + company + "/" + userName,
          null, HttpMethod.GET,  JSONObject.class);
      if (exchange.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR) && exchange.getBody() == null) {
        throw new Exception("Servidor no responde");
      }
      return getJsonObjectResponseEntity(exchange);
    }catch (Exception e){
      return buildObjectResponseEntity(e, 2);
    }
  }

  @Override
  public ResponseEntity<JSONObject> saveNitAssociate(AssociateNitRequest associateNitRequest) {
    logger.info(MSN_OPERATION_BEGIN.toUpperCase());
    try {
      BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
      BdbLoggerUtils.info("SAVE NIT ASSOCIATE WITH INFO: " + associateNitRequest.toString());
      ResponseEntity<JSONObject> exchange = restExchange.exchange(adapterCasesNit + "/customer/addUserToId", associateNitRequest, HttpMethod.POST, JSONObject.class);
      if (exchange.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR) && exchange.getBody() == null) {
        throw new Exception("Servidor no responde");
      }
      return exchange;
    } catch (Exception e) {
      return buildObjectResponseEntity(e, 2);
    }
  }

  public ResponseEntity<JSONObject> buildObjectResponseEntity(Exception e, int status) {
    logger.info("ERROR CONSUME ADPE ADAPTER");
    e.printStackTrace();
    return new ResponseEntity<>(response.response(
        response.buildStatus(status, e.getMessage().isEmpty() ? MSN_OPERATION_DB_BAD : e.getMessage()), null,
        false), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<JSONObject> getJsonObjectResponseEntity(ResponseEntity<JSONObject> exchange) {
    ResponseEntity<JSONObject> responseService;
    HttpStatus statusCode = exchange.getStatusCode();
    JSONObject body = exchange.getBody();
    if (statusCode.equals(HttpStatus.OK)) {
      responseService = new ResponseEntity<>(body, HttpStatus.OK);
    } else if (statusCode.equals(HttpStatus.CONFLICT)) {
      responseService = new ResponseEntity<>(body, HttpStatus.CONFLICT);
    } else {

      responseService = body != null ? new ResponseEntity<>(body, statusCode) : new ResponseEntity<>(statusCode);
    }
    BdbLoggerUtils.info("FINISH CONSUME ADPE ADAPTER");
    return responseService;
  }

}
