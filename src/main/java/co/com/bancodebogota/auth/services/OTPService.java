package co.com.bancodebogota.auth.services;

import co.com.bancodebogota.auth.dto.otp.OTPRequestDto;
import co.com.bancodebogota.auth.dto.otp.OTPResponseDto;
import org.springframework.http.ResponseEntity;

public interface OTPService{

	ResponseEntity<OTPResponseDto> validateToken(OTPRequestDto otpRequestDto);
	
	String getURL();
}
