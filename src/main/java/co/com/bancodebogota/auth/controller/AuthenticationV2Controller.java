package co.com.bancodebogota.auth.controller;

import co.com.bancodebogota.adpe.dto.response.Response;
import co.com.bancodebogota.adpe.dto.response.StatusResponse;
import co.com.bancodebogota.adpe.dto.rolesGroupName.DAResponseDtoV2;
import co.com.bancodebogota.adpe.dto.rolesGroupName.RolDtoV2;
import co.com.bancodebogota.adpe.logger.BdbLoggerUtils;
import co.com.bancodebogota.adpe.logger.RestExchange;
import co.com.bancodebogota.adpe.rest.Rest;
import co.com.bancodebogota.auth.dto.LDAResponseDtoV2;
import co.com.bancodebogota.auth.dto.idm.IDMRequestDto;
import co.com.bancodebogota.auth.dto.idm.IDMResponseDto;
import co.com.bancodebogota.auth.dto.mng.AuthValidateUserRequestDto;
import co.com.bancodebogota.auth.dto.mng.AuthValidateUserResponseDto;
import co.com.bancodebogota.auth.dto.user.UserRequestDto;
import co.com.bancodebogota.auth.encrypt.Encrypt3Des;
import co.com.bancodebogota.auth.services.*;
import co.com.bancodebogota.aws.Cognito;
import co.com.bancodebogota.aws.CognitoInitException;
import co.com.bancodebogota.aws.SessionToken;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v2")
public class AuthenticationV2Controller {

  @Autowired
  IDMService idmService;
  @Value("${adapter.adpe.validNit}")
  public String adapterCases;
  @Autowired
  CorporateService corporateService;
  @Autowired
  OTPService otpService;
  @Autowired
  CrudUserService crudUserService;
  @Autowired
  IAwsService iAwsService;
  private RestExchange restExchange;
  Encrypt3Des encrypt3Des;
  @Value("${encrypt.keyPri}")
  private String keyPri;
  @Value("${encrypt.seed}")
  private String semilla;
  private String DOMAIN="BANCODEBOGOTA";
  private static final Log logger = LogFactory.getLog(AssociateNitController.class);

