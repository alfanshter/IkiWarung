package com.alfanshter.IKI_Warung.Ui.History

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.alfanshter.IKI_Warung.Ui.History.uiHistory.FinishFragment
import com.alfanshter.IKI_Warung.Ui.History.uiHistory.ProcessFragment


class PagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
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
            1->"Selesai Hari Ini"
            else ->"Dalam Proses"
        }
    }

}