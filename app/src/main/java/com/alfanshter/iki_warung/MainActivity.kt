package com.alfanshter.iki_warung

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alfanshter.iki_warung.Ui.History.HistoryFragment
import com.alfanshter.iki_warung.Ui.MenuFragment
import com.alfanshter.iki_warung.Ui.ProfilFragment
import com.alfanshter.iki_warung.Ui.Riwayat.RiwayatFragment
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.iki_warung.viewmodel.UsersViewModel
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity(), AnkoLogger {
    lateinit var refbaru: DatabaseReference
    lateinit var refbarulistener: ValueEventListener

    var auth: FirebaseAuth? = null
    var UserID: String? = null


    lateinit var sessionManager: SessionManager
    lateinit var usersViewModel: UsersViewModel
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        MenuFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        HistoryFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }

                R.id.navigation_riwayat -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        RiwayatFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }
                R.id.navigation_notifications -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
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
        usersViewModel = ViewModelProviders.of(this).get(UsersViewModel::class.java)

        usersViewModel.cekstatusaktifasi()
        usersViewModel.getState().observer(this, Observer {
            handleUiState(it)
        })
        auth = FirebaseAuth.getInstance()
        UserID = auth!!.currentUser!!.uid
        info { "alfan ${UserID.toString()}" }
        if (auth == null) {
            UserID = null
            sessionManager.setLogin(false)
            startActivity<SplashScreen>()
            finish()
        }

        moveToFragment(MenuFragment())

    }

    private fun handleUiState(it: UsersViewModel.UserState?) {
        when (it) {
            is UsersViewModel.UserState.IsSuccess -> isloading(it.what!!)
        }
    }


    private fun moveToFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.frame, fragment)
        fragmentTrans.commit()
    }

    fun isloading(state: Int) {
        if (state == 1) {
            startActivity(intentFor<firstaddfoodActivity>().clearTask().newTask())
            finish()
        } else if (state == 2) {
            toast(Constant.selamat)
        }
    }

}