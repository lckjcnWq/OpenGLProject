package com.theswitchbot.openglproject.three.bean

import android.opengl.Matrix
import com.theswitchbot.openglproject.bean.Geometry
import java.util.*

class ParticleShooter {
    private var position: Geometry.Point? = null
    private var direction: Geometry.Vector? = null
    private var color = 0

    private var angleVariance = 0f
    private var speedVariance = 0f

    private val random = Random()

    private val rotationMatrix = FloatArray(16)
    private val directionVector = FloatArray(4)
    private val resultVector = FloatArray(4)

    constructor(
        position: Geometry.Point, direction: Geometry.Vector, color: Int,
        angleVarianceInDegrees: Float, speedVariance: Float
    ) {
        this.position = position
        this.direction = direction
        this.color = color
        this.angleVariance = angleVarianceInDegrees
        this.speedVariance = speedVariance
        directionVector[0] = direction.x
        directionVector[1] = direction.y
        directionVector[2] = direction.z
    }

    fun addParticles(
        particleSystem: ParticleSystem, currentTime: Float,
        count: Int
    ) {
        for (i in 0 until count) {
            Matrix.setRotateEulerM(
                rotationMatrix, 0,
                (random.nextFloat() - 0.5f) * angleVariance,
                (random.nextFloat() - 0.5f) * angleVariance,
                (random.nextFloat() - 0.5f) * angleVariance
            )
            Matrix.multiplyMV(
                resultVector, 0,
                rotationMatrix, 0,
                directionVector, 0
            )
            val speedAdjustment = 1f + random.nextFloat() * speedVariance
            val thisDirection = Geometry.Vector(
                resultVector[0] * speedAdjustment,
                resultVector[1] * speedAdjustment,
                resultVector[2] * speedAdjustment
            )
            position?.let { particleSystem.addParticle(it, color, thisDirection, currentTime) }
        }
    }
}