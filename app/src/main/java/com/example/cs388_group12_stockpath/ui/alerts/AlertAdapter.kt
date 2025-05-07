package com.example.cs388_group12_stockpath.ui.alerts

import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.cs388_group12_stockpath.Alert
import com.example.cs388_group12_stockpath.Asset
import com.example.cs388_group12_stockpath.R
import com.google.firebase.Timestamp
import java.util.Locale

class AlertAdapter(
    private var alerts: List<Alert>,
    private val priceCache: LiveData<MutableMap<String, Double>>,
    private val onClick: (Alert) -> Unit
) : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    init {
        priceCache.observeForever { updatedPrices ->
            updatePrices(updatedPrices)
        }
    }

    inner class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val symTextView: TextView = itemView.findViewById(R.id.alert_sym)
        val watchPriceTextView: TextView = itemView.findViewById(R.id.alert_watch_price)
        val watchTypeTextView: TextView = itemView.findViewById(R.id.alert_watch_type)
        val alertCurrentPriceTextView: TextView = itemView.findViewById(R.id.alert_current_price)
        val alertTimestampTextView: TextView = itemView.findViewById(R.id.alert_timestamp)

        fun bind(alert: Alert) {
            symTextView.text = alert.sym
            watchPriceTextView.text = "Watch Price: ${alert.watchPrice}"
            watchTypeTextView.text = "Type: ${alert.watchType}"
            alertCurrentPriceTextView.text = "Market Price: ${alert.currentPrice}"
            val formattedTimestamp = formatTimestamp(alert.timestamp)
            alertTimestampTextView.text = "Alert Timestamp: $formattedTimestamp"

            // Change background color based on trigger
            val isTriggered = isAlertTriggered(alert)
            val backgroundColor = if (isTriggered) {
                itemView.context.getColor(R.color.red) // Triggered: Red
            } else {
                itemView.context.getColor(R.color.blue) // Not Triggered: Blue
            }
            itemView.setBackgroundColor(backgroundColor)

            itemView.setOnClickListener { onClick(alert) }
        }
        private fun formatTimestamp(timestamp: Timestamp): String {
            val date = timestamp.toDate()
            val format = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault())
            return format.format(date)
        }
        
        private fun isAlertTriggered(alert: Alert): Boolean {
            return if (alert.watchType == "above") {
                alert.currentPrice > alert.watchPrice
            } else {
                alert.currentPrice < alert.watchPrice
            }
        
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

    fun updatePrices(prices: MutableMap<String, Double>) {
        alerts.forEach{ alert ->
            val sym = alert.sym
            val currentPrice = prices[sym]
            if (currentPrice != null && currentPrice != alert.currentPrice) {
                alert.currentPrice = currentPrice
            }
        }
        notifyDataSetChanged()
        Log.d("AlertAdapter", "Prices updated for alerts: $prices")
    }

    fun updateAlerts(newAlerts: List<Alert>) {
        alerts = newAlerts

        notifyDataSetChanged()
        Log.d("AlertAdapter", "Alerts updated: $newAlerts")
    }
}