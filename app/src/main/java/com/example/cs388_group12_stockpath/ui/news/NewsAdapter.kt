package com.example.cs388_group12_stockpath.ui.news

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs388_group12_stockpath.NewsItem
import com.example.cs388_group12_stockpath.R

class NewsAdapter(
    private var newsList: List<NewsItem>,
    private val onItemClick: (NewsItem) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.newsTitle)
        private val publisherTextView: TextView = itemView.findViewById(R.id.newsPublisher)

        fun bind(newsItem: NewsItem) {
            titleTextView.text = newsItem.title
            publisherTextView.text = newsItem.publisher

            // Open the link in a browser when the item is clicked
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.link))
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size

    fun updateNews(newNews: List<NewsItem>) {
        newsList = newNews
        notifyDataSetChanged()
    }
}