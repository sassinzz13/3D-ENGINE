package org.example.Core;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class WindowManager {
//Memorize or reuse

    //first declare variables
    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;

    private final String title;
    private int width, height;
    private long window;
    private boolean resize, vSync;

    private final Matrix4f projectionMatrix;

    //then declare the window manager
    public WindowManager(String title, int width, int height, boolean vSync) {
        //constructor
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        projectionMatrix = new Matrix4f();
    }

    //initialize
    public void init(){
        //error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //initialize GLFW
        if(!GLFW.glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        //set defaults to the window
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
        //GLFW CONTEXT VERSION
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);

        boolean maximized = false;
        if(width == 0 || height == 0){
            width = 100;
            height = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            maximized = true;
        }

        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        //CHECK IF WINDOW IS NULL
        if(window == MemoryUtil.NULL)
            throw new RuntimeException("failed to create GLFW WINDOW");

        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height)->{
           this.width = width;
           this.height = height;
           this.setResize(true);
        });
        GLFW.glfwSetKeyCallback(window,(window,key,scancode,action,mods)->{
           if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
               GLFW.glfwSetWindowShouldClose(window, true);
        });
        if(maximized)
            GLFW.glfwMaximizeWindow(window);
        else{
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(window,(vidMode.width() - width)/ 2, (vidMode.height() - height) / 2);
        }
        GLFW.glfwMakeContextCurrent(window);
        if(isvSync())
            GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(window);
        GL.createCapabilities();
        GL11.glClearColor(0.0f,0.0f,0.0f,0.0f);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public void update(){
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    public void cleanup(){
        GLFW.glfwDestroyWindow(window);
    }

    public void setClearColor(float r, float g, float b, float a){
        GL11.glClearColor(r,g,b,a);
    }

    public boolean isKeyPressed(int keycode){
        return GLFW.glfwGetKey(window,keycode)==GLFW.GLFW_PRESS;
    }

    public boolean windowShouldClose(){

        return GLFW.glfwWindowShouldClose(window);
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        GLFW.glfwSetWindowTitle(window, title);
    }

    public boolean isvSync(){
        return vSync;
    }

    public boolean isResize() {
        return resize;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getWindow() {
        return window;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f updateProjectionMatrix(){
        float aspectRatio =(float) width / height ;
        return projectionMatrix.setPerspective(FOV , aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f updateProjectMatrix(Matrix4f matrix, int width, int height){
        float aspectRatio =(float) width / height ;
        return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }
}
