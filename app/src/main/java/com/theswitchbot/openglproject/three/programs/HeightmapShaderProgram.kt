package com.theswitchbot.openglproject.three.programs

import android.content.Context
import android.opengl.GLES30
import com.theswitchbot.openglproject.R

class HeightmapShaderProgram(context: Context):ShaderXProgram(
    context, R.raw.heightmap_vertex_shader,
    R.raw.heightmap_fragment_shader){

    private var uMatrixLocation = 0
    private var aPositionLocation = 0

    init {
        uMatrixLocation = GLES30.glGetUniformLocation(program, U_MATRIX)
        aPositionLocation = GLES30.glGetAttribLocation(program, A_POSITION)
    }

    fun setUniforms(matrix: FloatArray) {
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }
}