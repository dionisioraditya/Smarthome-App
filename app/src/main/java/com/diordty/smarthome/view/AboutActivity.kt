package com.diordty.smarthome.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.airbnb.lottie.parser.ColorParser
import com.diordty.smarthome.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        colorActionBar()
    }
    private fun colorActionBar(){
        val colorDrawable = ColorDrawable(Color.parseColor("#051F51"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
    }
}