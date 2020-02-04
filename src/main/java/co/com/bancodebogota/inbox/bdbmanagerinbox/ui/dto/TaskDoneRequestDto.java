package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto;

import org.apache.logging.log4j.util.Strings;
import org.json.simple.JSONObject;

public class TaskDoneRequestDto {


	private String idCase;
	private String idTask;
	private String username;
	

	public String getIdCase() {
		return idCase;
	}

	public void setIdCase(String idCase) {
		this.idCase = idCase;
	}

	public String getIdTask() {
		return idTask;
	}

	public void setIdTask(String idTask) {
		this.idTask = idTask;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String toJSONString() {
		JSONObject object=new JSONObject();
		if(Strings.isNotBlank(idCase))
			object.put("idCase",idCase);
		if(Strings.isNotBlank(idTask))
			object.put("idTask",idTask);
		if(Strings.isNotBlank(username))
			object.put("username",username);
	    return object.toString();
	}

}
