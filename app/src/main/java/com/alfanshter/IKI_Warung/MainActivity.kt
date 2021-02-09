package com.alfanshter.IKI_Warung

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alfanshter.IKI_Warung.Model.Warung
import com.alfanshter.IKI_Warung.Ui.History.HistoryFragment
import com.alfanshter.IKI_Warung.Ui.MenuFragment
import com.alfanshter.IKI_Warung.Ui.ProfilFragment
import com.alfanshter.IKI_Warung.Ui.Riwayat.RiwayatFragment
import com.alfanshter.IKI_Warung.Utils.WarungResponse
import com.alfanshter.IKI_Warung.ViewModels.WarungViewModel
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity(),AnkoLogger {
    lateinit var refbaru : DatabaseReference
    lateinit var refbarulistener : ValueEventListener

    var auth :FirebaseAuth? = null
    var UserID : String? = null

    lateinit var sessionManager: SessionManager
    private lateinit var warungViewModel: WarungViewModel

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame,
                    MenuFragment()
                ).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame,
                    HistoryFragment()
                ).commit()
                return@OnNavigationItemSelectedListener true

            }

            R.id.navigation_riwayat -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame,
                    RiwayatFragment()
                ).commit()
                return@OnNavigationItemSelectedListener true

            }
            R.id.navigation_notifications -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame,
                    ProfilFragment()
                ).commit()
                return@OnNavigationItemSelectedListener true

            }

        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sessionManager = SessionManager(this)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val token = sessionManager.getAuthToken()

        if(token!!.isEmpty()){
            sessionManager.setLogin(false)
            startActivity<SplashScreen>()
            finish()
        }else{
//            warungViewModel = ViewModelProviders.of(this).get(WarungViewModel::class.java)
//            warungViewModel.getDataWarungById(sessionManager.getAuthToken().toString())
//            warungViewModel.getDataWarung().observe(this, Observer {
//                getdata(it)
//            })
        }



//        auth = FirebaseAuth.getInstance()
//
//        UserID = auth!!.currentUser!!.uid
//        info { "alfan ${UserID.toString()}" }
//        if (auth==null){
//            UserID = null
//            sessionManager.setLogin(false)
//            startActivity<SplashScreen>()
//            finish()
//        }





            moveToFragment(MenuFragment())
//            getdata()

    }

    private fun moveToFragment(fragment: Fragment)
    {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.frame, fragment)
        fragmentTrans.commit()
    }

    private fun getdata(warung : WarungResponse){
        var foto_awal = warung.fotoAwal
        if (foto_awal!!.isEmpty()){
            startActivity(intentFor<firstaddfoodActivity>().clearTask().newTask())
            finish()
        }
    }

    private fun toast(message : String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

//    private fun getdata(){
//        refbaru = FirebaseDatabase.getInstance().reference.child("Pandaan").child("Resto_Detail").child(UserID.toString())
//        refbarulistener = object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//
//                }
//                else{
//                    startActivity(intentFor<firstaddfoodActivity>().clearTask().newTask())
//                    finish()
//                }
//            }
//
//        }
//
//        refbaru.addListenerForSingleValueEvent(refbarulistener)
//
//
//    }


}