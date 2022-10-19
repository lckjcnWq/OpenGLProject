package com.theswitchbot.openglproject.data

import android.opengl.GLES30
import com.theswitchbot.openglproject.constant.Constants
import java.nio.ByteBuffer
import java.nio.ByteOrder

class IndexBuffer {
    private var bufferId = 0

    constructor(indexData: ShortArray) {
        // Allocate a buffer.
        val buffers = IntArray(1)
        GLES30.glGenBuffers(buffers.size, buffers, 0)
        if (buffers[0] == 0) {
            throw RuntimeException("Could not create a new index buffer object.")
        }
        bufferId = buffers[0]

        // Bind to the buffer. 
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, buffers[0])

        // Transfer data to native memory.
        val indexArray = ByteBuffer
            .allocateDirect(indexData.size * Constants.BYTES_PER_SHORT)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(indexData)
        indexArray.position(0)

        // Transfer data from native memory to the GPU buffer.        
        GLES30.glBufferData(
            GLES30.GL_ELEMENT_ARRAY_BUFFER, indexArray.capacity() * Constants.BYTES_PER_SHORT,
            indexArray, GLES30.GL_STATIC_DRAW
        )

        // IMPORTANT: Unbind from the buffer when we're done with it.
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0)

        // We let the native buffer go out of scope, but it won't be released
        // until the next time the garbage collector is run.
    }

    fun getBufferId(): Int {
        return bufferId
    }
}