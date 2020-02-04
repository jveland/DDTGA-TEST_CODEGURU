//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.dto.corporate;

public class CompanyElementDto {
    private Long isEnrolled;
    private String name;
    private Long allowAccess;

    public CompanyElementDto() {
    }

    public Long getIsEnrolled() {
        return this.isEnrolled;
    }

    public void setIsEnrolled(Long isEnrolled) {
        this.isEnrolled = isEnrolled;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAllowAccess() {
        return this.allowAccess;
    }

    public void setAllowAccess(Long allowAccess) {
        this.allowAccess = allowAccess;
    }

    public String toString() {
        return "CompanyElementDto [isEnrolled=" + this.isEnrolled + ", name=" + this.name + ", allowAccess=" + this.allowAccess + "]";
    }
}
