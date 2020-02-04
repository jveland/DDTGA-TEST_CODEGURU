package co.com.bancodebogota.auth.services;

import co.com.bancodebogota.auth.dto.user.UserRequestDto;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

public interface CrudUserService {
    public ResponseEntity<JSONObject> insertUser(UserRequestDto userRequestDto);
}
