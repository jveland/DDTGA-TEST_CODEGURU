package co.com.bancodebogota.auth.services;

import co.com.bancodebogota.auth.dto.corporate.CorporateRetrieveCompanyRequestDto;
import co.com.bancodebogota.auth.dto.corporate.CorporateRetrieveCompanyResponseDto;
import co.com.bancodebogota.auth.dto.corporate.CorporateValidateUserRequestDto;
import co.com.bancodebogota.auth.dto.corporate.CorporateValidateUserResponseDto;
import org.springframework.http.ResponseEntity;

public interface CorporateService {

	ResponseEntity<CorporateValidateUserResponseDto> validateUser(CorporateValidateUserRequestDto corporateRequestDto);
	
	ResponseEntity<CorporateRetrieveCompanyResponseDto> retrieveCompany(CorporateRetrieveCompanyRequestDto corporateRequestDto);
	
	String getURL();
}
