package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.impl;

import static co.com.bancodebogota.adpe.util.Constant.MSN_OPERATION_BEGIN;
import static co.com.bancodebogota.adpe.util.Constant.MSN_OPERATION_DB_BAD;
import static co.com.bancodebogota.adpe.util.Constant.MSN_OPERATION_OK;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.com.bancodebogota.RestExchange;
import co.com.bancodebogota.adpe.logger.BdbLoggerUtils;
import co.com.bancodebogota.adpe.util.Messages;
import co.com.bancodebogota.adpe.util.Response;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.AssignToDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.CaseDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.SaveAdditionalInfoRequestDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.TaskDoneRequestDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.UserDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.ICaseService;

@Service
public class CaseServiceImpl implements ICaseService {

	private static final Log logger = LogFactory.getLog(CaseServiceImpl.class);

	@Value("${adapter.adpe.endpoint}")
	public String adpeEndpoint;

	Response response;

	private RestExchange restExchange;

	private static final String[] inputRole = { "Gerente CEO", "Ejecutivo CEO" };
	private static final String[] outputRole = { "ROL_APROBADOR", "ROL_RADICADOR" };

	public CaseServiceImpl() {
		restExchange = new RestExchange();
		response = new Response();
	}

	@Override
	public ResponseEntity<JSONObject> caseList() {
		return getJsonObjectResponseEntity();
	}

	@Override
	public ResponseEntity<JSONObject> casesById(String idCase) {
		try {
			BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
			BdbLoggerUtils.info("RESPONSE CASE " + idCase);
			ResponseEntity<JSONObject> exchange = restExchange.exchange(adpeEndpoint + "/" + idCase, null,
					HttpMethod.GET, JSONObject.class);
			return getJsonObjectResponseEntity(exchange);
		} catch (Exception e) {
			return buildObjectResponseEntity(e, 2);
		}
	}

	@Override
	public ResponseEntity<JSONObject> casesByEmployee(String customerUsername, Pageable pageable) {
		logger.info(MSN_OPERATION_BEGIN.toUpperCase());

		try {
			BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
			BdbLoggerUtils.info("RESPONSE CASE BY EMPLOYE " + customerUsername);
			ResponseEntity<JSONObject> exchange = restExchange.exchange(adpeEndpoint + "/employee/" + customerUsername,
					null, HttpMethod.GET, JSONObject.class);
			return getJsonObjectResponseEntity(exchange);
		} catch (Exception e) {
			return buildObjectResponseEntity(e, 2);
		}
	}

	@Override
	public ResponseEntity<JSONObject> casesByCustomer(String identityType, BigDecimal identityNumber,
			Pageable pageable) {
		logger.info(MSN_OPERATION_BEGIN.toUpperCase());
		try {
			ResponseEntity<JSONObject> responseService;
			BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
			BdbLoggerUtils.info("RESPONSE CASE BY CUSTOMER [" + identityType + "]-" + identityNumber);
			ResponseEntity<JSONObject> exchange = restExchange.exchange(
					adpeEndpoint + "/customer/" + identityType + "/" + identityNumber, null, HttpMethod.GET,
					JSONObject.class);
			return getJsonObjectResponseEntity(exchange);
		} catch (Exception e) {
			return buildObjectResponseEntity(e, 2);
		}
	}

