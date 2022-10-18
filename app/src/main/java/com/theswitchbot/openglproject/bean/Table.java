package com.theswitchbot.openglproject.bean;
import static android.opengl.GLES30.GL_TRIANGLE_FAN;
import static android.opengl.GLES30.glDrawArrays;
import com.theswitchbot.openglproject.constant.Constants;
import com.theswitchbot.openglproject.data.VertexArray;
import com.theswitchbot.openglproject.programs.TextureShaderProgram;

public class Table {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    //定义位置坐标和纹理坐标
    private static final float[] VERTEX_DATA = {
        // Order of coordinates: X, Y, S, T

        // Triangle Fan
           0f,    0f, 0.5f, 0.5f, 
        -0.5f, -0.8f,   0f, 0.9f,  
         0.5f, -0.8f,   1f, 0.9f, 
         0.5f,  0.8f,   1f, 0.1f, 
        -0.5f,  0.8f,   0f, 0.1f, 
        -0.5f, -0.8f,   0f, 0.9f };
    
    private final VertexArray vertexArray;
    
    public Table() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }
    
    public void bindData(TextureShaderProgram textureProgram) {
        vertexArray.setVertexAttribPointer(
            0, 
            textureProgram.getPositionAttributeLocation(), 
            POSITION_COMPONENT_COUNT,
            STRIDE);
        
        vertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT, 
            textureProgram.getTextureCoordinatesAttributeLocation(),
            TEXTURE_COORDINATES_COMPONENT_COUNT, 
            STRIDE);
    }
    
    public void draw() {                                
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }
}
