package com.example.designpatterntest.ui.favorites

import android.annotation.SuppressLint
import com.example.designpatterntest.ui.shared.ProductAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.designpatterntest.databinding.FragmentFavoriteBinding
import com.example.designpatterntest.data.model.Product
import com.example.designpatterntest.ui.shared.listeners.OnFavoriteClickListener
import com.example.designpatterntest.util.makeSnackBar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteFragment : Fragment(), OnFavoriteClickListener {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteProductsViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        viewModel.getFavoriteProducts()
        showFavoriteProducts()

    }
    private fun setupUi(){
        binding.defaultImage.visibility = View.GONE
        binding.defaultText.visibility = View.GONE
        Log.d("alo", "setupUi: hidden done")
        binding.recyclerViewProgressBar.visibility = View.VISIBLE
        binding.recyclerView.layoutManager = GridLayoutManager(binding.root.context, 2)
    }


    private fun showFavoriteProducts() {
        lifecycleScope.launch {
            viewModel.viewState.collect() { state ->
                Log.d("getFavoriteProducts", "showFavoriteProducts: $state")
                when (state) {
                    is FavoriteProductsViewState.Loading -> showLoading()
                    is FavoriteProductsViewState.Success -> showProducts(state.products)
                    is FavoriteProductsViewState.Empty -> showEmptyState()
                    is FavoriteProductsViewState.ProductRemoved -> {
                        showSnackBar(state.message)
                        viewModel.getFavoriteProducts()
                    }
                    is FavoriteProductsViewState.LoadError -> showError(state.message)
                    is FavoriteProductsViewState.DeleteError -> showError(state.message)
                }

            }
        }
    }

    private fun showLoading() {
        binding.recyclerViewProgressBar.visibility = View.VISIBLE
        binding.defaultImage.visibility = View.GONE
        binding.defaultText.visibility = View.GONE
    }
    private fun showProducts(products: List<Product>) {
        binding.recyclerViewProgressBar.visibility = View.GONE
        binding.defaultImage.visibility = View.GONE
        binding.defaultText.visibility = View.GONE
        val adapter = ProductAdapter(onFavoriteClickListener = this@FavoriteFragment) {}
        Log.d("getFavoriteProducts", "showProducts: + ${products.size} ")
        adapter.submitList(products)
        binding.recyclerView.adapter = adapter
    }

    private fun showEmptyState() {
        binding.recyclerViewProgressBar.visibility = View.GONE
        binding.defaultImage.visibility = View.VISIBLE
        binding.defaultText.visibility = View.VISIBLE
        Log.d("getFavoriteProducts", "Empty called")
        (binding.recyclerView.adapter as? ProductAdapter)?.submitList(emptyList())
    }

    private fun showSnackBar(message: String) {
        Log.d("getFavoriteProducts", "showSnackBar: $message")
        makeSnackBar(message, binding.recyclerView)
    }

    private fun showError(message: String) {
        binding.recyclerViewProgressBar.visibility = View.GONE
        makeSnackBar(message, binding.recyclerView)
    }

    override fun onFavoriteClick(product: Product) {
        viewModel.toggleFavButton(product)
        viewModel.deleteProduct(product.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}