package com.example.college

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.example.college.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)

        binding.collegeLogo.startAnimation(logoAnimation)
        val splashScreenTimeOut = 4000

        val home = Intent(this, MainActivity::class.java)

        Handler().postDelayed({
            startActivity(home)
            finish()
        }, splashScreenTimeOut.toLong())
    }
}