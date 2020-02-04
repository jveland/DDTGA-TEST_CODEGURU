//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package co.com.bancodebogota.auth.controller;

import co.com.bancodebogota.adpe.logger.BdbLoggerUtils;
import co.com.bancodebogota.adpe.util.Response;
import co.com.bancodebogota.auth.dto.associateNitDTO.AssociateNitRequest;
import co.com.bancodebogota.auth.services.IAssociateNitService;
import javax.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(
    origins = {"*"}
)
@RestController
@RequestMapping({"/customer"})
public class AssociateNitController {
  @Autowired
  IAssociateNitService service;
  private static final Log logger = LogFactory.getLog(AssociateNitController.class);

  public AssociateNitController() {
  }

  @GetMapping({"/idByUser/{cb}/{userName}"})
  public ResponseEntity<JSONObject> findNit(@PathVariable("cb") String cb, @PathVariable("userName") String userName) {
    logger.info("PARAMETOS DE CONSULTA " + cb + " " + userName + "\n");
    return this.service.findNitAssociate(cb, userName);
  }

  @PostMapping({"/addUserToId"})
  public ResponseEntity<JSONObject> saveNit(@Valid @RequestBody AssociateNitRequest associateNitRequest, BindingResult bd) {
    Response response = new Response();
    if (bd.hasErrors()) {
      return new ResponseEntity(response.response(response.buildStatus(HttpStatus.BAD_REQUEST.value(), bd.getFieldError().toString()), (Object)null, false), HttpStatus.BAD_REQUEST);
    } else {
      try {
        BdbLoggerUtils.info("Inicio validación variables AssociateNitRequest.", new String[0]);
        if (associateNitRequest.getIdentityType().trim().equals("")) {
          BdbLoggerUtils.info("Error al validar identityType.", new String[0]);
          return new ResponseEntity(response.response(response.buildStatus(HttpStatus.BAD_REQUEST.value(), "Error al validar identityType. No puede ser vacío. "), (Object)null, false), HttpStatus.BAD_REQUEST);
        } else if (associateNitRequest.getIdentityNumber().trim().equals("")) {
          BdbLoggerUtils.info("Error al validar IdentityNumber.", new String[0]);
          return new ResponseEntity(response.response(response.buildStatus(HttpStatus.BAD_REQUEST.value(), "Error al validar IdentityNumber. No puede ser vacío. "), (Object)null, false), HttpStatus.BAD_REQUEST);
        } else if (associateNitRequest.getCompany().trim().equals("")) {
          BdbLoggerUtils.info("Error al validar Company.", new String[0]);
          return new ResponseEntity(response.response(response.buildStatus(HttpStatus.BAD_REQUEST.value(), "Error al validar Company. No puede ser vacío. "), (Object)null, false), HttpStatus.BAD_REQUEST);
        } else if (associateNitRequest.getUsername().trim().equals("")) {
          BdbLoggerUtils.info("Error al validar Username.", new String[0]);
          return new ResponseEntity(response.response(response.buildStatus(HttpStatus.BAD_REQUEST.value(), "Error al validar Username. No puede ser vacío. "), (Object)null, false), HttpStatus.BAD_REQUEST);
        } else {
          BdbLoggerUtils.info("Inicio consumo adaptador.", new String[0]);
          return this.service.saveNitAssociate(associateNitRequest);
        }
      } catch (Exception var5) {
        var5.printStackTrace();
        BdbLoggerUtils.info("Error consumo adaptador para guardar CB con nit asociado.", new String[0]);
        return new ResponseEntity(response.response(response.buildStatus(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error al consumir el adaptador de CRM." + var5.getMessage()), (Object)null, false), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
  }

  @GetMapping({"/health"})
  public ResponseEntity<String> health() {
    return new ResponseEntity("Customer Health ok!", HttpStatus.OK);
  }
}
