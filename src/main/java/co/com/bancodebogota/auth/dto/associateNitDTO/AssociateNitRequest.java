//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.dto.associateNitDTO;

import javax.validation.constraints.NotNull;

public class AssociateNitRequest {
  @NotNull
  private String identityType;
  @NotNull
  private String identityNumber;
  @NotNull
  private String company;
  @NotNull
  private String username;

  public AssociateNitRequest() {
  }

  public AssociateNitRequest(String identityType, String identityNumber, String company, String username) {
    this.identityType = identityType;
    this.identityNumber = identityNumber;
    this.company = company;
    this.username = username;
  }

  public String getIdentityType() {
    return this.identityType;
  }

  public void setIdentityType(String identityType) {
    this.identityType = identityType;
  }

  public String getIdentityNumber() {
    return this.identityNumber;
  }

  public void setIdentityNumber(String identityNumber) {
    this.identityNumber = identityNumber;
  }

  public String getCompany() {
    return this.company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String toString() {
    return "AssociateNitRequest{identityType='" + this.identityType + '\'' + ", identityNumber='" + this.identityNumber + '\'' + ", company='" + this.company + '\'' + ", username='" + this.username + '\'' + '}';
  }
}
