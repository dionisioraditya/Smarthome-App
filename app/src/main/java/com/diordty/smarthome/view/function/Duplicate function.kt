package com.diordty.smarthome

import android.app.Activity
import android.widget.Toast

fun Activity.showToastShort(message: String){
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
}
