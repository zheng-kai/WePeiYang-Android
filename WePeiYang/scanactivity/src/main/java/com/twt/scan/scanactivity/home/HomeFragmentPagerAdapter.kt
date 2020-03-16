package com.twt.scan.scanactivity.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class HomeFragmentPagerAdapter(fm: FragmentManager, private val fragments: List<HomeFragment>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? = when (fragments[position].title) {
        HomeTitle.MANAGER_TITLE -> "管理"
        HomeTitle.NOT_JOINED_TITLE -> "待参加"
        HomeTitle.JOINED_TITLE -> "已参加"
    }

}