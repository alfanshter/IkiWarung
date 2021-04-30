package com.alfanshter.iki_warung.Ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alfanshter.iki_warung.BuildConfig
import com.alfanshter.iki_warung.R
import com.alfanshter.iki_warung.auth.LoginActivity
import com.alfanshter.iki_warung.databinding.FragmentProfilBinding
import com.alfanshter.iki_warung.viewmodel.UsersViewModel
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profil.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.startActivity

class ProfilFragment : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var sessionManager: SessionManager
    lateinit var profilviewmodel: UsersViewModel
    lateinit var binding: FragmentProfilBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sessionManager = SessionManager(context!!.applicationContext)
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profil, container, false)
        profilviewmodel = ViewModelProviders.of(this).get(UsersViewModel::class.java)
        binding.viewmodels = profilviewmodel
        binding.lifecycleOwner = this

        profilviewmodel.ProfilWarung()
        profilviewmodel.getProfilWarung().observe(this, Observer {
            binding.txtNamawarung.text = it.namatoko
        })
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            sessionManager.setLogin(false)

            startActivity<LoginActivity>()
            activity!!.finish()

        }

        val versionName = BuildConfig.VERSION_NAME.toFloat()
        binding.version.text = "Versi Aplikasi ${versionName}"

        return binding.root
    }


}