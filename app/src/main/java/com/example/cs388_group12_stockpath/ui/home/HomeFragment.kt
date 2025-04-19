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
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeFragment : Fragment() {
    //*********************************************************************
    //User Login Section
    private val globalUserViewModel: GlobalUserView by activityViewModels()
    //*********************************************************************

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
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
        val API_KEY = BuildConfig.API_KEY
        Log.d("BuildConfig | AV---->API_KEY", "AV---->API_KEY: " + API_KEY)
        //*********************************************************************
        recyclerView = root.findViewById(R.id.recyclerViewAssets)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //getassets
        //globalUserViewModel.assets.observe(viewLifecycleOwner) { assets ->
        //val adapter = AssetAdapter(assets)
        //recyclerView.adapter = adapter
        //}

        globalUserViewModel.assets.observe(viewLifecycleOwner) { assets ->
            val adapter = AssetAdapter(assets) { asset ->
                //orderlist expands to display
                val intent = Intent(requireContext(), OrderListActivity::class.java)
                intent.putExtra("asset_sym", asset.sym)
                startActivity(intent)
            }
            recyclerView.adapter = adapter
        }

        //getorders and calculate assets
        globalUserViewModel.getOrders()
        globalUserViewModel.orders.observe(viewLifecycleOwner) {
            globalUserViewModel.calculateAssetsFromOrders()
        }
//        val dummyAssets = listOf(
//            Asset(sym = "AAPL", totalQuantity = 10.0, averagePrice = 150.0, currentPrice = 160.0, gainloss = 4.0),
//            Asset(sym = "GOOGL", totalQuantity = 5.0, averagePrice = 2800.0, currentPrice = 160.0, gainloss = 4.0),
//            Asset(sym = "TSLA", totalQuantity = 2.0, averagePrice = 700.0, currentPrice = 160.0, gainloss = 4.0),
//        )
//
//        val adapter = AssetAdapter(dummyAssets)
//        recyclerView.adapter = adapter
        //----textview section----


        //*********************************************************************

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}