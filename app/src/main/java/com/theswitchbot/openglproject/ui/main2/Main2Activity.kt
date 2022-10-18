package com.theswitchbot.openglproject.ui.main2

import com.theswitchbot.openglproject.common.BaseActivity
import com.theswitchbot.openglproject.databinding.ActivityMain1Binding

class Main2Activity : BaseActivity<ActivityMain1Binding>() {
    override fun setup() {
        setListener()
    }

    private fun setListener(){
        binding.glSurfaceView.setEGLContextClientVersion(3)
        binding.glSurfaceView.setRenderer(Main2Renderer())
    }

}