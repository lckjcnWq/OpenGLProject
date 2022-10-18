package com.theswitchbot.openglproject.ui.main7

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.theswitchbot.openglproject.R
import com.theswitchbot.openglproject.bean.MalletX
import com.theswitchbot.openglproject.bean.Puck
import com.theswitchbot.openglproject.bean.Table
import com.theswitchbot.openglproject.helper.TextureHelper
import com.theswitchbot.openglproject.programs.ColorShaderProgram
import com.theswitchbot.openglproject.programs.TextureShaderProgram
import com.theswitchbot.openglproject.utils.MatrixHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 1.如何把三角形组织成为三角形带和三角形扇
 * 2.如何定义视图矩阵: 平移、旋转和来回移动
 * */

class Main7Renderer(context: Context) : GLSurfaceView.Renderer {

    private val TAG="Main7Renderer"
    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)

    private var table: Table? = null
    private var mallet: MalletX? = null
    private var puck: Puck? = null

    private var textureProgram: TextureShaderProgram? = null
    private var colorProgram: ColorShaderProgram? = null

    private var texture = 0
    private var mContext = context


    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        table = Table()
        mallet = MalletX(0.08f, 0.15f, 32)
        puck = Puck(0.06f, 0.02f, 32)
        textureProgram = TextureShaderProgram(mContext)
        colorProgram = ColorShaderProgram(mContext)
        texture = TextureHelper.loadText(context = mContext, R.drawable.air_hockey_surface)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        //投影矩阵
        MatrixHelper.perspectiveM(
            projectionMatrix, 45f, width.toFloat()
                    / height.toFloat(), 1f, 10f
        )
        //视图矩阵
        //将眼睛放在（0,1.2,2.2）  眼睛所在的位置
        //(0,0,0)  眼睛正在看的地方
        //(0,1,0)  你的头指向的地方
        Matrix.setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f)
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        // Multiply the view and projection matrices together.
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Draw the table.
        positionTableInScene()
        textureProgram?.useProgram()
        textureProgram?.setUniforms(modelViewProjectionMatrix, texture)
        table?.bindData(textureProgram)
        table?.draw()

        // Draw the mallets.
        positionObjectInScene(0f, mallet!!.height / 2f, -0.4f)
        colorProgram?.useProgram()
        colorProgram?.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f)
        mallet?.bindData(colorProgram)
        mallet?.draw()

        positionObjectInScene(0f, mallet!!.height / 2f, 0.4f)
        colorProgram?.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f)
        mallet?.draw()

        // Draw the puck.
        positionObjectInScene(0f, puck!!.height / 2f, 0f)
        colorProgram?.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f)
        puck?.bindData(colorProgram)
        puck?.draw()
    }

    private fun positionTableInScene() {
        // The table is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f)
        Matrix.multiplyMM(
            modelViewProjectionMatrix, 0, viewProjectionMatrix,
            0, modelMatrix, 0
        )
    }

    // The mallets and the puck are positioned on the same plane as the table.
    private fun positionObjectInScene(x: Float, y: Float, z: Float) {
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, x, y, z)
        Matrix.multiplyMM(
            modelViewProjectionMatrix, 0, viewProjectionMatrix,
            0, modelMatrix, 0
        )
    }
}