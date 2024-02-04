package com.diordty.smarthome.Repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class UserRepository {
    private val userUid = FirebaseAuth.getInstance().currentUser?.uid
    private val databaseReference = Firebase.database.getReference("Users").child(userUid.toString()).child("username")
    @Volatile private var INSTANCE: UserRepository ?= null
    fun getInstance():UserRepository{
        return INSTANCE ?: synchronized(this){
            val instance = UserRepository()
            INSTANCE = instance
            instance
        }
    }
    fun loadUser(usernameData: MutableLiveData<String>){
        CoroutineScope(Dispatchers.IO).launch {
            databaseReference.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val _userData: String? = snapshot.getValue<String>()
                        usernameData.postValue(_userData)
                    } catch (e:Exception){
                        println(e)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    println(error)
                }
            })
        }

    }
}