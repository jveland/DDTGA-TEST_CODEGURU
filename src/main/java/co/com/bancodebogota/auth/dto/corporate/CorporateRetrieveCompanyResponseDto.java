//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.dto.corporate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(
    ignoreUnknown = true
)
public class CorporateRetrieveCompanyResponseDto {
    @JsonProperty("statusDto")
    private StatusDto statusDto;
    @JsonProperty("companyElement")
    private CompanyElementDto companyElement;
    @JsonProperty("userElement")
    private UserElementDto userElement;

    public CorporateRetrieveCompanyResponseDto() {
    }

    public StatusDto getStatusDto() {
        return this.statusDto;
    }

    public CompanyElementDto getCompanyElement() {
        return this.companyElement;
    }

    public void setCompanyElement(CompanyElementDto companyElement) {
        this.companyElement = companyElement;
    }

    public void setStatusDto(StatusDto statusDto) {
        this.statusDto = statusDto;
    }

    public UserElementDto getUserElement() {
        return this.userElement;
    }

    public void setUserElement(UserElementDto userElement) {
        this.userElement = userElement;
    }

    public String toString() {
        return "CorporateRetrieveCompanyResponseDto [statusDto=" + this.statusDto + ", companyElement=" + this.companyElement + ", userElement=" + this.userElement + "]";
    }
}