	@Override
	public ResponseEntity<JSONObject> createCase(CaseDto caseDto) {
		BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
		try {
			BdbLoggerUtils.json(caseDto, "RESPONSE ADPE CASE");
			List<UserDto> userList = caseDto.getUser();
			if(userList != null && userList.size()>0) {
				caseDto.setUser(null);
				ResponseEntity<JSONObject> jsonObjectResponseEntity = restExchange.exchange(adpeEndpoint + "/create",
						caseDto, HttpMethod.POST, JSONObject.class);
				caseDto.setUser(userList);
				return createCaseValidator(jsonObjectResponseEntity, caseDto);
			}
			else {
				return new ResponseEntity<>(
						response.response(response.buildStatus(2, "Lista de usuarios no puede ser vacía."), null, false),
						HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			return response.buildObjectResponseEntity(e, 2, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@Override
	public ResponseEntity<JSONObject> taskDone(TaskDoneRequestDto taskToFinish) {
		try {
			BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
			if (taskToFinish.getIdCase() == null || taskToFinish.getIdCase().trim().isEmpty()
					|| taskToFinish.getIdCase().isEmpty() || taskToFinish.getIdCase() == "") {
				BdbLoggerUtils.info(MSN_OPERATION_DB_BAD.toUpperCase());
				Messages mensajeInterno = response.buildStatus(2, "idCase invalido.");
				JSONObject mensaje = response.response(mensajeInterno, null, false);
				ResponseEntity<JSONObject> answer = new ResponseEntity<>(mensaje, HttpStatus.BAD_REQUEST);
				return answer;
			}
			if (taskToFinish.getIdTask() == null || taskToFinish.getIdTask().trim().isEmpty()
					|| taskToFinish.getIdTask().isEmpty() || taskToFinish.getIdTask() == "") {
				BdbLoggerUtils.info(MSN_OPERATION_DB_BAD.toUpperCase());
				return new ResponseEntity<>(response.response(response.buildStatus(2, "idtask invalido."), null, false),
						HttpStatus.BAD_REQUEST);
			}
			if (taskToFinish.getUsername() == null || taskToFinish.getUsername().trim().isEmpty()
					|| taskToFinish.getUsername().isEmpty() || taskToFinish.getUsername() == "") {
				BdbLoggerUtils.info(MSN_OPERATION_DB_BAD.toUpperCase());
				return new ResponseEntity<>(
						response.response(response.buildStatus(2, "username invalido.  invalido."), null, false),
						HttpStatus.BAD_REQUEST);
			}
			ResponseEntity<JSONObject> exchange = restExchange.exchange(adpeEndpoint + "/taskDone", taskToFinish,
					HttpMethod.POST, JSONObject.class);
			BdbLoggerUtils.info(MSN_OPERATION_OK.toUpperCase());
			return getJsonObjectResponseEntity(exchange);
		} catch (Exception e) {
			BdbLoggerUtils.info(MSN_OPERATION_DB_BAD.toUpperCase());
			BdbLoggerUtils.info(e.getMessage());
			return response.buildObjectResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<JSONObject> saveAditionalInfo(SaveAdditionalInfoRequestDto saveAdditionalInfoRequestDto) {
		BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
		try {
			ResponseEntity<JSONObject> jsonObjectResponseEntity = restExchange.exchange(
					adpeEndpoint + "/saveAditionalInfo", saveAdditionalInfoRequestDto, HttpMethod.POST,
					JSONObject.class);
			BdbLoggerUtils.info(MSN_OPERATION_OK.toUpperCase());
			org.json.JSONObject jsonObjectHelper = new org.json.JSONObject(
					jsonObjectResponseEntity.getBody().toString());
			return saveAditionalInfoValidator(jsonObjectHelper);

		} catch (Exception e) {
			return buildObjectResponseEntity(e, 2);
		}
	}

	@Override
	public ResponseEntity<JSONObject> getInfo(String saveAdditionalInfoRequestDto) {
		logger.info(MSN_OPERATION_BEGIN.toUpperCase());
		try {
			BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
			BdbLoggerUtils.info("RESPONSE ADPE CASE");
			ResponseEntity<JSONObject> exchange = restExchange.exchange(
					adpeEndpoint + "/aditionalInfo/" + saveAdditionalInfoRequestDto, null, HttpMethod.GET,
					JSONObject.class);
			return getJsonObjectResponseEntity(exchange);
		} catch (Exception e) {
			return buildObjectResponseEntity(e, 2);
		}
	}

	@Override
	public ResponseEntity<JSONObject> getTaskStatus(String taskId) {
		try {
			BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
			BdbLoggerUtils.info("RESPONSE ADPE CASE");
			BdbLoggerUtils.info("TASK INFO BY ID CASE");
			ResponseEntity<JSONObject> exchange = restExchange.exchange(adpeEndpoint + "/task/" + taskId, null,
					HttpMethod.GET, JSONObject.class);
			return getJsonObjectResponseEntity(exchange);
		} catch (Exception e) {
			return buildObjectResponseEntity(e, 2);
		}
	}

	//VALIDATORS
	/**
	 * 
	 * @param jsonObjectHelper
	 * @return
	 */
	public ResponseEntity<JSONObject> saveAditionalInfoValidator(org.json.JSONObject jsonObjectHelper) {
		try {
			if (jsonObjectHelper.has("statusResponse")) {
				org.json.JSONObject jsonObjectHelperStatusResponse = (org.json.JSONObject) jsonObjectHelper
						.get("statusResponse");
				if (jsonObjectHelperStatusResponse.has("status")) {
					if ((int) jsonObjectHelperStatusResponse.get("status") == 0) {
						return new ResponseEntity<>(response.response(response.buildStatusSuccessfull(), null, false),
								HttpStatus.OK);
					}
				}
			}
			return new ResponseEntity<>(response.response(
					response.buildStatus(2, "Error al intentar guardar la información de balance de cliente"), null,
					false), HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (Exception e) {
			return buildObjectResponseEntity(e, 2);
		}
	}

	/**
	 * 
	 * @param jsonObjectResponseEntity
	 * @param caseDto
	 * @return
	 */
	public ResponseEntity<JSONObject> createCaseValidator(ResponseEntity<JSONObject> jsonObjectResponseEntity,
			CaseDto caseDto) {
		try {
			if (jsonObjectResponseEntity.getBody() != null) {
				org.json.JSONObject responseFormatter = new org.json.JSONObject(jsonObjectResponseEntity.getBody());
				if (responseFormatter.getInt("statusCode") == 0) {
					if (!caseDto.getUser().isEmpty()) {
						String idCase = responseFormatter.getString("idCase");
						ResponseEntity<JSONObject> assignCaseResponse = assignCase(caseDto, idCase);
						if (assignCaseResponse.getBody() != null) {
							if (assignCaseResponse.getStatusCode() == HttpStatus.OK) {
								JSONObject jsonObject = new JSONObject();
								jsonObject.put("idCase", idCase);
								return new ResponseEntity<>(
										response.response(response.buildStatusSuccessfull(), jsonObject, true),
										HttpStatus.OK);
							} else {
								return new ResponseEntity<>(response.response(
										response.buildStatus(2, "Error al intentar asignar el caso a los usuarios"),
										null, false), jsonObjectResponseEntity.getStatusCode());
							}
						} else {
							return new ResponseEntity<>(response.response(
									response.buildStatus(2, "Error al intentar asignar el caso a los usuarios"), null,
									false), jsonObjectResponseEntity.getStatusCode());
						}
					} else {
						return new ResponseEntity<>(response.response(
								response.buildStatus(2, "Error al intentar asignar el caso a los usuarios"), null,
								false), jsonObjectResponseEntity.getStatusCode());
					}
				} else {
					return new ResponseEntity<>(
							response.response(response.buildStatus(2, "Error al intentar crear el caso"), null, false),
							jsonObjectResponseEntity.getStatusCode());
				}

			} else {
				return getJsonObjectResponseEntity(jsonObjectResponseEntity);
			}

		} catch (Exception e) {
			return response.buildObjectResponseEntity(e, 2, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// UTILS
	public String transformRole(String role) {
		String answer = "noAvailable";
		for (int i = 0; i < inputRole.length; i++) {
			if (role.equals(inputRole[i])) {
				answer = outputRole[i];
			}
		}
		return answer;
	}
	
	public ResponseEntity<JSONObject> buildObjectResponseEntity(Exception e, int status) {
		logger.info("ERROR CONSUME ADPE ADAPTER");
		e.printStackTrace();
		return new ResponseEntity<>(response.response(
				response.buildStatus(status, e.getMessage().isEmpty() ? MSN_OPERATION_DB_BAD : e.getMessage()), null,
				false), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<JSONObject> assignCase(CaseDto caseDto, String idCase) {

		Boolean atLeastOneSuccess = false;
		try {
			BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
			BdbLoggerUtils.json(caseDto, "RESPONSE ADPE CASE");
			if (caseDto != null) {
				if (caseDto.getCompanyId() != null && !caseDto.getCompanyId().isEmpty()) {
					if (caseDto.getUsername() != null && !caseDto.getUsername().isEmpty()) {

						List<UserDto> inputAssign = caseDto.getUser();
						for (UserDto eachUser : inputAssign) {
							if (eachUser.getRol() != null && !eachUser.getRol().isEmpty()) {
								AssignToDto assignUser = new AssignToDto();
								assignUser.setDomain(caseDto.getCompanyId());
								assignUser.setUsername(eachUser.getUsername());

								String roleTransform = transformRole(eachUser.getRol());
								if (!roleTransform.equals("noAvailable")) {
									assignUser.setRol(roleTransform);
									assignUser.setIdCase(idCase);
									ResponseEntity<JSONObject> jsonObjectResponseEntity = restExchange.exchange(
											adpeEndpoint + "/assignTo", assignUser, HttpMethod.POST, JSONObject.class);
									if (jsonObjectResponseEntity.getBody() != null) {
										org.json.JSONObject responseFormatter = new org.json.JSONObject(
												jsonObjectResponseEntity.getBody());
										if (responseFormatter.has("statusResponse")) {
											if (responseFormatter.getJSONObject("statusResponse").has("status")) {
												if (responseFormatter.getJSONObject("statusResponse")
														.getInt("status") == 0) {
													atLeastOneSuccess = true;
												}
											}
										}
									}
								}
							}

						}
					}
				}
			}
			if (atLeastOneSuccess) {
				return new ResponseEntity<>(response.response(response.buildStatusSuccessfull(), null, false),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						response.response(response.buildStatus(2, "Error al intentar asignar el caso a los usuarios."),
								null, false),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			return response.buildObjectResponseEntity(e, 2, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	public ResponseEntity<JSONObject> getJsonObjectResponseEntity() {
		logger.info(MSN_OPERATION_BEGIN.toUpperCase());

		try {
			BdbLoggerUtils.info(MSN_OPERATION_BEGIN.toUpperCase());
			BdbLoggerUtils.info("RESPONSE ADPE CASE");
			ResponseEntity<JSONObject> exchange = restExchange.exchange(adpeEndpoint, null, HttpMethod.GET,
					JSONObject.class);
			return getJsonObjectResponseEntity(exchange);
		} catch (Exception e) {
			return buildObjectResponseEntity(e, 2);
		}
	}

	private ResponseEntity<JSONObject> getJsonObjectResponseEntity(ResponseEntity<JSONObject> exchange) {
		ResponseEntity<JSONObject> responseService;
		HttpStatus statusCode = exchange.getStatusCode();
		JSONObject body = exchange.getBody();
		if (statusCode.equals(HttpStatus.OK)) {
			responseService = new ResponseEntity<>(body, HttpStatus.OK);
		} else if (statusCode.equals(HttpStatus.CONFLICT)) {
			responseService = new ResponseEntity<>(body, HttpStatus.CONFLICT);
		} else {

			responseService = body != null ? new ResponseEntity<>(body, statusCode) : new ResponseEntity<>(statusCode);
		}
		BdbLoggerUtils.info("FINISH CONSUME ADPE ADAPTER");
		return responseService;
	}
	
}
