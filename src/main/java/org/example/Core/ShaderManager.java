package org.example.Core;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

public class ShaderManager {

    private final int programID;
    private int vertexShaderID, fragmentShaderID;
    private final Map<String, Integer> uniforms;

    public ShaderManager() throws Exception{
        programID = GL20.glCreateProgram();
        if(programID == 0)
            throw new Exception("Could not create the shader");

        uniforms = new HashMap<>();
    }

    public void createUniform(String uniformName) throws Exception{
        int uniformLocation = GL20.glGetUniformLocation(programID, uniformName);
        if(uniformLocation < 0)
            throw new Exception("could not find uniform" + uniformName);
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniforms(String uniformname, Matrix4f value){
        try(MemoryStack stack = MemoryStack.stackPush()){
            GL20.glUniformMatrix4fv(uniforms.get(uniformname), false, value.get(stack.mallocFloat(16)));

        }
    }

    public void setUniforms(String uniformname, int value){
        GL20.glUniform1i(uniforms.get(uniformname), value);
    }

    public void createVertexShader(String shaderCode) throws Exception{
        vertexShaderID = createShader(shaderCode,GL20.GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception{
        fragmentShaderID = createShader(shaderCode,GL20.GL_FRAGMENT_SHADER);
    }

    public int createShader(String shaderCode, int shaderType) throws Exception{
        int shaderID = GL20.glCreateShader(shaderType);
        if(shaderID == 0)
            throw new Exception("Error creating shaders: type" + shaderType);

        GL20.glShaderSource(shaderID, shaderCode);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS)==0)
            throw new Exception("Error compiling shader code: Type:" + shaderType + "Info: " + GL20.glGetShaderInfoLog(shaderID, 1024));
        GL20.glAttachShader(programID, shaderID);
        return shaderID;
    }

    public void link() throws Exception{
        GL20.glLinkProgram(programID);
        if(GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS)==0)
            throw new Exception("Error Linking shader code: Info: " + GL20.glGetProgramInfoLog(programID, 1024));
        if(vertexShaderID != 0)
            GL20.glDetachShader(programID, vertexShaderID);
        if(fragmentShaderID != 0)
            GL20.glDetachShader(programID,fragmentShaderID);
        GL20.glValidateProgram(programID);
        if(GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS)==0)
            throw new Exception("Unable to validate shader code: " + GL20.glGetProgramInfoLog(programID, 1024));
    }
    public void bind(){
        GL20.glUseProgram(programID);
    }

    public void unbind(){
        GL20.glUseProgram(0);
    }

    public void cleanup(){
        unbind();
        if(programID != 0)
            GL20.glDeleteProgram(programID);
    }
}
