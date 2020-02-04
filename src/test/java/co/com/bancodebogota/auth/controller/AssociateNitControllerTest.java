package co.com.bancodebogota.auth.controller;

import co.com.bancodebogota.auth.dto.associateNitDTO.AssociateNitRequest;
import com.google.gson.JsonObject;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonbTester;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-dev.properties")
public class AssociateNitControllerTest {

  @Autowired
  AssociateNitController controller;

  @Test
  public void healthTest(){
    ResponseEntity<String> entity = controller.health();
    String JsonObject = entity.getBody();
    Assert.assertEquals("Customer Health ok!", JsonObject);
  }

  @Test
  public void findNitTest(){
    ResponseEntity<JSONObject> entity = controller.findNit("CB000000", "Usuario3");
    String JsonObject = entity.getBody().toString() ;
    System.out.println("JsonObject = " + JsonObject);
    Assert.assertNotNull(JsonObject);
  }

  @Test
  public void saveNit1Test(){
    AssociateNitRequest associateNitRequest = new AssociateNitRequest("N", "123456789", "CB000000", "");
    BindingResult result = mock(BindingResult.class);
    ResponseEntity<JSONObject> entity = controller.saveNit(associateNitRequest, result);
    String JsonObject = entity.getBody().toString() ;
    System.out.println("JsonObject = " + JsonObject);
    Assert.assertNotNull(JsonObject);
  }

  @Test
  public void saveNit2Test(){
    AssociateNitRequest associateNitRequest = new AssociateNitRequest("N", "123456789", "", "Usuario3");
    BindingResult result = mock(BindingResult.class);
    ResponseEntity<JSONObject> entity = controller.saveNit(associateNitRequest, result);
    String JsonObject = entity.getBody().toString() ;
    System.out.println("JsonObject = " + JsonObject);
    Assert.assertNotNull(JsonObject);
  }

  @Test
  public void saveNit3Test(){
    AssociateNitRequest associateNitRequest = new AssociateNitRequest("N", "", "CB000000", "Usuario3");
    BindingResult result = mock(BindingResult.class);
    ResponseEntity<JSONObject> entity = controller.saveNit(associateNitRequest, result);
    String JsonObject = entity.getBody().toString() ;
    System.out.println("JsonObject = " + JsonObject);
    Assert.assertNotNull(JsonObject);
  }

  @Test
  public void saveNit4Test(){
    AssociateNitRequest associateNitRequest = new AssociateNitRequest("", "123456789", "CB000000", "Usuario3");
    BindingResult result = mock(BindingResult.class);
    ResponseEntity<JSONObject> entity = controller.saveNit(associateNitRequest, result);
    String JsonObject = entity.getBody().toString() ;
    System.out.println("JsonObject = " + JsonObject);
    Assert.assertNotNull(JsonObject);
  }

  @Test
  public void saveNitTest(){
    AssociateNitRequest associateNitRequest = new AssociateNitRequest("N", "123456789", "CB000000", "Usuario3");
    BindingResult result = mock(BindingResult.class);
    ResponseEntity<JSONObject> entity = controller.saveNit(associateNitRequest, result);
    String JsonObject = entity.getBody().toString() ;
    System.out.println("JsonObject = " + JsonObject);
    Assert.assertNotNull(JsonObject);
  }

}
