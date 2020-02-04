//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.dto.idm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Info {
    @JsonProperty("groupName")
    List groupName;
    @JsonProperty("mail")
    String mail;
    @JsonProperty("fullName")
    String fullName;
    @JsonProperty("employeeId")
    String employeeid;
    @JsonProperty("userName")
    String userName;

    public Info() {
    }

    public List getGroupName() {
        return this.groupName;
    }

    public void setGroupName(List groupName) {
        this.groupName = groupName;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmployeeid() {
        return this.employeeid;
    }

    public void setEmployeeid(String employeeid) {
        this.employeeid = employeeid;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String toString() {
        return "Info [groupName=" + this.groupName + ", mail=" + this.mail + ", fullName=" + this.fullName + ", employeeid=" + this.employeeid + ", userName=" + this.userName + "]";
    }
}
