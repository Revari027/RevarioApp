package com.example.revarioapp

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class KalkulatorActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private var currentInput = ""
    private var firstNumber: Double? = null
    private var operator: String? = null
    private var isNewOperation = true
    private lateinit var buttonEffect: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalkulator)

        tvDisplay = findViewById(R.id.tv_display)

        // Inisialisasi MediaPlayer untuk suara tombol
        buttonEffect = MediaPlayer.create(this, R.raw.button_effect)

        // Ambil semua tombol
        val buttons = listOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9
        )

        // Set listener untuk tombol angka
        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                playButtonSound()  // Mainkan suara
                appendNumber((it as Button).text.toString())
            }
        }

        // Operator
        findViewById<Button>(R.id.btn_add).setOnClickListener {
            playButtonSound()
            setOperator("+")
        }
        findViewById<Button>(R.id.btn_subtract).setOnClickListener {
            playButtonSound()
            setOperator("-")
        }
        findViewById<Button>(R.id.btn_multiply).setOnClickListener {
            playButtonSound()
            setOperator("×")
        }
        findViewById<Button>(R.id.btn_divide).setOnClickListener {
            playButtonSound()
            setOperator("÷")
        }
        findViewById<Button>(R.id.btn_percent).setOnClickListener {
            playButtonSound()
            calculatePercent()
        }
        findViewById<Button>(R.id.btn_square).setOnClickListener {
            playButtonSound()
            calculateSquare()
        }
        findViewById<Button>(R.id.btn_sqrt).setOnClickListener {
            playButtonSound()
            calculateSqrt()
        }

        // Fungsi lainnya
        findViewById<Button>(R.id.btn_clear).setOnClickListener {
            playButtonSound()
            clearDisplay()
        }
        findViewById<Button>(R.id.btn_backspace).setOnClickListener {
            playButtonSound()
            deleteLastChar()
        }
        findViewById<Button>(R.id.btn_equals).setOnClickListener {
            playButtonSound()
            calculateResult()
        }
        findViewById<Button>(R.id.btn_back).setOnClickListener {
            playButtonSound()
            finish()  // Menutup aktivitas
        }
    }

    // Fungsi untuk memutar suara
    private fun playButtonSound() {
        if (buttonEffect.isPlaying) {
            buttonEffect.seekTo(0)  // Mengulang suara jika sedang diputar
        }
        buttonEffect.start()  // Memutar suara
    }

    // Fungsi menambahkan angka ke display
    private fun appendNumber(number: String) {
        if (isNewOperation) {
            currentInput = number
            isNewOperation = false
        } else {
            currentInput += number
        }
        updateDisplay()
    }

    // Fungsi memilih operator
    private fun setOperator(op: String) {
        if (currentInput.isNotEmpty()) {
            firstNumber = currentInput.toDoubleOrNull()
            operator = op
            isNewOperation = true
        }
    }

    // Fungsi untuk menghitung hasil
    private fun calculateResult() {
        if (firstNumber != null && operator != null && currentInput.isNotEmpty()) {
            val secondNumber = currentInput.toDoubleOrNull() ?: return
            val result = when (operator) {
                "+" -> firstNumber!! + secondNumber
                "-" -> firstNumber!! - secondNumber
                "×" -> firstNumber!! * secondNumber
                "÷" -> if (secondNumber != 0.0) firstNumber!! / secondNumber else "Error"
                else -> return
            }
            currentInput = result.toString()
            firstNumber = null
            operator = null
            isNewOperation = true
            updateDisplay()
        }
    }

    // Fungsi persen
    private fun calculatePercent() {
        if (currentInput.isNotEmpty()) {
            val result = currentInput.toDoubleOrNull()?.div(100)
            currentInput = result.toString()
            updateDisplay()
        }
    }

    // Fungsi pangkat dua (x²)
    private fun calculateSquare() {
        if (currentInput.isNotEmpty()) {
            val result = currentInput.toDoubleOrNull()?.let { it * it }
            currentInput = result.toString()
            updateDisplay()
        }
    }

    // Fungsi akar kuadrat (√)
    private fun calculateSqrt() {
        if (currentInput.isNotEmpty()) {
            val result = currentInput.toDoubleOrNull()?.let { sqrt(it) }
            currentInput = result.toString()
            updateDisplay()
        }
    }

    // Fungsi untuk hapus per karakter (⌫)
    private fun deleteLastChar() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            if (currentInput.isEmpty()) currentInput = "0"
            updateDisplay()
        }
    }

    // Fungsi untuk menghapus semuanya (C)
    private fun clearDisplay() {
        currentInput = "0"
        firstNumber = null
        operator = null
        isNewOperation = true
        updateDisplay()
    }

    // Fungsi update tampilan
    private fun updateDisplay() {
        tvDisplay.text = currentInput
    }

    // Pastikan untuk melepaskan resource MediaPlayer saat aktivitas dihancurkan
    override fun onDestroy() {
        super.onDestroy()
        buttonEffect.release()
    }
}
