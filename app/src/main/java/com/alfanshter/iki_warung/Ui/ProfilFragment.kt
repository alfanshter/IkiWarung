package com.alfanshter.iki_warung.Ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.alfanshter.iki_warung.BuildConfig
import com.alfanshter.iki_warung.R
import com.alfanshter.iki_warung.auth.LoginActivity
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profil.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.startActivity

class ProfilFragment : Fragment() {

    lateinit var ref : DatabaseReference
    lateinit var auth: FirebaseAuth
    var UserID : String?=null
    lateinit var buttonlogout : Button
    lateinit var version : TextView
    lateinit var sessionManager: SessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_profil, container, false)
        sessionManager = SessionManager(context)
        buttonlogout = root.find(R.id.btn_logout)
        version = root.find(R.id.version)
        auth = FirebaseAuth.getInstance()
        UserID = auth.currentUser!!.uid
        ref = FirebaseDatabase.getInstance().reference.child("Pandaan").child("Akun_Resto")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                        var data = snapshot.child(UserID.toString()).child("namapemilik").value.toString()
                    root.txt_namawarung.text = data
                  }

        })

        buttonlogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            sessionManager.setLogin(false)

            startActivity<LoginActivity>()
            activity!!.finish()

        }

        val versionName = BuildConfig.VERSION_NAME.toFloat()
      version.text = "Versi Aplikasi ${versionName.toString()}"

        return root
    }


}