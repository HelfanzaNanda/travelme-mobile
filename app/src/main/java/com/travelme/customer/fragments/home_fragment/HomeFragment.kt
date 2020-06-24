package com.travelme.customer.fragments.home_fragment


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.travelme.customer.R
import com.travelme.customer.fragments.home_fragment.destination_other_fragment.DestinationOtherFragment
import com.travelme.customer.fragments.home_fragment.destination_tegal_fragment.DestinationTegalFragment

import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment : Fragment(R.layout.fragment_home){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(DestinationOtherFragment(), getString(R.string.mau_kemana))
        adapter.addFragment(DestinationTegalFragment(), getString(R.string.dari_mana))
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)
    }

    class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

        private val fragments : MutableList<Fragment> = ArrayList()
        private val titles : MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment = fragments[position]

        override fun getCount(): Int = fragments.size

        fun addFragment(fragment: Fragment, title : String){
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? = titles[position]
    }
}