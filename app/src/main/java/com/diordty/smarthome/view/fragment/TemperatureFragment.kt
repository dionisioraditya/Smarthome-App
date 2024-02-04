package com.diordty.smarthome.view.fragment

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import com.diordty.smarthome.R
import com.diordty.smarthome.databinding.FragmentTemperatureBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class TemperatureFragment : Fragment() {
    private var _binding: FragmentTemperatureBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTemperatureBinding.inflate(inflater, container, false)
        val database = Firebase.database
        val temperatureRef = database.getReference("iot").child("sensor").child("temperature")
        temperatureRef.addValueEventListener(object :ValueEventListener{
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue<Long>()
                Log.d(ContentValues.TAG, "Value is: "+ value)
                if (value != null) {
                    val ttb = AnimationUtils.loadAnimation(activity, R.anim.ttb)
                    val minus: Int = -15
                    binding.circleDiagramTemperature.startAnimation(ttb)
                    binding.tvTemperature.startAnimation(ttb)
                    binding.circleDiagramTemperature.min = minus
                    binding.circleDiagramTemperature.max = 40
                    binding.circleDiagramTemperature.progress = value.toInt()
                    binding.tvTemperature.text = "$value°C"
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })

        return binding.root
    }
}