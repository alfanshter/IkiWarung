package com.alfanshter.IKI_Warung.Ui.History

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.IKI_Warung.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var root : View
    lateinit var refinfo: DatabaseReference
    lateinit var auth: FirebaseAuth
    var userID : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root =  inflater.inflate(R.layout.fragment_history, container, false)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid


        return  root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fragmentAdapter = PagerAdapter(activity!!.supportFragmentManager)
        view_pesanan.adapter = fragmentAdapter
        tab_pesanan.setupWithViewPager(view_pesanan)
    }




}