package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto;

import javax.validation.constraints.NotNull;

public class JuridicalCustomerInformationRqDto {

	@NotNull
	private String typeID;
	
	@NotNull
	private String identificationNumber;

	public String getTypeID() {
		return typeID;
	}

	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

}
