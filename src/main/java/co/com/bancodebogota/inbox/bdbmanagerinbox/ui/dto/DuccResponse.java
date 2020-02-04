package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto;

import java.util.List;

public class DuccResponse {
	private String statusCode;
	private String statusDescription;
	private List<ValidCustomerBlackListRp> customers;
	public List<ValidCustomerBlackListRp> getCustomers() {
		return customers;
	}
	public void setCustomers(List<ValidCustomerBlackListRp> customers) {
		this.customers = customers;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
}
