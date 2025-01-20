package com.example.designpatterntest.allProducts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.designpatterntest.api.RetrofitHelper
import com.example.designpatterntest.local.ProductDatabase
import com.example.designpatterntest.model.Product
import com.example.designpatterntest.databinding.FragmentHomeBinding
import com.example.designpatterntest.util.OnDeleteClickListener
import com.example.designpatterntest.util.OnFavoriteClickListener
import com.example.designpatterntest.util.makeSnackBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment(), AllProductsView, OnFavoriteClickListener, OnDeleteClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: AllProductsPresenter

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
        binding.recyclerViewProgressBar.visibility = View.VISIBLE
        binding.recyclerView.layoutManager = GridLayoutManager(binding.root.context, 2)
    }
    private fun setupPresenter() {
        val productDao = ProductDatabase.getInstance(binding.root.context).productDao()
        presenter = AllProductsPresenter(this, productDao)
        lifecycleScope.launch(Dispatchers.IO) {
            presenter.getFavoriteProducts()
        }

    }


    override fun showProducts(products: List<Product>) {
        binding.recyclerViewProgressBar.visibility = View.GONE
        val adapter = ProductAdapter(this@HomeFragment,this@HomeFragment)
        adapter.submitList( products.filter { it.id !in listOf(6, 9, 19) })
        binding.recyclerView.adapter = adapter
    }

    override fun showMessage(str: String) {
        binding.recyclerViewProgressBar.visibility = View.GONE
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