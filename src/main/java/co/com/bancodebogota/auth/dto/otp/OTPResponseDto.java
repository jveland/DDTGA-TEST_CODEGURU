//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.dto.otp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OTPResponseDto {
    private String statusCode;
    private String serverStatusCode;
    private String severity;
    private String statusDesc;
    private String authCode;

    public OTPResponseDto() {
    }

    public String getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getServerStatusCode() {
        return this.serverStatusCode;
    }

    public void setServerStatusCode(String serverStatusCode) {
        this.serverStatusCode = serverStatusCode;
    }

    public String getSeverity() {
        return this.severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatusDesc() {
        return this.statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getAuthCode() {
        return this.authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String toString() {
        return "OTPResponseDto [statusCode=" + this.statusCode + ", serverStatusCode=" + this.serverStatusCode + ", severity=" + this.severity + ", statusDesc=" + this.statusDesc + ", authCode=" + this.authCode + "]";
    }
}
