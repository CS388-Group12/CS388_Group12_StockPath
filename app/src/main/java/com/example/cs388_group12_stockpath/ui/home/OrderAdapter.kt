package com.example.cs388_group12_stockpath.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs388_group12_stockpath.Order
import com.example.cs388_group12_stockpath.R

class OrderAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val symbolTextView: TextView = itemView.findViewById(R.id.textViewOrderSymbol)
        val detailsTextView: TextView = itemView.findViewById(R.id.textViewOrderDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.symbolTextView.text = order.sym
        holder.detailsTextView.text = "Type: ${order.type}, Qty: ${order.qty}, Price: ${String.format("%.2f", order.price)}"
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}