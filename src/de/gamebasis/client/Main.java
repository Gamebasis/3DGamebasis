package de.gamebasis.client;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import de.gamebasis.clientlistener.ClientListener;
import de.gamebasis.event.GameStartEvent;
import de.gamebasis.networkconnector.NetworkConnector;
import de.gamebasis.pluginsystem.GamePluginManager;
import de.gamebasis.screen.StartScreen;
import de.gamebasislib.camera.GameCameraManager;
import de.gamebasislib.console.GameConsoleMessage;
import de.gamebasislib.event.GameEventManager;
import de.gamebasislib.gameobject.GameObjectManager;
import de.gamebasislib.gameworld.GameWorldHeightMap;
import de.gamebasislib.gameworldlighting.GameWorldLighting;
import de.gamebasislib.player.PlayerPosMessage;
import de.gamebasislib.ui.UIManager;
import de.gamebasislib.ui.UIWindowManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tonegod.gui.controls.buttons.Button;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.Password;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.controls.windows.Window;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ScreenController {
    
    protected Nifty nifty = null;
    protected boolean debug = false;
    protected Client client = null;
    protected int port = 6143;
    protected String server = "localhost";
    protected GameClientStateListener gameclientstatelistener = new GameClientStateListener();
    
    protected int clientID = 0;
    protected ClientListener clientlistener = new ClientListener();
    protected UIManager uimanager = null;
    protected UIWindowManager uiwindowmanager = null;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        /*Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);*/
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                                                          inputManager,
                                                          audioRenderer,
                                                          guiViewPort);
        
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/Nifty/HUD.xml", "start", this);
        
        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);
        
        inputManager.setCursorVisible(true);
        
        inputManager.addMapping("DebugInformation",  new KeyTrigger(KeyInput.KEY_D));//KeyInput.KEY_LMENU Windows Key
        
        ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (keyPressed) {
                if (name.equals("DebugInformation")) {
                    //Debuginformationen anzeigen
                    if (debug) {
                        Main.this.setDisplayStatView(false);
                        Main.this.setDisplayFps(false);
                    } else {
                        Main.this.setDisplayStatView(true);
                        Main.this.setDisplayFps(true);
                    }
                    
                    debug = !debug;
                }
            }
        }
        };
        
        //ActionListener registrieren
        inputManager.addListener(actionListener, new String[]{"DebugInformation"});
        
        flyCam.setEnabled(false);
        setDisplayStatView(false);
        this.setDisplayFps(false);
        
        this.uimanager = new UIManager(this);
        this.uiwindowmanager = this.uimanager.getWindowManagerByID(1);
        
        NetworkConnector networkconnector = new NetworkConnector(this.client, this.gameclientstatelistener);
        
        //Startscreen
        //StartScreen startScreenState = new StartScreen(this);
        //stateManager.attach(startScreenState);
        // [...] boilerplate init nifty omitted
        //nifty.fromXml("Interface/start.xml", "start", startScreenState); //one of the XML screen elements needs to reference StartScreenState controller class
        
        //Gameplugins laden
        GamePluginManager.loadGamePlugins("./ext/app");
        GamePluginManager.simpleInitApp(this);
        
        GameCameraManager gamecameramanager = new GameCameraManager(this);
        GameCameraManager.setInstance(gamecameramanager);
        
        GameState.isConnected = false;
        GameState.isLoaded = false;
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        if (GameState.isConnected && GameState.isLoaded) {
            //GameWorldLighting updaten
            GameWorldLighting.getInstance().simpleUpdate(tpf);
            
            //GameObjects updaten
            GameObjectManager.getInstance().simpleUpdate(tpf);
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
        if (GameState.isConnected && GameState.isLoaded) {
            //
        }
    }
    
    public Client getClient () {
        return this.client;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        System.out.println("bind( " + screen.getScreenId() + ")");
    }

    @Override
    public void onStartScreen() {
        System.out.println("onStartScreen");
    }

    @Override
    public void onEndScreen() {
        System.out.println("onEndScreen");
    }
    
    public void quit () {
        nifty.gotoScreen("end");
    }
    
    @Override
    public void destroy() {
        if (GameState.isConnected) {
            this.client.close();
        }
        super.destroy();
    }
}
