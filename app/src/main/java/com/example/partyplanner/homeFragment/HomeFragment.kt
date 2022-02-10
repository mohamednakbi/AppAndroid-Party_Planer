package com.example.partyplanner.homeFragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast

import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.example.partyplanner.model.UserData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.partyplanner.PREF_NAME
import com.example.partyplanner.R
import com.example.partyplanner.model.Event
import com.example.partyplanner.retrofit.Request
import com.example.partyplanner.retrofit.retrofit
import com.example.partyplanner.view.UserAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.internal.ContextUtils
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(){

    private lateinit var addsBtn:FloatingActionButton
    private lateinit var recv: RecyclerView
    private lateinit var userList:ArrayList<UserData>
    private lateinit var userAdapter: UserAdapter
    private lateinit var mSharedPref : SharedPreferences

    var eventModels: java.util.ArrayList<Event> = java.util.ArrayList<Event>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        /* GET CURRENT USER EMAIL */
        val bundle = arguments
        val CURRENT_EMAIL  = bundle?.getString("CURRENT_EMAIL")


        /**set List*/
        userList = ArrayList()
        loadListEvents()
        /**set find Id*/
        addsBtn = view.findViewById(R.id.addingBtn)
        recv = view.findViewById(R.id.mRecycler)
        /**set Adapter*/
        userAdapter = UserAdapter(this.requireContext())
        /**setRecycler view Adapter*/
        recv.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL ,false)
        recv.adapter = userAdapter
        /**set Dialog*/
        addsBtn.setOnClickListener {
                addInfo(CURRENT_EMAIL.toString())
        }


        return view
    }



    private fun addInfo(CURRENT_EMAIL : String) {
       val inflater = LayoutInflater.from(this.context)
        val v = inflater.inflate(R.layout.add_item,null)
        /* loading data */
        val event_title = v.findViewById<EditText>(R.id.etEvent_title)
        val venue = v.findViewById<EditText>(R.id.etVenue)
        val maxParticipant = v.findViewById<EditText>(R.id.etMax_participant)
        val date = v.findViewById<EditText>(R.id.etDate)
        val time = v.findViewById<EditText>(R.id.etTime)

        val calendar = Calendar.getInstance()
        val addDialog = AlertDialog.Builder(this.context)

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val sDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                date.setText(sDate)
            }
        }

        date!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(requireContext(),
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        val mTimePicker: TimePickerDialog
        val mCurrentTime = Calendar.getInstance()
        val hour = mCurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mCurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(requireContext(), object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                time.setText(String.format("%d:%d", hourOfDay, minute))
            }
        }, hour, minute, false)

        time.setOnClickListener {
            mTimePicker.show()
        }
        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){
                dialog,_->
            addNewEvent(event_title!!.text.toString(),venue!!.text.toString(),Integer.parseInt(maxParticipant!!.text.toString()),date.text.toString(),time!!.text.toString(),CURRENT_EMAIL)

            /*userList.add(UserData("Title Event: ${event_title!!.text.toString()}","Date : ${date.text.toString()}"))
            userAdapter.notifyDataSetChanged()*/

            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
            Toast.makeText(this.context,"Cancel", Toast.LENGTH_SHORT).show()

        }
        addDialog.create()
        addDialog.show()
    }


    fun addNewEvent(event_title:String,venue:String,maxParticipant : Int,date:String,time : String,email : String){
        // Create Retrofit
        val retrofit: Retrofit = retrofit.getInstance()
        val service: Request = retrofit.create(Request::class.java)
        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("event_title", event_title)
        jsonObject.put("venue", venue)
        jsonObject.put("maxParticipant", maxParticipant)
        jsonObject.put("date",date)
        jsonObject.put("time",time)
        jsonObject.put("email",email)
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()
        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.addEvent(requestBody)
            try {
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Convert raw JSON to pretty JSON using GSON library
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson =
                            gson.toJson(JsonParser.parseString(response.body()?.string()))
                        Log.d("Pretty Printed JSON :", prettyJson)

                        Toast.makeText(
                            requireContext(),
                            "Event added successfully !! ",
                            Toast.LENGTH_SHORT
                        ).show()
                        changeFragment(HomeFragment(), requireContext())
                    } else {
                        Log.e("RETROFIT_ERROR", response.code().toString())
                        Toast.makeText(
                            requireContext(),
                            "error while adding a new event ! ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            catch (e: Exception) {
                println("Error")
                Log.e("RETROFIT_ERROR", response.code().toString())
                println("Message :" + response.errorBody()?.string())
            }
        }

    }

    fun changeFragment(newFragment: Fragment?, context: Context) {
        val transaction: FragmentTransaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment!!)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun loadListEvents() {
        mSharedPref = requireContext().getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE);
        val emailUser = mSharedPref.getString("EmailUser", null)
        ////////////////////////////////////////////////////
        val retrofit: Retrofit = retrofit.getInstance()
        val service: Request = retrofit.create(Request::class.java)
        val call: Call<List<Event>> = service.loadMyEvents(emailUser!!)
        call.enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                println("Email ============================>>>>$emailUser")
                eventModels = ArrayList<Event>(response.body())
                userAdapter.setDataList(eventModels)
            }
            @SuppressLint("RestrictedApi")
            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                ContextUtils.getActivity(context)?.runOnUiThread(java.lang.Runnable {
                })
                 Log.d("***", "Opppsss" + t.message)
            }
        })
    }

}

