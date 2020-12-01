package com.alfanshter.IKI_Warung

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.alfanshter.IKI_Warung.Ui.History.HistoryFragment
import com.alfanshter.IKI_Warung.Ui.MenuFragment
import com.alfanshter.IKI_Warung.Ui.ProfilFragment
import com.alfanshter.IKI_Warung.Ui.RiwayatFragment
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

class MainActivity : AppCompatActivity() {
    lateinit var refbaru : DatabaseReference
    lateinit var refbarulistener : ValueEventListener

    lateinit var auth :FirebaseAuth
    var UserID : String? = null
    lateinit var sessionManager: SessionManager
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

        auth = FirebaseAuth.getInstance()
        UserID = auth.currentUser!!.uid
        moveToFragment(MenuFragment())
        getdata()

    }

    private fun moveToFragment(fragment: Fragment)
    {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.frame, fragment)
        fragmentTrans.commit()
    }

    private fun getdata(){
        refbaru = FirebaseDatabase.getInstance().reference.child("Pandaan").child("Resto_Detail").child(UserID.toString())
        refbarulistener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    toast("yeri gendeng")

                }
                else{
                    startActivity<firstaddfoodActivity>()
                }
            }

        }

        refbaru.addValueEventListener(refbarulistener)


    }

    override fun onDestroy() {
        super.onDestroy()
        refbaru.removeEventListener(refbarulistener)
    }
}