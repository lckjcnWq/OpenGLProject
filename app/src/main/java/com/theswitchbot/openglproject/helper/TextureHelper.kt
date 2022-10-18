package com.theswitchbot.openglproject.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLUtils
import android.util.Log

object TextureHelper {
    private val TAG = "TextureHelper"

    /**
     * Loads a texture from a resource ID, returning the OpenGL ID for that
     * texture. Returns 0 if the load failed.
     *
     * @param context
     * @param resourceId
     * @return
     */
    fun loadText(context: Context, resourceId: Int): Int {
        val textureObjectIds = IntArray(1)
        GLES30.glGenTextures(1, textureObjectIds, 0)
        if (textureObjectIds[0] == 0) {
            Log.d(TAG, "could not generate a new OpenGL texture object")
            return 0
        }

        val options = BitmapFactory.Options()
        options.inScaled = false

        val bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)
        if (bitmap == null) {
            Log.d(TAG, "Resource ID $resourceId  could not be decoded.")
            GLES30.glDeleteTextures(1, textureObjectIds, 0)
            return 0
        }
        //二维纹理对待
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureObjectIds[0])
        //缩小使用三线性过滤
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_LINEAR_MIPMAP_LINEAR)
        //放大使用双线性过滤
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR)

        //加载纹理到openGL
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D,0,bitmap,0)
        //生成位图
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D)
        bitmap.recycle()

        //解除绑定
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,0)
        return textureObjectIds[0]
    }

    fun loadCubeMap(context: Context, cubeResources: IntArray): Int {
        val textureObjectIds = IntArray(1)
        GLES30.glGenTextures(1, textureObjectIds, 0)
        if (textureObjectIds[0] == 0) {
            Log.w(TAG, "Could not generate a new OpenGL texture object.")
            return 0
        }
        val options = BitmapFactory.Options()
        options.inScaled = false
        val cubeBitmaps = arrayOfNulls<Bitmap>(6)
        for (i in 0..5) {
            cubeBitmaps[i] = BitmapFactory.decodeResource(
                context.resources,
                cubeResources[i], options
            )
            if (cubeBitmaps[i] == null) {
                Log.w(
                    TAG, "Resource ID " + cubeResources[i]
                            + " could not be decoded."
                )
                GLES30.glDeleteTextures(1, textureObjectIds, 0)
                return 0
            }
        }
        // Linear filtering for minification and magnification
        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, textureObjectIds[0])
        //防止变形
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
        //左右下上前后传递立方体的面
        //立方体贴图的惯例：在立方体内部使用左手坐标系统，而在立方体外部使用右手坐标系统
        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmaps[0], 0)
        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmaps[1], 0)
        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmaps[2], 0)
        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmaps[3], 0)
        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmaps[4], 0)
        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmaps[5], 0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
        for (bitmap in cubeBitmaps) {
            bitmap?.recycle()
        }
        return textureObjectIds[0]
    }
}