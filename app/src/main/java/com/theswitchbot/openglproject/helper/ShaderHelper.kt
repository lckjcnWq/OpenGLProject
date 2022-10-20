package com.theswitchbot.openglproject.helper

import android.opengl.GLES20
import android.opengl.GLES30
import android.util.Log

object ShaderHelper {
    private const val TAG = "ShaderHelper"
    fun compileVertexShader(shaderCode: String): Int {
        return compileShader(GLES30.GL_VERTEX_SHADER, shaderCode)
    }

    fun compileFragmentShader(shaderCode: String): Int {
        return compileShader(GLES30.GL_FRAGMENT_SHADER, shaderCode)
    }

    /**
     * 获取编译着色器id
     * */
    private fun compileShader(type: Int, shaderCode: String): Int {
        //1.创建shader对象
        val shaderObjectId = GLES30.glCreateShader(type)
        if (shaderObjectId == 0) {
            Log.d(TAG, "Could not create new shader")
            return 0
        }
        //2.上传和编译着色器源码
        GLES30.glShaderSource(shaderObjectId, shaderCode)
        GLES30.glCompileShader(shaderObjectId)

        //3.获取编译状态
        val compileStatus = IntArray(1)
        GLES30.glGetShaderiv(shaderObjectId, GLES30.GL_COMPILE_STATUS, compileStatus, 0)
        Log.d(TAG, "compile of shader state=${compileStatus[0]}   ,log=${GLES30.glGetShaderInfoLog(shaderObjectId)}")
        if (compileStatus[0] == 0) {
            GLES30.glDeleteShader(shaderObjectId)
            return 0
        }
        return shaderObjectId
    }

    /**
     * 链接Program
     * */
    fun linkProgram(vertexShader: Int, fragmentShaderId: Int): Int {
        //1.新建程序对象
        val programObjectId = GLES30.glCreateProgram()
        if (programObjectId == 0) {
            Log.d(TAG, "Could not create new program")
            return 0
        }
        //2.将顶点着色器和片元着色器附加在程序对象上
        GLES30.glAttachShader(programObjectId, vertexShader)
        GLES30.glAttachShader(programObjectId, fragmentShaderId)

        //3.获取编译状态
        GLES30.glLinkProgram(programObjectId)
        val linkStatus = IntArray(1)
        GLES30.glGetProgramiv(programObjectId, GLES30.GL_LINK_STATUS, linkStatus, 0)
        Log.d(TAG, "link of shader state=${linkStatus[0]}   ,log=${GLES30.glGetProgramInfoLog(programObjectId)}")
        if (linkStatus[0] == 0) {
            GLES30.glDeleteProgram(programObjectId)
            return 0
        }
        return programObjectId
    }

    /**
     * 检测Program有效性
     * */
    fun validateProgram(programObjectId: Int): Boolean {
        GLES30.glValidateProgram(programObjectId)
        val validateStatus = IntArray(1)
        GLES30.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0)
        Log.d(TAG, "Results of program source :${validateStatus[0]} ,log= ${GLES30.glGetProgramInfoLog(programObjectId)}")
        return validateStatus[0] != 0
    }

    /**
     * Helper function that compiles the shaders, links and validates the
     * program, returning the program ID.
     */
    fun buildProgram(
        vertexShaderSource: String,
        fragmentShaderSource: String
    ): Int {
        // Compile the shaders.
        val vertexShader = compileVertexShader(vertexShaderSource)
        val fragmentShader = compileFragmentShader(fragmentShaderSource)

        // Link them into a shader program.
        val program = linkProgram(vertexShader, fragmentShader)
        validateProgram(program)
        return program
    }
}