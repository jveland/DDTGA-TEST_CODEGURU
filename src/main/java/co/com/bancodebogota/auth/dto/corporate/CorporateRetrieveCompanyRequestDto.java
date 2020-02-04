//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.dto.corporate;

import javax.validation.constraints.NotNull;

public class CorporateRetrieveCompanyRequestDto {
    @NotNull
    private String custLoginId;
    @NotNull
    private String userId;
    private String spNameType;
    public CorporateRetrieveCompanyRequestDto() {
    }

    public String getCustLoginId() {
        return this.custLoginId;
    }

    public void setCustLoginId(String custLoginId) {
        this.custLoginId = custLoginId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String toString() {
        return "CorporateRetrieveCompanyRequestDto [custLoginId=" + this.custLoginId + ", userId=" + this.userId + "]";
    }

	public String getSpNameType() {
		return spNameType;
	}

	public void setSpNameType(String spNameType) {
		this.spNameType = spNameType;
	}
}
