package com.example.cs388_group12_stockpath
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import com.example.cs388_group12_stockpath.R
import com.example.cs388_group12_stockpath.Asset

class AssetAdapter(private val assets: List<Asset>) : RecyclerView.Adapter<AssetAdapter.AssetViewHolder>() {

    class AssetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val symbolTextView: TextView = itemView.findViewById(R.id.textViewAssetSymbol)
        val detailsTextView: TextView = itemView.findViewById(R.id.textViewAssetDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_asset, parent, false)
        return AssetViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        val asset = assets[position]
        Log.d("AssetAdapter", "Binding asset: $asset")
        holder.symbolTextView.text = asset.sym
        holder.detailsTextView.text = "Quantity: ${asset.totalQuantity}, Avg Price: ${String.format("%.2f", asset.averagePrice)}, Current Price: ${String.format("%.2f", asset.currentPrice)}, Gain/Loss: ${String.format("%.2f", asset.gainloss)}"
    }
    override fun getItemCount(): Int {
        return assets.size
    }
}