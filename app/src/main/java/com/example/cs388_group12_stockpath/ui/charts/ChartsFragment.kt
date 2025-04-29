package com.example.cs388_group12_stockpath.ui.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cs388_group12_stockpath.R
import com.example.cs388_group12_stockpath.StockFuncs
import com.example.cs388_group12_stockpath.Candlestick
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.example.cs388_group12_stockpath.databinding.FragmentChartsBinding
import com.example.cs388_group12_stockpath.databinding.FragmentHomeBinding
import com.example.cs388_group12_stockpath.ui.home.HomeViewModel


class ChartsFragment : Fragment() {

    private var _binding: FragmentChartsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var stockFuncs: StockFuncs
    private lateinit var candlestickChart: CandleStickChart
    private lateinit var symbolInput: EditText
    private lateinit var searchButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val root = inflater.inflate(R.layout.fragment_charts, container, false)


        val chartsViewModel =
        ViewModelProvider(this).get(ChartsViewModel::class.java)

        _binding = FragmentChartsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize views
        candlestickChart = root.findViewById(R.id.candlestick_chart)
        symbolInput = root.findViewById(R.id.symbol_input)
        searchButton = root.findViewById(R.id.search_button)

        // Initialize StockFuncs
        stockFuncs = StockFuncs()

        // Set up search button click listener
        searchButton.setOnClickListener {
            val symbol = symbolInput.text.toString().trim()
            if (symbol.isNotEmpty()) {
                fetchAndDisplayData(symbol)
            }
        }

        return root
    }

    private fun fetchAndDisplayData(symbol: String) {
        stockFuncs.get_historical_data(
            symbol = symbol,
            interval = "1d",
            range = "1mo",
            onSuccess = { candlesticks ->
                updateChart(candlesticks)
            },
            onError = { error ->
                // Handle error (e.g., show a Toast or log the error)
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
            color = R.color.black
            shadowColor = R.color.black
            shadowWidth = 0.8f
            decreasingColor = resources.getColor(R.color.red, null)
            decreasingPaintStyle = android.graphics.Paint.Style.FILL
            increasingColor = resources.getColor(R.color.green, null)
            increasingPaintStyle = android.graphics.Paint.Style.FILL
            neutralColor = R.color.blue
        }

        val candleData = CandleData(dataSet)
        candlestickChart.data = candleData
        candlestickChart.invalidate() // Refresh the chart
    }
}

