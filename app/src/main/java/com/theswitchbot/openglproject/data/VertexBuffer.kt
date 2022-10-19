package com.theswitchbot.openglproject.data

import android.opengl.GLES30
import com.theswitchbot.openglproject.constant.Constants
import java.nio.ByteBuffer
import java.nio.ByteOrder

class VertexBuffer {
    private var bufferId: Int = 0

    constructor(vertexData:FloatArray){
        // Allocate a buffer.
        val buffers = IntArray(1)
        GLES30.glGenBuffers(buffers.size, buffers, 0)
        if (buffers[0] == 0) {
            throw RuntimeException("Could not create a new vertex buffer object.")
        }
        bufferId = buffers[0]

        // Bind to the buffer. 
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, buffers[0])

        // Transfer data to native memory.        
        val vertexArray = ByteBuffer
            .allocateDirect(vertexData.size * Constants.BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData)
        vertexArray.position(0)


        // Transfer data from native memory to the GPU buffer.              
        GLES30.glBufferData(
            GLES30.GL_ARRAY_BUFFER, vertexArray.capacity() * Constants.BYTES_PER_FLOAT,
            vertexArray, GLES30.GL_STATIC_DRAW
        )

        // IMPORTANT: Unbind from the buffer when we're done with it.
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
        // We let vertexArray go out of scope, but it won't be released
        // until the next time the garbage collector is run.
    }

    fun setVertexAttribPointer(
        dataOffset: Int, attributeLocation: Int,
        componentCount: Int, stride: Int
    ) {
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, bufferId)
        // This call is slightly different than the glVertexAttribPointer we've
        // used in the past: the last parameter is set to dataOffset, to tell OpenGL
        // to begin reading data at this position of the currently bound buffer.
        GLES30.glVertexAttribPointer(attributeLocation, componentCount, GLES30.GL_FLOAT, false, stride, dataOffset)
        GLES30.glEnableVertexAttribArray(attributeLocation)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
    }
}