package com.example.revarioapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Pastikan layout sesuai

        val nama = findViewById<EditText>(R.id.editTextText)
        val pass = findViewById<EditText>(R.id.editTextTextPassword)
        val buttonClick = findViewById<Button>(R.id.button)

        buttonClick.setOnClickListener {
            if (nama.text.toString() == "user" && pass.text.toString() == "123") {
                val intent = Intent(this, MainActivity::class.java)
                Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            } else {
                Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
