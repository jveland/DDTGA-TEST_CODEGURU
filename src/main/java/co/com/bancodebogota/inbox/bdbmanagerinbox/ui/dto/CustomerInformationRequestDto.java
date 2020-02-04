package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto;

import javax.validation.constraints.NotNull;

public class CustomerInformationRequestDto {
	
	@NotNull
	private String identityType;
	
	@NotNull
	private String identityNumber;
	
	@NotNull
	private String customerUserName;

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public String getCustomerUserName() {
		return customerUserName;
	}

	public void setCustomerUserName(String customerUserName) {
		this.customerUserName = customerUserName;
	}

	@Override
	public String toString() {
		return "CustomerInformationRequestDto [identityType=" + identityType + ", identityNumber=" + identityNumber
				+ ", customerUserName=" + customerUserName + "]";
	}

}
