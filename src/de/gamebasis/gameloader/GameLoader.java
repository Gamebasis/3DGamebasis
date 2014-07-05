/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gamebasis.gameloader;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.network.Client;
import com.jme3.network.serializing.Serializer;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;
import de.gamebasis.client.GameState;
import de.gamebasis.permissionsystem.GamePermissionChangedMessage;
import de.gamebasis.permissionsystem.GamePermissionSystem;
import de.gamebasislib.gameobject.GameObjectManager;
import de.gamebasislib.gameworld.GameWorld;
import de.gamebasislib.gameworldlighting.GameWorldLighting;
import de.gamebasislib.player.IPlayer;

/**
 *
 * @author Justin
 */
public class GameLoader implements Runnable {
    
    protected Client client = null;
    protected SimpleApplication app = null;
    
    public GameLoader (SimpleApplication app, Client client) {
        this.app = app;
        this.client = client;
    }
    
    @Override
    public void run () {
        //GameWorld laden
        GameWorld gameworld = new GameWorld(this.app);
        
        //GamePermissionSystem laden
        GamePermissionSystem gamepermissionsystem = new GamePermissionSystem();
        Serializer.registerClass(GamePermissionChangedMessage.class);
        this.client.addMessageListener(gamepermissionsystem, GamePermissionChangedMessage.class);

        //Player laden
        IPlayer player = null;
        
        //GameWorldLighting laden
        GameWorldLighting gameworldlighting = new GameWorldLighting(this.app, gameworld, player);
        GameWorldLighting.setInstance(gameworldlighting);
        
        //GameObjectManager laden
        GameObjectManager gameobjectmanager = new GameObjectManager(this.app);
        GameObjectManager.setInstance(gameobjectmanager);
        
        //Adding Gamesky
        this.app.getRootNode().attachChild(SkyFactory.createSky(
            this.app.getAssetManager(), "Textures/Sky/Bright/BrightSky.jpg", false));
        
        GameState.isLoaded = true;
    }
    
}
