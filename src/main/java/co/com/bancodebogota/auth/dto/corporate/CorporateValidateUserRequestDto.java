//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.dto.corporate;

import javax.validation.constraints.NotNull;

public class CorporateValidateUserRequestDto {
    @NotNull
    private String custLoginId;
    @NotNull
    private String loginId;
    @NotNull
    private String password;
    @NotNull
    private String IPAddress;

    public CorporateValidateUserRequestDto() {
    }

    public String getCustLoginId() {
        return this.custLoginId;
    }

    public void setCustLoginId(String custLoginId) {
        this.custLoginId = custLoginId;
    }

    public String getLoginId() {
        return this.loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIPAddress() {
        return this.IPAddress;
    }

    public void setIPAddress(String iPAddress) {
        this.IPAddress = iPAddress;
    }

    public String toString() {
        return "CorporateValidateUserRequestDto [custLoginId=" + this.custLoginId + ", loginId=" + this.loginId + ", password=" + this.password + ", IPAddress=" + this.IPAddress + "]";
    }
}
