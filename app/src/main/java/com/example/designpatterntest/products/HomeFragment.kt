package com.example.designpatterntest.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.designpatterntest.db.ProductDatabase
import com.example.designpatterntest.model.Product
import com.example.designpatterntest.databinding.FragmentHomeBinding
import com.example.designpatterntest.util.OnDeleteClickListener
import com.example.designpatterntest.util.OnFavoriteClickListener
import com.example.designpatterntest.util.ShimmerAdapter
import com.example.designpatterntest.util.makeSnackBar
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), ProductsView, OnFavoriteClickListener, OnDeleteClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: ProductsPresenter
    private lateinit var shimmerAdapter: ShimmerAdapter

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
        setupPresenter()

    }
    private fun setupUi(){
        binding.recyclerView.layoutManager = GridLayoutManager(binding.root.context, 2)
        shimmerAdapter = ShimmerAdapter()
        binding.recyclerViewShimmer.layoutManager = GridLayoutManager(binding.root.context, 2)
        binding.recyclerViewShimmer.adapter = shimmerAdapter
        showShimmer(true)
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
    private fun setupPresenter() {
        val productDao = ProductDatabase.getInstance(binding.root.context).productDao()
        presenter = ProductsPresenter(this, productDao)
        lifecycleScope.launch(Dispatchers.IO) {
            presenter.getFavoriteProducts()
        }

    }


    override fun showProducts(products: List<Product>) {
        showShimmer(false)
        val adapter = ProductAdapter(this@HomeFragment,this@HomeFragment){ product->
            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(product)
            findNavController().navigate(action)
        }
        adapter.submitList( products.filter { it.id !in listOf(6, 9, 19) })
        binding.recyclerView.adapter = adapter
    }

    override fun showMessage(str: String) {
        showShimmer(false)
        makeSnackBar(str,binding.root)
    }

    override fun onFavoriteClick(product: Product) {
        lifecycleScope.launch(Dispatchers.IO) {
            presenter.addProduct(product)
        }
    }

    override fun onDeleteClickListener(id: Int) {
        lifecycleScope.launch(Dispatchers.IO){
            presenter.deleteProduct(id)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}