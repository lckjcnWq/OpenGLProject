package com.theswitchbot.openglproject.three.programs

import android.content.Context
import android.opengl.GLES30
import com.theswitchbot.openglproject.R

class ParticleShaderProgram(context: Context) : ShaderXProgram(
    context, R.raw.particle_vertex1_shader,
    R.raw.particle_fragment1_shader
) {
    // Uniform locations
    private val uMatrixLocation: Int
    private val uTimeLocation: Int

    // Attribute locations
    val positionAttributeLocation: Int
    val colorAttributeLocation: Int
    val directionVectorAttributeLocation: Int
    val particleStartTimeAttributeLocation: Int
    private val uTextureUnitLocation: Int

    fun setUniforms(matrix: FloatArray, elapsedTime: Float, textureId: Int) {
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        GLES30.glUniform1f(uTimeLocation, elapsedTime)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glUniform1i(uTextureUnitLocation, 0)
    }

    init {
        // Retrieve uniform locations for the shader program.
        uMatrixLocation = GLES30.glGetUniformLocation(program, U_MATRIX)
        uTimeLocation = GLES30.glGetUniformLocation(program, ShaderXProgram.U_TIME)
        uTextureUnitLocation = GLES30.glGetUniformLocation(program, U_TEXTURE_UNIT)

        // Retrieve attribute locations for the shader program.
        positionAttributeLocation = GLES30.glGetAttribLocation(program, A_POSITION)
        colorAttributeLocation = GLES30.glGetAttribLocation(program, A_COLOR)
        directionVectorAttributeLocation =
            GLES30.glGetAttribLocation(program, ShaderXProgram.A_DIRECTION_VECTOR)
        particleStartTimeAttributeLocation =
            GLES30.glGetAttribLocation(program, ShaderXProgram.A_PARTICLE_START_TIME)
    }
}