package org.example.Core;

import org.example.Core.Utils.Consts;
import org.example.Main;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class EngineManager {
    //NANOSECOND 1 BILLION
    public static final long NANOSECOND = 1000000000L;
    //FRAMERATE DEFAULT TO A THOUSAND
    public static final float FRAMERATE = 1000;
    //FPS
    private static int fps;
    //FRAMETIME 1/FRAMERATE
    private static float frametime = 1.0f/ FRAMERATE;
    //BOOL TO CHECK IF ENGINE IS RUNNING
    private boolean isRunning;
    //REFERENCE TO THE WINDOW MANAGER
    private WindowManager window;
    //ERROR CALLBACK TO CAPTURE ERRORS
    private GLFWErrorCallback errorCallback;
    //captures and throws exception is there is any
    private iLogic gameLogic;
    private void init() throws Exception{
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Main.getWindow();
        gameLogic = Main.getGame();
        window.init();
        gameLogic.init();
    }
    public void start() throws Exception{
        init();
        if(isRunning)
            return;
        run();
    }

    public void run(){
        this.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while(isRunning){
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double)NANOSECOND;
            frameCounter += passedTime;

            input();

            //call input
            while(unprocessedTime > frametime){
                render= true;
                unprocessedTime -= frametime;

                if(window.windowShouldClose())
                    stop();

                if(frameCounter >= NANOSECOND){
                    setFps(frames);
                    window.setTitle(Consts.TITLE+getFps());
                    frames = 0;
                    frameCounter = 0;
                }
            }
            if(render){
                update();
                render();
                frames++;
            }
        }
        cleanup();
    }

    private void stop(){
        if(!isRunning)
            return;
        isRunning = false;
    }

    private void input(){
        gameLogic.input();
    }

    private void render(){
        gameLogic.render();
        window.update();
    }

    private void update(){
        gameLogic.update();
    }

    private void cleanup(){
        window.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }
}
