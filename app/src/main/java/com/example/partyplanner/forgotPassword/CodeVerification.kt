package com.example.partyplanner.forgotPassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.partyplanner.R

class CodeVerification : AppCompatActivity() {

    private lateinit var btn_next : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_verification)

        btn_next = findViewById(R.id.btn_step2)
        btn_next.setOnClickListener {
            val intent = Intent(this,ResetPassword::class.java)
            startActivity(intent)
        }
    }
}