package com.example.revarioapp

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private var progressStatus = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        progressBar = findViewById(R.id.progressBar)

        // Ubah warna progress bar menjadi biru
        progressBar.progressDrawable.setColorFilter(
            resources.getColor(android.R.color.holo_blue_light, theme),
            PorterDuff.Mode.SRC_IN
        )

        // Jalankan animasi progress bar
        startProgressAnimation()
    }

    private fun startProgressAnimation() {
        Thread {
            while (progressStatus < 100) {
                progressStatus += 2 // Menambah progress secara bertahap
                handler.post {
                    progressBar.progress = progressStatus
                }
                try {
                    Thread.sleep(60) // Delay agar animasi smooth (total ± 3 detik)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            // Cek status login
            val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

            if (isLoggedIn) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }.start()
    }
}
