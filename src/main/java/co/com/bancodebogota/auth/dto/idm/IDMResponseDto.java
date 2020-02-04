//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.dto.idm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonInclude(Include.NON_NULL)
public class IDMResponseDto {
    @JsonProperty("statusDesc")
    @NotNull
    String statusDesc;
    @JsonProperty("timestamp")
    Date timestamp;
    @NotNull
    @JsonProperty("status")
    Integer status;
    @JsonProperty("info")
    Info info;

    public IDMResponseDto() {
    }

    public String getStatusDesc() {
        return this.statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Info getInfo() {
        return this.info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String toString() {
        return "IDMResponseDto [statusDesc=" + this.statusDesc + ", timestamp=" + this.timestamp + ", status=" + this.status + ", info=" + this.info + "]";
    }
}
