package com.example.partyplanner.forgotPassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.partyplanner.R
import com.example.partyplanner.login

class ResetPassword : AppCompatActivity() {
    private lateinit var btn_confirm : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        btn_confirm = findViewById(R.id.btn_step3_confirm)
        btn_confirm.setOnClickListener {
            val intent = Intent(this,login::class.java)
            startActivity(intent)
        }

    }
}