package com.example.partyplanner

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.example.partyplanner.forgotPassword.ForgotPassword
import com.example.partyplanner.model.User
import com.example.partyplanner.retrofit.Request
import com.example.partyplanner.retrofit.retrofit
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit


const val PREF_NAME = "LOGIN_PP"
class login : AppCompatActivity() {

    private lateinit var tilUserEmail : TextInputLayout
    private lateinit var etEmail : TextInputEditText

    private lateinit var tilUserPassword : TextInputLayout
    private lateinit var etUserPassword : TextInputEditText
    private lateinit var btn_login : Button
    private lateinit var tvSignUp : TextView
    private lateinit var forgotPassword : TextView
    lateinit var mSharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        tilUserEmail = findViewById(R.id.tilUserEmail)
        etEmail = findViewById(R.id.etUserEmail)

        tilUserPassword = findViewById(R.id.tilUserPassword)
        etUserPassword = findViewById(R.id.etUserPassword)

        fun ServiceLogin(email:String,password:String) {
            // Create Retrofit
            val retrofit: Retrofit = retrofit.getInstance()
            val service: Request = retrofit.create(Request::class.java)
            val User = User()
            User.setEmail(email)
            User.setPassword(password)
            // Create JSON using JSONObject

            CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response
                val response = service.Login2(User)
                mSharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                try {
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val editor = mSharedPref.edit()
                            editor.putString("id",response.body()?.getId())
                            editor.putString("NameUser", response.body()?.getName())
                            editor.putString("password",password)
                            editor.putString("EmailUser", response.body()?.getEmail())
                            editor.putInt("age",response.body()?.getAge()!!)
                            editor.putString("phone",response.body()?.getPhone())
                            println("Email ====>>>>> " + response.body()?.getEmail())
                            editor.apply()  //Save Data
                            Log.d("ID_USER","ID_USER : ${response.body()?.getId()}")
                            //  println("Token =============>>>>>>>>>  "+response.body()?.string())
                            GoToHome(this@login, email)
                            finish()//GoTo Page Home

                        } else {
                            Log.e("RETROFIT_ERROR", response.code().toString())
                            println("Message :" + response.errorBody()?.string())
                            mSharedPref.edit().clear().apply()

                        }
                    }
                }
                catch ( e : Exception)
                {
                    println(e.printStackTrace())
                    println("Error")
                }
            }
        }




        forgotPassword = findViewById(R.id.tvForgotPassword)
        forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        tvSignUp = findViewById(R.id.tvSignUp)
        tvSignUp.setOnClickListener {
            val intent = Intent(this,sign_up::class.java)
            startActivity(intent)
        }



        btn_login = findViewById(R.id.btn_signIn)
        btn_login.setOnClickListener {
            /*val intent = Intent(this,HomePage::class.java)
            startActivity(intent)*/
            ServiceLogin(etEmail.text.toString(),etUserPassword.text.toString())
        }


    }
}


fun GoToHome(context: Context,email : String) {
    val intent = Intent(context, HomePage::class.java).apply {
        putExtra("CURRENT_EMAIL",email)
        Log.d("email","$email : from login activity ")

    }
    context.startActivity(intent)
}

