package com.example.partyplanner.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.preference.PreferenceManager
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.partyplanner.R
import com.example.partyplanner.homeFragment.HomeFragment
import com.example.partyplanner.model.Event
import com.example.partyplanner.model.UserData
import com.example.partyplanner.retrofit.Request
import com.example.partyplanner.retrofit.retrofit
import com.example.partyplanner.view.UserAdapter.UserViewHolder
import com.google.android.material.internal.ContextUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.util.*
import kotlin.collections.ArrayList

class UserAdapter(val context: Context):RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v  = inflater.inflate(R.layout.list_item,parent,false)
        return UserViewHolder(v)
    }
  //  var dataList = mutableListOf<Event>()
    var filteredNeedyList= ArrayList<Event>()

    internal fun setDataList(userArrayList: ArrayList<Event>) {
       // this.dataList = userArrayList
        this.filteredNeedyList=userArrayList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int){
        val newList = filteredNeedyList[position]
        holder.name.text = newList.eventTitle
        holder.mbNum.text = newList.date

/*        holder.itemView.setOnClickListener { //Show Popup With Information Hospitals
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.putString("Eventid", newList.getId())
            editor.apply()  //Save Data
        }*/
        holder.mMenus.setOnClickListener {
            popupMenus(holder.mMenus,newList.id!!,newList.eventTitle!!,newList.date!!,newList.venue!!,newList.maxParticipant!!,newList.time!!)
        }
    }

    inner class UserViewHolder (val v: View):RecyclerView.ViewHolder(v){
        var name: TextView
        var mbNum: TextView
        var mMenus: ImageView

        init {
            name = v.findViewById<TextView>(R.id.mTitle)
            mbNum = v.findViewById<TextView>(R.id.mSubTitle)
            mMenus = v.findViewById( R.id.mMenus)
           // mMenus.setOnClickListener { popupMenus(it) }
        }
    }

    private fun popupMenus(v:View,Eventid:String,titlee:String,datee:String,venuee:String,participationn:Int,timee:String) {
        val popupMenus = PopupMenu(context,v)
        popupMenus.inflate(R.menu.show_menu)
        popupMenus.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.editText->{
                    val v = LayoutInflater.from(context).inflate(R.layout.add_item,null)

                    val event_title = v.findViewById<EditText>(R.id.etEvent_title)
                    val date = v.findViewById<EditText>(R.id.etDate)
                    val venue = v.findViewById<EditText>(R.id.etVenue)
                    val maxParticipant = v.findViewById<EditText>(R.id.etMax_participant)
                    val time = v.findViewById<EditText>(R.id.etTime)
                    val POPUPTITLE = v.findViewById<TextView>(R.id.POPUPTITLE)

                    POPUPTITLE.text ="Update the Event Information"
                    ///////////////////////////////////////////////////////////
                    val calendar = Calendar.getInstance()
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
                            DatePickerDialog(context,
                                dateSetListener,
                                // set DatePickerDialog to point to today's date when it loads up
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)).show()
                        }

                    })

                    val mTimePicker: TimePickerDialog
                    val mcurrentTime = Calendar.getInstance()
                    val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                    val minute = mcurrentTime.get(Calendar.MINUTE)

                    mTimePicker = TimePickerDialog(context, object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                            time.setText(String.format("%d:%d", hourOfDay, minute))
                        }
                    }, hour, minute, false)

                    time.setOnClickListener {
                        mTimePicker.show()
                    }

                    event_title.setText(titlee)
                    date.setText(datee)
                    venue.setText(venuee)
                    maxParticipant.setText(participationn.toString())
                    time.setText(timee)

                    ///////////////////////////////////////////////////////////
                    AlertDialog.Builder(context)
                        .setView(v)
                        .setPositiveButton("Ok"){
                                dialog,_->
/*                           if(validateEditNeedyPhone())
                            {*/
                                val retrofit: Retrofit = retrofit.getInstance()
                                val service: Request = retrofit.create(Request::class.java)
                                val Event = Event()
                                Event.setId(Eventid)
                                Event.setEventTitle(event_title!!.text.toString())
                                Event.setVenue(venue!!.text.toString())
                                Event.setMaxParticipant(Integer.parseInt(maxParticipant!!.text.toString()))
                                Event.setDate(date.text.toString())
                                Event.setTime(time!!.text.toString())

                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        // Do the POST request and get response
                                        val response = service.updateEvent(Event)
                                        withContext(Dispatchers.Main) {
                                            if (response!!.isSuccessful) {
                                                dialog.dismiss()
                                                changeFragment(HomeFragment(),context)
                                                println("success")
                                            } else {
                                                Log.e("RETROFIT_ERROR", response.code().toString())
                                                println("Message :" + response.errorBody()?.string())
                                            }
                                        }
                                    } catch (e: Exception) {
                                        println("Error")
                                        println(e.printStackTrace())
                                    }
                                }
                           // }

                             notifyDataSetChanged()
                            Toast.makeText(context,"User Information is Edited", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()

                        }
                        .setNegativeButton("Cancel"){
                                dialog,_->
                            dialog.dismiss()

                        }
                        .create()
                        .show()

                    true
                }
                R.id.delete->{
                    //**set delete*//*
                    AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setIcon(R.drawable.ic_warning)
                        .setMessage("Are you sure delete this Information")
                        .setPositiveButton("Yes"){
                                dialog,_->
                             println("Iddddddd ==>>>>>>>>>>>>>>>>>>>>>> "+Eventid)

                            if(Eventid.isNotEmpty())
                            {
                                val retrofit: Retrofit = retrofit.getInstance()
                                val service: Request = retrofit.create(Request::class.java)
                                val Event = Event()
                                Event.setId(Eventid)
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        val response = service.deleteEvent(Event)
                                        withContext(Dispatchers.Main) {
                                            if (response.isSuccessful) {
                                                dialog.dismiss()
                                            } else {
                                                Log.e("RETROFIT_ERROR", response.code().toString())
                                                println("Message :" + response.errorBody()?.string())
                                            }
                                        }
                                    } catch (e: Exception) {
                                        println("Error")
                                        println(e.printStackTrace())
                                    }
                                }
                            }
                            changeFragment(HomeFragment(),context)
                            dialog.dismiss()
                        }
                        .setNegativeButton("No"){
                                dialog,_->
                            dialog.dismiss()
                        }
                        .create()
                        .show()

                    true
                }
                else-> true
            }

        }
        popupMenus.show()
        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupMenus)
        menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
            .invoke(menu,true)
    }

     override fun getItemCount(): Int {
         return filteredNeedyList.size
     }

    fun changeFragment(newFragment: Fragment?, context: Context) {
        val transaction: FragmentTransaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment!!)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}