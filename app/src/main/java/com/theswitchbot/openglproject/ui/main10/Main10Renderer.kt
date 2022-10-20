package com.theswitchbot.openglproject.ui.main10

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import com.theswitchbot.openglproject.R
import com.theswitchbot.openglproject.bean.Geometry
import com.theswitchbot.openglproject.helper.TextureHelper
import com.theswitchbot.openglproject.three.bean.Heightmap
import com.theswitchbot.openglproject.three.bean.ParticleShooter
import com.theswitchbot.openglproject.three.bean.ParticleSystem
import com.theswitchbot.openglproject.three.bean.Skybox
import com.theswitchbot.openglproject.three.programs.HeightmapShaderProgram
import com.theswitchbot.openglproject.three.programs.ParticleShaderProgram
import com.theswitchbot.openglproject.three.programs.SkyboxShaderProgram
import com.theswitchbot.openglproject.helper.MatrixHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**效果：增加地形
 * 1.创建一个高度图，并用顶点缓冲区对象和索引缓冲区对象把它加载到程序中
 * 2.剔除和深度缓冲区：用于遮罩隐藏的物体
 *
 * */

class Main10Renderer(context: Context) : GLSurfaceView.Renderer {

    private var heightmap: Heightmap? = null
    private var heightmapProgram: HeightmapShaderProgram? = null
    private var texture: Int = 0
    private var skyboxTexture: Int = 0
    private var skyboxProgram: SkyboxShaderProgram? = null
    private var skybox: Skybox? = null
    private var redParticleShooter: ParticleShooter? = null
    private var blueParticleShooter: ParticleShooter? = null
    private var greenParticleShooter: ParticleShooter? = null
    private var globalStartTime: Long = 0
    private var particleSystem: ParticleSystem? = null
    private var particleProgram: ParticleShaderProgram? = null
    private val TAG = "Main8Renderer"
    private val mContext = context
    private val viewProjectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val viewMatrixForSkybox = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private var xRotation: Float = 0f
    private var yRotation: Float = 0f
    private val tempMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)

    fun handleTouchDrag(deltaX: Float, deltaY: Float) {
        xRotation += deltaX / 16f
        yRotation += deltaY / 16f
        if (yRotation < -90) {
            yRotation = -90f
        } else if (yRotation > 90) {
            yRotation = 90f
        }
        // Setup view matrix
        updateViewMatrices()
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        GLES30.glEnable(GLES30.GL_CULL_FACE)

        //创建高度
        heightmapProgram = HeightmapShaderProgram(mContext)
        heightmap =
            Heightmap((mContext.resources.getDrawable(R.drawable.heightmap) as BitmapDrawable).bitmap)

        //创建立方体模型
        skyboxProgram = SkyboxShaderProgram(mContext)
        skybox = Skybox()

        //创建烟花
        particleProgram = ParticleShaderProgram(mContext)
        particleSystem = ParticleSystem(3000)
        globalStartTime = System.nanoTime()

        val particleDirection = Geometry.Vector(0f, 0.5f, 0f)

        val angleVarianceInDegrees = 5.0f
        val speedVariance = 1.0f

        redParticleShooter = ParticleShooter(
            Geometry.Point(-1f, 0f, 0f),
            particleDirection,
            Color.rgb(255, 50, 5),
            angleVarianceInDegrees,
            speedVariance
        )
        greenParticleShooter = ParticleShooter(
            Geometry.Point(0f, 0f, 0f),
            particleDirection,
            Color.rgb(25, 255, 25),
            angleVarianceInDegrees,
            speedVariance
        )
        blueParticleShooter = ParticleShooter(
            Geometry.Point(1f, 0f, 0f),
            particleDirection,
            Color.rgb(5, 50, 255),
            angleVarianceInDegrees,
            speedVariance
        )

        texture = TextureHelper.loadText(mContext, R.drawable.particle_texture)

        skyboxTexture = TextureHelper.loadCubeMap(
            mContext, intArrayOf(
                R.drawable.left, R.drawable.right,
                R.drawable.bottom, R.drawable.top,
                R.drawable.front, R.drawable.back
            )
        )
        Log.d(TAG, "onSurfaceCreated texture=$texture  ,skyboxTexture=$skyboxTexture")
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        MatrixHelper.perspectiveM(
            projectionMatrix, 45f, width.toFloat()
                    / height.toFloat(), 1f, 10f
        )
        updateViewMatrices()
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        drawHeightmap()
        drawSkybox()
        drawParticles()
    }

    private fun drawHeightmap() {
        Matrix.setIdentityM(modelMatrix, 0)
        // Expand the heightmap's dimensions, but don't expand the height as
        // much so that we don't get insanely tall mountains.
        Matrix.scaleM(modelMatrix, 0, 100f, 10f, 100f)
        updateMvpMatrix()
        heightmapProgram?.useProgram()
        heightmapProgram?.setUniforms(modelViewProjectionMatrix)
        heightmapProgram?.let { heightmap?.bindData(it) }
        heightmap?.draw()
    }

    private fun updateViewMatrices() {
        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f)
        Matrix.rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f)
        System.arraycopy(viewMatrix, 0, viewMatrixForSkybox, 0, viewMatrix.size)

        // We want the translation to apply to the regular view matrix, and not
        // the skybox.
        Matrix.translateM(viewMatrix, 0, 0f, -1.5f, -5f)
    }


    private fun drawSkybox() {
        Matrix.setIdentityM(modelMatrix, 0)
        updateMvpMatrixForSkybox()
        GLES30.glDepthFunc(GLES30.GL_LEQUAL) // This avoids problems with the skybox itself getting clipped.
        skyboxProgram?.useProgram()
        skyboxProgram?.setUniforms(modelViewProjectionMatrix, skyboxTexture)
        skybox?.bindData(skyboxProgram!!)
        skybox?.draw()
        GLES30.glDepthFunc(GLES30.GL_LESS)
    }

    private fun drawParticles() {
        val currentTime = (System.nanoTime() - globalStartTime) / 1000000000f
        redParticleShooter!!.addParticles(particleSystem!!, currentTime, 1)
        greenParticleShooter!!.addParticles(particleSystem!!, currentTime, 1)
        blueParticleShooter!!.addParticles(particleSystem!!, currentTime, 1)
        Matrix.setIdentityM(modelMatrix, 0)
        updateMvpMatrix()
        GLES30.glDepthMask(false)
        GLES30.glEnable(GLES30.GL_BLEND)
        GLES30.glBlendFunc(GLES30.GL_ONE, GLES30.GL_ONE)
        particleProgram?.useProgram()
        particleProgram?.setUniforms(modelViewProjectionMatrix, currentTime, texture)
        particleSystem?.bindData(particleProgram!!)
        particleSystem?.draw()
        GLES30.glDisable(GLES30.GL_BLEND)
        GLES30.glDepthMask(true)
    }

    private fun updateMvpMatrix() {
        Matrix.multiplyMM(tempMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, tempMatrix, 0)
    }

    private fun updateMvpMatrixForSkybox() {
        Matrix.multiplyMM(tempMatrix, 0, viewMatrixForSkybox, 0, modelMatrix, 0)
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, tempMatrix, 0)
    }

}