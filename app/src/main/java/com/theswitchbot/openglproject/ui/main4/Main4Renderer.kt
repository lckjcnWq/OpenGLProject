package com.theswitchbot.openglproject.ui.main3

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.blankj.utilcode.util.ResourceUtils
import com.theswitchbot.openglproject.R
import com.theswitchbot.openglproject.helper.ShaderHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Main4Renderer : GLSurfaceView.Renderer {

    private var program: Int = 0
    private val BYTES_PER_FLOAT = 4
    private var vertexData:FloatBuffer
    private var vertexShader:String
    private var fragmentShader:String
    private val POSITION_COMPONENT_COUNT = 2
    private val COLOR_COMPONENT_COUNT = 3
    private val STRIDE = 20
    private var aPositionLocation:Int = 0
    private var aColorLocation:Int = 0
    private var uMatrixLocation:Int = 0
    private val projectionMatrix = FloatArray(16)

    //定点属性
    private val tableVerticesWithTriangles = floatArrayOf(
        // Triangle Fan
        // Order of coordinates: X, Y, R, G, B
        0f, 0f, 1f, 1f, 1f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

        // Line 1
        -0.5f, 0f, 1f, 0f, 0f,
        0.5f, 0f, 1f, 0f, 0f,

        // Mallets
        0f, -0.4f, 0f, 0f, 1f,
        0f, 0.4f, 1f, 0f, 0f
    )

    init {
        //将顶点坐标写入到openGL可读取的内存中(native)
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexData.put(tableVerticesWithTriangles)
        fragmentShader = ResourceUtils.readRaw2String(R.raw.simple_fragment4_shader)
        vertexShader = ResourceUtils.readRaw2String(R.raw.simple_vertex4_shader)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        //1.将着色器连接到新程序上
        val vertexShader = ShaderHelper.compileVertexShader(vertexShader)
        val fragmentShader = ShaderHelper.compileFragmentShader(fragmentShader)
        program = ShaderHelper.linkProgram(vertexShader,fragmentShader)
        ShaderHelper.validateProgram(program)
        GLES30.glUseProgram(program)

        //2.元素的位置
        aPositionLocation = GLES30.glGetAttribLocation(program,"a_Position")
        aColorLocation = GLES30.glGetAttribLocation(program,"a_Color")
        uMatrixLocation = GLES30.glGetUniformLocation(program,"u_Matrix")

        //3.enable aPosition
        vertexData.position(0)
        GLES30.glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GLES30.GL_FLOAT, false,STRIDE,vertexData)
        GLES30.glEnableVertexAttribArray(aPositionLocation)

        //4.enable aColor
        vertexData.position(POSITION_COMPONENT_COUNT)
        GLES30.glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT,GLES30.GL_FLOAT, false,STRIDE,vertexData)
        GLES30.glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        val aspectRation = if(width>height){
            width.toFloat()/height.toFloat()
        }else{
            height.toFloat()/width.toFloat()
        }
        if(width>height){
            Matrix.orthoM(projectionMatrix,0,-aspectRation,aspectRation,-1f,1f,-1f,1f)
        }else{
            Matrix.orthoM(projectionMatrix,0,-1f,1f,-aspectRation,aspectRation,-1f,1f)
        }
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        GLES30.glUniformMatrix4fv(uMatrixLocation,1,false,projectionMatrix,0)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN,0,6)
        GLES30.glDrawArrays(GLES30.GL_LINES,6,2)
        GLES30.glDrawArrays(GLES30.GL_LINES,8,1)
        GLES30.glDrawArrays(GLES30.GL_POINTS,9,1)

    }
}