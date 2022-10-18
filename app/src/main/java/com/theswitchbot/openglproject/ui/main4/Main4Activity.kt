package com.theswitchbot.openglproject.ui.main3

import com.theswitchbot.openglproject.common.BaseActivity
import com.theswitchbot.openglproject.databinding.ActivityMain1Binding
import com.theswitchbot.openglproject.ui.main3.Main3Renderer

class Main4Activity : BaseActivity<ActivityMain1Binding>() {
    override fun setup() {
        setListener()
    }

    private fun setListener(){
        binding.glSurfaceView.setEGLContextClientVersion(3)
        binding.glSurfaceView.setRenderer(Main4Renderer())
    }

}