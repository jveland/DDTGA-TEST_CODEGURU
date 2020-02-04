package co.com.bancodebogota.auth.dto.corporate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class CorporateValidateUserResponseDto {
    @JsonProperty("statusResponse")
    private StatusDto statusDto;
    @JsonProperty("pseValidateUser")
    private PseValidateUserDto pseValidateUserDto;

    public CorporateValidateUserResponseDto() {
    }

    public StatusDto getStatusDto() {
        return this.statusDto;
    }

    public void setStatusDto(StatusDto statusDto) {
        this.statusDto = statusDto;
    }

    public PseValidateUserDto getPseValidateUserDto() {
        return this.pseValidateUserDto;
    }

    public void setPseValidateUserDto(PseValidateUserDto pseValidateUserDto) {
        this.pseValidateUserDto = pseValidateUserDto;
    }

    public String toString() {
        return "CorporateValidateUserResponseDto [statusDto=" + this.statusDto + ", pseValidateUserDto=" + this.pseValidateUserDto + "]";
    }
}
