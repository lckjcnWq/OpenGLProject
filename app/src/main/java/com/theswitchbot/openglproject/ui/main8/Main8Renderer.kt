package com.theswitchbot.openglproject.ui.main8

import android.content.Context
import android.graphics.Color
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.theswitchbot.openglproject.R
import com.theswitchbot.openglproject.bean.Geometry
import com.theswitchbot.openglproject.helper.TextureHelper
import com.theswitchbot.openglproject.three.bean.ParticleShooter
import com.theswitchbot.openglproject.three.bean.ParticleSystem
import com.theswitchbot.openglproject.three.programs.ParticleShaderProgram
import com.theswitchbot.openglproject.helper.MatrixHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 1.建立一个粒子系统需要什么
 * 2.添加一些喷泉，让它们向空中喷射一些粒子
 * 3.如何让粒子更好看
 * */

class Main8Renderer(context: Context) : GLSurfaceView.Renderer {

    private var texture: Int=0
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

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        particleProgram =  ParticleShaderProgram(mContext)
        particleSystem = ParticleSystem(3000)
        globalStartTime = System.nanoTime()
        
        val particleDirection = Geometry.Vector(0f,0.8f,0f)
        
        redParticleShooter = ParticleShooter(Geometry.Point(-1f,0f,0f),particleDirection, Color.rgb(255,50,5),20f,1.0f)
        greenParticleShooter = ParticleShooter(Geometry.Point(0f,0f,0f),particleDirection, Color.rgb(25,255,25),20f,1.0f)
        blueParticleShooter = ParticleShooter(Geometry.Point(1f,0f,0f),particleDirection, Color.rgb(5,50,255),20f,1.0f)

        texture = TextureHelper.loadText(mContext, R.drawable.particle_texture)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        MatrixHelper.perspectiveM(
            projectionMatrix, 45f, width.toFloat()
                    / height.toFloat() , 1f, 10f
        )
        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.translateM(viewMatrix, 0, 0f, -1.5f, -5f)
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        val currentTime = (System.nanoTime() - globalStartTime) / 1000000000f

        redParticleShooter?.addParticles(particleSystem!!, currentTime, 5)
        greenParticleShooter?.addParticles(particleSystem!!, currentTime, 5)
        blueParticleShooter?.addParticles(particleSystem!!, currentTime, 5)

        particleProgram?.useProgram()
        particleProgram?.setUniforms(viewProjectionMatrix, currentTime, texture)
        particleSystem?.bindData(particleProgram!!)
        particleSystem?.draw()
    }

}