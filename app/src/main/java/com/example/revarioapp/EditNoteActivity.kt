package com.example.revarioapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.revarioapp.databinding.ActivityEditNoteBinding

class EditNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditNoteBinding
    private lateinit var dbHelper: DatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        // Ambil data dari Intent
        noteId = intent.getIntExtra("NOTE_ID", -1)
        val title = intent.getStringExtra("NOTE_TITLE") ?: ""
        val content = intent.getStringExtra("NOTE_CONTENT") ?: ""

        binding.titleEditText.setText(title)
        binding.contentEditText.setText(content)

        binding.saveButton.setOnClickListener {
            val updatedTitle = binding.titleEditText.text.toString()
            val updatedContent = binding.contentEditText.text.toString()

            if (updatedTitle.isNotEmpty() && updatedContent.isNotEmpty()) {
                val updatedNote = Note(noteId, updatedTitle, updatedContent)
                val success = dbHelper.updateNote(updatedNote)

                if (success) {
                    Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to update note", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Title and Content cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}