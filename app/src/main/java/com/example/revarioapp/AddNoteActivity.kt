package com.example.revarioapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.revarioapp.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString().trim()
            val content = binding.contentEditText.text.toString().trim()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val newNote = Note(0, title, content) // ID harus 0 agar auto-increment berjalan
                val isInserted = dbHelper.insertNote(newNote)

                if (isInserted) {
                    Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
                    finish() // Kembali ke NoteActivity
                } else {
                    Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }


    }
}