package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto;

import org.apache.logging.log4j.util.Strings;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BasicInfoCustomer {

	@NotNull(message = "identityNumber cannot be null")
	private BigDecimal identityNumber;
	
	private String firstName;

	private String middleName;
	
	private String lastName;
	
	private String legalName;
	
	private BigDecimal phoneNumber;

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	private String secondLastName;

	private String cellPhone;

	public BigDecimal getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(BigDecimal identityNumber) {
		this.identityNumber = identityNumber;
	}

	public BigDecimal getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(BigDecimal phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSecondLastName() {
		return secondLastName;
	}

	public void setSecondLastName(String secondLastName) {
		this.secondLastName = secondLastName;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	
	
	@AssertTrue(message = "legalName or FirstName|lastName must be setted")
	public boolean isValid() {
		if(!Strings.isBlank(legalName) || (!Strings.isBlank(firstName) && !Strings.isBlank(lastName))) {
			if(identityNumber!=null) {
				return true;
			}
			
		}
		return false;
	}
}