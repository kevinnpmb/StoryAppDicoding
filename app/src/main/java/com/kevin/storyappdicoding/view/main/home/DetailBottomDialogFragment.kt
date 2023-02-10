package com.kevin.storyappdicoding.view.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kevin.storyappdicoding.adapter.StoriesAdapter
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.databinding.FragmentDetailBottomDialogBinding
import com.kevin.storyappdicoding.databinding.FragmentHomeBinding
import com.kevin.storyappdicoding.view.common.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailBottomDialogFragment : BaseBottomSheetDialogFragment() {
    private lateinit var binding: FragmentDetailBottomDialogBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    companion object {
        private const val STORY_ID = "STORY_ID"

        @JvmStatic
        fun newInstance(storyId: String) =
            DetailBottomDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(STORY_ID, storyId)
                }
            }
    }
}