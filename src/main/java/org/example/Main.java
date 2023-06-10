package org.example;

import org.example.Core.WindowManager;
import org.lwjgl.Version;

public class Main {
    public static void main(String[] args) {
        WindowManager window = new WindowManager("Etits Engine", 600, 600, false);
        window.init();

        while(!window.windowShouldClose()){
            window.update();
        }

        window.cleanup();
    }
}