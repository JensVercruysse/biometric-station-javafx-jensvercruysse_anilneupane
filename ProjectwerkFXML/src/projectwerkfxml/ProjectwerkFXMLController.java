/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectwerkfxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mqttbiometricdataservice.IMqttDataHandler;
import mqttbiometricdataservice.MqttBiometricDataService;

/**
 *
 * @author jensv
 */
public class ProjectwerkFXMLController implements Initializable, IMqttDataHandler {

    @FXML
    private Label heartbeat;
    @FXML
    private Label temperature;
    @FXML
    private Label acceleroAll;
    @FXML
    private Label acceleroX;
    @FXML
    private Label acceleroY;
    @FXML
    private Label acceleroZ;
    @FXML
    private Button start;
    @FXML
    private LineChart heartbeatChart;
    @FXML
    private LineChart temperatureChart;

    private int heartbeat_x_value = 0;
    private int temperature_x_value = 0;

    private MqttBiometricDataService jens_heartbeat;
    private MqttBiometricDataService jens_temperature;
    private MqttBiometricDataService jens_x_value;
    private MqttBiometricDataService jens_y_value;
    private MqttBiometricDataService jens_z_value;

    private String heartbeatDataAsString = "0";
    private int heartbeatDataAsInt = 0;
    private String temperatureDataAsString = "00,00";
    private double temperatureDataAsDouble = 0;
    private String accelero_x_value = "000";
    private String accelero_y_value = "000";
    private String accelero_z_value = "000";

    private XYChart.Series heartbeatValues[];
    private XYChart.Series temperatureValues[];

    private final int NUMBER_OF_HEARTBEAT_SERIES = 1;
    private final int NUMBER_OF_TEMPERATURE_SERIES = 1;

    private XYChart.Series createXYSeries(String name) {
        XYChart.Series series = new XYChart.Series();
        series.setName(name);
        return series;
    }

    @FXML
    private void handleStart(ActionEvent event) {
        System.out.println("Starting biometric station.");
        showValues();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hideValues();

        heartbeatValues = new XYChart.Series[NUMBER_OF_HEARTBEAT_SERIES];
        heartbeatValues[0] = createXYSeries("Heartbeat");
        heartbeatChart.getData().add(heartbeatValues[0]);
        heartbeatChart.getYAxis().setLabel("BPM");

        temperatureValues = new XYChart.Series[NUMBER_OF_TEMPERATURE_SERIES];
        temperatureValues[0] = createXYSeries("Temperature");
        temperatureChart.getData().add(temperatureValues[0]);
        temperatureChart.getYAxis().setLabel("Temperature [Celcius]");

        jens_heartbeat = new MqttBiometricDataService("jens", "heartbeat");
        jens_heartbeat.setDataHandler(this);
        jens_temperature = new MqttBiometricDataService("jens", "temperature");
        jens_temperature.setDataHandler(this);
        jens_x_value = new MqttBiometricDataService("jens", "accelero_x_value");
        jens_x_value.setDataHandler(this);
        jens_y_value = new MqttBiometricDataService("jens", "accelero_y_value");
        jens_y_value.setDataHandler(this);
        jens_z_value = new MqttBiometricDataService("jens", "accelero_z_value");
        jens_z_value.setDataHandler(this);

        disconnectClientOnClose();
    }

    private void hideValues() {
        heartbeat.setVisible(false);
        temperature.setVisible(false);
        acceleroAll.setVisible(false);
        acceleroX.setVisible(false);
        acceleroY.setVisible(false);
        acceleroZ.setVisible(false);
    }

    private void showValues() {
        heartbeat.setVisible(true);
        temperature.setVisible(true);
        acceleroAll.setVisible(true);
        acceleroX.setVisible(true);
        acceleroY.setVisible(true);
        acceleroZ.setVisible(true);
    }

    @Override
    public void dataArrived(String channel, String data) {
        System.out.println("Received data on channel " + channel + ": " + data);

        if (channel.equals("heartbeat")) {
            heartbeatDataAsString = data;
            heartbeatDataAsInt = Integer.parseInt(data);
            runHeartbeat();
        } else if (channel.equals("temperature")) {
            temperatureDataAsString = data;
            temperatureDataAsDouble = Double.parseDouble(data);
            runTemperature();
        } else if (channel.equals("accelero_x_value")) {
            accelero_x_value = data;
            runAcceleroX();
        } else if (channel.equals("accelero_y_value")) {
            accelero_y_value = data;
            runAcceleroY();
        } else if (channel.equals("accelero_z_value")) {
            accelero_z_value = data;
            runAcceleroZ();
        }
    }

    private void runHeartbeat() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Update UI here     
                heartbeat.setText(heartbeatDataAsString);
                heartbeatValues[0].getData().add(new XYChart.Data(heartbeat_x_value, heartbeatDataAsInt));
                heartbeat_x_value++;
            }
        });
    }
    private void runTemperature(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Update UI here     
            temperature.setText(temperatureDataAsString);
            temperatureValues[0].getData().add(new XYChart.Data(temperature_x_value, temperatureDataAsDouble));
            temperature_x_value++;
            }
        });       
    }
    private void runAcceleroX(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Update UI here     
            acceleroX.setText(accelero_x_value);
            acceleroAll.setText("[ " + accelero_x_value + " | " + accelero_y_value + " | " + accelero_z_value + " ]");
            }
        });       
    }
    private void runAcceleroY(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Update UI here     
            acceleroY.setText(accelero_y_value);
            acceleroAll.setText("[ " + accelero_x_value + " | " + accelero_y_value + " | " + accelero_z_value + " ]");
            }
        });       
    }
    private void runAcceleroZ(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Update UI here     
            acceleroZ.setText(accelero_z_value);
            acceleroAll.setText("[ " + accelero_x_value + " | " + accelero_y_value + " | " + accelero_z_value + " ]");
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
                            jens_temperature.disconnect();
                            jens_x_value.disconnect();
                            jens_y_value.disconnect();
                            jens_z_value.disconnect();
                        });
                    }
                });
            }
        });
    }
}
