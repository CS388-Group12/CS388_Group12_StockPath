package com.example.cs388_group12_stockpath.ui.alerts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs388_group12_stockpath.Alert
import com.example.cs388_group12_stockpath.R

class AlertAdapter(
    private val alerts: List<Alert>,
    private val onClick: (Alert) -> Unit
) : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    inner class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val symTextView: TextView = itemView.findViewById(R.id.alert_sym)
        val watchPriceTextView: TextView = itemView.findViewById(R.id.alert_watch_price)
        val watchTypeTextView: TextView = itemView.findViewById(R.id.alert_watch_type)

        fun bind(alert: Alert) {
            symTextView.text = alert.sym
            watchPriceTextView.text = "Watch Price: ${alert.watchPrice}"
            watchTypeTextView.text = "Type: ${alert.watchType}"
            itemView.setOnClickListener { onClick(alert) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alert, parent, false)
        return AlertViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        holder.bind(alerts[position])
    }

    override fun getItemCount(): Int = alerts.size
}