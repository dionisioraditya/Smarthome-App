package com.diordty.smarthome.view.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.diordty.smarthome.R
import com.diordty.smarthome.models.Relay
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

class RelayAdapter : RecyclerView.Adapter<RelayAdapter.MyViewHolder>() {
    private val relayList = ArrayList<Relay>()
    private var relayState = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_row_relay,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = relayList[position]
        holder.tvRelay.text = currentItem.name
        val database = Firebase.database
        val statusRef = database.getReference("iot").child("relay").child("relay$position").child("status")
        relayState(currentItem,holder.container, holder.cardView)
        //val count = relayList.count()
        //println("count of list is: $count")

        holder.itemView.setOnClickListener {
            if (relayState){
                statusRef.setValue("on")
                relayState = false
            }else{
                statusRef.setValue("off")
                relayState = true
            }
        }
    }
    override fun getItemCount(): Int {
        return relayList.size
    }
    fun updateRelayList(relayList: List<Relay>){
        this.relayList.clear()
        this.relayList.addAll(relayList)
        notifyDataSetChanged()
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvRelay: TextView = itemView.findViewById(R.id.tv_relay)
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        val container: ConstraintLayout = itemView.findViewById(R.id.card_container)
        val cardView: CardView = itemView.findViewById(R.id.card_view)
    }
    private fun relayState(currentItem: Relay, container: ConstraintLayout, cardView: CardView){
        if (currentItem.status == "on"){
            cardView.setBackgroundColor(Color.parseColor("#051F51"))
            container.setBackgroundResource(R.drawable.list_btn_on)
        }else{
            cardView.setBackgroundColor(Color.parseColor("#051F51"))
            container.setBackgroundResource(R.drawable.list_btn_off)
        }
    }
}