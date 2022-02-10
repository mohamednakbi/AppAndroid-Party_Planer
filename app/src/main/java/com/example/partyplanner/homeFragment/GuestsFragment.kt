package com.example.partyplanner.homeFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import java.util.ArrayList

import com.example.partyplanner.R


class GuestsFragment : Fragment()//, AdapterView.OnItemSelectedListener
 {
//    private val AlcoholicBevarages: ArrayList<String>? = null
//    private val NonAlcoholicBevarages: ArrayList<String>? = null
//    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_guests, container, false)

//
//        //SPINNER
//        //==============================================================
//        spinner = view.findViewById(R.id.spinner)
//
//        val adapter = ArrayAdapter.createFromResource(
//            requireContext(), R.array.numbers,android.R.layout.simple_spinner_dropdown_item
//        )
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner.adapter = adapter
//        spinner.setOnItemSelectedListener(this)

        return view
    }

//    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onNothingSelected(p0: AdapterView<*>?) {
//        TODO("Not yet implemented")
//    }


}