package com.example.designpatterntest.ui.products

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.designpatterntest.data.model.Product
import com.example.designpatterntest.databinding.FragmentHomeBinding
import com.example.designpatterntest.ui.shared.ProductAdapter
import com.example.designpatterntest.ui.shared.ShimmerAdapter
import com.example.designpatterntest.ui.shared.listeners.OnFavoriteClickListener
import com.example.designpatterntest.util.makeSnackBar
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class HomeFragment : Fragment(), OnFavoriteClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var shimmerAdapter: ShimmerAdapter
    private lateinit var productAdapter: ProductAdapter
    private val viewModel: ProductsViewModel by activityViewModel()

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
        viewModel.getProducts()
        showProducts()
    }

    private fun setupUi() {
        binding.recyclerView.layoutManager = GridLayoutManager(binding.root.context, calculateSpanCount())
        shimmerAdapter = ShimmerAdapter()
        binding.recyclerViewShimmer.layoutManager = GridLayoutManager(binding.root.context, calculateSpanCount())
        binding.recyclerViewShimmer.adapter = shimmerAdapter
    }
    private fun calculateSpanCount(): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val columnWidth = 160 // Adjust based on your design
        return maxOf(2, (screenWidthDp / columnWidth).toInt()) // At least 2 columns
    }


    private fun showShimmer(show: Boolean) {
        if (show) {
            binding.shimmerFrameLayout.visibility = View.VISIBLE
            binding.shimmerFrameLayout.startShimmer()
        } else {
            binding.shimmerFrameLayout.visibility = View.GONE
            binding.shimmerFrameLayout.stopShimmer()
        }
        shimmerAdapter.showShimmer(show)
    }

    private fun showProducts() {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProductsViewState.Loading -> showShimmer(true)
                is ProductsViewState.Success -> {
                    handleSuccessState(state)
                }
                is ProductsViewState.Message -> handleMessageState(state)
                is ProductsViewState.Error -> handleErrorState(state)
            }
        }
    }

    private fun handleSuccessState(state: ProductsViewState.Success) {
        showShimmer(false)
        productAdapter = ProductAdapter(this@HomeFragment) { product ->
            viewModel.setProduct(product)
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment())
        }
        productAdapter.submitList(state.products)
        binding.recyclerView.adapter = productAdapter
    }

    private fun handleMessageState(state: ProductsViewState.Message) {
        when (state.action) {
            ProductsViewState.Action.ADDED_TO_FAVORITES -> makeSnackBar(state.message, binding.recyclerView)
            ProductsViewState.Action.REMOVED_FROM_FAVORITES -> makeSnackBar(state.message, binding.recyclerView)
        }
    }

    private fun handleErrorState(state: ProductsViewState.Error) {
        when (state.type) {
            ProductsViewState.ErrorType.LoadError -> {
                showShimmer(false)
                makeSnackBar(state.message, binding.recyclerView)
            }
            ProductsViewState.ErrorType.AddToFavoriteError -> makeSnackBar(state.message, binding.recyclerView)
            ProductsViewState.ErrorType.DeleteError -> makeSnackBar(state.message, binding.recyclerView)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onFavoriteClick(product: Product) {
        viewModel.toggleFavButton(product)
        if (product.isFavorite) {
            viewModel.addProduct(product)
        } else {
            viewModel.deleteProduct(product.id)
        }
        productAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
