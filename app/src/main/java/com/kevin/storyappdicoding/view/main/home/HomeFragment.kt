package com.kevin.storyappdicoding.view.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevin.storyappdicoding.adapter.StoriesAdapter
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.databinding.FragmentHomeBinding
import com.kevin.storyappdicoding.view.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    var scrollToTop: Boolean = false
    private val adapter = StoriesAdapter {
        DetailBottomDialogFragment.newInstance(it.id)
            .show(parentFragmentManager, "story-detail-${it.id}")
    }
    val viewModel: HomeViewModel by viewModels()

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
            rvHome.adapter = adapter
        }
        initObserver()
        initListener()
        viewModel.getStories()
    }

    fun refreshStories() {
        viewModel.getStories()
        scrollToTop = true
    }

    private fun initObserver() {
        viewModel.storiesResult.observe(viewLifecycleOwner) {
            binding.apply {
                loading.root.isVisible = it is ApiResponse.Loading
                error.root.isVisible = it is ApiResponse.Error
                when (it) {
                    is ApiResponse.Loading -> {
                        rvHome.isVisible = false
                    }
                    is ApiResponse.Success -> {
                        root.isRefreshing = false
                        val stories =
                            it.data?.listStory
                        rvHome.isVisible = !stories.isNullOrEmpty()
                        empty.root.isVisible = !rvHome.isVisible
                        if (!stories.isNullOrEmpty()) {
                            adapter.submitList(stories) {
                                if (scrollToTop) {
                                    (rvHome.layoutManager as LinearLayoutManager).smoothScrollToPosition(
                                        rvHome,
                                        null,
                                        0
                                    )
                                    scrollToTop = false
                                }
                            }
                        }
                    }
                    is ApiResponse.Error -> {
                        root.isRefreshing = false
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    private fun initListener() {
        binding.apply {
            root.setOnRefreshListener {
                viewModel.getStories()
            }
        }
    }
}