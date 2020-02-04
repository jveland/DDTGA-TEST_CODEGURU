//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.dto.corporate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class StatusDto {
    private Long statusCode;
    private String severity;
    private String statusDesc;

    public StatusDto() {
    }

    public Long getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(Long statusCode) {
        this.statusCode = statusCode;
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

    public String toString() {
        return "StatusDto [statusCode=" + this.statusCode + ", severity=" + this.severity + ", statusDesc=" + this.statusDesc + "]";
    }
}
