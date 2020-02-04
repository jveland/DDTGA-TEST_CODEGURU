//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.dto.otp;

import javax.validation.constraints.NotNull;

public class OTPRequestDto {
    @NotNull
    private String custIdCustPermId;
    @NotNull
    private String userIdCustPermId;
    @NotNull
    private String otp;

    public OTPRequestDto() {
    }

    public String getCustIdCustPermId() {
        return this.custIdCustPermId;
    }

    public void setCustIdCustPermId(String custIdCustPermId) {
        this.custIdCustPermId = custIdCustPermId;
    }

    public String getUserIdCustPermId() {
        return this.userIdCustPermId;
    }

    public void setUserIdCustPermId(String userIdCustPermId) {
        this.userIdCustPermId = userIdCustPermId;
    }

    public String getOtp() {
        return this.otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String toString() {
        return "OTPRequestDto [custIdCustPermId=" + this.custIdCustPermId + ", userIdCustPermId=" + this.userIdCustPermId + ", otp=" + this.otp + "]";
    }
}
