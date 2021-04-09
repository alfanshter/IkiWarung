package com.alfanshter.iki_warung.Ui.History

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.alfanshter.iki_warung.R
import com.alfanshter.iki_warung.Ui.History.uiHistory.FinishFragment
import com.alfanshter.iki_warung.Ui.History.uiHistory.ProcessFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*

class HistoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var root : View
    lateinit var refinfo: DatabaseReference
    lateinit var auth: FirebaseAuth
    var userID : String? = null
    var viewPager: ViewPager? = null
    var tabLayout: TabLayout? = null


    fun HistoryFragment() {
        // Required empty public constructor
    }

    fun getInstance() {
        return  HistoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root =  inflater.inflate(R.layout.fragment_history, container, false)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid

        viewPager = root.findViewById(R.id.view_pesanan)
        tabLayout = root.findViewById(R.id.tab_pesanan)

        val fragmentAdapter = PagerAdapter(activity!!.supportFragmentManager)

        // Inflate the layout for this fragment

        root.view_pesanan.adapter = fragmentAdapter

        root.tab_pesanan.setupWithViewPager(view_pesanan)


        return  root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewPager(viewPager!!)
        tabLayout!!.setupWithViewPager(viewPager)
        tabLayout!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setUpViewPager(viewPager: ViewPager) {
        val adapter = PagerAdapter(childFragmentManager)
        adapter.addFragment(FinishFragment(), "Finish")
        adapter.addFragment(ProcessFragment(), "Process")
        viewPager.adapter = adapter
    }






}