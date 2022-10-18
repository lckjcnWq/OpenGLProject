package com.theswitchbot.openglproject.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.theswitchbot.openglproject.utils.ViewBindingUtils


abstract class BaseActivity<T : ViewBinding> : AppCompatActivity(){
    lateinit var binding: T
    private val TAG = BaseActivity::class.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding()?.apply {
            binding = this
            setContentView(root)
        }
        setup()
    }


    protected open fun binding(): T? {
        return ViewBindingUtils.binding(this, layoutInflater, null, false)
    }

    protected abstract fun setup()

}