  @PostMapping("validateUser")
  public ResponseEntity<AuthValidateUserResponseDto> validateUser(@Valid @RequestBody AuthValidateUserRequestDto authRequestDto, BindingResult bd) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
    /* AuthValidateUserResponseDto answer = new AuthValidateUserResponseDto(); */
    LDAResponseDtoV2 responseDtoV2 = new LDAResponseDtoV2();
    Response response = new Response();
    if (bd.hasErrors()) {
      response.setStatusResponse(initStatusResponse(2, "Bad Credential"));
      response.setData(new ArrayList<>());
      BdbLoggerUtils.info("ERROR AUTH MANAGER PROCESSING REQUEST IN V3" + this.idmService.getURL());
      BdbLoggerUtils.debug("WITH CREDENTIAL " + authRequestDto.getLoginID());
      // BdbLoggerUtils.info(answer.getStatusDesc());
      return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    } else {
      BdbLoggerUtils.info("VALIDATE REQUEST V3");
      String invalidMessage = "";
      boolean validRequest = true;
      HttpStatus responseStatus = HttpStatus.OK;
      if (authRequestDto.getLoginID().isEmpty() || authRequestDto.getLoginID().trim().isEmpty()) {
        BdbLoggerUtils.info("LOGIN REQUEST INVALID V3");
        validRequest = false;
        invalidMessage = invalidMessage + "Login invalido. ";
      }

      if (authRequestDto.getPassword().isEmpty() || authRequestDto.getPassword().trim().isEmpty()) {
        BdbLoggerUtils.info("PASSWORD REQUEST INVALID V3");
        validRequest = false;
        invalidMessage = invalidMessage + "Password invalido. ";
      }

      if (validRequest) {
        BdbLoggerUtils.info("START AUTH MANAGER FOR VALIDATE IDM USER V3");
        IDMRequestDto idmRequest = new IDMRequestDto();
        idmRequest.setUser(authRequestDto.getLoginID().trim());

        String decifrarRsa = encrypt3Des.decryptRsa(authRequestDto.getPassword().toString(), keyPri);

        idmRequest.setPassword(decifrarRsa);
        BdbLoggerUtils.info("INIT REQUEST IDM ADAPTER V3");
        ResponseEntity<IDMResponseDto> idmResponse = this.idmService.validateAuthentication(idmRequest);
        BdbLoggerUtils.info("VALIDATE RESPONSE IDM ADAPTER V3");
        IDMResponseDto answerIDM = (IDMResponseDto)idmResponse.getBody();
        if ( idmResponse.getStatusCode().equals(HttpStatus.OK) && answerIDM!=null &&
            ((IDMResponseDto)idmResponse.getBody()).getStatus() != null && ((IDMResponseDto)idmResponse.getBody()).getStatus().equals(0)) {
          BdbLoggerUtils.info("if 1 V3");
          if (((IDMResponseDto)idmResponse.getBody()).getInfo() != null) {
            BdbLoggerUtils.info("if 2 V3");
            if(checkCreateIdmUser(answerIDM, authRequestDto)){
              BdbLoggerUtils.info("if 3 V3");
              String loginID = authRequestDto.getLoginID();
              /*TODO EMPLOYEE*/
              try {
                BdbLoggerUtils.info("try 1 V3");
                iAwsService.init();
                Cognito cognito = new Cognito(loginID);
                SessionToken sessionToken = null;

                int code=cognito.signUp("",DOMAIN,((IDMResponseDto)idmResponse.getBody()).getInfo().getMail(),((IDMResponseDto)idmResponse.getBody()).getInfo().getUserName());
                sessionToken=null;
                if(code==200){
                  sessionToken=cognito.signIn();

                  BdbLoggerUtils.info("paso 1");

                  if (((IDMResponseDto)idmResponse.getBody()).getInfo().getGroupName() != null) {
                    BdbLoggerUtils.info("paso 2");
                    // answer.setGroups(((IDMResponseDto)idmResponse.getBody()).getInfo().getGroupName());
                    responseDtoV2.setGroupName( ((IDMResponseDto)idmResponse.getBody()).getInfo().getGroupName());
                    BdbLoggerUtils.info("------getInfo");
                    BdbLoggerUtils.info(((IDMResponseDto)idmResponse.getBody()).getInfo().toString());

                    Rest allowedRolesRest = new Rest();
                    List<String> allowedRoles = null;
                    try {
                      allowedRoles = allowedRolesRest.clean().withUrl(adapterCases).withMethod(HttpMethod.GET).withPath("/roles/v1/groups").send(new TypeReference<List<String>>() {
                      });
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                    String rolGroupName = getRoleAllowed(allowedRoles, responseDtoV2.getGroupName());
                    if (!rolGroupName.equals("")){
                      Rest rest = new Rest();
                      RolDtoV2 rolList = null;
                      BdbLoggerUtils.info("Tiene un grupo autorizado el empleado");
                      BdbLoggerUtils.info("rolGroupName" + rolGroupName);

                      try {
                        rolList = rest.clean().withUrl(adapterCases).withMethod(HttpMethod.GET).withPath("/roles/v1/roles/" + rolGroupName).send(new TypeReference<RolDtoV2>() {
                        });
                      } catch (Exception e) {
                        e.printStackTrace();
                      }
                      responseDtoV2.setActions(rolList.getActions());

                      if (((IDMResponseDto)idmResponse.getBody()).getInfo().getMail() != null) {
                        responseDtoV2.setMail(((IDMResponseDto)idmResponse.getBody()).getInfo().getMail());

                        if (((IDMResponseDto)idmResponse.getBody()).getInfo().getUserName() != null) {
                          responseDtoV2.setUserName( ((IDMResponseDto)idmResponse.getBody()).getInfo().getUserName());
                          response.setStatusResponse(initStatusResponse(0, "Successfull"));

                          if (((IDMResponseDto)idmResponse.getBody()).getInfo().getFullName() != null) {
                            responseDtoV2.setFullName(   ((IDMResponseDto)idmResponse.getBody()).getInfo().getFullName());
                          } else {
                            responseDtoV2.setFullName("");
                          }
                          if (((IDMResponseDto)idmResponse.getBody()).getInfo().getEmployeeid() != null) {
                            responseDtoV2.setEmployeeId(  Long.parseLong(((IDMResponseDto)idmResponse.getBody()).getInfo().getEmployeeid()));
                          } else {
                            responseDtoV2.setEmployeeId(null);
                          }

                          responseDtoV2.setAccessToken(sessionToken.getAccessToken());
                          responseDtoV2.setTokenId( sessionToken.getTokenId());
                          response.setData(responseDtoV2);

                        } else {
                          System.out.println("no tiene userName asignado el empleado");
                          response.setStatusResponse(initStatusResponse(2, "No hay un userName asignado para este empleado"));
                          response.setData(new ArrayList<>());
                          BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE ROLES V4");
                          responseStatus = HttpStatus.BAD_REQUEST;
                        }
                      } else {
                        System.out.println("no tiene correo asignado el empleado");
                        response.setStatusResponse(initStatusResponse(2, "No hay un correo asignado para este empleado"));
                        response.setData(new ArrayList<>());
                        BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE ROLES V4");
                        responseStatus = HttpStatus.BAD_REQUEST;
                      }

                    } else {
                      System.out.println("no tiene groupName asignado el empleado");
                      response.setStatusResponse(initStatusResponse(2, "No hay un groupName autorizado para este empleado"));
                      response.setData(new ArrayList<>());
                      BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE ROLES V3");
                      responseStatus = HttpStatus.BAD_REQUEST;
                    }
                  }
                }else{
                  // answer.setStatusCode("1");
                  // answer.setStatusDesc( "Error de Auth AWS");
                  response.setStatusResponse(initStatusResponse(2, "Error de Auth AWS"));
                  response.setData(new ArrayList<>());
                  BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE AWS V3");
                  responseStatus = HttpStatus.BAD_REQUEST;
                }
              } catch (CognitoInitException e) {
                e.printStackTrace();

                BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE AWS V3");
                BdbLoggerUtils.info(e.getMessage());

                // answer.setStatusCode("2");
                // answer.setStatusDesc(e.getMessage());
                response.setStatusResponse(initStatusResponse(2, e.getMessage()));
                response.setData(new ArrayList<>());

                BdbLoggerUtils.info("CONNECTION ERROR AWS THE IDM USER V3");
                responseStatus = HttpStatus.BAD_REQUEST;
              }

            }
            else{
              // answer.setStatusCode("2");
              // answer.setStatusDesc("Error de conexión");
              response.setStatusResponse(initStatusResponse(2, "Error de conexión"));
              response.setData(new ArrayList<>());
              BdbLoggerUtils.info("CONNECTION ERROR CREATING THE LOCAL USER V3");
              responseStatus = HttpStatus.BAD_REQUEST;
            }
            // answer.setLoginDT(DateUtils.dateFormat(new Date()));
            BdbLoggerUtils.info("FINISH AUTH MANAGER FOR VALIDATE IDM USER V3");

          }
          else{
            // answer.setStatusCode(String.valueOf(((IDMResponseDto)idmResponse.getBody()).getStatus()));
            // answer.setStatusDesc(((IDMResponseDto)idmResponse.getBody()).getStatusDesc());

            response.setStatusResponse(initStatusResponse(((IDMResponseDto)idmResponse.getBody()).getStatus(), ((IDMResponseDto)idmResponse.getBody()).getStatusDesc()));
            response.setData(new ArrayList<>());
            BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE IDM USER V3");
            // BdbLoggerUtils.info(answer.getStatusDesc());
            responseStatus = idmResponse.getStatusCode();
          }
          //CREATE USER IN LOCAL DATABASE

        } else {
          BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE IDM USER V3");
          // BdbLoggerUtils.info(answer.getStatusDesc());
          if (answerIDM!=null && ((IDMResponseDto)idmResponse.getBody()).getStatus() != null && ((IDMResponseDto)idmResponse.getBody()).getStatus()!=null){
            response.setStatusResponse(initStatusResponse(((IDMResponseDto)idmResponse.getBody()).getStatus(), ((IDMResponseDto)idmResponse.getBody()).getStatusDesc() ));
            response.setData(new ArrayList<>());

            responseStatus = HttpStatus.BAD_REQUEST;
          }else{
            response.setStatusResponse(initStatusResponse(1, "usuario/password inválidos"));
            response.setData(new ArrayList<>());

            responseStatus = idmResponse.getStatusCode();
          }

        }
      } else {
        responseStatus = HttpStatus.BAD_REQUEST;
        response.setStatusResponse(initStatusResponse(2, invalidMessage));
        response.setData(new ArrayList<>());
      }

      return new ResponseEntity(response, responseStatus);
    }
  }

