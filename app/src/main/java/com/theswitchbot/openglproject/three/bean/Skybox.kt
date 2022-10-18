package com.theswitchbot.openglproject.three.bean

import android.opengl.GLES30
import com.theswitchbot.openglproject.data.VertexArray
import com.theswitchbot.openglproject.three.programs.SkyboxShaderProgram
import java.nio.ByteBuffer

class Skybox {
    private val POSITION_COMPONENT_COUNT = 3
    private var vertexArray: VertexArray? = null
    private var indexArray: ByteBuffer? = null

    constructor() {
        // Create a unit cube.
        vertexArray = VertexArray(floatArrayOf(
                -1f, 1f, 1f,
                1f, 1f, 1f,
                -1f, -1f, 1f,
                1f, -1f, 1f,
                -1f, 1f, -1f,
                1f, 1f, -1f,
                -1f, -1f, -1f,
                1f, -1f, -1f
            )
        )

        // 6 indices per cube side
        indexArray = ByteBuffer.allocateDirect(6 * 6)
            .put(byteArrayOf( // Front
                    1, 3, 0,
                    0, 3, 2,  // Back
                    4, 6, 5,
                    5, 6, 7,  // Left
                    0, 2, 4,
                    4, 2, 6,  // Right
                    5, 7, 1,
                    1, 7, 3,  // Top
                    5, 1, 4,
                    4, 1, 0,  // Bottom
                    6, 2, 7,
                    7, 2, 3
                )
            )
        indexArray?.position(0)
    }

    fun bindData(skyboxProgram: SkyboxShaderProgram) {
        vertexArray?.setVertexAttribPointer(
            0,
            skyboxProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT, 0
        )
    }

    fun draw() {
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 36, GLES30.GL_UNSIGNED_BYTE, indexArray)
    }
}