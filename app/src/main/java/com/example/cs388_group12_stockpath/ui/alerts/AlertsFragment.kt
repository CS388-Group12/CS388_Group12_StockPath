package com.example.cs388_group12_stockpath.ui.alerts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs388_group12_stockpath.GlobalUserView
import com.example.cs388_group12_stockpath.databinding.FragmentAlertsBinding
import com.example.cs388_group12_stockpath.ui.home.AddAlertActivity
import com.example.cs388_group12_stockpath.ui.home.AddOrderActivity

class AlertsFragment : Fragment() {

    private val globalUserViewModel: GlobalUserView by activityViewModels()

    private var _binding: FragmentAlertsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val alertsViewModel =
            ViewModelProvider(this).get(AlertsViewModel::class.java)

        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAlerts
        alertsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val recyclerView = binding.recyclerViewAlerts
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //
        globalUserViewModel.alerts.observe(viewLifecycleOwner) { alerts ->
            val adapter = AlertAdapter(alerts, globalUserViewModel.priceCache) { alert ->
                // Handle alert click (e.g., expand for detail view)
                Toast.makeText(requireContext(), "Clicked on alert for ${alert.sym}", Toast.LENGTH_SHORT).show()
            }
            recyclerView.adapter = adapter
        }

        globalUserViewModel.priceCache.observe(viewLifecycleOwner) { updatedPrices: MutableMap<String, Double> ->
            val adapter = recyclerView.adapter as AlertAdapter
            adapter.updatePrices(updatedPrices.toMutableMap())
            Log.d("AlertsFragment", "Prices updated for alerts: $updatedPrices")
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

        //init getalerts
        globalUserViewModel.getAlerts()


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}