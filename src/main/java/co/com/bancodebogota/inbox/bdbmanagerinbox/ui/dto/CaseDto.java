package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

public class CaseDto {

	private String type;
	private String identityType;
	private String identityNumber;
	private String idCustomerType;
	private String companyName;
	private String username;
	private String companyUser;
	private String userEmail;
	private String rol;
	private String companyId;
	private List<UserDto> user;
	private Integer stepCustomer;
	private Integer stepEmployee;
	private String deliveryMethod;
	private String tributary;
	private String office;

	public CaseDto() {
	}

	public CaseDto(List<UserDto> user) {
		this.user = user;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public String getIdCustomerType() {
		return idCustomerType;
	}

	public void setIdCustomerType(String idCustomerType) {
		this.idCustomerType = idCustomerType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCompanyUser() {
		return companyUser;
	}

	public void setCompanyUser(String companyUser) {
		this.companyUser = companyUser;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public List<UserDto> getUser() {
		return user;
	}

	public void setUser(List<UserDto> user) {
		this.user = user;
	}

	public Integer getStepCustomer() {
		return stepCustomer;
	}

	public void setStepCustomer(Integer stepCustomer) {
		this.stepCustomer = stepCustomer;
	}

	public Integer getStepEmployee() {
		return stepEmployee;
	}

	public void setStepEmployee(Integer stepEmployee) {
		this.stepEmployee = stepEmployee;
	}

	public String getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public String getTributary() {
		return tributary;
	}

	public void setTributary(String tributary) {
		this.tributary = tributary;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}
	
	

}
