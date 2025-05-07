package com.example.cs388_group12_stockpath.ui.home
import com.example.cs388_group12_stockpath.GlobalUserView
import com.example.cs388_group12_stockpath.R
import com.example.cs388_group12_stockpath.databinding.FragmentHomeBinding
import android.content.Intent
import com.example.cs388_group12_stockpath.BuildConfig
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    //*********************************************************************
    //User Login Section
    private val globalUserViewModel: GlobalUserView by activityViewModels()
    //*********************************************************************

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var assetAdapter: AssetAdapter
    private lateinit var recyclerView: RecyclerView

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        //Add order
        val buttonAddOrder: Button = binding.buttonAddOrder
        buttonAddOrder.setOnClickListener {
            Log.d("HomeFragment", "Add Order button clicked")
            val intent = Intent(requireContext(), AddOrderActivity::class.java)
            startActivity(intent)
        }
        //Add alert
        val buttonAddAlert: Button = binding.buttonAddAlert
        buttonAddAlert.setOnClickListener {
            Log.d("HomeFragment", "Add Alert button clicked")
            val intent = Intent(requireContext(), AddAlertActivity::class.java)
            startActivity(intent)
        }
        //Alpha Vantage API Section
        // val API_KEY = BuildConfig.API_KEY
        // Log.d("BuildConfig | AV---->API_KEY", "AV---->API_KEY: " + API_KEY)
        //*********************************************************************
        recyclerView = root.findViewById(R.id.recyclerViewAssets)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        

        //assetAdapter
        assetAdapter = AssetAdapter(emptyList(), globalUserViewModel.priceCache) { asset ->
            val intent = Intent(requireContext(), OrderListActivity::class.java)
            intent.putExtra("asset_sym", asset.sym)
            startActivity(intent)
        }
        recyclerView.adapter = assetAdapter

        //getassets
        //get assets and update adapter
        globalUserViewModel.assets.observe(viewLifecycleOwner) { assets ->
            // val prices = globalUserViewModel.currentPrices.value
            // assets?.forEach { asset ->
            //     asset.currentPrice = prices?.get(asset.sym) ?: asset.currentPrice
            // }
            if (assets != null) {
                assetAdapter.updateAssets(assets)
                Log.d("HomeFragment", "Assets updated: $assets")
            } else {
                Log.d("HomeFragment", "Assets: null")
            }
            //assetAdapter.updateAssets(assets ?: emptyList())
        }

        
        //globalUserViewModel.calculateAssetsFromOrders()
        
        globalUserViewModel.assets.observe(viewLifecycleOwner){ updatedPrices ->
            if (updatedPrices != null) {
                assetAdapter.updateAssets(updatedPrices)
            }
            Log.d("HomeFragment RecyclerView assetadapter", "Assets updated in RecyclerView: $updatedPrices")
        }

        //dummy data for testing
//        val dummyAssets = listOf(
//            Asset(sym = "AAPL", totalQuantity = 10.0, averagePrice = 150.0, currentPrice = 160.0, gainloss = 4.0),
//            Asset(sym = "GOOGL", totalQuantity = 5.0, averagePrice = 2800.0, currentPrice = 160.0, gainloss = 4.0),
//            Asset(sym = "TSLA", totalQuantity = 2.0, averagePrice = 700.0, currentPrice = 160.0, gainloss = 4.0),
//        )
//
//        val adapter = AssetAdapter(dummyAssets)
//        recyclerView.adapter = adapter

        // Observe alerts and log them or handle them as needed
        globalUserViewModel.getAlerts()
        globalUserViewModel.alerts.observe(viewLifecycleOwner) { alerts ->
            Log.d("HomeFragment", "Fetched alerts: $alerts")
        //display alerts in a RecyclerView/ui
        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updatePricesInAdapters(prices: Map<String, Double>) {
        assetAdapter.updatePrices(prices)
        Log.d("HomeFragment", "Prices updated in adapter: $prices")
    }

}