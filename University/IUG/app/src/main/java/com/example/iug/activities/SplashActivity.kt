package com.example.iug.activities

import android.content.Intent
import android.os.Bundle
import com.example.iug.databinding.ActivitySplashBinding
import android.view.animation.AnimationUtils
import com.example.iug.R
import android.os.Handler

class SplashActivity : BaseActivity()
{
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dismissActionBar()

        val logoAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_logo_animation)

        binding.storeLogo.startAnimation(logoAnimation)

        val home = Intent(this, LoginActivity::class.java)

        @Suppress("DEPRECATION")
        Handler().postDelayed(
            {
                startActivity(home)
                finish()
            }
            , 4000
        )
    }
}