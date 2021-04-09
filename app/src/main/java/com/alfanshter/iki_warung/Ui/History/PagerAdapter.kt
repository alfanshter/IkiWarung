package com.alfanshter.iki_warung.Ui.History

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.alfanshter.iki_warung.Ui.History.uiHistory.FinishFragment
import com.alfanshter.iki_warung.Ui.History.uiHistory.ProcessFragment
import java.util.*


class PagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

     private var fragmentList= ArrayList<Fragment>()
     private var titleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return when (position){
            0-> {
                ProcessFragment()
            }
            1->{
                FinishFragment()
            }

            else->
                ProcessFragment()
        }

    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0->"Dalam Proses"
            1->"Selesai"
            else ->"Dalam Proses"
        }
    }

    fun addFragment(
        fragment: Fragment?,
        title: String?
    ) {
        fragmentList.add(fragment!!)
        titleList.add(title!!)
    }

}