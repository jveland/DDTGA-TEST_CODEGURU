package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto;

import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.util.Strings;
import org.json.simple.JSONObject;

public class SaveAdditionalInfoRequestDto {

	@NotNull
	String idCase;
	@NotNull
	String income;
	@NotNull
	String expenses;
	@NotNull
	String assets;
	@NotNull
	String liabilities;
	@NotNull
	String issueDate;

	public SaveAdditionalInfoRequestDto(@NotNull String idCase, @NotNull String income, @NotNull String expenses,
			@NotNull String assets, @NotNull String liabilities, @NotNull String issueDate) {
		super();
		this.idCase = idCase;
		this.income = income;
		this.expenses = expenses;
		this.assets = assets;
		this.liabilities = liabilities;
		this.issueDate = issueDate;
	}

	public String getIdCase() {
		return idCase;
	}

	public void setIdCase(String idCase) {
		this.idCase = idCase;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getExpenses() {
		return expenses;
	}

	public void setExpenses(String expenses) {
		this.expenses = expenses;
	}

	public String getAssets() {
		return assets;
	}

	public void setAssets(String assets) {
		this.assets = assets;
	}

	public String getLiabilities() {
		return liabilities;
	}

	public void setLiabilities(String liabilities) {
		this.liabilities = liabilities;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

}
