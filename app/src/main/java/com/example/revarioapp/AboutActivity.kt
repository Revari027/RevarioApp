package com.example.revarioapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat

class AboutActivity : AppCompatActivity() {

    private lateinit var ivProfile: ImageView
    private lateinit var ivBack: ImageView
    private val PICK_IMAGE_REQUEST = 1
    private val PREFS_NAME = "UserPrefs"
    private val KEY_PROFILE_IMAGE = "profile_image"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        ivProfile = findViewById(R.id.ivProfile)
        ivBack = findViewById(R.id.ivBack)

        // Cek & minta izin akses galeri
        checkPermission()

        // Klik untuk mengganti foto profil
        ivProfile.setOnClickListener {
            openGallery()
        }

        // Tombol kembali
        ivBack.setOnClickListener {
            onBackPressed()
        }

        // Load foto profil yang tersimpan
        loadProfileImage()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 100)
            }
        }
    }

    private fun openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Harap izinkan akses ke galeri!", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            ivProfile.setImageURI(imageUri)

            // Simpan foto profil ke SharedPreferences
            saveProfileImage(imageUri.toString())
        }
    }

    private fun saveProfileImage(imageUri: String) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PROFILE_IMAGE, imageUri)
        editor.apply()
    }

    private fun loadProfileImage() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val imageUriString = sharedPreferences.getString(KEY_PROFILE_IMAGE, null)

        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            ivProfile.setImageURI(imageUri)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Izin diberikan!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Izin ditolak! Harap izinkan akses di pengaturan.", Toast.LENGTH_LONG).show()
            }
        }
    }
}