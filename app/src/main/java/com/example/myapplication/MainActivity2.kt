package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.pdf.PDFView
import com.example.myapplication.pdf.PdfUtils

class MainActivity2 : AppCompatActivity() {


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main2)

        val pdf = findViewById<PDFView>(R.id.pdf)
        pdf.openPage(38, false)


   }
}