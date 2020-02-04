//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.services.impl;

import co.com.bancodebogota.adpe.logger.BdbLoggerUtils;
import co.com.bancodebogota.adpe.logger.RestExchange;
import co.com.bancodebogota.auth.dto.corporate.*;
import co.com.bancodebogota.auth.handler.RestTemplateResponseErrorHandler;
import co.com.bancodebogota.auth.services.CorporateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CorporateServiceImpl implements CorporateService {
    @Value("${adapter.corporate.endpoint}")
    public String corporateEndpoint;
    private static final String RETRIEVE_COMPANY = "/retrieveCompany";
    private static final String VALIDATE_USER = "/validateUser";
    RestExchange restTemplate=new RestExchange();
   

    public ResponseEntity<CorporateValidateUserResponseDto> validateUser(CorporateValidateUserRequestDto corporateRequestDto) {
    	ResponseEntity<CorporateValidateUserResponseDto> outputCorporate;
        try {
            BdbLoggerUtils.info("BEGIN CONSUME CORPORATE ADAPTER " + this.corporateEndpoint + VALIDATE_USER);
            outputCorporate = this.restTemplate.exchange(this.corporateEndpoint + VALIDATE_USER, corporateRequestDto,HttpMethod.POST,CorporateValidateUserResponseDto.class);
            BdbLoggerUtils.info("FINISH CONSUME CORPORATE ADAPTER");
            return outputCorporate;
        } catch (Exception var5) {
        	var5.printStackTrace();
        	CorporateValidateUserResponseDto  errorCorporate = new CorporateValidateUserResponseDto();
            StatusDto statusDto = new StatusDto();
            statusDto.setStatusCode(new Long(2L));
            statusDto.setStatusDesc("Error interno en el servidor");
            errorCorporate.setStatusDto(statusDto);
            BdbLoggerUtils.error("ERROR CONSUME CORPORATE ADAPTER IN" + this.corporateEndpoint + "/validateUser");
            BdbLoggerUtils.error(var5.getMessage());
            return new ResponseEntity(errorCorporate, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CorporateRetrieveCompanyResponseDto> retrieveCompany(CorporateRetrieveCompanyRequestDto corporateRetrieveCompanyRequestDto) {
    	ResponseEntity<CorporateRetrieveCompanyResponseDto> outputCorporate;
        try {
        	BdbLoggerUtils.info("BEGIN CONSUME CORPORATE ADAPTER" + this.corporateEndpoint + RETRIEVE_COMPANY);
            outputCorporate = this.restTemplate.exchange(this.corporateEndpoint + RETRIEVE_COMPANY, corporateRetrieveCompanyRequestDto, HttpMethod.POST,CorporateRetrieveCompanyResponseDto.class);
            BdbLoggerUtils.info("FINISH CONSUME CORPORATE ADAPTER");
            return outputCorporate;
        } catch (Exception var5) {
        	CorporateRetrieveCompanyResponseDto error = new CorporateRetrieveCompanyResponseDto();
            StatusDto statusDto = new StatusDto();
            statusDto.setStatusCode(new Long(2L));
            statusDto.setStatusDesc("Error interno en el servidor");
            error.setStatusDto(statusDto);
            BdbLoggerUtils.error("ERROR CONSUME CORPORATE ADAPTER IN" + this.corporateEndpoint + "/retrieveCompany");
            BdbLoggerUtils.error(var5.getMessage());
            return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String getURL() {
        return this.corporateEndpoint;
    }
}
