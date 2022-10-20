package com.theswitchbot.openglproject.three.bean

import android.graphics.Bitmap
import android.graphics.Color
import android.opengl.GLES30
import com.theswitchbot.openglproject.data.IndexBuffer
import com.theswitchbot.openglproject.data.VertexBuffer
import com.theswitchbot.openglproject.three.programs.HeightmapShaderProgram

class Heightmap {
    private var vertexBuffer: VertexBuffer?=null
    private var indexBuffer: IndexBuffer?=null
    private val POSITION_COMPONENT_COUNT = 3
    private var width:Int = 0
    private var height:Int = 0
    private var numElements:Int = 0

    constructor(bitmap:Bitmap){
        width = bitmap.width
        height = bitmap.height
        if (width * height > 65536) {
            throw RuntimeException("Heightmap is too large for the index buffer.")
        }
        numElements = calculateNumElements()
        vertexBuffer = VertexBuffer(loadBitmapData(bitmap))
        indexBuffer = IndexBuffer(createIndexData())
    }


    /**
     * Create an index buffer object for the vertices to wrap them together into
     * triangles, creating indices based on the width and height of the
     * heightmap.
     */
    private fun createIndexData(): ShortArray {
        val indexData = ShortArray(numElements)
        var offset = 0
        for (row in 0 until height - 1) {
            for (col in 0 until width - 1) {
                // Note: The (short) cast will end up underflowing the number
                // into the negative range if it doesn't fit, which gives us the
                // right unsigned number for OpenGL due to two's complement.
                // This will work so long as the heightmap contains 65536 pixels
                // or less.
                val topLeftIndexNum = (row * width + col).toShort()
                val topRightIndexNum = (row * width + col + 1).toShort()
                val bottomLeftIndexNum = ((row + 1) * width + col).toShort()
                val bottomRightIndexNum = ((row + 1) * width + col + 1).toShort()

                // Write out two triangles.
                indexData[offset++] = topLeftIndexNum
                indexData[offset++] = bottomLeftIndexNum
                indexData[offset++] = topRightIndexNum
                indexData[offset++] = topRightIndexNum
                indexData[offset++] = bottomLeftIndexNum
                indexData[offset++] = bottomRightIndexNum
            }
        }
        return indexData
    }

    /**
     * Copy the heightmap data into a vertex buffer object.
     */
    private fun loadBitmapData(bitmap: Bitmap): FloatArray {
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        bitmap.recycle()
        val heightmapVertices = FloatArray(width * height * POSITION_COMPONENT_COUNT)
        var offset = 0
        for (row in 0 until height) {
            for (col in 0 until width) {
                // The heightmap will lie flat on the XZ plane and centered
                // around (0, 0), with the bitmap width mapped to X and the
                // bitmap height mapped to Z, and Y representing the height. We
                // assume the heightmap is grayscale, and use the value of the
                // red color to determine the height.
                val xPosition = col.toFloat() / (width - 1).toFloat() - 0.5f
                val yPosition = Color.red(pixels[row * height + col])
                    .toFloat() / 255.toFloat()
                val zPosition = row.toFloat() / (height - 1).toFloat() - 0.5f
                heightmapVertices[offset++] = xPosition
                heightmapVertices[offset++] = yPosition
                heightmapVertices[offset++] = zPosition
            }
        }
        return heightmapVertices
    }

    private fun calculateNumElements(): Int {
        // There should be 2 triangles for every group of 4 vertices, so a
        // heightmap of, say, 10x10 pixels would have 9x9 groups, with 2
        // triangles per group and 3 vertices per triangle for a total of (9 x 9
        // x 2 x 3) indices.
        return (width - 1) * (height - 1) * 2 * 3
    }

    fun bindData(heightmapProgram: HeightmapShaderProgram) {
        vertexBuffer!!.setVertexAttribPointer(
            0,
            heightmapProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT, 0
        )
    }

    fun draw() {
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, indexBuffer!!.getBufferId())
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, numElements, GLES30.GL_UNSIGNED_SHORT, 0)
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0)
    }
}