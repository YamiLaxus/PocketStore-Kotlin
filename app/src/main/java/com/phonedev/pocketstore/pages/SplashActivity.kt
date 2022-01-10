package com.phonedev.pocketstore.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.phonedev.pocketstore.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        startTimer()
    }

    private fun startTimer() {
        object : CountDownTimer(2000, 500) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                val intent = Intent(applicationContext, StartActivityUno::class.java).apply {  }
                startActivity(intent)
                finish()
            }
        }.start()
    }
}