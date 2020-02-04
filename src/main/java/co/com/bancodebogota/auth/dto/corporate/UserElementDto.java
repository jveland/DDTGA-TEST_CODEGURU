//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.dto.corporate;

public class UserElementDto {
    private Long isActive;
    private String email;
    private String givenName;
    private String familyName;
    private String userAccountAndFunctionalAccess;
    private String userApprovalFunction;
    private String bnkuserKy;

    public UserElementDto() {
    }

    public Long getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGivenName() {
        return this.givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getUserAccountAndFunctionalAccess() {
        return this.userAccountAndFunctionalAccess;
    }

    public void setUserAccountAndFunctionalAccess(String userAccountAndFunctionalAccess) {
        this.userAccountAndFunctionalAccess = userAccountAndFunctionalAccess;
    }

    public String getUserApprovalFunction() {
        return this.userApprovalFunction;
    }

    public void setUserApprovalFunction(String userApprovalFunction) {
        this.userApprovalFunction = userApprovalFunction;
    }

    public String getBnkuserKy() {
        return this.bnkuserKy;
    }

    public void setBnkuserKy(String bnkuserKy) {
        this.bnkuserKy = bnkuserKy;
    }

    public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	@Override
	public String toString() {
		return "UserElementDto [isActive=" + isActive + ", email=" + email + ", givenName=" + givenName
				+ ", familyName=" + familyName + ", userAccountAndFunctionalAccess=" + userAccountAndFunctionalAccess
				+ ", userApprovalFunction=" + userApprovalFunction + ", bnkuserKy=" + bnkuserKy + "]";
	}

	
}
