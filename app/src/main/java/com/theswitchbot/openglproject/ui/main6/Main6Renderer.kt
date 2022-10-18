package com.theswitchbot.openglproject.ui.main6

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import com.theswitchbot.openglproject.R
import com.theswitchbot.openglproject.bean.Mallet
import com.theswitchbot.openglproject.bean.Table
import com.theswitchbot.openglproject.helper.TextureHelper
import com.theswitchbot.openglproject.programs.ColorShaderProgram
import com.theswitchbot.openglproject.programs.TextureShaderProgram
import com.theswitchbot.openglproject.utils.MatrixHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 1.将纹理加载进来
 * 2.显示纹理，调整代码使其支持多个着色器程序
 * 3.不同的过滤模式以及他们的对应应用场景
 * */

class Main6Renderer(context: Context) : GLSurfaceView.Renderer {

    private val TAG="Main6Renderer"
    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val table = Table()
    private val mallet = Mallet()
    private var mContext = context
    private var textureProgram:TextureShaderProgram ?=null
    private var colorProgram: ColorShaderProgram?=null
    private var texture:Int = 0

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        textureProgram = TextureShaderProgram(mContext)
        colorProgram = ColorShaderProgram(mContext)
        texture = TextureHelper.loadText(mContext,R.drawable.air_hockey_surface)
        Log.d(TAG,"onSurfaceCreated texture=$texture")
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        //1.创建一个45度角  视椎体从z值为-1的位置开始，在Z值为-10的位置结束的投影矩阵
        //因openGL z默认为0 此时桌面看不见
        MatrixHelper.perspectiveM(projectionMatrix,45f,width.toFloat()/height.toFloat(),1f,10f)

        //2.利用模型矩阵移动物体（-1,1）的范围
        Matrix.setIdentityM(modelMatrix,0)  //设置为单位矩阵
        Matrix.translateM(modelMatrix,0,-0f,0f,-2.5f)  //沿z轴平移-2.5
        Matrix.rotateM(modelMatrix,0,-45f,1f,0f,0f)

        //3.将矩阵应用到每个顶点  投影矩阵*模型矩阵(此顺序不能变，重要)
        val temp = FloatArray(16)
        Matrix.multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0)
        System.arraycopy(temp,0,projectionMatrix,0,temp.size)
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        //draw table
        textureProgram?.useProgram()
        textureProgram?.setUniforms(projectionMatrix,texture)
        table.bindData(textureProgram)
        table.draw()

        //draw mallets
        colorProgram?.useProgram()
        colorProgram?.setUniforms(projectionMatrix)
        mallet.bindData(colorProgram)
        mallet.draw()
    }
}