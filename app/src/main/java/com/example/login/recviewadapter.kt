package com.example.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class recviewadapter (private val dataList: List<StatementsData>) :
    RecyclerView.Adapter<recviewadapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titheTextView: TextView = itemView.findViewById(R.id.tithe)
        val offeringsTextView: TextView = itemView.findViewById(R.id.offerings)
        val campMeetingTextView: TextView = itemView.findViewById(R.id.campmeeting)
        val thirteenthTextView: TextView = itemView.findViewById(R.id.thirteenth)
        val conferenceTextView: TextView = itemView.findViewById(R.id.conference)
        val totalTextView: TextView = itemView.findViewById(R.id.total)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.titheTextView.text = data.tithe
        holder.offeringsTextView.text = data.offerings
        holder.campMeetingTextView.text = data.campMeeting
        holder.thirteenthTextView.text = data.thirteenth
        holder.conferenceTextView.text = data.conference
        holder.totalTextView.text = data.total
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}