package co.com.bancodebogota.inbox.bdbmanagerinbox.ui.controller;

import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.dto.CustomerInformationRequestDto;
import co.com.bancodebogota.inbox.bdbmanagerinbox.ui.service.IBlackListService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/validateList")
public class BlackLisController {
  @Autowired
  IBlackListService iBlackListService;

  @PostMapping
  public ResponseEntity<JSONObject> customerInformation(
      @Valid @RequestBody CustomerInformationRequestDto customerInformationRqDto, BindingResult bd) {
    ResponseEntity<JSONObject> jsonObjectResponseEntity = iBlackListService.beginCRM(customerInformationRqDto);
    return jsonObjectResponseEntity;
  }
}
