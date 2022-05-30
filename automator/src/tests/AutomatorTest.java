package tests;

import com.testsigma.automator.AppBridge;
import com.testsigma.automator.AutomatorConfig;
import com.testsigma.automator.entity.EnvironmentRunResult;
import com.testsigma.automator.entity.TestDeviceEntity;
//import com.testsigma.agent.tasks.TestPlanRunTask; 
import com.testsigma.automator.entity.WebDriverSettingsDTO;

import java.io.FileWriter;

import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.testsigma.automator.exceptions.AgentDeletedException;
import com.testsigma.automator.http.HttpResponse;
import lombok.extern.log4j.Log4j2;
import tests.http.WebAppHttpClient;

import org.apache.http.Header;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;


@Component
@Log4j2
public class AutomatorTest {
  protected final WebApplicationContext webApplicationContext = null;
  public static void main(String[] args) {
    try {
      Thread.currentThread().setName("AutomatorTest");
      AutomatorTest test = new AutomatorTest();
      TestDeviceEntity readObject = (TestDeviceEntity)readObjectAb("260", new TestDeviceEntity());
      //Selenide s = new Selenide();
      //Selenide.open("http://www.google.com");
      test.startExecutions(readObject);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void startExecutions(TestDeviceEntity testDeviceEntity) {
    if (testDeviceEntity != null) {
      AutomatorConfig ac = AutomatorConfig.getInstance();
      AppBridge appBridge = new CloudAppBridge(new WebAppHttpClient(), new AgentConfig());
      ac.setAppBridge(appBridge);
      log.info("The EnvironmentEntity - " + testDeviceEntity.getId() + " executions returned from servers....");
      TestPlanRunTask task = new TestPlanRunTask(testDeviceEntity);
      task.setName("ExecutionTask - Environment Result ID [" + testDeviceEntity.getEnvironmentResultId() + "]");
      task.setWebApplicationContext(webApplicationContext);
      task.start();
    } else {
      log.info("There are no executions in queue....");
    }
  }

  public static void save(TestDeviceEntity testDeviceEntity) {
    try {
      Gson gson = new Gson();
      String fileName = "/Users/tiago/DEV/DELTA/nailab/gson/" + testDeviceEntity.getId() + ".json";
      FileWriter writer = new FileWriter(fileName);
      gson.toJson(testDeviceEntity, writer);
      writer.flush(); // flush data to file <---
      writer.close(); // close write <---
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
  public static void save(Object oo) {
    try {
      Gson gson = new Gson();
      String fileName = "/Users/tiago/DEV/DELTA/nailab/gson/" + oo.getClass()+"_.json";
      FileWriter writer = new FileWriter(fileName);
      gson.toJson(oo, writer);
      writer.flush(); // flush data to file <---
      writer.close(); // close write <---
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public static void save(EnvironmentRunResult environmentRunResult) {
    try {
      Gson gson = new Gson();
      String fileName = "/Users/tiago/DEV/DELTA/nailab/gson/" + environmentRunResult.getId() + "_result.json";
      FileWriter writer = new FileWriter(fileName);
      gson.toJson(environmentRunResult, writer);
      writer.flush(); // flush data to file <---
      writer.close(); // close write <---
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public static TestDeviceEntity readObject(String id) {
    try {
      TestDeviceEntity testDeviceEntity;
      String fileName = "/Users/tiago/DEV/DELTA/nailab/gson/" + id + ".json";
      Gson gson = new Gson();
      com.google.gson.stream.JsonReader reader = new com.google.gson.stream.JsonReader(
          new java.io.FileReader(fileName));
      testDeviceEntity = gson.fromJson(reader, TestDeviceEntity.class);
      return testDeviceEntity;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
  public static WebDriverSettingsDTO readWebDriver(String id) {
    try {
      WebDriverSettingsDTO object;
      String fileName = "/Users/tiago/DEV/DELTA/nailab/gson/" + id + ".json";
      Gson gson = new Gson();
      com.google.gson.stream.JsonReader reader = new com.google.gson.stream.JsonReader(
          new java.io.FileReader(fileName));
          object = gson.fromJson(reader, WebDriverSettingsDTO.class);
      return object;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
  public static Object readObjectAb(String id, Object classObject) {
    try {
      Object object;
      String fileName = "/Users/tiago/DEV/DELTA/nailab/gson/" + id + "_"+ classObject.getClass().getName()+".json";
      Gson gson = new Gson();
      com.google.gson.stream.JsonReader reader = new com.google.gson.stream.JsonReader(
          new java.io.FileReader(fileName));
          object = gson.fromJson(reader, classObject.getClass());
      return object;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

}
