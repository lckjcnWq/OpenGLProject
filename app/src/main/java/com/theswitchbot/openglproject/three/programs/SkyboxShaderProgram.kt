package com.theswitchbot.openglproject.three.programs

import android.content.Context
import android.opengl.GLES30
import com.theswitchbot.openglproject.R

class SkyboxShaderProgram(context: Context) : ShaderXProgram(
    context,
    R.raw.skybox_vertex1_shader,
    R.raw.skybox_fragment1_shader
) {
    private var uMatrixLocation = 0
    private var uTextureUnitLocation = 0
    private var aPositionLocation = 0

    init {
        uMatrixLocation = GLES30.glGetUniformLocation(program, U_MATRIX)
        uTextureUnitLocation = GLES30.glGetUniformLocation(program, U_TEXTURE_UNIT)
        aPositionLocation = GLES30.glGetAttribLocation(program, A_POSITION)
    }

    fun setUniforms(matrix: FloatArray, textureId: Int) {
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, textureId)
        GLES30.glUniform1i(uTextureUnitLocation, 0)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

}