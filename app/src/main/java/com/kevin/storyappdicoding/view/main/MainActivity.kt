package com.kevin.storyappdicoding.view.main

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.kevin.storyappdicoding.R
import com.kevin.storyappdicoding.adapter.MainViewPagerAdapter
import com.kevin.storyappdicoding.databinding.ActivityMainBinding
import com.kevin.storyappdicoding.view.common.BaseActivity
import com.kevin.storyappdicoding.view.main.add.AddStoryBottomDialogFragment
import com.kevin.storyappdicoding.view.main.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavView.apply {
            menu.getItem(1).isEnabled = false
            background = null
        }
        binding.vpHostFragment.adapter = MainViewPagerAdapter(this)
        binding.vpHostFragment.isUserInputEnabled = false
        initListener()
    }

    private fun initListener() {
        binding.apply {
            vpHostFragment.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    bottomNavView.menu.findItem(HomeMenu.values()[position].menuId).isChecked = true
                }
            })

            bottomNavView.setOnItemSelectedListener { item ->
                HomeMenu.fromMenuId(item.itemId)?.let {
                    vpHostFragment.currentItem = it.ordinal
                    return@setOnItemSelectedListener true
                }?: return@setOnItemSelectedListener false
            }

            addPhoto.setOnClickListener {
                AddStoryBottomDialogFragment().show(supportFragmentManager, "add-story-tag")
            }
        }
    }

    enum class HomeMenu(val fragmentClass: Class<*>, @IdRes val menuId: Int) {
        Home(HomeFragment::class.java, R.id.home),
        Account(Fragment::class.java, R.id.account);

        companion object {
            private val map = values().associateBy(HomeMenu::menuId)
            fun get(ordinal: Int) = values()[ordinal]
            fun fromMenuId(@IdRes menuId: Int): HomeMenu? = map[menuId]
        }
    }
}