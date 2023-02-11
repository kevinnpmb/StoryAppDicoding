package com.kevin.storyappdicoding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kevin.storyappdicoding.view.main.MainActivity


class MainViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val list = MainActivity.HomeMenu.values()

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun createFragment(position: Int): Fragment {
        return list[position].fragmentClass.newInstance() as Fragment
    }
}