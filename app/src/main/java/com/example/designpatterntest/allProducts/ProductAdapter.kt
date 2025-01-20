package com.example.designpatterntest.allProducts

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.designpatterntest.util.OnDeleteClickListener
import com.example.designpatterntest.util.OnFavoriteClickListener
import com.example.designpatterntest.R
import com.example.designpatterntest.model.Product
import com.example.designpatterntest.databinding.ProductItemBinding

class ProductAdapter(
    private val onFavoriteClickListener: OnFavoriteClickListener?= null
    ,private val onDeleteClickListener: OnDeleteClickListener
) :
    ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()){

    class ProductViewHolder(private val binding: ProductItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product, onFavoriteClickListener: OnFavoriteClickListener?= null, onDeleteClickListener: OnDeleteClickListener) {
            binding.productName.text = product.title
            binding.productPrice.text = "$${product.price}"
            binding.rateText.text = product.rating.toString()
            val favoriteBtn = binding.favoriteButton
            val imageProgressBar = binding.imageProgressBar
            imageProgressBar.visibility = View.VISIBLE
            Glide.with(binding.root.context)
                .load(product.thumbnail)
                .apply(RequestOptions.centerCropTransform())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageProgressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageProgressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(binding.productImage)
            favoriteBtn.setImageResource(
                if (product.isFavorite) R.drawable.red_fav else R.drawable.fav_icon_border
            )
            favoriteBtn.setOnClickListener {
                product.isFavorite = !product.isFavorite
                favoriteBtn.setImageResource(
                    if (product.isFavorite) R.drawable.red_fav else R.drawable.fav_icon_border
                )
                if(product.isFavorite){
                    onFavoriteClickListener?.onFavoriteClick(product)
                }else{
                    onDeleteClickListener.onDeleteClickListener(product.id)
                }
            }

        }
    }



    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position),onFavoriteClickListener,onDeleteClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }
    class ProductDiffCallback: DiffUtil.ItemCallback<Product>() {
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

    }


}