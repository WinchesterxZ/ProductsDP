package com.example.designpatterntest.ui.productsDetails

import ReviewAdapter
import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.designpatterntest.MainActivity
import com.example.designpatterntest.R
import com.example.designpatterntest.data.model.Product
import com.example.designpatterntest.databinding.FragmentProductDetailsBinding
import com.example.designpatterntest.ui.products.ProductsViewModel
import com.example.designpatterntest.ui.products.ProductsViewState
import com.example.designpatterntest.util.makeSnackBar
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ProductDetailsFragment : Fragment() {
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private var originalToolbarBackground: Drawable? = null
    private lateinit var toolbar: MaterialToolbar
    private lateinit var bottomNavBar: BottomNavigationView
    private val productsViewModel: ProductsViewModel by activityViewModel()
    private var originalToolbarHeight: Int = 0
    private lateinit var pageChangeListener: ViewPager2.OnPageChangeCallback
    private lateinit var viewPager2: ViewPager2
    private lateinit var constraintLayout: ConstraintLayout
    private val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        setMargins(8, 0, 8, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeProductDetails()
        observeViewState()
    }

    private fun setupViews() {
        val activityBinding = (requireActivity() as MainActivity).binding
        toolbar = activityBinding.toolbar.root
        originalToolbarHeight = toolbar.layoutParams.height
        originalToolbarBackground = toolbar.background
        bottomNavBar = activityBinding.bottomNav
        viewPager2 = activityBinding.toolbar.viewPager
        constraintLayout = activityBinding.toolbar.viewPagerLayout

        bottomNavBar.visibility = View.GONE
        view?.post {
            toolbar.layoutParams.height = requireView().height / 3
            toolbar.requestLayout()
        }

        toolbar.menu.clear()
        constraintLayout.visibility = View.VISIBLE
        toolbar.inflateMenu(R.menu.product_details_menu)
    }

    private fun observeProductDetails() {
        productsViewModel.product.observe(viewLifecycleOwner) { product ->
            setUpToolbarFavorite(product)
            setupViewPager(product)
            setProductDetails(product)
        }
    }

    private fun setUpToolbarFavorite(product: Product) {
        val favItem = toolbar.menu.findItem(R.id.action_favorite)
        favItem.setIcon(if (product.isFavorite) R.drawable.red_fav else R.drawable.fav_icon_border)
        favItem.setOnMenuItemClickListener {
            productsViewModel.toggleFavButton(product)
            if (product.isFavorite) {
                productsViewModel.addProduct(product)
                favItem.setIcon(R.drawable.red_fav)
            } else {
                productsViewModel.deleteProduct(product.id)
                favItem.setIcon(R.drawable.fav_icon_border)
            }
            true
        }
    }

    private fun setupViewPager(product: Product) {
        viewPager2.adapter = CardAdapter(product.images)
        val slideDotLayout = (requireActivity() as MainActivity).binding.toolbar.slideDot
        slideDotLayout.removeAllViews()
        if(product.images.size > 1) {
            val dotsImage = Array(product.images.size) { ImageView(binding.root.context) }
            dotsImage.forEach {
                it.setImageResource(R.drawable.inactive_dot)
                slideDotLayout.addView(it, params)
            }
            pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateDots(dotsImage, position)
                }
            }


        }else{
            pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            }
        }
        if (viewPager2.adapter != null) {
            viewPager2.registerOnPageChangeCallback(pageChangeListener)
        }
    }

    private fun updateDots(dotsImage: Array<ImageView>, position: Int) {
        dotsImage.mapIndexed { index, imageView ->
            if (position == index) {
                imageView.setImageResource(R.drawable.active_dot)
            } else {
                imageView.setImageResource(R.drawable.inactive_dot)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setProductDetails(product: Product) {
        binding.productName.text = product.title
        handleReadMore(product.description)
        binding.productPrice.text = "$${calculateDiscountedPrice(product.price, product.discountPercentage)}"
        binding.originalPrice.text = "$${product.price}"
        binding.originalPrice.paintFlags = binding.originalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.rateText.text = product.rating.toString()
        binding.reviewNumber.text = "${product.reviews.size} Reviews"
        binding.availablePeaces.text = "${product.stock} Available"
        binding.availability.text = product.availabilityStatus
        binding.availability.setTextColor(
            if (product.availabilityStatus == "In Stock") {
                ContextCompat.getColor(requireContext(), R.color.green)
            } else {
                ContextCompat.getColor(requireContext(), R.color.red)
            })
        binding.reviewRecyclerView.adapter = ReviewAdapter(product.reviews)
        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.reviewRecyclerView.setHasFixedSize(true)
        binding.reviewRecyclerView.isNestedScrollingEnabled = false
    }

    private fun observeViewState() {
        productsViewModel.viewState.observe(viewLifecycleOwner) { state ->
            if (state is ProductsViewState.Message) {
                when (state.action) {
                    ProductsViewState.Action.ADDED_TO_FAVORITES -> {
                        makeSnackBar(state.message, binding.root)
                    }
                    ProductsViewState.Action.REMOVED_FROM_FAVORITES -> {
                        makeSnackBar(state.message, binding.root)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        toolbar.menu.clear()
        toolbar.layoutParams.height = originalToolbarHeight
        toolbar.background = originalToolbarBackground
        bottomNavBar.visibility = View.VISIBLE
        constraintLayout.visibility = View.GONE
    }

    @SuppressLint("DefaultLocale")
    private fun calculateDiscountedPrice(price: Double, discountPercentage: Double): String {
        val discountedPrice = price * (1 - discountPercentage / 100)
        return String.format("%.2f", discountedPrice)
    }


    private fun handleReadMore(description: String) {
        if (description.length > 160) {
            val shortText = description.substring(0, 140) + "… "
            val readMore = "Read More"
            val spannableString = SpannableString(shortText + readMore)

            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.blue)),
                shortText.length,
                spannableString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    showFullDescription(description)
                }
            }

            spannableString.setSpan(
                clickableSpan,
                shortText.length,
                spannableString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            binding.productDescription.text = spannableString
            binding.productDescription.movementMethod = android.text.method.LinkMovementMethod.getInstance()
        } else {
            binding.productDescription.text = description
        }
    }

    private fun showFullDescription(description: String) {
        val fullSpannable = SpannableString("$description Read Less")

        fullSpannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.blue)),
            description.length + 1,
            fullSpannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                handleReadMore(description)
            }
        }

        fullSpannable.setSpan(
            clickableSpan,
            description.length + 1,
            fullSpannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.productDescription.text = fullSpannable
        binding.productDescription.maxLines = Int.MAX_VALUE
        binding.productDescription.movementMethod = android.text.method.LinkMovementMethod.getInstance()
    }
}
