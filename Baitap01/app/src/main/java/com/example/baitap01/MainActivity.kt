package com.example.baitap01

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- Phần ẩn thanh tiêu đề ---
        hideTitleBar()

        // --- Yêu cầu 4: Lọc số chẵn và lẻ từ ArrayList ---
        setupFilterButton()

        // --- Yêu cầu 5: Đảo ngược chuỗi --- 
        setupReverseStringFeature()
    }

    private fun hideTitleBar() {
        supportActionBar?.hide()
    }

    private fun setupFilterButton() {
        val numbersInput = findViewById<EditText>(R.id.numbers_input_edittext)
        val filterButton = findViewById<Button>(R.id.filter_button)
        val evenTextView = findViewById<TextView>(R.id.even_numbers_textview)
        val oddTextView = findViewById<TextView>(R.id.odd_numbers_textview)

        filterButton.setOnClickListener {
            val inputText = numbersInput.text.toString()
            if (inputText.isBlank()) {
                Toast.makeText(this, "Vui lòng nhập dãy số", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val numbers = inputText.split(",").mapNotNull { it.trim().toIntOrNull() }

            if (numbers.isEmpty()) {
                Toast.makeText(this, "Định dạng không hợp lệ hoặc không có số nào", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val evenNumbers = numbers.filter { it % 2 == 0 }
            val oddNumbers = numbers.filter { it % 2 != 0 }

            // In ra Log.d
            Log.d("BaiTap4", "Các số chẵn: $evenNumbers")
            Log.d("BaiTap4", "Các số lẻ: $oddNumbers")

            // Hiển thị trên TextView
            evenTextView.text = "Các số chẵn: ${evenNumbers.joinToString()}"
            oddTextView.text = "Các số lẻ: ${oddNumbers.joinToString()}"
        }
    }

    private fun setupReverseStringFeature() {
        val inputEditText = findViewById<EditText>(R.id.input_edittext)
        val reverseButton = findViewById<Button>(R.id.reverse_button)
        val resultTextView = findViewById<TextView>(R.id.result_textview)

        reverseButton.setOnClickListener {
            val inputText = inputEditText.text.toString()
            if (inputText.isNotBlank()) {
                val reversedText = inputText.trim().split(" ").filter { it.isNotEmpty() }.reversed().joinToString(" ").uppercase()
                resultTextView.text = reversedText
                Toast.makeText(this, reversedText, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Vui lòng nhập chuỗi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}