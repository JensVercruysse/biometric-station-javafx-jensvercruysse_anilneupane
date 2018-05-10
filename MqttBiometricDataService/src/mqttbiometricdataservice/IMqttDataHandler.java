/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqttbiometricdataservice;

/**
 *
 * @author jensv
 */

public interface IMqttDataHandler {
    public void dataArrived(String channel, String data);
}

