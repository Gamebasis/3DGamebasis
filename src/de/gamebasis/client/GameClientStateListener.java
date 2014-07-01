/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gamebasis.client;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import de.gamebasis.pluginsystem.GamePluginManager;

/**
 *
 * @author Justin
 */
public class GameClientStateListener implements ClientStateListener {

    public void clientConnected(Client c) {
        GamePluginManager.onConnected(c);
        GameState.isConnected = true;
    }

    public void clientDisconnected(Client c, DisconnectInfo info) {
        GamePluginManager.onDisconnected(c, info);
        GameState.isConnected = false;
    }
    
}
