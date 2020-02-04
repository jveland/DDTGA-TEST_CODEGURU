package co.com.bancodebogota.auth.controller;

import co.com.bancodebogota.adpe.logger.BdbLoggerUtils;
import co.com.bancodebogota.auth.dto.corporate.CorporateRetrieveCompanyRequestDto;
import co.com.bancodebogota.auth.dto.corporate.CorporateRetrieveCompanyResponseDto;
import co.com.bancodebogota.auth.dto.corporate.CorporateValidateUserRequestDto;
import co.com.bancodebogota.auth.dto.corporate.CorporateValidateUserResponseDto;
import co.com.bancodebogota.auth.dto.idm.IDMRequestDto;
import co.com.bancodebogota.auth.dto.idm.IDMResponseDto;
import co.com.bancodebogota.auth.dto.mng.AuthValidateUserRequestDto;
import co.com.bancodebogota.auth.dto.mng.AuthValidateUserResponseDto;
import co.com.bancodebogota.auth.dto.mng.ValidateUserCorporateRequestDto;
import co.com.bancodebogota.auth.dto.mng.ValidateUserCorporateResponseDto;
import co.com.bancodebogota.auth.dto.otp.OTPRequestDto;
import co.com.bancodebogota.auth.dto.otp.OTPResponseDto;
import co.com.bancodebogota.auth.dto.user.UserRequestDto;
import co.com.bancodebogota.auth.encrypt.Encrypt3Des;
import co.com.bancodebogota.auth.services.*;
import co.com.bancodebogota.auth.utils.DateUtils;
import co.com.bancodebogota.aws.Cognito;
import co.com.bancodebogota.aws.CognitoInitException;
import co.com.bancodebogota.aws.SessionToken;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;
import java.security.InvalidKeyException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@CrossOrigin(
    origins = {"*"}
)
@RestController
public class AuthenticationController {
    @Autowired
    IDMService idmService;
    @Autowired
    CorporateService corporateService;
    @Autowired
    OTPService otpService;
    @Autowired
    CrudUserService crudUserService;

    @Autowired
    IAwsService iAwsService;

    Encrypt3Des encrypt3Des;

    @Value("${encrypt.keyPri}")
    private String keyPri;
    @Value("${encrypt.seed}")
    private String semilla;

    private String DOMAIN="BANCODEBOGOTA";

    /*public AuthenticationController() {
    }*/

