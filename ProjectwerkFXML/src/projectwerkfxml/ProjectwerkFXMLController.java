/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectwerkfxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author jensv
 */
public class ProjectwerkFXMLController implements Initializable {
    
    @FXML
    private Label heartbeat;
    @FXML
    private Button start;
    
    @FXML
    private void handleStart(ActionEvent event) {
        System.out.println("Starting biometric station.");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
