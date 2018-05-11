/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectwerkfxml;

import be.vives.oop.mqtt.chatservice.IMqttMessageHandler;
import be.vives.oop.mqtt.chatservice.MqttChatService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author jensv
 */
public class ProjectwerkFXMLController implements Initializable, IMqttMessageHandler {
    
    @FXML
    private Label heartbeat;
    @FXML
    private Button start;
   
    private MqttChatService jens_heartbeat;
    private MqttChatService jens_temperature;
    private MqttChatService jens_x_value;
    private MqttChatService jens_y_value;
    private MqttChatService jens_z_value;
        
    @FXML
    private void handleStart(ActionEvent event) {
        System.out.println("Starting biometric station.");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        jens_heartbeat = new MqttChatService("jens", "jens_heartbeat");
        jens_heartbeat.setMessageHandler(this);
        jens_temperature = new MqttChatService("jens", "jens_temperature");
        jens_temperature.setMessageHandler(this);
        jens_x_value = new MqttChatService("jens", "jens_x_value");
        jens_x_value.setMessageHandler(this);
        jens_y_value = new MqttChatService("jens", "jens_y_value");
        jens_y_value.setMessageHandler(this);
        jens_z_value = new MqttChatService("jens", "jens_z_value");
        jens_z_value.setMessageHandler(this);
        disconnectClientOnClose();
    }    
    
    @Override
    public void messageArrived(String channel, String message) {
        System.out.println("Received chat messages (on channel = " + channel + "): " + message);  
        
        // Om van die cross-thread error af te komen heb je onderstaand stukje code nodig.
        // Gevonden op stack-overflow https://stackoverflow.com/questions/11690683/javafx-update-ui-from-another-thread
        Platform.runLater(new Runnable() {
            @Override public void run() {
              //Update UI here     
              heartbeat.setText(message);
            }
        });
    }    
    
    private void disconnectClientOnClose() {
        // Source: https://stackoverflow.com/a/30910015
        start.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                // scene is set for the first time. Now its the time to listen stage changes.
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        // stage is set. now is the right time to do whatever we need to the stage in the controller.
                        ((Stage) newWindow).setOnCloseRequest((event) -> {
                            jens_heartbeat.disconnect();
                            
                            // Hier moet je ook je andere services disconnecten !!!!
                        });
                    }
                });
            }
        });
    }
}
