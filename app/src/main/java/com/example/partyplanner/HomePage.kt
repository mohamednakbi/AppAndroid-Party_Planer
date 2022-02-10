package com.example.partyplanner

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.partyplanner.homeFragment.GuestsFragment
import com.example.partyplanner.homeFragment.HomeFragment
import com.example.partyplanner.homeFragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePage : AppCompatActivity() {

    private lateinit var bottom_navigation : BottomNavigationView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)


        val toolbar: Toolbar = findViewById(R.id.app_bar)
        setSupportActionBar(toolbar)
        /* load data from login page */
        val CURRENT_EMAIL = intent.getStringExtra("CURRENT_EMAIL")
        Log.d("email","$CURRENT_EMAIL : from Home activity !! ")

        val homeFragment = HomeFragment()
        val mBundle = Bundle()
        mBundle.putString("CURRENT_EMAIL",CURRENT_EMAIL.toString())
        homeFragment.arguments = mBundle
        replaceFragment(homeFragment)



        bottom_navigation = findViewById(R.id.bottom_navigation)

        bottom_navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> {
                    val homeFragment = HomeFragment()
                    val mBundle = Bundle()
                    mBundle.putString("CURRENT_EMAIL",CURRENT_EMAIL.toString())
                    homeFragment.arguments = mBundle
                    replaceFragment(homeFragment)
                }
                R.id.ic_guests -> {
                    val eventFragment = GuestsFragment()
                    val mBundle = Bundle()
                    mBundle.putString("CURRENT_EMAIL",CURRENT_EMAIL.toString())
                    homeFragment.arguments = mBundle
                    replaceFragment(eventFragment)
                }
                else ->  {
                    val profileFragment = ProfileFragment()
                    val mBundle = Bundle()
                    mBundle.putString("CURRENT_EMAIL",CURRENT_EMAIL.toString())
                    homeFragment.arguments = mBundle
                    replaceFragment(profileFragment)
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,fragment)
        transaction.commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.ic_logoutt -> {
                getSharedPreferences(PREF_NAME,MODE_PRIVATE).edit().clear().apply()
                val intent = Intent(this,login::class.java)
                startActivity(intent)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}