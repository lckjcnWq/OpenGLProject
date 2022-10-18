package com.theswitchbot.openglproject.ui.main2

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.blankj.utilcode.util.ResourceUtils
import com.theswitchbot.openglproject.R
import com.theswitchbot.openglproject.utils.ShaderHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Main2Renderer : GLSurfaceView.Renderer {

    private var program: Int = 0
    private val BYTES_PER_FLOAT = 4
    private var vertexData:FloatBuffer
    private var vertexShader:String
    private var fragmentShader:String
    private var uColorLocation:Int = 0
    private var aPositionLocation:Int = 0
    private val POSITION_COMPONENT_COUNT = 2

    //定点属性
    private val tableVerticesWithTriangles = floatArrayOf(
        // Triangle 1
        -0.5f, -0.5f,
        0.5f, 0.5f,
        -0.5f, 0.5f,

        // Triangle 2
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f, 0.5f,

        // Line 1
        -0.5f, 0f,
        0.5f, 0f,

        // Mallets
        0f, -0.25f,
        0f, 0.25f,

    )

    init {
        //将顶点坐标写入到openGL可读取的内存中(native)
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexData.put(tableVerticesWithTriangles)
        fragmentShader = ResourceUtils.readRaw2String(R.raw.simple_fragment2_shader)
        vertexShader = ResourceUtils.readRaw2String(R.raw.simple_vertex2_shader)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        //1.将着色器连接到新程序上
        val vertexShader = ShaderHelper.compileVertexShader(vertexShader)
        val fragmentShader = ShaderHelper.compileFragmentShader(fragmentShader)
        program = ShaderHelper.linkProgram(vertexShader,fragmentShader)
        ShaderHelper.validateProgram(program)

        GLES30.glUseProgram(program)

        //2.找到对应元素的位置
        uColorLocation = GLES30.glGetUniformLocation(program,"u_Color")
        aPositionLocation = GLES30.glGetAttribLocation(program,"a_Position")

        //3.找到对应元素对应的数据并且能使用
        vertexData.position(0)
        GLES30.glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GLES30.GL_FLOAT, false,0,vertexData)
        GLES30.glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        //画长方形
        GLES30.glUniform4f(uColorLocation,1.0f,1.0f,1.0f,1.0f)  //白色画笔
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,0,6)

        //画分割线
        GLES30.glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f)  //红色画笔
        GLES30.glDrawArrays(GLES30.GL_LINES,6,2)

        //绘制俩个点
        GLES30.glUniform4f(uColorLocation,0.0f,0.0f,1.0f,1.0f)  //蓝色画笔
        GLES30.glDrawArrays(GLES30.GL_POINTS,8,1)

        GLES30.glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f)  //红色画笔
        GLES30.glDrawArrays(GLES30.GL_POINTS,9,1)

    }
}