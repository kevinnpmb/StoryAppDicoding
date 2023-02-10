package com.kevin.storyappdicoding.view.main.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.kevin.storyappdicoding.adapter.StoriesAdapter
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.databinding.FragmentHomeBinding
import com.kevin.storyappdicoding.view.common.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddStoryBottomDialogFragment : BaseBottomSheetDialogFragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: AddStoryViewModel by viewModels()
    private val adapter = StoriesAdapter {

    }

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
                            adapter.submitList(stories)
                        }
                    }
                    is ApiResponse.Error -> {
                        root.isRefreshing = false
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_LONG)
                            .show()
                        Log.d("TAG", "initObserver: ${it.errorMessage}")
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