package com.example.designpatterntest.products

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.designpatterntest.data.db.ProductDatabase
import com.example.designpatterntest.data.model.Product
import com.example.designpatterntest.databinding.FragmentHomeBinding
import com.example.designpatterntest.util.OnDeleteClickListener
import com.example.designpatterntest.util.OnFavoriteClickListener
import com.example.designpatterntest.util.ShimmerAdapter
import com.example.designpatterntest.util.makeSnackBar
import androidx.navigation.fragment.findNavController
import com.example.designpatterntest.data.network.RetrofitHelper


class HomeFragment : Fragment(), OnFavoriteClickListener, OnDeleteClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var shimmerAdapter: ShimmerAdapter
    private lateinit var viewModel: ProductsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupViewModel()
        viewModel.getProducts()
        showProducts()
    }
    private fun setupUi(){
        binding.recyclerView.layoutManager = GridLayoutManager(binding.root.context, 2)
        shimmerAdapter = ShimmerAdapter()
        binding.recyclerViewShimmer.layoutManager = GridLayoutManager(binding.root.context, 2)
        binding.recyclerViewShimmer.adapter = shimmerAdapter
    }
    private fun showShimmer(show: Boolean) {
        if (show) {
            binding.shimmerFrameLayout.visibility = View.VISIBLE
            binding.shimmerFrameLayout.startShimmer() // Start shimmer animation
        } else {
            binding.shimmerFrameLayout.visibility = View.GONE
            binding.shimmerFrameLayout.stopShimmer() // Stop shimmer animation
        }
        shimmerAdapter.showShimmer(show) // Enable/disable shimmer in adapter
    }
    private fun setupViewModel() {
       val retrofit = RetrofitHelper.retrofitService
        val productDao = ProductDatabase.getInstance(binding.root.context).productDao()
        val viewModelFactory = ViewModelFactory(productDao,retrofit)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProductsViewModel::class.java]
    }


    private fun showProducts() {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProductsViewState.Loading -> {
                    showShimmer(true) // Show shimmer loading effect
                }
                is ProductsViewState.Success -> {
                    showShimmer(false) // Hide shimmer loading effect
                    val adapter = ProductAdapter(this@HomeFragment, this@HomeFragment) { product ->
                        val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(product)
                        findNavController().navigate(action)
                    }
                    adapter.submitList(state.products)
                    binding.recyclerView.adapter = adapter
                }
                is ProductsViewState.ProductAdded -> {
                    makeSnackBar(state.message, binding.recyclerView)
                }
                is ProductsViewState.ProductRemoved -> {
                    makeSnackBar(state.message, binding.recyclerView)
                }
                is ProductsViewState.LoadError -> {
                    showShimmer(false)
                    makeSnackBar(state.message, binding.recyclerView)
                }
                is ProductsViewState.DeleteError -> {
                    makeSnackBar(state.message, binding.recyclerView)
                }
                is ProductsViewState.AddToFavoriteError -> {
                    makeSnackBar(state.message, binding.recyclerView)
                }
            }
        }

    }



    override fun onFavoriteClick(product: Product) {
        viewModel.addProduct(product)
    }

    override fun onDeleteClickListener(id: Int) {
        viewModel.deleteProduct(id)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}