package com.example.cs388_group12_stockpath.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs388_group12_stockpath.NewsItem
import com.example.cs388_group12_stockpath.R
import com.example.cs388_group12_stockpath.StockFuncs
import com.example.cs388_group12_stockpath.GlobalUserView
import com.example.cs388_group12_stockpath.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var stockFuncs: StockFuncs
    private lateinit var newsAdapter: NewsAdapter

    private val globalUserViewModel: GlobalUserView by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize StockFuncs
        stockFuncs = StockFuncs()

        // Initialize RecyclerView
        val recyclerView: RecyclerView = binding.recyclerViewNews
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(emptyList()) { newsItem ->
            // Handle news item click
            Toast.makeText(requireContext(), "Clicked: ${newsItem.title}", Toast.LENGTH_SHORT).show()
            // Navigate to detailed news fragment or open link
        }
        recyclerView.adapter = newsAdapter

        // Observe assets from GlobalUserView
        globalUserViewModel.assets.observe(viewLifecycleOwner) { assets ->
            if (assets != null && assets.isNotEmpty()) {
                fetchNewsForAssets(assets.map { it.sym })
            } else {
                Toast.makeText(requireContext(), "No assets found", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun fetchNewsForAssets(symbols: List<String>) {
        val allNewsItems = mutableListOf<NewsItem>()

        symbols.forEach { symbol ->
            stockFuncs.fetchNews(
                sym = symbol,
                onSuccess = { newsItems ->
                    allNewsItems.addAll(newsItems)
                    newsAdapter.updateNews(allNewsItems) // Update the adapter with all news items
                },
                onError = { error ->
                    Toast.makeText(requireContext(), "Error fetching news for $symbol: $error", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}