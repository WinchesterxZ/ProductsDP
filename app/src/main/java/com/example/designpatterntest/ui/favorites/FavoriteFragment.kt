package com.example.designpatterntest.favorites

import com.example.designpatterntest.products.ProductAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.designpatterntest.databinding.FragmentFavoriteBinding
import com.example.designpatterntest.data.db.ProductDatabase
import com.example.designpatterntest.data.model.Product
import com.example.designpatterntest.util.OnDeleteClickListener
import com.example.designpatterntest.util.makeSnackBar

class FavoriteFragment : Fragment(),OnDeleteClickListener {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoriteProductsViewModel
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
        setupViewModel()
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
    private fun setupViewModel() {
        val productDao = ProductDatabase.getInstance(binding.root.context).productDao()
        val viewModelFactory = FavProductsViewModelFactory(productDao)
        viewModel = ViewModelProvider(this, viewModelFactory)[FavoriteProductsViewModel::class.java]
    }

    private fun showFavoriteProducts() {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoriteProductsViewState.Loading -> showLoading()
                is FavoriteProductsViewState.Success -> showProducts(state.products)
                is FavoriteProductsViewState.Empty -> showEmptyState()
                is FavoriteProductsViewState.ProductRemoved -> showSnackBar(state.message)
                is FavoriteProductsViewState.LoadError -> showError(state.message)
                is FavoriteProductsViewState.DeleteError -> showError(state.message)
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
        val adapter = ProductAdapter(onDeleteClickListener = this@FavoriteFragment) {}
        adapter.submitList(products)
        binding.recyclerView.adapter = adapter
    }

    private fun showEmptyState() {
        binding.recyclerViewProgressBar.visibility = View.GONE
        binding.defaultImage.visibility = View.VISIBLE
        binding.defaultText.visibility = View.VISIBLE
        (binding.recyclerView.adapter as? ProductAdapter)?.submitList(emptyList())
    }

    private fun showSnackBar(message: String) {
        makeSnackBar(message, binding.recyclerView)
    }

    private fun showError(message: String) {
        binding.recyclerViewProgressBar.visibility = View.GONE
        makeSnackBar(message, binding.recyclerView)
    }




    override fun onDeleteClickListener(id: Int) {
        viewModel.deleteProduct(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}