package com.theswitchbot.openglproject.programs

import android.content.Context
import android.opengl.GLES30
import com.theswitchbot.openglproject.utils.ShaderHelper
import com.theswitchbot.openglproject.utils.TextResourceReader.readTextFileFromResource

/**  1.模型矩阵
 * 模型矩阵是用来把物体放在世界空间（world-space） 坐标系的。比如，我们可能有
冰球模型和木槌模型，它们初始的中心点都在（0, 0, 0） o没有模型矩阵，这些模
型就会卡在那里：如果我们想要移动它们，就不得不自己更新每个模型的每个顶点。
如果不想这样做，我们可以使用一个模型矩阵，把那些顶点与这个矩阵相乘来变换
它们。如果我们想把冰球移动到（5, 5） ,我们只需要准备一个模型矩阵，它会为我
们完成的。
    2.视图矩阵
视图矩阵是出于同模型矩阵一样的原因被使用的，但是它平等地影响场景中的每一
个物体。因为它影响所有的东西，它在功能上等同于一个相机：来回移动相机，你
会从不同的视角看见那些东西。
使用另外一个矩阵的优势是它让我们预先把许多变换处理成单个矩阵。举个例子，
想象一下我们要来回旋转一个场景，并把它移动一定量的距离。能实现这些的一种
方式是把同样的旋转和平移调用应用于每一个单个的物体。尽管那样可行，但如果
只把这些变换存到另外一个矩阵，并把这个矩阵应用于每个物体，会更容易实现

vertex ndc
这是归一化设备坐标系中的一个顶点。一旦一个顶点落在这个坐标中，OpenGL就会
把它映射到视口，你就能从屏幕上看到它了。
这个链条看上去如下所示：
vertex.甲=ProjectionMatrix * vertexcyc
vertexchp = ProjectionMatrix * ViewMatrix * vertexworld
vertexdip = ProjectionMatrix * ViewMatrix * ModelMatrix * vertexmodd
要得到正确的结果，我们需要应用本章中的每个矩阵。
 * */
abstract class ShaderProgram protected constructor(
    context: Context, vertexShaderResourceId: Int,
    fragmentShaderResourceId: Int
) {
    // Shader program
    @JvmField
    protected val program: Int = ShaderHelper.buildProgram(
        readTextFileFromResource(
            context, vertexShaderResourceId
        ),
        readTextFileFromResource(
            context, fragmentShaderResourceId
        )
    )

    fun useProgram() {
        // Set the current OpenGL shader program to this program.
        GLES30.glUseProgram(program)
    }

    companion object {
        // Uniform constants
        const val U_MATRIX = "u_Matrix"
        const val U_COLOR = "u_Color"
        const val U_TEXTURE_UNIT = "u_TextureUnit"

        // Attribute constants
        const val A_POSITION = "a_Position"
        const val A_COLOR = "a_Color"
        const val A_TEXTURE_COORDINATES = "a_TextureCoordinates"
    }
}