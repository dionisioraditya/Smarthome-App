package com.diordty.smarthome.Repository

import androidx.lifecycle.MutableLiveData
import com.diordty.smarthome.models.Relay
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class RelayRepository {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("iot").child("relay")
    @Volatile private var INSTANCE : RelayRepository ?= null
    fun getInstance(): RelayRepository{
        return INSTANCE ?: synchronized(this){
            val instance = RelayRepository()
            INSTANCE = instance
            instance
        }
    }
    fun loadRelays(relayList : MutableLiveData<List<Relay>>){
        CoroutineScope(Dispatchers.IO).launch {
            databaseReference.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val _relayList : List<Relay> = snapshot.children.map { dataSnapshot ->
                            dataSnapshot.getValue(Relay::class.java)!!
                        }
                        relayList.postValue(_relayList)
                    }catch (e: Exception){
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    println(error)
                }
            })
        }

    }

}