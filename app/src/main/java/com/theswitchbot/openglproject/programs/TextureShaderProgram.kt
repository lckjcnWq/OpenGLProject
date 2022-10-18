package com.theswitchbot.openglproject.programs

import android.content.Context
import android.opengl.GLES30
import com.theswitchbot.openglproject.R

class TextureShaderProgram(context: Context) : ShaderProgram(
    context, R.raw.texture_vertex2_shader,
    R.raw.texture_fragment2_shader
) {
    // Uniform locations
    private val uMatrixLocation: Int
    private val uTextureUnitLocation: Int

    // Attribute locations
    val positionAttributeLocation: Int
    val textureCoordinatesAttributeLocation: Int

    /**
     * 1.将活动的纹理单元设置为纹理单元0
     * 2.将纹理绑定到此单元
     * 3.将纹理单元0传递给片段着色器中的u_TextureUnit
     * */
    fun setUniforms(matrix: FloatArray, textureId: Int) {
        // Pass the matrix into the shader program.
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)

        // Set the active texture unit to texture unit 0.
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)

        // Bind the texture to this unit.
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        GLES30.glUniform1i(uTextureUnitLocation, 0)
    }

    init {
        // Retrieve uniform locations for the shader program.
        uMatrixLocation = GLES30.glGetUniformLocation(program, U_MATRIX)
        uTextureUnitLocation = GLES30.glGetUniformLocation(program, U_TEXTURE_UNIT)

        // Retrieve attribute locations for the shader program.
        positionAttributeLocation = GLES30.glGetAttribLocation(program, A_POSITION)
        textureCoordinatesAttributeLocation =
            GLES30.glGetAttribLocation(program, A_TEXTURE_COORDINATES)
    }
}