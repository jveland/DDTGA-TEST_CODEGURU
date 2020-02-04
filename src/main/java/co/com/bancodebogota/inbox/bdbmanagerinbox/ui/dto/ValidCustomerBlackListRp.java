package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto;

public class ValidCustomerBlackListRp {
	

	private String refCodeBlackList;
	private String refDescBlackList;
	private Boolean isPossibleContinue;
	private String identityNumber;
	public String getIdentityNumber() {
		return identityNumber;
	}
	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public String getRefCodeBlackList() {
		return refCodeBlackList;
	}
	public void setRefCodeBlackList(String refCodeBlackList) {
		this.refCodeBlackList = refCodeBlackList;
	}
	public String getRefDescBlackList() {
		return refDescBlackList;
	}
	public void setRefDescBlackList(String refDescBlackList) {
		this.refDescBlackList = refDescBlackList;
	}
	public Boolean getIsPossibleContinue() {
		return isPossibleContinue;
	}
	public void setIsPossibleContinue(Boolean isPossibleContinue) {
		this.isPossibleContinue = isPossibleContinue;
	}
	
	
}
