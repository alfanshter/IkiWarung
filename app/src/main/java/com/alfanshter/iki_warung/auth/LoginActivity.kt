package com.alfanshter.iki_warung.auth

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alfanshter.iki_warung.MainActivity
import com.alfanshter.iki_warung.R
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*

class LoginActivity : AppCompatActivity(),AnkoLogger {
    lateinit var sessionManager: SessionManager
    lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var progressdialog: ProgressDialog
    var email : String? = null
    var password : String? = null
    lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firestore = FirebaseFirestore.getInstance()
        progressdialog = ProgressDialog(this)
        sessionManager = SessionManager(this)

        if (sessionManager.getLogin()!!){
            startActivity(intentFor<MainActivity>().clearTask().newTask())
            finish()
        }

        btn_login.setOnClickListener {
            loginfirestore()
        }


    }


    fun loginfirestore(){
        var userss = username_input.text.toString()
        var pass = pass.text.toString()
        if (userss.isEmpty() || pass.isEmpty()){
            toast("Masukan email dan password")
        }else{
            progressdialog.show()
            progressdialog.setTitle("Sedang Menunggu")
            progressdialog.setCanceledOnTouchOutside(false)
            auth = FirebaseAuth.getInstance()
            val docref =         firestore.collection("Warung_Akun").whereEqualTo("username",userss).whereEqualTo("password",pass)
            docref.get().addOnSuccessListener {
                    document->
                for (doc in document){
                    if (doc.exists()){
                        auth.signInWithEmailAndPassword(userss,pass).addOnCompleteListener {
                                task ->
                            if (task.isSuccessful) {
                                sessionManager.setLogin(true)
                                startActivity(intentFor<MainActivity>().clearTask().newTask())
                                progressdialog.dismiss()
                                finish()
                            }
                            else
                            {
                                progressdialog.dismiss()
                                toast("gagal login")

                            }
                        }
                    }
                    progressdialog.dismiss()
                }
            }.addOnFailureListener {
                info { "tester ${it.message}" }
            }

        }

    }
//    fun login() {
//        val progressDialog = ProgressDialog(this)
//        progressDialog.setTitle("Sedang Login .....")
//        progressDialog.show()
//        progressDialog.setCanceledOnTouchOutside(false)
//        auth = FirebaseAuth.getInstance()
//        var userss = username_input.text.toString()
//        var pass = pass.text.toString()
//
//        databaseReference = FirebaseDatabase.getInstance().reference
//        databaseReference.child("Pandaan").child("Akun_Resto").addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                for (data in p0.children){
//                    val ambildata = data.getValue(UserModel::class.java)
//                    info { "dinda $ambildata" }
//                    email = ambildata!!.username.toString()
//                    password = ambildata.password.toString()
//                    if (!TextUtils.isEmpty(userss) && !TextUtils.isEmpty(password)) {
//                        if (userss == email && pass == password)
//                        {
//                            auth.signInWithEmailAndPassword(userss, pass)
//                                .addOnCompleteListener { task ->
//
//                                    if (task.isSuccessful) {
//                                        sessionManager.setLogin(true)
//                                        startActivity(intentFor<MainActivity>().clearTask().newTask())
//                                        progressDialog.dismiss()
//                                        finish()
//                                    }
//                                    else
//                                    {
//                                        progressdialog.dismiss()
//                                        toast("gagal login")
//
//                                    }
//                                }
//                        }
//
//                        else{
//                            progressdialog.dismiss()
//                        }
//
//                    }
//                    else {
//                        toast("masukkan username dan password")
//                        progressDialog.dismiss()
//
//                    }
//
//
//                }
//            }
//
//        })
///*
//        if (!TextUtils.isEmpty(userss) && !TextUtils.isEmpty(password)) {
//
//
//                auth.signInWithEmailAndPassword(userss, pass)
//                    .addOnCompleteListener { task ->
//
//                        if (task.isSuccessful) {
//                            sessionManager.setLoginadmin(true)
//                            startActivity<MainActivity>()
//                            progressDialog.dismiss()
//                        }
//                        else
//                        {
//                            toast("gagal login")
//
//                        }
//                    }
//        }
//        else {
//            toast("masukkan username dan password")
//
//        }
//*/
//
//    }
}