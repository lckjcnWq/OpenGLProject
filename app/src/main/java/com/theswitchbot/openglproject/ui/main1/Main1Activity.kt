package com.theswitchbot.openglproject.ui.main1

import com.theswitchbot.openglproject.common.BaseActivity
import com.theswitchbot.openglproject.databinding.ActivityMain1Binding
import com.theswitchbot.openglproject.databinding.ActivityMainBinding

class Main1Activity : BaseActivity<ActivityMain1Binding>() {
    override fun setup() {
        setListener()
    }

    private fun setListener(){
        binding.glSurfaceView.setEGLContextClientVersion(3)
        binding.glSurfaceView.setRenderer(Main1Renderer())
    }

}