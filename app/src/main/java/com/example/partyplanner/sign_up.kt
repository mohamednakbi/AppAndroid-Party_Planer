package com.example.partyplanner

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.partyplanner.retrofit.Request
import com.example.partyplanner.retrofit.retrofit
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit

class sign_up : AppCompatActivity() {

    private lateinit var tvSignIn : TextView
    private lateinit var registerr : Button
    private lateinit var til_userName : TextInputLayout
    private lateinit var til_email : TextInputLayout
    private lateinit var til_password : TextInputLayout
    private lateinit var til_age : TextInputLayout
    private lateinit var til_phone : TextInputLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        til_userName = findViewById(R.id.tilUserName)
        til_email = findViewById(R.id.tilEmail)
        til_password = findViewById(R.id.tilPassword)
        til_age = findViewById(R.id.tilAge)
        til_phone = findViewById(R.id.tilPhone)


        tvSignIn = findViewById(R.id.tvSignIn)
        tvSignIn.setOnClickListener {
            val intent = Intent(this,login::class.java)
            startActivity(intent)
        }
        registerr = findViewById(R.id.btn_register)

        registerr.setOnClickListener{
            //val intent = Intent(this,HomePage::class.java)
           // startActivity(intent)

            ServiceSignuP(til_userName.editText!!.text.toString(),til_email.editText!!.text.toString(),til_password.editText!!.text.toString(),Integer.parseInt(til_age.editText!!.text.toString()),til_phone.editText!!.text.toString());
        }

    }

    fun ServiceSignuP(name:String,email:String,password:String,age : Int,phone : String) {
        // Create Retrofit
        val retrofit: Retrofit = retrofit.getInstance()
        val service: Request = retrofit.create(Request::class.java)
        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        jsonObject.put("email", email)
        jsonObject.put("password", password)
        jsonObject.put("age",age)
        jsonObject.put("phone",phone)


        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()
        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.Signup(requestBody)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(JsonParser.parseString(response.body()?.string()))
                    Log.d("Pretty Printed JSON :", prettyJson)

                    GoToLogin(this@sign_up) //GoTo Page Home

                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }

    fun GoToLogin(context: Context) {
        val intent = Intent(context, login::class.java)
        context.startActivity(intent)
        finish()
    }
}