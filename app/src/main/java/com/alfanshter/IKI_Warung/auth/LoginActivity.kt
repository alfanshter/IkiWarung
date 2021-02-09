package com.alfanshter.IKI_Warung.auth

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alfanshter.IKI_Warung.MainActivity
import com.alfanshter.IKI_Warung.R
import com.alfanshter.IKI_Warung.ViewModels.UserState
import com.alfanshter.IKI_Warung.ViewModels.UserViewModel
import com.alfanshter.IKI_Warung.firstaddfoodActivity
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*

class LoginActivity : AppCompatActivity(),AnkoLogger {
    lateinit var sessionManager: SessionManager
    private lateinit var userViewModel: UserViewModel

    lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var progressdialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressdialog = ProgressDialog(this)
        sessionManager = SessionManager(this)

        if (!sessionManager.getAuthToken()!!.isEmpty()){
            startActivity(intentFor<MainActivity>().clearTask().newTask())
            finish()
        }

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })

        btn_login.setOnClickListener {
                login()
        }
    }

    private fun login(){
        val email = username_input.text.toString()
        val password = pass.text.toString()
        if (userViewModel.validate(email,password)){
            userViewModel.login(email,password)
        }
    }

    private fun handleUIState(it : UserState){
        when(it){
            is UserState.Reset -> {
                setEmailError(null)
                setPasswordError(null)
            }
            is UserState.Error -> {
                isLoading(false)
                toast(it.err)
            }

            is UserState.ShowToast -> toast(it.message)
            is UserState.Failed -> {
                isLoading(false)
                toast(it.message)
            }

            is UserState.Success -> {
                sessionManager.setAuthToken(it.token)
                startActivity(intentFor<MainActivity>().clearTask().newTask())
//                                        progressDialog.dismiss()
                finish()
                toast("Login Berhasil")
            }

            is UserState.IsLoading -> isLoading(it.state)
        }
    }

    private fun isLoading(state : Boolean){
        val progressDialog = ProgressDialog(this)
        if (state){
            btn_login.isEnabled = false
//            progressDialog.setTitle("Tunggu sebentar")
//            progressDialog.show()
        }else{
//            progressDialog.dismiss()
            btn_login.isEnabled = true
        }
    }

    private fun setEmailError(err : String?){ username_input.error = err }
    private fun setPasswordError(err : String?){ pass.error = err }
    private fun toast(message : String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()


//    fun login() {
//        val progressDialog = ProgressDialog(this)
//        progressDialog.setTitle("Sedang Login .....")
//        progressDialog.show()
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