package com.theswitchbot.openglproject.ui.main9

import android.content.Context
import android.graphics.Color
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import com.theswitchbot.openglproject.R
import com.theswitchbot.openglproject.bean.Geometry
import com.theswitchbot.openglproject.helper.TextureHelper
import com.theswitchbot.openglproject.three.bean.ParticleShooter
import com.theswitchbot.openglproject.three.bean.ParticleSystem
import com.theswitchbot.openglproject.three.bean.Skybox
import com.theswitchbot.openglproject.three.programs.ParticleShaderProgram
import com.theswitchbot.openglproject.three.programs.SkyboxShaderProgram
import com.theswitchbot.openglproject.utils.MatrixHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**效果：背景模型
 * 1.使用立方体贴图定义一个天空盒，用这个方法可以将6个不同的纹理一起拼接到一个立方体上
 * 2.索引数组，减少顶点重复的一个放阿飞
 * 3.如何让粒子更好看
 * */

class Main9Renderer(context: Context) : GLSurfaceView.Renderer {

    private var texture: Int=0
    private var skyboxTexture: Int=0
    private var skyboxProgram: SkyboxShaderProgram?=null
    private var skybox: Skybox?=null
    private var redParticleShooter: ParticleShooter?=null
    private var blueParticleShooter: ParticleShooter?=null
    private var greenParticleShooter: ParticleShooter?=null
    private var globalStartTime: Long = 0
    private var particleSystem: ParticleSystem?=null
    private var particleProgram: ParticleShaderProgram?=null
    private val TAG="Main8Renderer"
    private val mContext = context
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)
    private var xRotation:Float = 0f
    private var yRotation:Float = 0f

    fun handleTouchDrag(deltaX: Float, deltaY: Float) {
        xRotation += deltaX / 16f
        yRotation += deltaY / 16f
        if (yRotation < -90) {
            yRotation = -90f
        } else if (yRotation > 90) {
            yRotation = 90f
        }
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        //创建立方体模型
        skyboxProgram = SkyboxShaderProgram(mContext)
        skybox = Skybox()

        //创建烟花
        particleProgram =  ParticleShaderProgram(mContext)
        particleSystem = ParticleSystem(3000)
        globalStartTime = System.nanoTime()
        
        val particleDirection = Geometry.Vector(0f,0.5f,0f)

        val angleVarianceInDegrees = 5.0f
        val speedVariance = 1.0f

        redParticleShooter = ParticleShooter(Geometry.Point(-1f,0f,0f),particleDirection, Color.rgb(255,50,5),angleVarianceInDegrees,speedVariance)
        greenParticleShooter = ParticleShooter(Geometry.Point(0f,0f,0f),particleDirection, Color.rgb(25,255,25),angleVarianceInDegrees,speedVariance)
        blueParticleShooter = ParticleShooter(Geometry.Point(1f,0f,0f),particleDirection, Color.rgb(5,50,255),angleVarianceInDegrees,speedVariance)

        texture = TextureHelper.loadText(mContext, R.drawable.particle_texture)

        skyboxTexture = TextureHelper.loadCubeMap(mContext, intArrayOf(
                R.drawable.left, R.drawable.right,
                R.drawable.bottom, R.drawable.top,
                R.drawable.front, R.drawable.back
            )
        )
        Log.d(TAG,"onSurfaceCreated texture=$texture  ,skyboxTexture=$skyboxTexture")
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        MatrixHelper.perspectiveM(
            projectionMatrix, 45f, width.toFloat()
                    / height.toFloat() , 1f, 10f
        )
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        drawSkybox()
        drawParticles()
    }

    private fun drawSkybox() {
        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f)
        Matrix.rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f)
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        skyboxProgram?.useProgram()
        skyboxProgram?.setUniforms(viewProjectionMatrix, skyboxTexture)
        skybox?.bindData(skyboxProgram!!)
        skybox?.draw()
    }

    private fun drawParticles() {
        val currentTime = (System.nanoTime() - globalStartTime) / 1000000000f
        redParticleShooter?.addParticles(particleSystem!!, currentTime, 1)
        greenParticleShooter?.addParticles(particleSystem!!, currentTime, 1)
        blueParticleShooter?.addParticles(particleSystem!!, currentTime, 1)
        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f)
        Matrix.rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f)
        Matrix.translateM(viewMatrix, 0, 0f, -1.5f, -5f)
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        GLES30.glEnable(GLES30.GL_BLEND)
        GLES30.glBlendFunc(GLES30.GL_ONE, GLES30.GL_ONE)
        particleProgram?.useProgram()
        particleProgram?.setUniforms(viewProjectionMatrix, currentTime, texture)
        particleSystem?.bindData(particleProgram!!)
        particleSystem?.draw()
        GLES30.glDisable(GLES30.GL_BLEND)
    }

}