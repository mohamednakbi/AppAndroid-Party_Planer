package com.example.partyplanner.forgotPassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.partyplanner.R

class ForgotPassword : AppCompatActivity() {

    private lateinit var btn_next : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btn_next = findViewById(R.id.btn_step1)
        btn_next.setOnClickListener {
            val intent = Intent(this,CodeVerification::class.java)
            startActivity(intent)
        }
    }

}