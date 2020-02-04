//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.services.impl;

import co.com.bancodebogota.adpe.logger.BdbLoggerUtils;
import co.com.bancodebogota.adpe.logger.RestExchange;
import co.com.bancodebogota.auth.dto.idm.IDMRequestDto;
import co.com.bancodebogota.auth.dto.idm.IDMResponseDto;
import co.com.bancodebogota.auth.handler.RestTemplateResponseErrorHandler;
import co.com.bancodebogota.auth.services.IDMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class IDMServiceImpl implements IDMService {
	@Value("${adapter.idm.endpoint}")
	public String idmEndpoint;

	@Value("${adapter.usuarios.endpoint}")
	public String crudUsuariosEndpoint;

	RestExchange restTemplate=new RestExchange();

	public ResponseEntity<IDMResponseDto> validateAuthentication(IDMRequestDto idmRequestDto) {
		ResponseEntity<IDMResponseDto> outputIDM;
		try {
			BdbLoggerUtils.info("BEGIN CONSUME IDM ADAPTER IN " + this.idmEndpoint);
			outputIDM =  this.restTemplate.exchange(this.idmEndpoint, idmRequestDto,HttpMethod.POST,
					IDMResponseDto.class);
			BdbLoggerUtils.info("FINISH CONSUME IDM ADAPTER");
			return outputIDM;
		} catch (Exception var4) {
			var4.printStackTrace();
			IDMResponseDto error = new IDMResponseDto();
			error.setStatus(500);
			error.setStatusDesc("Error interno en el servidor. " + var4.getMessage());
			BdbLoggerUtils.info("ERROR IN CONSUME IDM ADAPTER IN " + this.idmEndpoint);
			BdbLoggerUtils.info(var4.getMessage());
			return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public String getURL() {
		return this.idmEndpoint;
	}
}
