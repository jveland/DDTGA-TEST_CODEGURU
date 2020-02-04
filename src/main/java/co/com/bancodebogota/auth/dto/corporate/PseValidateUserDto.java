package co.com.bancodebogota.auth.dto.corporate;

public class PseValidateUserDto {
    private String externalAuthId;
    private String bnkUserKy;

    public PseValidateUserDto() {
    }

    public String getExternalAuthId() {
        return this.externalAuthId;
    }

    public void setExternalAuthId(String externalAuthId) {
        this.externalAuthId = externalAuthId;
    }

    public String getBnkUserKy() {
        return this.bnkUserKy;
    }

    public void setBnkUserKy(String bnkUserKy) {
        this.bnkUserKy = bnkUserKy;
    }

    public String toString() {
        return "PseValidateUserDto [externalAuthId=" + this.externalAuthId + ", bnkUserKy=" + this.bnkUserKy + "]";
    }
}
