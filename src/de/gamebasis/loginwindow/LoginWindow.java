/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gamebasis.loginwindow;

import com.jme3.app.SimpleApplication;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.Vector2f;
import de.gamebasis.gameloader.GameLoader;
import de.gamebasis.networkconnector.NetworkConnector;
import de.gamebasislib.ui.UIWindowManager;
import tonegod.gui.controls.buttons.Button;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.Password;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author Justin
 */
public class LoginWindow {
    
    protected Screen screen = null;
    protected SimpleApplication app = null;
    protected Window window = null;
    protected UIWindowManager uiwindowmanager = null;
    
    protected String server = "";
    protected int port = 6143;
    protected String username = "";
    protected String password = "";
    
    public LoginWindow (SimpleApplication app, Screen screen, UIWindowManager uiwindowmanager) {
        this.app = app;
        this.screen = screen;
        this.uiwindowmanager = uiwindowmanager;
    }
    
    public void showWindow () {
        final Window window = this.uiwindowmanager.createWindow("console", new Vector2f(15, 15), new Vector2f(this.uiwindowmanager.getScreen().getWidth() - 60, this.uiwindowmanager.getScreen().getHeight() - 60));
        window.setWindowIsMovable(true);
        window.setWindowTitle("Login");
        
        Label label = new Label(this.uiwindowmanager.getScreen(), new Vector2f(100, 100));
        label.setText("Server: ");
        label.setPosition(100, 60);
        window.addChild(label);
        
        final TextField servertextfield = new TextField(this.uiwindowmanager.getScreen(), "TextField", new Vector2f(200, 100));
        window.addChild(servertextfield);
        
        label = new Label(this.uiwindowmanager.getScreen(), new Vector2f(100, 100));
        label.setText("Port: ");
        label.setPosition(340, 60);
        window.addChild(label);
        
        final TextField portfield = new TextField(this.uiwindowmanager.getScreen(), "TextField", new Vector2f(480, 100));
        portfield.setType(TextField.Type.NUMERIC);
        portfield.setText("6143");
        window.addChild(portfield);
        
        label = new Label(this.uiwindowmanager.getScreen(), new Vector2f(100, 100));
        label.setText("Username: ");
        label.setPosition(100, 100);
        window.addChild(label);
        
        final TextField usernametextfield = new TextField(this.uiwindowmanager.getScreen(), "TextField", new Vector2f(200, 140));
        window.addChild(usernametextfield);
        
        label = new Label(this.uiwindowmanager.getScreen(), new Vector2f(400, 100));
        label.setText("Password (optional): ");
        label.setPosition(340, 100);
        window.addChild(label);
        
        final Password pw = new Password(this.uiwindowmanager.getScreen(), "password", new Vector2f(480, 140));
        window.addChild(pw);
        
        Button button = new Button(this.uiwindowmanager.getScreen(), "button", new Vector2f(100, 180)) {

            @Override
            public void onButtonMouseLeftDown(MouseButtonEvent mbe, boolean bln) {
                //Login
                LoginWindow.this.server = servertextfield.getText();
                LoginWindow.this.port = Integer.parseInt(portfield.getText());
                LoginWindow.this.username = usernametextfield.getText();
                LoginWindow.this.password = pw.getText();
                
                NetworkConnector networkconnector = NetworkConnector.getInstance();
                
                if (networkconnector.setUpNetwork(LoginWindow.this.server, LoginWindow.this.port, LoginWindow.this.username, LoginWindow.this.password)) {
                    window.hideWindow();
                    window.removeAllChildren();
                    window.cleanup();
                    
                    GameLoader gameloader = new GameLoader(LoginWindow.this.app, networkconnector.getClient());
                    Thread thread = new Thread(gameloader);
                    thread.start();
                } else {
                    pw.setText("");
                    Label label = new Label(LoginWindow.this.uiwindowmanager.getScreen(), new Vector2f(480, 200));
                    label.setText("Wrong Login!");
                    window.addChild(label);
                }
            }

            @Override
            public void onButtonMouseRightDown(MouseButtonEvent mbe, boolean bln) {
                //
            }

            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent mbe, boolean bln) {
                //
            }

            @Override
            public void onButtonMouseRightUp(MouseButtonEvent mbe, boolean bln) {
                //
            }

            @Override
            public void onButtonFocus(MouseMotionEvent mme) {
                //
            }

            @Override
            public void onButtonLostFocus(MouseMotionEvent mme) {
                //
            }
        };
        button.setText("Connect");
        window.addChild(button);
        
        this.uiwindowmanager.addWindow(window);
    }
    
}
