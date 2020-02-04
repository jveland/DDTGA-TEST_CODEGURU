package co.com.bancodebogota.auth.dto.mng;

public class AuthValidateUserRequestDto {
	String loginID;
	String password;

	public String getLoginID() {
		return loginID;
	}

	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
