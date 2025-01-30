package com.example.designpatterntest.ui.shared
import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.designpatterntest.R
import com.example.designpatterntest.data.model.Product
import com.example.designpatterntest.databinding.ProductItemBinding
import com.example.designpatterntest.ui.shared.listeners.OnFavoriteClickListener

class ProductAdapter(
    private val onFavoriteClickListener: OnFavoriteClickListener,
    private val onItemClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    inner class ProductViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) = binding.apply {
            productName.text = product.title
            productPrice.text = "$${product.price}"
            //originalProductPrice.paintFlags = originalProductPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            rateText.text = product.rating.toString()
            productImage.load(product.thumbnail) {
                crossfade(true) // Optional: Enable crossfade animation
                transformations(CircleCropTransformation()) // Optional: Apply transformations
                listener(
                    onStart = { imageProgressBar.visibility = View.VISIBLE },
                    onSuccess = { _, _ -> imageProgressBar.visibility = View.GONE },
                    onError = { _, _ -> imageProgressBar.visibility = View.GONE }
                )
            }

            // Set favorite icon and click listener
            setFavoriteIcon(product)
            favoriteButton.setOnClickListener {
                toggleFavorite(product)
            }
            // Set item click listener
            root.setOnClickListener { onItemClick(product) }
        }

        private fun setFavoriteIcon(product: Product) {
            binding.favoriteButton.setImageResource(
                if (product.isFavorite) R.drawable.red_fav else R.drawable.fav_icon_border
            )
        }

        private fun toggleFavorite(product: Product) {
            onFavoriteClickListener.onFavoriteClick(product)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
