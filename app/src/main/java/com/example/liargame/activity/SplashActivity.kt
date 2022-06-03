package com.example.liargame.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import com.example.liargame.R

class SplashActivity : AppCompatActivity() {

    private var clickableStartButton : Boolean = false
    private lateinit var startButton : Button

    // 다른 액티비티에서도 사용할 수 있도록 하는 object
    companion object {
        var WRITER : String = "이아영"
    }

    /**
     * onCreate()
     *
     * Setting it's own Activity Data
     *
     * TODO
     * 1. lateinit value binding
     * 2. button animation task
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startButton = findViewById<Button>(R.id.activity_splash_start_button)

        Handler(Looper.getMainLooper()).post {
            startButton.animate().alpha(1f).setDuration(30000).start()
        }
    }

    /**
     * onStart()
     *
     * Write up initial logics and settings of this Activity
     *
     * TODO
     * 1. writer textview text setting
     */
    override fun onStart() {
        super.onStart()
        findViewById<TextView>(R.id.activity_splash_writer).text = "Made By $WRITER"
    }

    /**
     * onResume()
     *
     * Write up logics of this Activity
     * When User returns this Activity, this lifecycle will be called
     *
     * TODO
     * 1. button clickable flag binding
     * 2. click event regist
     */
    override fun onResume() {
        super.onResume()

        if (startButton.alpha >= 1f) {
            clickableStartButton = true
        }

        if (clickableStartButton) {
            startButton.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}