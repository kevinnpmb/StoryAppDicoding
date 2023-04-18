package com.kevin.storyappdicoding.view.main.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.kevin.storyappdicoding.data.model.Response
import com.kevin.storyappdicoding.databinding.FragmentDetailBottomDialogBinding
import com.kevin.storyappdicoding.utils.Utilities.setImageResource
import com.kevin.storyappdicoding.view.common.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class DetailBottomDialogFragment : BaseBottomSheetDialogFragment() {
    private lateinit var binding: FragmentDetailBottomDialogBinding
    private var storyId by Delegates.notNull<Int>()
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            storyId = it.getInt(STORY_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBottomDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initListener()
        viewModel.getStory(storyId)
        playAnimation()
    }

    private fun initObserver() {
        viewModel.storyResult.observe(viewLifecycleOwner) {
            binding.apply {
                loading.root.isVisible = it is Response.Loading
                error.root.isVisible = it is Response.Error
                background.isVisible = loading.root.isVisible || error.root.isVisible
                when (it) {
                    is Response.Loading -> {
                        nsvDetail.isVisible = false
                    }
                    is Response.Success -> {
                        nsvDetail.isVisible = true
                        storyImage.setImageResource(it.data?.photoPath)
                        storyDescription.text = it.data?.description
                        storyName.text = it.data?.name
                    }
                    is Response.Error -> {
                        nsvDetail.isVisible = false
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    private fun initListener() {
        binding.apply {
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun playAnimation() {
        AnimatorSet().apply {
            playSequentially(
                ObjectAnimator.ofFloat(binding.storyName, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.storyDescription, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.btnClose, View.ALPHA, 1f).setDuration(500),
            )
            start()
        }
    }

    companion object {
        private const val STORY_ID = "STORY_ID"

        @JvmStatic
        fun newInstance(storyId: Int) =
            DetailBottomDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(STORY_ID, storyId)
                }
            }
    }
}