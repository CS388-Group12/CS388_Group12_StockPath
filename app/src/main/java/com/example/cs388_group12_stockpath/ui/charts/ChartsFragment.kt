package com.example.cs388_group12_stockpath.ui.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs388_group12_stockpath.R
import com.example.cs388_group12_stockpath.StockFuncs
import com.example.cs388_group12_stockpath.Candlestick
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.example.cs388_group12_stockpath.databinding.FragmentChartsBinding
import com.example.cs388_group12_stockpath.ui.home.AssetAdapter
import com.example.cs388_group12_stockpath.GlobalUserView

class ChartsFragment : Fragment() {

    private var _binding: FragmentChartsBinding? = null
    private val binding get() = _binding!!

    private lateinit var stockFuncs: StockFuncs
    private lateinit var candlestickChart: CandleStickChart
    private lateinit var symbolInput: EditText
    private lateinit var searchButton: Button
    private lateinit var selectedInfoTextView: TextView

    private val globalUserViewModel: GlobalUserView by activityViewModels()
    private lateinit var assetAdapter: AssetAdapter

    private var selectedInterval: String = "1d" // Default interval
    private var selectedRange: String = "1mo"  // Default range
    private var currentlySelectedSymbol: String? = null // Track the currently selected symbol

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize views
        candlestickChart = root.findViewById(R.id.candlestick_chart)
        symbolInput = root.findViewById(R.id.symbol_input)
        searchButton = root.findViewById(R.id.search_button)
        selectedInfoTextView = root.findViewById(R.id.selected_info)

        // Initialize StockFuncs
        stockFuncs = StockFuncs()

        // Set up search button click listener
        searchButton.setOnClickListener {
            val symbol = symbolInput.text.toString().trim()
            if (symbol.isNotEmpty()) {
                validateAndFetchData(symbol)
            } else {
                Toast.makeText(requireContext(), "Please enter a symbol", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up time interval buttons
        root.findViewById<Button>(R.id.button_1d).setOnClickListener { updateInterval("1d", "1mo") }
        root.findViewById<Button>(R.id.button_1w).setOnClickListener { updateInterval("1d", "1wk") }
        root.findViewById<Button>(R.id.button_1m).setOnClickListener { updateInterval("1d", "1mo") }
        root.findViewById<Button>(R.id.button_1y).setOnClickListener { updateInterval("1wk", "1y") }

        val recyclerView: RecyclerView = binding.recyclerViewAssets
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize AssetAdapter
        assetAdapter = AssetAdapter(emptyList(), globalUserViewModel.priceCache) { asset ->
            currentlySelectedSymbol = asset.sym // Update the currently selected symbol
            fetchAndDisplayData(asset.sym)
        }
        recyclerView.adapter = assetAdapter

        // Observe assets from GlobalUserView
        globalUserViewModel.assets.observe(viewLifecycleOwner) { assets ->
            if (assets != null) {
                assetAdapter.updateAssets(assets)

                if (assets.isNotEmpty()) {
                    val firstAsset = assets[0]
                    currentlySelectedSymbol = firstAsset.sym // Set the first asset as the default
                    fetchAndDisplayData(firstAsset.sym)
                }
            }
        }

        return root
    }

    private fun updateInterval(interval: String, range: String) {
        selectedInterval = interval
        selectedRange = range

        // Fetch and update the chart for the currently selected asset
        if (currentlySelectedSymbol != null) {
            fetchAndDisplayData(currentlySelectedSymbol!!)
        } else {
            Toast.makeText(requireContext(), "No asset selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateAndFetchData(symbol: String) {
        stockFuncs.sym_search(
            sym = symbol,
            onSuccess = { validSymbols ->
                if (validSymbols.isNotEmpty()) {
                    // Automatically select the top result
                    val topResult = validSymbols[0]
                    currentlySelectedSymbol = topResult // Update the currently selected symbol
                    fetchAndDisplayData(topResult)
                    Toast.makeText(requireContext(), "Selected top result: $topResult", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "No valid symbols found for: $symbol", Toast.LENGTH_SHORT).show()
                }
            },
            onError = { error ->
                Toast.makeText(requireContext(), "Error validating symbol: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun fetchAndDisplayData(symbol: String) {
        stockFuncs.get_historical_data(
            symbol = symbol,
            interval = selectedInterval,
            range = selectedRange,
            onSuccess = { candlesticks ->
                updateChart(candlesticks)
                updateSelectedInfo(symbol) // Update the selected info text
            },
            onError = { error ->
                Toast.makeText(requireContext(), "Error fetching data: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun updateChart(candlesticks: List<Candlestick>) {
        val entries = candlesticks.mapIndexed { index, candlestick ->
            CandleEntry(
                index.toFloat(),
                candlestick.high.toFloat(),
                candlestick.low.toFloat(),
                candlestick.open.toFloat(),
                candlestick.close.toFloat()
            )
        }

        val dataSet = CandleDataSet(entries, "Candlestick Data").apply {
            color = R.color.white
            shadowColor = R.color.white
            shadowWidth = 0.8f
            decreasingColor = resources.getColor(R.color.red, null)
            decreasingPaintStyle = android.graphics.Paint.Style.FILL
            increasingColor = resources.getColor(R.color.green, null)
            increasingPaintStyle = android.graphics.Paint.Style.FILL
            neutralColor = R.color.blue
            valueTextColor = resources.getColor(R.color.white, null)
            valueTextSize = 12f
        }

        val candleData = CandleData(dataSet)
        candlestickChart.data = candleData

        candlestickChart.setBackgroundColor(resources.getColor(R.color.black, null))
        candlestickChart.axisLeft.textColor = resources.getColor(R.color.white, null)
        candlestickChart.axisRight.textColor = resources.getColor(R.color.white, null)
        candlestickChart.xAxis.textColor = resources.getColor(R.color.white, null)
        candlestickChart.legend.textColor = resources.getColor(R.color.white, null)
        candlestickChart.description.textColor = resources.getColor(R.color.white, null)

        candlestickChart.invalidate() // Refresh the chart
    }

    private fun updateSelectedInfo(symbol: String) {
        selectedInfoTextView.text = "Selected: $symbol - $selectedInterval - $selectedRange"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}