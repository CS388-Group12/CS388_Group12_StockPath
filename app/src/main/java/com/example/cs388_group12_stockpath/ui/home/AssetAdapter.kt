package com.example.cs388_group12_stockpath.ui.home
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import com.example.cs388_group12_stockpath.R
import com.example.cs388_group12_stockpath.Asset

class AssetAdapter(private val assets: List<Asset>, private val onAssetClick: (Asset) -> Unit
) : RecyclerView.Adapter<AssetAdapter.AssetViewHolder>() {

    class AssetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val symbolTextView: TextView = itemView.findViewById(R.id.textViewAssetSymbol)
        val detailsTextView: TextView = itemView.findViewById(R.id.textViewAssetDetails)
        val orderCountTextView: TextView = itemView.findViewById(R.id.textViewOrderCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_asset, parent, false)
        return AssetViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        val asset = assets[position]
        holder.symbolTextView.text = asset.sym
        holder.detailsTextView.text = "Quantity: ${asset.totalQuantity}, Avg Price: ${String.format("%.2f", asset.averagePrice)}"
        holder.orderCountTextView.text = "Orders: [${asset.orderCount}]"
        holder.itemView.setOnClickListener {
            onAssetClick(asset)
        }
    }

    override fun getItemCount(): Int {
        return assets.size
    }
}