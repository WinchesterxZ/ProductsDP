package com.example.designpatterntest

import ProductAdapter
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.recyclerView
        val recyclerViewProgressBar = binding.recyclerViewProgressBar
        recyclerView.layoutManager = GridLayoutManager(binding.root.context, 2)
        getProducts(recyclerViewProgressBar)

    }

    private fun getProducts(recyclerViewProgressBar: ProgressBar) {
        recyclerViewProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            val products = RetrofitHelper.retrofitService.getProducts()
            val favList = ProductDatabase.getInstance(binding.root.context).productDao().getFavoriteProducts()
            if(products.isSuccessful){
                withContext(Dispatchers.Main){
                    recyclerViewProgressBar.visibility = View.GONE
                    val productList = products.body()?.products
                    if (productList != null) {
                        val favIds = favList.map { it.id }.toSet()
                        productList.forEach {
                            it.isFavorite = it.id in favIds
                        }
                        val productDao = ProductDatabase.getInstance(binding.root.context).productDao()
                        val adapter = ProductAdapter(object : OnFavoriteClickListener {
                            override fun onFavoriteClick(product: Product) {
                                lifecycleScope.launch(Dispatchers.IO) {
                                    productDao.insertProduct(product)
                                }

                            }
                        },
                            object : OnDeleteClickListener {
                                override fun onDeleteClickListener(id: Int) {
                                    lifecycleScope.launch(Dispatchers.IO){
                                        productDao.deleteProduct(id)
                                    }
                                }
                            })

                        adapter.submitList( productList.filter { it.id !in listOf(6, 9, 19) })
                        binding.recyclerView.adapter = adapter
                    }
                }
            }else{
                withContext(Dispatchers.Main){
                    recyclerViewProgressBar.visibility = View.GONE
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}