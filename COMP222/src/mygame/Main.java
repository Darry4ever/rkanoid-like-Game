package mygame;

import com.jme3.system.AppSettings;

/**
 * This is the main method of the game The width of the game is set to 1280 and
 * the height of the game is set to 720
 *
 * author: Wenjia Wang 201448494
 */
public class Main {

    public static void main(String[] args) {

        gameStructure app = new gameStructure();
        //set setting of the game
        AppSettings setting = new AppSettings(true);
        setting.put("Width", 1280);
        setting.put("Height", 720);
        setting.put("Title", "Wenjia Wang");
        app.setSettings(setting);
        app.setShowSettings(false);
        //start the game
        app.start();
    }
}
