package com.kevin.storyappdicoding.view.main.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.kevin.storyappdicoding.adapter.StoriesAdapter
import com.kevin.storyappdicoding.data.model.Response
import com.kevin.storyappdicoding.databinding.FragmentHomeBinding
import com.kevin.storyappdicoding.view.common.BaseFragment
import com.kevin.storyappdicoding.view.maps.MapsActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private var scrollToTop: Boolean = false
    private val adapter = StoriesAdapter {
        DetailBottomDialogFragment.newInstance(it.id)
            .show(parentFragmentManager, "story-detail-${it.id}")
    }
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    root.isEnabled = !rvHome.canScrollVertically(-1) && dy < 0
                }
            })
            rvHome.adapter = adapter
        }
        initObserver()
        initListener()
        if (savedInstanceState == null) {
            viewModel.getStories()
        }
    }

    fun refreshStories() {
        scrollToTop = true
        viewModel.getStories()
    }

    private fun initObserver() {
        viewModel.storiesResult.observe(viewLifecycleOwner) {
            binding.apply {
                root.isRefreshing = false
                loading.root.isVisible = it is Response.Loading
                error.root.isVisible = it is Response.Error
                empty.root.isVisible = false
                rvHome.isVisible = false

                when (it) {
                    is Response.Loading -> {}
                    is Response.Error -> {
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                    is Response.Success -> {
                        empty.root.isVisible = it.data.isNullOrEmpty()
                        rvHome.isVisible = !empty.root.isVisible
                        adapter.submitList(it.data)
                    }
                }
            }
            if (scrollToTop) {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.rvHome.smoothScrollToPosition(0)
                    scrollToTop = false
                }, 500)
            }
        }
    }

    private fun initListener() {
        binding.apply {
            root.setOnRefreshListener {
                viewModel.getStories()
            }

            showOnMap.setOnClickListener {
                startActivity(
                    Intent(requireContext(), MapsActivity::class.java)
                )
            }
        }
    }
}