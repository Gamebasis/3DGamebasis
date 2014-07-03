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
import de.gamebasis.client.GameState;
import de.gamebasis.permissionsystem.GamePermissionChangedMessage;
import de.gamebasis.permissionsystem.GamePermissionSystem;

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
        
        //GamePermissionSystem laden
        GamePermissionSystem gamepermissionsystem = new GamePermissionSystem();
        Serializer.registerClass(GamePermissionChangedMessage.class);
        this.client.addMessageListener(gamepermissionsystem, GamePermissionChangedMessage.class);

        // Create material from Terrain Material Definition
        Material matRock = new Material(this.app.getAssetManager(), "Common/MatDefs/Terrain/Terrain.j3md");
        
        // Load alpha map (for splat textures)
        matRock.setTexture("Alpha", this.app.getAssetManager().loadTexture("Textures/Terrain/splat/alphamap.png"));
        // load heightmap image (for the terrain heightmap)
        Texture heightMapImage = this.app.getAssetManager().loadTexture("Textures/Terrain/splat/mountains512.png");
        // load grass texture
        Texture grass = this.app.getAssetManager().loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(WrapMode.Repeat);
        matRock.setTexture("Tex1", grass);
        matRock.setFloat("Tex1Scale", 64f);
        // load dirt texture
        Texture dirt = this.app.getAssetManager().loadTexture("Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(WrapMode.Repeat);
        matRock.setTexture("Tex2", dirt);
        matRock.setFloat("Tex2Scale", 32f);
        // load rock texture
        Texture rock = this.app.getAssetManager().loadTexture("Textures/Terrain/splat/road.jpg");
        rock.setWrap(WrapMode.Repeat);
        matRock.setTexture("Tex3", rock);
        matRock.setFloat("Tex3Scale", 128f);
        
        GameState.isLoaded = true;
    }
    
}
