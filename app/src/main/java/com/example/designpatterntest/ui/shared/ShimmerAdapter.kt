package com.example.designpatterntest.util

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.designpatterntest.R

class ShimmerAdapter : RecyclerView.Adapter<ShimmerAdapter.ShimmerViewHolder>() {

    private var isShimmerEnabled = true
    private val shimmerItemCount = 8

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item_holder, parent, false)
        return ShimmerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {
        // No need to bind data for shimmer items
    }

    override fun getItemCount(): Int {
        return if (isShimmerEnabled) shimmerItemCount else 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showShimmer(show: Boolean) {
        isShimmerEnabled = show
        notifyDataSetChanged()
    }

    class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}