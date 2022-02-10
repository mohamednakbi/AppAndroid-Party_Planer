package com.example.partyplanner.homeFragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.partyplanner.PREF_NAME
import com.example.partyplanner.R
import com.example.partyplanner.model.User
import com.example.partyplanner.retrofit.Request
import com.example.partyplanner.retrofit.retrofit
import com.google.android.material.internal.ContextUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit


class ProfileFragment : Fragment() {

    private lateinit var tvChangePass : TextView
    private lateinit var mSharedPref : SharedPreferences

    private lateinit var editAgeSetting : EditText
    private lateinit var editUserNameSetting : EditText
    private lateinit var editEmailSetting : EditText
    private lateinit var editPhoneSetting : EditText
    private lateinit var tvEditName : TextView
    private lateinit var tvEditAge : TextView
    private lateinit var tvEditEmail : TextView
    private lateinit var tvEditPhone : TextView
    private lateinit var btnSaveChanges : Button



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)


        editUserNameSetting = view.findViewById(R.id.EditUserNameSetting)
        editAgeSetting = view.findViewById(R.id.EditAgeSetting)
        editEmailSetting = view.findViewById(R.id.EditEmailSetting)
        editPhoneSetting = view.findViewById(R.id.EditPhoneSetting)

        editUserNameSetting.isEnabled = false
        editAgeSetting.isEnabled = false
        editEmailSetting.isEnabled = false
        editPhoneSetting.isEnabled = false


        tvEditName = view.findViewById(R.id.tvEditName)
        tvEditAge = view.findViewById(R.id.tvEditAge)
        tvEditEmail = view.findViewById(R.id.tvEditEmail)
        tvEditPhone = view.findViewById(R.id.tvEditPhone)

        mSharedPref = requireContext().getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE);
        val id = mSharedPref.getString("id","")
        val age = mSharedPref.getInt("age",20)
        val userName = mSharedPref.getString("NameUser","")
        val email = mSharedPref.getString("EmailUser","")
        val phoneNumber = mSharedPref.getString("phone","")


        editAgeSetting.setText("$age")
        editUserNameSetting.setText("$userName")
        editEmailSetting.setText("$email")
        editPhoneSetting.setText("$phoneNumber")


        tvEditName.setOnClickListener {
            editUserNameSetting.isEnabled = true

        }

        tvEditAge.setOnClickListener {
            editAgeSetting.isEnabled = true
        }

        tvEditEmail.setOnClickListener {
            editEmailSetting.isEnabled = true
        }

        tvEditPhone.setOnClickListener {
            editPhoneSetting.isEnabled = true
        }

        // save changes : UPDATE USER INFORMATION
        btnSaveChanges = view.findViewById(R.id.buttonSave)
        btnSaveChanges.setOnClickListener {
            val updatedUser = User()
            updatedUser.setId(id)
            updatedUser.setName(editUserNameSetting.text.toString())
            updatedUser.setAge(Integer.parseInt(editAgeSetting.text.toString()))
            updatedUser.setEmail(editEmailSetting.text.toString())
            updatedUser.setPhone(editPhoneSetting.text.toString())

            update(updatedUser)
            editUserNameSetting.isEnabled = false
            editAgeSetting.isEnabled = false
            editEmailSetting.isEnabled = false
            editPhoneSetting.isEnabled = false
        }


        tvChangePass = view.findViewById(R.id.tvChangePassword)
        tvChangePass.setOnClickListener {
            changePassword()
        }



        return view
    }

    private fun changePassword(){
        val inflater = LayoutInflater.from(this.context)
        val v = inflater.inflate(R.layout.change_password,null)
        val changePassDialog = AlertDialog.Builder(this.context)

        val old_password = v.findViewById<EditText>(R.id.etOldPass)
        val new_password = v.findViewById<EditText>(R.id.etNewPassword)
        val renew_password = v.findViewById<EditText>(R.id.etReNewPassword)
        val btn_confirm = v.findViewById<Button>(R.id.btnSaveChanges)

        btn_confirm.setOnClickListener {
            if(checkPassword(old_password.text!!.toString(),new_password.text.toString(),renew_password.text.toString())){
                val current_id = mSharedPref.getString("id","")
                val updatedUser = User()
                updatedUser.setId(current_id)
                updatedUser.setPassword(new_password.text.toString())
                val retrofit: Retrofit = retrofit.getInstance()
                val service: Request = retrofit.create(Request::class.java)

                CoroutineScope(Dispatchers.IO).launch {
                    try{
                        // Do the POST request and get response
                        val response = service.updatePassword(updatedUser)
                        Log.d("id","id from update password : ${updatedUser.getId()}")
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                mSharedPref.edit().putString("password", updatedUser.getPassword()).apply()
                                Log.d("password","password from check method : ${updatedUser.getPassword()}")

                                Toast.makeText(requireContext(),"password updated successfully ! ",Toast.LENGTH_SHORT).show()

                            } else {
                                Log.e("RETROFIT_ERROR", response.code().toString())
                                println("Message :"+response.errorBody()?.string())
                            }
                        }
                    }
                    catch (e: Exception)
                    {
                        println("Error")

                    }
                }
            }

        }

        changePassDialog.setView(v)
        changePassDialog.setPositiveButton("Close"){
                dialog,_-> run {
            }

            dialog.dismiss()
        }

        changePassDialog.create()
        changePassDialog.show()
    }

    private fun checkPassword(oldPass: String, newPass: String, rePass: String): Boolean {
        val mPassword = mSharedPref.getString("password","")
        if(oldPass.compareTo(mPassword!!) == 0){
            if(newPass.compareTo(rePass) == 0){
                return true
            }
        }
        else{
            Toast.makeText(requireContext(),"",Toast.LENGTH_SHORT).show()
        }
        return false
    }

    @SuppressLint("RestrictedApi")
    fun update(user : User) {
        val retrofit: Retrofit = retrofit.getInstance()
        val service: Request = retrofit.create(Request::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try{
                // Do the POST request and get response
                val response = service.UpdateUser(user)
                Log.d("id","id from update method : ${user.getId()}")
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        mSharedPref.edit().putString("NameUser", user.getName()).apply()
                        mSharedPref.edit().putString("EmailUser", user.getEmail()).apply()
                        mSharedPref.edit().putInt("age", user.getAge()!!).apply()
                        mSharedPref.edit().putString("phone", user.getPhone()).apply()

                        Toast.makeText(requireContext(),"user updated successfully ! ",Toast.LENGTH_SHORT).show()

                    } else {
                        Log.e("RETROFIT_ERROR", response.code().toString())
                        println("Message :"+response.errorBody()?.string())
                    }
                }
            }
            catch (e: Exception)
            {
                println("Error")
                ContextUtils.getActivity(context)?.runOnUiThread(java.lang.Runnable {
                })
            }
        }
    }


}