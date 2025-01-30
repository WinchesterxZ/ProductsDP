package com.example.designpatterntest.ui.productsDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.designpatterntest.databinding.ItemCardBinding

class CardAdapter(private val cards: List<String>) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = cards[position]

        // Show the ProgressBar while loading the image
        holder.binding.imageProgressBar.visibility = View.VISIBLE
        holder.binding.imageView.load(imageUrl) {
            // On success, hide the ProgressBar
            listener(
                onSuccess = { _, _ ->
                    holder.binding.imageProgressBar.visibility = View.GONE
                },
                onError = { _, _ ->
                    holder.binding.imageProgressBar.visibility = View.GONE
                }
            )
        }
    }

    override fun getItemCount() = cards.size

    class ViewHolder(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root)
}
