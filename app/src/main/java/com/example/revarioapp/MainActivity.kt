package com.example.revarioapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val calculatorButton1 = findViewById<LinearLayout>(R.id.calculator_button1)
        calculatorButton1.setOnClickListener {

            Toast.makeText(this, "Calculator Button 1 clicked", Toast.LENGTH_SHORT).show()


            val intent = Intent(this, KalkulatorActivity::class.java)
            startActivity(intent)
        }

        val notes = findViewById<LinearLayout>(R.id.calculator_button2)
        notes.setOnClickListener {
        Toast.makeText(this, "Notes di buka yang mulia", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, NoteActivity::class.java)
        startActivity(intent)
        }

        val btnClose = findViewById<Button>(R.id.button)
        btnClose.setOnClickListener {
            finish()
        }


    }
}
