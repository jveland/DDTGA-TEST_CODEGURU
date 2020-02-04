//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.dto.mng;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(
    ignoreUnknown = true
)
@JsonInclude(Include.NON_NULL)
public class ValidateUserCorporateResponseDto {
    private String statusCode;
    private String statusDesc;
    private String userEmail;
    private String userName;
    private String userLastName;
    private String secureCode;
    private String loginDT;
    private String accessToken;

    public ValidateUserCorporateResponseDto() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDesc() {
        return this.statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSecureCode() {
        return this.secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }

    public String getLoginDT() {
        return this.loginDT;
    }

    public void setLoginDT(String loginDT) {
        this.loginDT = loginDT;
    }

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
    
}
