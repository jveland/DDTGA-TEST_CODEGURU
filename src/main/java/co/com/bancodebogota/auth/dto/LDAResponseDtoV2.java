package co.com.bancodebogota.auth.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class LDAResponseDtoV2 {
  private List groupName;
  private String mail;
  private String fullName;
  private Long employeeId;
  private String userName;
  private String actions;
  private String accessToken;
  private String tokenId;



  public List getGroupName() {
    return groupName;
  }

  public void setGroupName(List groupName) {
    this.groupName = groupName;
  }

  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public Long getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Long employeeId) {
    this.employeeId = employeeId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getActions() {
    return actions;
  }

  public void setActions(String actions) {
    this.actions = actions;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getTokenId() {
    return tokenId;
  }

  public void setTokenId(String tokenId) {
    this.tokenId = tokenId;
  }
}