//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.dto.mng;

public class ValidateUserCorporateRequestDto {
    private String loginId;
    private String password;
    private String ipAddress;
    private String custLoginId;
    private String otp;
    private String custPermId;

    public ValidateUserCorporateRequestDto() {
    }

    public String getCustPermId() {
        return this.custPermId;
    }

    public void setCustPermId(String custPermId) {
        this.custPermId = custPermId;
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

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCustLoginId() {
        return this.custLoginId;
    }

    public void setCustLoginId(String custLoginId) {
        this.custLoginId = custLoginId;
    }

    public String getOtp() {
        return this.otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String toString() {
        return "ValidateUserCorporateRequestDto [loginId=" + this.loginId + ", password=" + this.password + ", ipAddress=" + this.ipAddress + ", custLoginId=" + this.custLoginId + ", otp=" + this.otp + ", custPermId=" + this.custPermId + "]";
    }
}
