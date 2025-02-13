package com.example.revarioapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = DatabaseHelper(this)

        val fullName = findViewById<EditText>(R.id.editTextFullName)
        val username = findViewById<EditText>(R.id.editTextUsername)
        val password = findViewById<EditText>(R.id.editTextPassword)
        val registerButton = findViewById<Button>(R.id.buttonRegister)
        val backButton = findViewById<Button>(R.id.buttonRegister2)

        // Set onClickListener untuk tombol Register
        registerButton.setOnClickListener {
            val name = fullName.text.toString()
            val user = username.text.toString()
            val pass = password.text.toString()

            if (name.isNotEmpty() && user.isNotEmpty() && pass.isNotEmpty()) {
                val insertSuccess = dbHelper.insertUser(name, user, pass)
                if (insertSuccess) {
                    Toast.makeText(this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    showErrorDialog("Registrasi Gagal", "Username sudah digunakan!")
                }
            } else {
                showErrorDialog("Input Kosong", "Harap isi semua field!")
            }
        }

        // Set onClickListener untuk tombol Back
        backButton.setOnClickListener {
            finish()  // Menutup RegisterActivity dan kembali ke LoginActivity
        }
    }

    private fun showErrorDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}