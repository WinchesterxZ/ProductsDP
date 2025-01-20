package com.example.designpatterntest

import ProductAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.designpatterntest.local.ProductDatabase
import com.example.designpatterntest.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductAdapter

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
        getFavoriteProducts(recyclerViewProgressBar)
    }

    private fun getFavoriteProducts(recyclerViewProgressBar: ProgressBar) {
        recyclerViewProgressBar.visibility = View.VISIBLE
        val productDao = ProductDatabase.getInstance(binding.root.context).productDao()
        lifecycleScope.launch(Dispatchers.IO) {
            val products = productDao.getFavoriteProducts()
            withContext(Dispatchers.Main) {
                recyclerViewProgressBar.visibility = View.GONE
                 adapter = ProductAdapter(null,object : OnDeleteClickListener {
                     override fun onDeleteClickListener(id: Int) {
                         lifecycleScope.launch(Dispatchers.IO) {
                             productDao.deleteProduct(id)
                             val updatedProducts = productDao.getFavoriteProducts()
                             withContext(Dispatchers.Main){
                                 adapter.submitList(updatedProducts)
                             }
                         }
                     }
                 })
                adapter.submitList(products)
                binding.recyclerView.adapter = adapter
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}