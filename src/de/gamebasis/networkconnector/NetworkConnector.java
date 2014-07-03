/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gamebasis.networkconnector;

import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import de.gamebasis.client.GameClientStateListener;
import de.gamebasis.client.GameState;
import de.gamebasis.client.Main;
import de.gamebasis.clientlistener.ClientListener;
import de.gamebasis.event.GameStartEvent;
import de.gamebasislib.console.GameConsoleMessage;
import de.gamebasislib.event.GameEventManager;
import de.gamebasislib.gameworld.GameWorldHeightMap;
import de.gamebasislib.player.PlayerPosMessage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Justin
 */
public class NetworkConnector {
    
    protected Client client = null;
    protected String server = "";
    protected int port = 6143;
    protected GameClientStateListener gameclientstatelistener = null;
    protected int clientID = -1;
    protected ClientListener clientlistener = null;
    
    protected GameConsoleMessage gameconsolemessage = new GameConsoleMessage();
    protected static NetworkConnector instance = null;
    protected String username = "";
    protected String password = "";
    
    public NetworkConnector (Client client, GameClientStateListener gameclientstatelistener) {
        this.client = client;
        this.gameclientstatelistener = gameclientstatelistener;
        
        NetworkConnector.setInstance(this);
    }
    
    public boolean setUpNetwork (String server, int port, String username, String password) {
        try {
            this.server = server;
            this.port = port;
            this.username = username;
            this.password = password;
            
            //Client mit Server verbinden
            this.client = Network.connectToServer(this.server, this.port);
            this.client.start();
            
            //ClientStateListener hinzufügen
            this.client.addClientStateListener(this.gameclientstatelistener);
            
            this.clientlistener = new ClientListener();
            
            this.clientID = this.client.getId();
            
            //Message Klassen beim Serializer registrieren
            Serializer.registerClass(GameWorldHeightMap.class);
            Serializer.registerClass(PlayerPosMessage.class);
            Serializer.registerClass(GameConsoleMessage.class);
            
            this.client.addMessageListener(this.clientlistener, GameWorldHeightMap.class);
            
            //Event werfen
            GameStartEvent gamestartevent = new GameStartEvent(client);
            GameEventManager.raiseEvent(gamestartevent);
            
            GameState.isConnected = true;
            
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public String getServer () {
        return this.server;
    }
    
    public int getPort () {
        return this.port;
    }
    
    public int getClientID () {
        return this.clientID;
    }
    
    public Client getClient () {
        return this.client;
    }
    
    public GameClientStateListener getGameClientStateListener () {
        return this.gameclientstatelistener;
    }
    
    public ClientListener getClientListener () {
        return this.clientlistener;
    }
    
    public static NetworkConnector getInstance () {
        return NetworkConnector.instance;
    }
    
    public static void setInstance (NetworkConnector networkconnector) {
        NetworkConnector.instance = networkconnector;
    }
    
}
