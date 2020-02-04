package co.com.bancodebogota.auth.services;

import co.com.bancodebogota.auth.dto.idm.IDMRequestDto;
import co.com.bancodebogota.auth.dto.idm.IDMResponseDto;
import org.springframework.http.ResponseEntity;

public interface IDMService{

	ResponseEntity<IDMResponseDto> validateAuthentication(IDMRequestDto idmRequestDto);
	
	String getURL();
}
