package co.com.bancodebogota.auth.dto.mng;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

@JsonIgnoreProperties(
    ignoreUnknown = true
)
@JsonInclude(Include.NON_NULL)
public class AuthValidateUserResponseDto {
    String statusCode;
    String statusDesc;
    String userEmail;
    String userName;
    String loginDT;
    List groups;
    private String accessToken;


    public AuthValidateUserResponseDto() {
    }

    public List getGroups() {
        return this.groups;
    }

    public void setGroups(List groups) {
        this.groups = groups;
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

    public String getLoginDT() {
        return this.loginDT;
    }

    public void setLoginDT(String loginDT) {
        this.loginDT = loginDT;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
