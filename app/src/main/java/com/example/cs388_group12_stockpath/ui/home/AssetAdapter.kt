package com.example.cs388_group12_stockpath.ui.home
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.cs388_group12_stockpath.R
import com.example.cs388_group12_stockpath.Asset
import com.example.cs388_group12_stockpath.GlobalUserView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AssetAdapter(private var assets: List<Asset>, private val priceCache: LiveData<MutableMap<String, Double>>, private val onAssetClick: (Asset) -> Unit
) : RecyclerView.Adapter<AssetAdapter.AssetViewHolder>() {

    init {
        priceCache.observeForever { updatedPrices ->
            updatePrices(updatedPrices)
        }
    }

    class AssetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val symbolTextView: TextView = itemView.findViewById(R.id.textViewAssetSymbol)
        val detailsTextView: TextView = itemView.findViewById(R.id.textViewAssetDetails)
        val orderCountTextView: TextView = itemView.findViewById(R.id.textViewOrderCount)
        val currentPriceView: TextView = itemView.findViewById(R.id.currentPrice)
        var priceUpdateJob: Job? = null
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
        holder.currentPriceView.text = "$${String.format("%.2f", asset.currentPrice)}"

        //holder.priceUpdateJob?.cancel()
//         holder.priceUpdateJob = CoroutineScope(Dispatchers.Main).launch {
//             asset.currentPriceFlow.collect { newPrice ->
// //                if(newPrice != 0.0) {
// //                    holder.currentPriceView.text = "$${String.format("%.2f", newPrice)}"
// //                    Log.d("AssetAdapter*", "Price updated for ${asset.sym}: $newPrice")
// //                }
// //                else {
// //                    Log.d("AssetAdapter*", "Price not updated for ${asset.sym}")
// //                }

//                 holder.currentPriceView.text = "$${String.format("%.2f", newPrice)}"
//                 Log.d("AssetAdapter*", "Price updated for ${asset.sym}: $newPrice")
//             }
//         }

        holder.itemView.setOnClickListener {
            onAssetClick(asset)
        }
    }
    override fun onViewRecycled(holder: AssetViewHolder) {
        super.onViewRecycled(holder)
        holder.priceUpdateJob?.cancel()
    }

    override fun getItemCount(): Int {
        return assets.size
    }

    fun updateAssets(newAssets: List<Asset>) {
        assets = newAssets
        notifyDataSetChanged()
    }

    fun updatePrices(prices: Map<String, Double>) {
        assets.forEach { asset ->
            if(asset.currentPrice == prices[asset.sym]){
                Log.d("AssetAdapter", "skip -> Price not updated for ${asset.sym}")
            } else {
                val newPrice = prices[asset.sym]
                if (newPrice != null && newPrice != asset.currentPrice) {
                    asset.updatePrice(newPrice) // Use the updatePrice method
                }
            }
        }
        notifyDataSetChanged()
        Log.d("AssetAdapter", "Prices updated: $prices")
    }

}