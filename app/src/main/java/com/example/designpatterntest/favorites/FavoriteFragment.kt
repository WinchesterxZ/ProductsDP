package com.example.designpatterntest.favorites

import com.example.designpatterntest.products.ProductAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.designpatterntest.databinding.FragmentFavoriteBinding
import com.example.designpatterntest.db.ProductDatabase
import com.example.designpatterntest.model.Product
import com.example.designpatterntest.util.OnDeleteClickListener
import com.example.designpatterntest.util.makeSnackBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment(),FavoritesView,OnDeleteClickListener {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: FavoritePresenter

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
        setupPresenter()

    }
    private fun setupUi(){
        binding.defaultImage.visibility = View.GONE
        binding.defaultText.visibility = View.GONE
        binding.recyclerViewProgressBar.visibility = View.VISIBLE
        binding.recyclerView.layoutManager = GridLayoutManager(binding.root.context, 2)
    }
    private fun setupPresenter() {
        val productDao = ProductDatabase.getInstance(binding.root.context).productDao()
        presenter = FavoritePresenter(this, productDao)
        lifecycleScope.launch(Dispatchers.IO) {
            presenter.getFavoriteProducts()
        }
    }


    override fun getFavoriteProducts(products: List<Product>) {
        binding.recyclerViewProgressBar.visibility = View.GONE
        if (products.isEmpty()){
            binding.defaultImage.visibility = View.VISIBLE
            binding.defaultText.visibility = View.VISIBLE
        }
        val adapter = ProductAdapter(onDeleteClickListener = this@FavoriteFragment){
            //
        }
        adapter.submitList(products)
        binding.recyclerView.adapter = adapter
    }

    override fun showMessage(str: String) {
        binding.recyclerViewProgressBar.visibility = View.GONE
        makeSnackBar(str,binding.root)
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