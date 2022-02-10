package com.example.partyplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var btn_started : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_started = findViewById(R.id.btn_getStarted)

        btn_started.setOnClickListener {
            val intent = Intent(this,login::class.java)
            startActivity(intent)
        }


    }


}