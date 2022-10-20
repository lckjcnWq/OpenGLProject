package com.theswitchbot.openglproject

import android.content.Intent
import com.theswitchbot.openglproject.common.BaseActivity
import com.theswitchbot.openglproject.databinding.ActivityMainBinding
import com.theswitchbot.openglproject.ui.main1.Main1Activity
import com.theswitchbot.openglproject.ui.main10.Main10Activity
import com.theswitchbot.openglproject.ui.main2.Main2Activity
import com.theswitchbot.openglproject.ui.main3.Main3Activity
import com.theswitchbot.openglproject.ui.main3.Main4Activity
import com.theswitchbot.openglproject.ui.main5.Main5Activity
import com.theswitchbot.openglproject.ui.main6.Main6Activity
import com.theswitchbot.openglproject.ui.main7.Main7Activity
import com.theswitchbot.openglproject.ui.main8.Main8Activity
import com.theswitchbot.openglproject.ui.main9.Main9Activity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun setup() {
        setListener()
    }

    private fun setListener(){
        binding.btnStart1.setOnClickListener {
            startActivity(Intent(this, Main1Activity::class.java))
        }
        binding.btnStart2.setOnClickListener {
            startActivity(Intent(this, Main2Activity::class.java))
        }
        binding.btnStart3.setOnClickListener {
            startActivity(Intent(this, Main3Activity::class.java))
        }
        binding.btnStart4.setOnClickListener {
            startActivity(Intent(this, Main4Activity::class.java))
        }
        binding.btnStart5.setOnClickListener {
            startActivity(Intent(this, Main5Activity::class.java))
        }
        binding.btnStart6.setOnClickListener {
            startActivity(Intent(this, Main6Activity::class.java))
        }
        binding.btnStart7.setOnClickListener {
            startActivity(Intent(this, Main7Activity::class.java))
        }
        binding.btnStart8.setOnClickListener {
            startActivity(Intent(this, Main8Activity::class.java))
        }
        binding.btnStart9.setOnClickListener {
            startActivity(Intent(this, Main9Activity::class.java))
        }
        binding.btnStart10.setOnClickListener {
            startActivity(Intent(this, Main10Activity::class.java))
        }
    }

}