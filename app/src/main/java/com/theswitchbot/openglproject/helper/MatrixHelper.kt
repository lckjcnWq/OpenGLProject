package com.theswitchbot.openglproject.helper

import kotlin.math.tan

object MatrixHelper {
    /**
     * a         焦距  1/tan(视野/2)  正切函数
     * aspect    屏幕宽高比
     * near      到近处平面的距离
     * far       到远处平面的距离
     * */
    fun perspectiveM(m: FloatArray, yFovInDegrees: Float, aspect: Float, near: Float, far: Float) {
//        Matrix.perspectiveM()
        //1.计算焦距
        val angleInRadians = (yFovInDegrees * Math.PI / 100.0).toFloat()
        val a = (1.0 / tan(angleInRadians / 2.0)).toFloat()
        //2.将参数放到透视投影
        m[0] = a / aspect
        m[1] = 0f
        m[2] = 0f
        m[3] = 0f

        m[4] = 0f
        m[5] = a
        m[6] = 0f
        m[7] = 0f

        m[8] = 0f
        m[9] = 0f
        m[10] = -((far + near) / (far - near))
        m[11] = -1f

        m[12] = 0f
        m[13] = 0f
        m[14] = -(2f * far * near / (far - near))
        m[15] = 0f
    }
}