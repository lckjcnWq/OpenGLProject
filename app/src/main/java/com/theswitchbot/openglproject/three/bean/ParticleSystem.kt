package com.theswitchbot.openglproject.three.bean

import android.graphics.Color
import android.opengl.GLES20
import com.theswitchbot.openglproject.bean.Geometry
import com.theswitchbot.openglproject.constant.Constants
import com.theswitchbot.openglproject.data.VertexArray
import com.theswitchbot.openglproject.three.programs.ParticleShaderProgram

class ParticleSystem(private var maxParticleCount: Int) {
    private val POSITION_COMPONENT_COUNT = 3
    private val COLOR_COMPONENT_COUNT = 3
    private val VECTOR_COMPONENT_COUNT = 3
    private val PARTICLE_START_TIME_COMPONENT_COUNT = 1

    private val TOTAL_COMPONENT_COUNT = (POSITION_COMPONENT_COUNT
            + COLOR_COMPONENT_COUNT
            + VECTOR_COMPONENT_COUNT
            + PARTICLE_START_TIME_COMPONENT_COUNT)

    private val STRIDE = TOTAL_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT

    private var particles: FloatArray = FloatArray(0)
    private var vertexArray: VertexArray? = null

    private var currentParticleCount = 0
    private var nextParticle = 0

    init {
        particles = FloatArray(maxParticleCount * TOTAL_COMPONENT_COUNT)
        vertexArray = VertexArray(particles)
    }

    fun addParticle(
        position: Geometry.Point, color: Int, direction: Geometry.Vector,
        particleStartTime: Float
    ) {
        val particleOffset = nextParticle * TOTAL_COMPONENT_COUNT
        var currentOffset = particleOffset
        nextParticle++
        if (currentParticleCount < maxParticleCount) {
            currentParticleCount++
        }
        if (nextParticle == maxParticleCount) {
            // Start over at the beginning, but keep currentParticleCount so
            // that all the other particles still get drawn.
            nextParticle = 0
        }
        particles[currentOffset++] = position.x
        particles[currentOffset++] = position.y
        particles[currentOffset++] = position.z

        particles[currentOffset++] = Color.red(color) / 255f
        particles[currentOffset++] = Color.green(color) / 255f
        particles[currentOffset++] = Color.blue(color) / 255f

        particles[currentOffset++] = direction.x
        particles[currentOffset++] = direction.y
        particles[currentOffset++] = direction.z

        particles[currentOffset++] = particleStartTime
        vertexArray?.updateBuffer(particles, particleOffset, TOTAL_COMPONENT_COUNT)
    }

    fun bindData(particleProgram: ParticleShaderProgram) {
        var dataOffset = 0
        vertexArray?.setVertexAttribPointer(
            dataOffset,
            particleProgram.positionAttributeLocation,
            POSITION_COMPONENT_COUNT, STRIDE
        )
        dataOffset += POSITION_COMPONENT_COUNT
        vertexArray?.setVertexAttribPointer(
            dataOffset,
            particleProgram.colorAttributeLocation,
            COLOR_COMPONENT_COUNT, STRIDE
        )
        dataOffset += COLOR_COMPONENT_COUNT
        vertexArray?.setVertexAttribPointer(
            dataOffset,
            particleProgram.directionVectorAttributeLocation,
            VECTOR_COMPONENT_COUNT, STRIDE
        )
        dataOffset += VECTOR_COMPONENT_COUNT
        vertexArray?.setVertexAttribPointer(
            dataOffset,
            particleProgram.particleStartTimeAttributeLocation,
            PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE
        )
    }

    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, currentParticleCount)
    }
}