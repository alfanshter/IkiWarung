package com.alfanshter.iki_warung.viewmodel

import androidx.lifecycle.ViewModel
import com.alfanshter.iki_warung.Model.UsersModel
import com.alfanshter.iki_warung.Utils.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class UsersViewModel : ViewModel(), AnkoLogger {
    private var state: SingleLiveEvent<UserState> = SingleLiveEvent()

    //firebase
    lateinit var firestoreuser: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var UserId: String? = null
    fun inisialisasidatabase() {
        firestoreuser = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        UserId = auth.currentUser!!.uid
    }

    fun cekstatusaktifasi() {
        inisialisasidatabase()
        state.value = UserState.Isloading(true)
        var docref = firestoreuser.collection("Warung_Akun").document(UserId.toString()).get()
        docref.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists() && documentSnapshot != null) {
                var user = documentSnapshot.toObject(UsersModel::class.java)
                if (user!!.status_aktivasi == false) {
                    state.value = UserState.IsSuccess(1)    // 1 pergi ke halaman aktifasi akun
                } else if (user.status_aktivasi == true) {
                    state.value = UserState.IsSuccess(2)   // 2 Cus Kerja
                }
            }
        }
        state.value = UserState.Isloading(false)
    }

    fun getState() = state


    sealed class UserState {
        data class ShowToast(var message: String) : UserState()
        data class Message(var message: String) : UserState()
        data class IsSukses(var sukses: Boolean = false) : UserState()
        data class IsToken(var sukses: Boolean = false) : UserState()
        data class Isloading(var userstate: Boolean = false) : UserState()
        data class IsSuccess(var what: Int? = null) : UserState()
        data class IsRegister(var register: Boolean = false) : UserState()
    }
}