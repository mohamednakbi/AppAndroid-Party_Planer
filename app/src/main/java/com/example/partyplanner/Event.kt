package com.example.partyplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Event : AppCompatActivity() {

    private lateinit var btn_cancel : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        btn_cancel = findViewById(R.id.btn_cancel)
        btn_cancel.setOnClickListener {
            finish()
        }
    }
}