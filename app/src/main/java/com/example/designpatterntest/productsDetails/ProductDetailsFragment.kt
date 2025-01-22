package com.example.designpatterntest.productsDetails

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.designpatterntest.MainActivity
import com.example.designpatterntest.R
import com.example.designpatterntest.databinding.FragmentProductDetailsBinding
import com.google.android.material.appbar.MaterialToolbar

class ProductDetailsFragment : Fragment() {
    private  var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private var originalToolbarHeight: Int = 0
    private var originalToolbarBackground: Drawable? = null
    private lateinit var toolbar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = (requireActivity() as MainActivity).findViewById<MaterialToolbar>(R.id.toolbar)
        originalToolbarHeight = toolbar.layoutParams.height
        originalToolbarBackground = toolbar.background
        toolbar.layoutParams.height = 300
        // Set a background image on the Toolbar
        val product = arguments?.let {
            ProductDetailsFragmentArgs.fromBundle(it).product
        }
        if(product!=null){
            binding.test.text = product.title
            Glide.with(requireContext())
                .load(product.thumbnail)
                .override(300, 300) // Set custom size (300x300)
                .apply(RequestOptions().centerCrop())
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        // Set the loaded image as the Toolbar's background
                        toolbar.background = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Handle placeholder or cleanup
                    }
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        toolbar.layoutParams.height = originalToolbarHeight
        toolbar.background = originalToolbarBackground
    }


}