/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gamebasis.event;

import com.jme3.network.Client;
import de.gamebasislib.event.GameEvent;

/**
 *
 * @author Justin
 */
public class GameStartEvent implements GameEvent {
    
    protected Client client = null;
    
    public GameStartEvent (Client client) {
        this.client = client;
    }
    
    public Client getClient () {
        return this.client;
    }
    
}
