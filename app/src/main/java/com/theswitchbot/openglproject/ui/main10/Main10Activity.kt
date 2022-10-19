package com.theswitchbot.openglproject.ui.main10

import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.theswitchbot.openglproject.common.BaseActivity
import com.theswitchbot.openglproject.databinding.ActivityMain1Binding
import com.theswitchbot.openglproject.ui.main9.Main9Renderer

class Main10Activity : BaseActivity<ActivityMain1Binding>() {
    override fun setup() {
        setListener()
    }

    private fun setListener(){
        binding.glSurfaceView.setEGLContextClientVersion(3)
        val renderer = Main9Renderer(this)
        binding.glSurfaceView.setRenderer(renderer)
        binding.glSurfaceView.setOnTouchListener(object : OnTouchListener {
            var previousX = 0f
            var previousY = 0f
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                return if (event != null) {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        previousX = event.x
                        previousY = event.y
                    } else if (event.action == MotionEvent.ACTION_MOVE) {
                        val deltaX = event.x - previousX
                        val deltaY = event.y - previousY
                        previousX = event.x
                        previousY = event.y
                        binding.glSurfaceView.queueEvent(Runnable {
                            renderer.handleTouchDrag(
                                deltaX, deltaY
                            )
                        })
                    }
                    true
                } else {
                    false
                }
            }
        })
    }

}