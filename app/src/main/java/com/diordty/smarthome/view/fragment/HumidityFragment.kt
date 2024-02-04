package com.diordty.smarthome.view.fragment

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.diordty.smarthome.R
import com.diordty.smarthome.databinding.FragmentHumidityBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class HumidityFragment : Fragment() {
    private var _binding: FragmentHumidityBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHumidityBinding.inflate(inflater, container, false)
        val database = Firebase.database
        val humidityRef = database.getReference("iot").child("sensor").child("humidity")
        humidityRef.addValueEventListener(object : ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue<Long>()
                Log.d(ContentValues.TAG, "Value is: "+ value)
                if (value != null) {
                    val ttb = AnimationUtils.loadAnimation(activity, R.anim.ttb);
                    binding.tvHumidity.startAnimation(ttb)
                    binding.circleDiagramHumidity.startAnimation(ttb)
                    binding.circleDiagramHumidity.progress = value.toInt()
                    binding.tvHumidity.text = "$value%"
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
        return binding.root
    }
}