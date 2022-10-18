package com.theswitchbot.openglproject.programs

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES30
import com.theswitchbot.openglproject.R

class ColorShaderProgram(context: Context) : ShaderProgram(
    context, R.raw.simple_vertex8_shader,
    R.raw.simple_fragment8_shader
) {
    // Uniform locations
    private val uMatrixLocation: Int
    private val uColorLocation :Int
    // Attribute locations
    val positionAttributeLocation: Int
    val colorAttributeLocation: Int
    fun setUniforms(matrix: FloatArray) {
        // Pass the matrix into the shader program.
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }

    fun setUniforms(matrix: FloatArray, r: Float, g: Float, b: Float) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        GLES20.glUniform4f(uColorLocation, r, g, b, 1f)
    }

    init {
        // Retrieve uniform locations for the shader program.
        uMatrixLocation = GLES30.glGetUniformLocation(program, U_MATRIX)
        uColorLocation = GLES30.glGetUniformLocation(program, U_COLOR)
        // Retrieve attribute locations for the shader program.
        positionAttributeLocation = GLES30.glGetAttribLocation(program, A_POSITION)
        colorAttributeLocation = GLES30.glGetAttribLocation(program, A_COLOR)
    }
}