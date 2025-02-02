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
    private val numberStack = mutableListOf<Double>()  // Stack untuk menyimpan angka
    private val operatorStack = mutableListOf<String>()  // Stack untuk menyimpan operator
    private var isNewOperation = true
    private lateinit var buttonEffect: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalkulator)

        tvDisplay = findViewById(R.id.tv_display)

        buttonEffect = MediaPlayer.create(this, R.raw.button_effect)

        val buttons = listOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9
        )

        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                playButtonSound()
                appendNumber((it as Button).text.toString())
            }
        }

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
            finish()
        }
    }

    private fun playButtonSound() {
        if (buttonEffect.isPlaying) {
            buttonEffect.seekTo(0)
        }
        buttonEffect.start()
    }

    private fun appendNumber(number: String) {
        if (isNewOperation) {
            currentInput = number
            isNewOperation = false
        } else {
            currentInput += number
        }
        updateDisplay()
    }

    private fun setOperator(op: String) {
        if (currentInput.isNotEmpty()) {
            numberStack.add(currentInput.toDouble())
            operatorStack.add(op)
            currentInput = ""
            isNewOperation = true
        }
    }

    private fun calculateResult() {
        if (currentInput.isNotEmpty()) {
            numberStack.add(currentInput.toDouble()) // Menambahkan angka terakhir

            var result = numberStack[0]
            for (i in 1 until numberStack.size) {
                val operator = operatorStack[i - 1]
                when (operator) {
                    "+" -> result += numberStack[i]
                    "-" -> result -= numberStack[i]
                    "×" -> result *= numberStack[i]
                    "÷" -> if (numberStack[i] != 0.0) result /= numberStack[i] else result = Double.NaN
                }
            }
            currentInput = result.toString()
            numberStack.clear()
            operatorStack.clear()
            isNewOperation = true
            updateDisplay()
        }
    }

    private fun calculatePercent() {
        if (currentInput.isNotEmpty()) {
            val result = currentInput.toDoubleOrNull()?.div(100)
            currentInput = result.toString()
            updateDisplay()
        }
    }

    private fun calculateSquare() {
        if (currentInput.isNotEmpty()) {
            val result = currentInput.toDoubleOrNull()?.let { it * it }
            currentInput = result.toString()
            updateDisplay()
        }
    }

    private fun calculateSqrt() {
        if (currentInput.isNotEmpty()) {
            val result = currentInput.toDoubleOrNull()?.let { sqrt(it) }
            currentInput = result.toString()
            updateDisplay()
        }
    }

    private fun deleteLastChar() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            if (currentInput.isEmpty()) currentInput = "0"
            updateDisplay()
        }
    }

    private fun clearDisplay() {
        currentInput = "0"
        numberStack.clear()
        operatorStack.clear()
        isNewOperation = true
        updateDisplay()
    }

    private fun updateDisplay() {
        tvDisplay.text = currentInput
    }

    override fun onDestroy() {
        super.onDestroy()
        buttonEffect.release()
    }
}