  public boolean checkCreateIdmUser(IDMResponseDto answerIDM, AuthValidateUserRequestDto authRequestDto){
    //CREATE USER EMPLOYEE IN LOCAL DATABASE
    UserRequestDto userRequestDto = new UserRequestDto();
    userRequestDto.setUsername(authRequestDto.getLoginID().toUpperCase());
    userRequestDto.setType("E");
    userRequestDto.setDomain(DOMAIN);
    userRequestDto.setName(answerIDM.getInfo().getFullName());
    userRequestDto.setEmail(answerIDM.getInfo().getMail());
    userRequestDto.setLastLogin(new java.sql.Date((new Date()).getTime()));

    JSONObject aditionalInfo = new JSONObject();
    userRequestDto.setAditionalInfo(aditionalInfo.toJSONString());
    ResponseEntity<JSONObject> responseInsertUser = crudUserService.insertUser(userRequestDto);
    BdbLoggerUtils.info(responseInsertUser.getBody().toJSONString());
    return responseInsertUser.getStatusCode().equals(HttpStatus.OK);
  }

  public static StatusResponse initStatusResponse(Integer status, String statusDesc) {
    StatusResponse response = new StatusResponse();
    response.setStatus(status);
    response.setStatusDesc(statusDesc);
    response.setTimestamp(new Date());
    return response;
  }

  public String getRoleAllowed(List<String> arr1, List<String> arr2) {
    String respuesta = "";
    for (int i = 0; i < arr2.size(); i++) {
      if (arr1.contains(arr2.get(i).toUpperCase())){
        respuesta = arr2.get(i).toUpperCase();
        break;
      }
    }
    return respuesta;
  }
}