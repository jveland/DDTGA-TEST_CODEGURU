package co.com.bancodebogota.auth.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;

public class UserRequestDto {

	@NotNull(message = "Username cannot be null")
	@Size(max = 20, message = "Type should not be greater than 20")
	private String username;

	@NotNull(message = "Name cannot be null")
	@Size(max = 1, message = "Type should not be greater than 1")
	private String type;

	@NotNull(message = "Domain cannot be null")
	private String domain;

	@NotNull(message = "Name cannot be null")
	@Size(max = 45, message = "Type should not be greater than 45")
	private String name;

	@NotNull(message = "Email cannot be null")
	@Size(max = 75, message = "Type should not be greater than 75")
	@Email(message = "Email should be valid")
	private String email;

	@NotNull(message = "LastLogin cannot be null")
	private Date lastLogin;

	private String aditionalInfo;


	public UserRequestDto() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getAditionalInfo() {
		return aditionalInfo;
	}

	public void setAditionalInfo(String aditionalInfo) {
		this.aditionalInfo = aditionalInfo;
	}

}
