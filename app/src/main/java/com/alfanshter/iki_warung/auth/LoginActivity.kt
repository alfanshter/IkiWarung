package com.alfanshter.iki_warung.auth

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alfanshter.iki_warung.MainActivity
import com.alfanshter.iki_warung.R
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*

class LoginActivity : AppCompatActivity(),AnkoLogger {
    lateinit var sessionManager: SessionManager
    //database
    lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    lateinit var progressdialog: ProgressDialog

    var email : String? = null
    var password : String? = null

    //token
    var token: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
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
            progressdialog.setTitle("Sedang Menunggu")
            progressdialog.setCanceledOnTouchOutside(false)
            progressdialog.show()
            info { "dinda ok" }
            val docref =         firestore.collection(Constant.warung_akun).whereEqualTo("username",userss).whereEqualTo("password",pass)
            docref.get().addOnSuccessListener {
                    document->
                for (doc in document){
                    if (doc.exists()){
                        info { "dinda dapat" }
                        auth.signInWithEmailAndPassword(userss,pass).addOnCompleteListener {
                                task ->
                            if (task.isSuccessful) {
                                gettoken()
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

    private fun gettoken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            token = task.result
            if (token!=null){
                val req = firestore.collection(Constant.warung_akun).document(auth.currentUser!!.uid).update("id_token",token.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        sessionManager.setLogin(true)
                        startActivity(intentFor<MainActivity>().clearTask().newTask())
                        progressdialog.dismiss()
                        finish()

                    }
                }
            }

        })
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