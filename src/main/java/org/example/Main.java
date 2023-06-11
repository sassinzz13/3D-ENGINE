package org.example;

import org.example.Core.EngineManager;
import org.example.Core.Utils.Consts;
import org.example.Core.WindowManager;
import org.lwjgl.Version;

public class Main {
    private static WindowManager window;
    private static TestGame game;
    public static void main(String[] args) {
         window = new WindowManager(Consts.TITLE, 0, 0, false);
         game = new TestGame();
         EngineManager engine = new EngineManager();

        try{
            engine.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static WindowManager getWindow() {
        return window;
    }

    public static TestGame getGame() {
        return game;
    }
}