package co.com.bancodebogota.auth.services.impl;

import co.com.bancodebogota.adpe.logger.RestExchange;
import co.com.bancodebogota.adpe.logger.BdbLoggerUtils;
import co.com.bancodebogota.adpe.util.Response;
import co.com.bancodebogota.auth.dto.user.UserRequestDto;
import co.com.bancodebogota.auth.services.CrudUserService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CrudUserServiceImpl implements CrudUserService {

    @Value("${adapter.usuarios.endpoint}")
    public String crudUsuariosEndpoint;

    Response response;
    private RestExchange restExchange;

    public CrudUserServiceImpl() {
        restExchange = new RestExchange();
        response = new Response();
    }

    public ResponseEntity<JSONObject> insertUser(UserRequestDto userRequestDto) {
        ResponseEntity<JSONObject> eJsonObjectSimple;
        try {
            BdbLoggerUtils.info("BEGIN CONSUME USER ADAPTER RESPONSE IN "  + this.crudUsuariosEndpoint);
            eJsonObjectSimple = this.restExchange.exchange(crudUsuariosEndpoint, userRequestDto, HttpMethod.POST, JSONObject.class);
            BdbLoggerUtils.info("FINISH CONSUME USER ADAPTER RESPONSE");
            return eJsonObjectSimple;
        } catch (Exception e) {
        	e.printStackTrace();
            BdbLoggerUtils.info("ERROR CONSUME USER ADAPTER RESPONSE" + e.getMessage());
            return response.buildObjectResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
