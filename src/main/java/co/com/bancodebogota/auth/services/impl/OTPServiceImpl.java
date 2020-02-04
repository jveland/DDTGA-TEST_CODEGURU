//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.services.impl;

import co.com.bancodebogota.adpe.logger.BdbLoggerUtils;
import co.com.bancodebogota.adpe.logger.RestExchange;
import co.com.bancodebogota.auth.dto.otp.OTPRequestDto;
import co.com.bancodebogota.auth.dto.otp.OTPResponseDto;
import co.com.bancodebogota.auth.handler.RestTemplateResponseErrorHandler;
import co.com.bancodebogota.auth.services.OTPService;
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
public class OTPServiceImpl implements OTPService {
    @Value("${adapter.otp.endpoint}")
    public String otpEndpoint;
    RestExchange restTemplate=new RestExchange();

    public ResponseEntity<OTPResponseDto> validateToken(OTPRequestDto otpRequestDto) {
    	ResponseEntity<OTPResponseDto> outputOTP;
        try {
        	BdbLoggerUtils.info("BEGIN CONSUME OTP ADAPTER IN " + this.otpEndpoint);
            outputOTP = this.restTemplate.exchange(this.otpEndpoint, otpRequestDto, HttpMethod.POST,OTPResponseDto.class);
            BdbLoggerUtils.info("FINISH CONSUME OTP ADAPTER");
            return outputOTP;
        } catch (Exception var4) {
        	var4.printStackTrace();
        	OTPResponseDto error = new OTPResponseDto();
        	error.setStatusCode("500");
        	error.setStatusDesc("Error interno en el servidor. " + var4.getMessage());
            BdbLoggerUtils.error("ERROR IN CONSUME OTP ADAPTER IN " + this.otpEndpoint);
            BdbLoggerUtils.error(var4.getMessage());
            return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String getURL() {
        return this.otpEndpoint;
    }
}
