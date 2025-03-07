package com.example.revarioapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Tombol Kalkulator
        val calculatorButton = findViewById<LinearLayout>(R.id.calculator_button1)
        calculatorButton.setOnClickListener {
            Toast.makeText(this, "Membuka Kalkulator", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, KalkulatorActivity::class.java))
        }

        // Tombol Catatan (Notes)
        val notesButton = findViewById<LinearLayout>(R.id.calculator_button2) // ID lebih deskriptif
        notesButton.setOnClickListener {
            Toast.makeText(this, "Membuka Catatan", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, NoteActivity::class.java))
        }

        // Tombol Ebook
        val ebookButton = findViewById<LinearLayout>(R.id.calculator_button3) // ID lebih deskriptif
        notesButton.setOnClickListener {
            Toast.makeText(this, "Membuka Ebook", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainBook::class.java))
        }

        // Tombol Close (Keluar dari aplikasi)
        val btnClose = findViewById<Button>(R.id.button)
        btnClose.setOnClickListener {
            finishAffinity() // Menutup semua activity
        }
    }

    // Logika untuk keluar dengan double-tap
    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
