package com.kevin.storyappdicoding.view.main.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.kevin.storyappdicoding.adapter.LoadingStateAdapter
import com.kevin.storyappdicoding.adapter.StoriesAdapter
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
            rvHome.adapter = adapter.apply {
                stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                addLoadStateListener { loadStates ->
                    viewModel.taskListState.value = when (loadStates.source.refresh) {
                        is LoadState.NotLoading -> {
                            if (loadStates.append.endOfPaginationReached && itemCount < 1) {
                                HomeViewModel.TaskListState.EMPTY
                            } else {
                                HomeViewModel.TaskListState.PRESENT
                            }
                        }
                        is LoadState.Loading -> {
                            if (adapter.itemCount == 0) {
                                HomeViewModel.TaskListState.LOADING
                            } else {
                                HomeViewModel.TaskListState.PRESENT
                            }
                        }
                        is LoadState.Error -> HomeViewModel.TaskListState.ERROR
                    }
                }
            }.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
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
            adapter.submitData(lifecycle, it)
            if (scrollToTop) {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.rvHome.smoothScrollToPosition(0)
                    scrollToTop = false
                }, 300)
            }
        }

        viewModel.taskListState.observe(viewLifecycleOwner) {
            binding.apply {
                root.isRefreshing = false
                loading.root.isVisible = it == HomeViewModel.TaskListState.LOADING
                empty.root.isVisible = it == HomeViewModel.TaskListState.EMPTY
                error.root.isVisible = it == HomeViewModel.TaskListState.ERROR
                rvHome.isVisible = it == HomeViewModel.TaskListState.PRESENT
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