    @PostMapping({"/validateUserIDM"})
    public ResponseEntity<AuthValidateUserResponseDto> validateUserIDM(@Valid @RequestBody AuthValidateUserRequestDto authRequestDto, BindingResult bd) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        AuthValidateUserResponseDto answer = new AuthValidateUserResponseDto();
        if (bd.hasErrors()) {
            answer = new AuthValidateUserResponseDto();
            answer.setStatusCode("2");
            answer.setStatusDesc("Bad Credential");
            BdbLoggerUtils.info("ERROR AUTH MANAGER PROCESSING REQUEST IN " + this.idmService.getURL());
            BdbLoggerUtils.debug("WITH CREDENTIAL " + authRequestDto.getLoginID());
            BdbLoggerUtils.info(answer.getStatusDesc());
            return new ResponseEntity(answer, HttpStatus.BAD_REQUEST);
        } else {
            BdbLoggerUtils.info("VALIDATE REQUEST");
            boolean validRequest = true;
            String invalidMessage = "";
            HttpStatus responseStatus = HttpStatus.OK;
            if (authRequestDto.getLoginID().isEmpty() || authRequestDto.getLoginID().trim().isEmpty()) {
                BdbLoggerUtils.info("LOGIN REQUEST INVALID");
                validRequest = false;
                invalidMessage = invalidMessage + "Login invalido. ";
            }

            if (authRequestDto.getPassword().isEmpty() || authRequestDto.getPassword().trim().isEmpty()) {
                BdbLoggerUtils.info("PASSWORD REQUEST INVALID");
                validRequest = false;
                invalidMessage = invalidMessage + "Password invalido. ";
            }

            if (validRequest) {
                BdbLoggerUtils.info("START AUTH MANAGER FOR VALIDATE IDM USER");
                IDMRequestDto idmRequest = new IDMRequestDto();
                idmRequest.setUser(authRequestDto.getLoginID().trim());

                String decifrarRsa = encrypt3Des.decryptRsa(authRequestDto.getPassword().toString(), keyPri);

                idmRequest.setPassword(decifrarRsa);
                BdbLoggerUtils.info("INIT REQUEST IDM ADAPTER");
                ResponseEntity<IDMResponseDto> idmResponse = this.idmService.validateAuthentication(idmRequest);
                BdbLoggerUtils.info("VALIDATE RESPONSE IDM ADAPTER");
                IDMResponseDto answerIDM = (IDMResponseDto)idmResponse.getBody();
                if ( idmResponse.getStatusCode().equals(HttpStatus.OK) && answerIDM!=null &&
                    ((IDMResponseDto)idmResponse.getBody()).getStatus() != null && ((IDMResponseDto)idmResponse.getBody()).getStatus().equals(0)) {

                    if (((IDMResponseDto)idmResponse.getBody()).getInfo() != null) {

                        if(checkCreateIdmUser(answerIDM, authRequestDto)){

                            String loginID = authRequestDto.getLoginID();
                            /*TODO EMPLOYEE*/
                            try {
                                iAwsService.init();
                                Cognito cognito = new Cognito(loginID);
                                SessionToken sessionToken = null;

                                int code=cognito.signUp("",DOMAIN,((IDMResponseDto)idmResponse.getBody()).getInfo().getMail(),((IDMResponseDto)idmResponse.getBody()).getInfo().getUserName());
                                sessionToken=null;
                                if(code==200){
                                    sessionToken=cognito.signIn();

                                    answer.setStatusCode("0");
                                    answer.setStatusDesc("success");
                                    answer.setUserEmail(((IDMResponseDto)idmResponse.getBody()).getInfo().getMail());
                                    answer.setUserName(((IDMResponseDto)idmResponse.getBody()).getInfo().getUserName());
                                    if (((IDMResponseDto)idmResponse.getBody()).getInfo().getGroupName() != null) {
                                        answer.setGroups(((IDMResponseDto)idmResponse.getBody()).getInfo().getGroupName());
                                    }
                                    answer.setAccessToken(sessionToken.getTokenId());

                                }else{
                                    answer.setStatusCode("1");
                                    answer.setStatusDesc( "Error de Auth AWS");
                                    BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE AWS");
                                    responseStatus = HttpStatus.BAD_REQUEST;
                                }
                            } catch (CognitoInitException e) {
                                e.printStackTrace();

                                BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE AWS");
                                BdbLoggerUtils.info(e.getMessage());

                                answer.setStatusCode("2");
                                answer.setStatusDesc(e.getMessage());
                                BdbLoggerUtils.info("CONNECTION ERROR AWS THE IDM USER");
                                responseStatus = HttpStatus.BAD_REQUEST;
                            }

                        }
                        else{
                            answer.setStatusCode("2");
                            answer.setStatusDesc("Error de conexión");
                            BdbLoggerUtils.info("CONNECTION ERROR CREATING THE LOCAL USER");
                            responseStatus = HttpStatus.BAD_REQUEST;
                        }
                        answer.setLoginDT(DateUtils.dateFormat(new Date()));
                        BdbLoggerUtils.info("FINISH AUTH MANAGER FOR VALIDATE IDM USER");

                    }
                    else{
                        answer.setStatusCode(String.valueOf(((IDMResponseDto)idmResponse.getBody()).getStatus()));
                        answer.setStatusDesc(((IDMResponseDto)idmResponse.getBody()).getStatusDesc());
                        BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE IDM USER");
                        BdbLoggerUtils.info(answer.getStatusDesc());
                        responseStatus = idmResponse.getStatusCode();
                    }
                    //CREATE USER IN LOCAL DATABASE

                } else {
                    BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE IDM USER");
                    BdbLoggerUtils.info(answer.getStatusDesc());
                    if (answerIDM!=null && ((IDMResponseDto)idmResponse.getBody()).getStatus() != null && ((IDMResponseDto)idmResponse.getBody()).getStatus()!=null){

                        answer.setStatusCode(String.valueOf(((IDMResponseDto)idmResponse.getBody()).getStatus()));
                        answer.setStatusDesc(((IDMResponseDto)idmResponse.getBody()).getStatusDesc());
                        responseStatus = HttpStatus.BAD_REQUEST;
                    }else{
                        answer.setStatusCode("1");
                        answer.setStatusDesc("usuario/password inválidos");
                        responseStatus = idmResponse.getStatusCode();
                    }

                }
            } else {
                responseStatus = HttpStatus.BAD_REQUEST;
                answer.setStatusCode("2");
                answer.setStatusDesc(invalidMessage);
            }

            return new ResponseEntity(answer, responseStatus);
        }
    }

    @PostMapping({"/validateUserCorporate"})
    public ResponseEntity<ValidateUserCorporateResponseDto> validateUserCorporate(@Valid @RequestBody ValidateUserCorporateRequestDto authRequestDto, BindingResult bd) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        new ValidateUserCorporateResponseDto();
        ValidateUserCorporateResponseDto answerValidUserCorporate;
        if (!bd.hasErrors()) {
            BdbLoggerUtils.info("START AUTH MANAGER FOR VALIDATE CORPORATE");
            String invalidMessage = this.validateUserCorporateRequestDto(authRequestDto);
            boolean validRequest = false;
            if (invalidMessage.equals("")) {
                validRequest = true;
            }

            HttpStatus responseStatus = HttpStatus.OK;
            BdbLoggerUtils.info("loginId: " + authRequestDto.getLoginId() + ", ipAddress: " + authRequestDto.getIpAddress() + ", custPermId: " + authRequestDto.getCustPermId());
            if (validRequest) {
                CorporateValidateUserResponseDto answerValidateUser = this.mapperRequestValidateUser(authRequestDto);
                if (this.checkValidateUser(answerValidateUser)) {
                    OTPResponseDto answerValidateOtp = this.mapperRequestOtpAuth(authRequestDto, answerValidateUser);
                    if (this.checkAuthenticationOTP(answerValidateOtp)) {
                        CorporateRetrieveCompanyResponseDto answerRetrieveCompany = this.mapperRequestRetrieveCompany(authRequestDto);
                        if (this.checkCorporateRetrieveCompanyResponseDto(answerRetrieveCompany)) {
                            //CREATE USER IN LOCAL DATABASE
                            if(checkCreateCorporateUser(authRequestDto, answerRetrieveCompany, answerValidateOtp)){

                                try {
                                    iAwsService.init();
                                    Cognito cognito = new Cognito(authRequestDto.getLoginId());
                                    SessionToken sessionToken = null;

                                    int code=cognito.signUp("",DOMAIN,answerRetrieveCompany.getUserElement().getEmail(),answerRetrieveCompany.getUserElement().getGivenName());
                                    sessionToken=null;
                                    if(code==200){
                                        sessionToken=cognito.signIn();

                                        answerValidUserCorporate = this.mapperResponseValidateUserCorporate(answerRetrieveCompany, answerValidateOtp,sessionToken.getTokenId());
                                    }else{
                                        answerValidUserCorporate = this.mapperException("1", "Error de Auth AWS");
                                        BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE RETRIEVE COMPANY CORPORATE");
                                        BdbLoggerUtils.info(answerValidUserCorporate.getStatusDesc());
                                        responseStatus = HttpStatus.BAD_REQUEST;
                                    }
                                } catch (CognitoInitException e) {
                                    e.printStackTrace();
                                    answerValidUserCorporate = this.mapperException("2", e.getMessage());
                                    BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE RETRIEVE COMPANY CORPORATE");
                                    BdbLoggerUtils.info(answerValidUserCorporate.getStatusDesc());
                                    responseStatus = HttpStatus.BAD_REQUEST;
                                }

                            }
                            else{
                                answerValidUserCorporate = this.mapperException("2", "Error de conexión");
                                BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE RETRIEVE COMPANY CORPORATE");
                                BdbLoggerUtils.info(answerValidUserCorporate.getStatusDesc());
                                responseStatus = HttpStatus.BAD_REQUEST;
                            }

                        } else {
                            BdbLoggerUtils.info("loginId: " + authRequestDto.getLoginId() + ", ipAddress: " + authRequestDto.getIpAddress() + ", custPermId: " + authRequestDto.getCustPermId());
                            answerValidUserCorporate = this.mapperException(String.valueOf(answerRetrieveCompany.getStatusDto().getStatusCode()), String.valueOf(answerRetrieveCompany.getStatusDto().getStatusDesc()));
                            BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE RETRIEVE COMPANY CORPORATE");
                            BdbLoggerUtils.info(answerValidUserCorporate.getStatusDesc());
                            responseStatus = HttpStatus.BAD_REQUEST;
                        }
                    } else {
                        BdbLoggerUtils.info("loginId: " + authRequestDto.getLoginId() + ", ipAddress: " + authRequestDto.getIpAddress() + ", custPermId: " + authRequestDto.getCustPermId());
                        answerValidUserCorporate = this.mapperException(String.valueOf(answerValidateOtp.getStatusCode()), String.valueOf(answerValidateOtp.getStatusDesc()));
                        BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE USER OTP");
                        BdbLoggerUtils.info(answerValidUserCorporate.getStatusDesc());
                        responseStatus = HttpStatus.BAD_REQUEST;
                    }
                } else {
                    BdbLoggerUtils.info("loginId: " + authRequestDto.getLoginId() + ", ipAddress: " + authRequestDto.getIpAddress() + ", custPermId: " + authRequestDto.getCustPermId());
                    answerValidUserCorporate = this.mapperException(String.valueOf(answerValidateUser.getStatusDto().getStatusCode()), String.valueOf(answerValidateUser.getStatusDto().getStatusDesc()));
                    BdbLoggerUtils.info("ERROR AUTH MANAGER FOR VALIDATE USER CORPORATE");
                    BdbLoggerUtils.info(answerValidUserCorporate.getStatusDesc());
                    responseStatus = HttpStatus.BAD_REQUEST;
                }
            } else {
                BdbLoggerUtils.info("loginId: " + authRequestDto.getLoginId() + ", ipAddress: " + authRequestDto.getIpAddress() + ", custPermId: " + authRequestDto.getCustPermId());
                responseStatus = HttpStatus.BAD_REQUEST;
                answerValidUserCorporate = this.mapperException("2", invalidMessage);
            }

            return new ResponseEntity(answerValidUserCorporate, responseStatus);
        } else {
            answerValidUserCorporate = new ValidateUserCorporateResponseDto();
            answerValidUserCorporate.setStatusCode("2");
            answerValidUserCorporate.setStatusDesc("Bad Credential");
            BdbLoggerUtils.info("ERROR AUTH MANAGER PROCESSING REQUEST IN " + this.otpService.getURL() + " AND " + this.corporateService.getURL());
            BdbLoggerUtils.info("loginId: " + authRequestDto.getLoginId() + ", ipAddress: " + authRequestDto.getIpAddress() + ", custPermId: " + authRequestDto.getCustPermId());
            BdbLoggerUtils.info(answerValidUserCorporate.getStatusDesc());
            return new ResponseEntity(answerValidUserCorporate, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping({"/health"})
    public ResponseEntity<String> health() {
        return new ResponseEntity("Health ok!", HttpStatus.OK);
    }

    public ValidateUserCorporateResponseDto mapperException(String statusCode, String statusDesc) {
        ValidateUserCorporateResponseDto answer = new ValidateUserCorporateResponseDto();
        answer.setStatusCode(statusCode);
        answer.setStatusDesc(statusDesc);
        return answer;
    }

    public CorporateValidateUserResponseDto mapperRequestValidateUser(ValidateUserCorporateRequestDto requestValidateUserCorporate) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException{
        CorporateValidateUserRequestDto corporateValidateUserRequest = new CorporateValidateUserRequestDto();
        BdbLoggerUtils.info("INIT REQUEST ADAPTER CORPORATE OPERATION VALIDATE USER");
        corporateValidateUserRequest.setCustLoginId(requestValidateUserCorporate.getCustLoginId());
        corporateValidateUserRequest.setLoginId(requestValidateUserCorporate.getLoginId());
        String decifrarRsa1 = encrypt3Des.decryptRsa(requestValidateUserCorporate.getPassword().toString(), keyPri);
        String cifrar3desr = encrypt3Des.encrypt3Des(decifrarRsa1, semilla);
        corporateValidateUserRequest.setPassword(cifrar3desr);
        corporateValidateUserRequest.setIPAddress(requestValidateUserCorporate.getIpAddress());
        ResponseEntity<CorporateValidateUserResponseDto> corporateValidateUserResponse = this.corporateService.validateUser(corporateValidateUserRequest);
        BdbLoggerUtils.info("VALIDATE RESPONSE CORPORATE ADAPTER OPERATION VALIDATE USER");
        CorporateValidateUserResponseDto answerValidateUser = (CorporateValidateUserResponseDto)corporateValidateUserResponse.getBody();
        return answerValidateUser;
    }

    public OTPResponseDto mapperRequestOtpAuth(ValidateUserCorporateRequestDto requestValidateUserCorporate, CorporateValidateUserResponseDto answerValidateUser) {
        OTPRequestDto otpRequestDto = new OTPRequestDto();
        BdbLoggerUtils.info("INIT REQUEST ADAPTER OTP OPERATION VALIDATEOTP");
        otpRequestDto.setCustIdCustPermId(requestValidateUserCorporate.getCustLoginId().substring(2));
        otpRequestDto.setUserIdCustPermId(answerValidateUser.getPseValidateUserDto().getBnkUserKy());
        otpRequestDto.setOtp(requestValidateUserCorporate.getOtp());
        ResponseEntity<OTPResponseDto> otpResponse = this.otpService.validateToken(otpRequestDto);
        BdbLoggerUtils.info("VALIDATE RESPONSE OTP ADAPTER OPERATION VALIDATEOTP");
        OTPResponseDto answerValidateOtp = (OTPResponseDto)otpResponse.getBody();
        return answerValidateOtp;
    }

    public CorporateRetrieveCompanyResponseDto mapperRequestRetrieveCompany(ValidateUserCorporateRequestDto requestValidateUserCorporate) {
        CorporateRetrieveCompanyRequestDto corporateRetrieveCompanyRequest = new CorporateRetrieveCompanyRequestDto();
        BdbLoggerUtils.info("INIT REQUEST CORPORATE ADAPTER OPERATION RETRIEVE COMPANY");
        corporateRetrieveCompanyRequest.setCustLoginId(requestValidateUserCorporate.getCustLoginId());
        corporateRetrieveCompanyRequest.setUserId(requestValidateUserCorporate.getLoginId());
        corporateRetrieveCompanyRequest.setSpNameType("N");
        ResponseEntity<CorporateRetrieveCompanyResponseDto> corporateRetrieveCompanyResponse = this.corporateService.retrieveCompany(corporateRetrieveCompanyRequest);
        BdbLoggerUtils.info("VALIDATE RESPONSE CORPORATE ADAPTER OPERATION RETRIEVE COMPANY");
        CorporateRetrieveCompanyResponseDto answerRetrieveCompany = (CorporateRetrieveCompanyResponseDto)corporateRetrieveCompanyResponse.getBody();
        return answerRetrieveCompany;
    }

    public ValidateUserCorporateResponseDto mapperResponseValidateUserCorporate(CorporateRetrieveCompanyResponseDto answerRetrieveCompany, OTPResponseDto answerValidateOtp, String token) {
        ValidateUserCorporateResponseDto answerValidUserCorporate = new ValidateUserCorporateResponseDto();
        answerValidUserCorporate.setStatusCode("0");
        answerValidUserCorporate.setStatusDesc("success");
        answerValidUserCorporate.setUserEmail(answerRetrieveCompany.getUserElement().getEmail());
        answerValidUserCorporate.setUserName(answerRetrieveCompany.getUserElement().getGivenName());
        answerValidUserCorporate.setUserLastName(answerRetrieveCompany.getUserElement().getFamilyName());
        answerValidUserCorporate.setSecureCode(answerValidateOtp.getAuthCode());
        answerValidUserCorporate.setLoginDT(this.actualDate());
        answerValidUserCorporate.setAccessToken(token);
        return answerValidUserCorporate;
    }

    public boolean checkValidateUser(CorporateValidateUserResponseDto answerValidateUser) {
        boolean answer = false;
        if (answerValidateUser.getStatusDto().getStatusCode() != null && answerValidateUser.getStatusDto().getStatusCode().equals(new Long(0L)) && answerValidateUser.getStatusDto().getStatusDesc().equals("Success") && answerValidateUser.getStatusDto().getSeverity().equals("Info")) {
            answer = true;
        }
        return answer;
    }

    public boolean checkAuthenticationOTP(OTPResponseDto answerOtp) {
        boolean answer = false;
        if (answerOtp.getStatusCode() != null && answerOtp.getStatusCode().equals("0") && answerOtp.getServerStatusCode().equals("0") && answerOtp.getSeverity().equals("INFO") && answerOtp.getStatusDesc().equals("Operacion Exitosa")) {
            answer = true;
        }
        return answer;
    }

    public boolean checkCorporateRetrieveCompanyResponseDto(CorporateRetrieveCompanyResponseDto answerRetrieveCompany) {
        boolean answer = false;
        if (answerRetrieveCompany.getStatusDto() != null && answerRetrieveCompany.getStatusDto().getStatusCode() != null && answerRetrieveCompany.getStatusDto().getStatusCode().equals(new Long(0L)) && answerRetrieveCompany.getStatusDto().getSeverity().equals("Info") && answerRetrieveCompany.getStatusDto().getStatusDesc().equals("Success")) {
            answer = true;
        }
        return answer;
    }

    public String validateUserCorporateRequestDto(ValidateUserCorporateRequestDto authRequestDto) {
        String invalidMessage = "";
        if (authRequestDto.getLoginId().isEmpty() || authRequestDto.getLoginId().trim().isEmpty()) {
            BdbLoggerUtils.info("LOGIN REQUEST INVALID");
            invalidMessage = invalidMessage + "Login invalido. ";
        }

        if (authRequestDto.getPassword().isEmpty() || authRequestDto.getPassword().trim().isEmpty()) {
            BdbLoggerUtils.info("LOGIN REQUEST INVALID");
            invalidMessage = invalidMessage + "Password invalido. ";
        }

        if (authRequestDto.getIpAddress().isEmpty() || authRequestDto.getIpAddress().trim().isEmpty()) {
            BdbLoggerUtils.info("LOGIN REQUEST INVALID");
            invalidMessage = invalidMessage + "IPAddress invalido. ";
        }

        if (authRequestDto.getCustLoginId().isEmpty() || authRequestDto.getCustLoginId().trim().isEmpty() || authRequestDto.getCustLoginId().length() != 8) {
            BdbLoggerUtils.info("LOGIN REQUEST INVALID");
            invalidMessage = invalidMessage + "CustLoginId invalido. ";
        }

        if (authRequestDto.getOtp().isEmpty() || authRequestDto.getOtp().trim().isEmpty()) {
            BdbLoggerUtils.info("LOGIN REQUEST INVALID");
            invalidMessage = invalidMessage + "Otp invalido. ";
        }

        if (authRequestDto.getCustPermId().isEmpty() || authRequestDto.getCustPermId().trim().isEmpty() || authRequestDto.getCustPermId().length() < 4 || authRequestDto.getCustPermId().length() > 11) {
            BdbLoggerUtils.info("LOGIN REQUEST INVALID");
            invalidMessage = invalidMessage + "NIT invalido. ";
        }

        return invalidMessage;
    }

    public String actualDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String dateString = df.format(today);
        String[] dateArray = dateString.split(" ");
        return dateArray[0] + "T" + dateArray[1];
    }

    public boolean checkCreateCorporateUser(ValidateUserCorporateRequestDto authRequestDto, CorporateRetrieveCompanyResponseDto answerRetrieveCompany, OTPResponseDto answerValidateOtp){
        //CREATE USER IN LOCAL DATABASE
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername(authRequestDto.getLoginId().toUpperCase());
        userRequestDto.setType("C");

        userRequestDto.setDomain(authRequestDto.getCustLoginId());

        userRequestDto.setName(answerRetrieveCompany.getUserElement().getGivenName());
        userRequestDto.setEmail(answerRetrieveCompany.getUserElement().getEmail());
        userRequestDto.setLastLogin(new java.sql.Date((new Date()).getTime()));

        JSONObject aditionalInfo = new JSONObject();
        aditionalInfo.put("identityType", "N");
        aditionalInfo.put("identityNumber", authRequestDto.getCustPermId());
        userRequestDto.setAditionalInfo(aditionalInfo.toJSONString());
        ResponseEntity<JSONObject> responseInsertUser = crudUserService.insertUser(userRequestDto);
        BdbLoggerUtils.info(responseInsertUser.getBody().toJSONString());
        return responseInsertUser.getStatusCode().equals(HttpStatus.OK);
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
}
