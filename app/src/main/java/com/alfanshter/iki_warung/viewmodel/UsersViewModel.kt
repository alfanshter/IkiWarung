package com.alfanshter.iki_warung.viewmodel

import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfanshter.iki_warung.EditActivity
import com.alfanshter.iki_warung.EditRestoActivity
import com.alfanshter.iki_warung.Model.MakananModels
import com.alfanshter.iki_warung.Model.UsersModel
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.iki_warung.Utils.FirebaseInisalisasi.Companion.firestore
import com.alfanshter.iki_warung.Utils.SingleLiveEvent
import com.alfanshter.iki_warung.aktifasiWarungActivity
import com.alfanshter.iki_warung.aktifasiWarungActivity.Companion.image_uri
import com.alfanshter.iki_warung.aktifasiWarungActivity.Companion.jambukawarung
import com.alfanshter.iki_warung.aktifasiWarungActivity.Companion.jamtutupwarung
import com.alfanshter.iki_warung.aktifasiWarungActivity.Companion.openday
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import kotlin.math.roundToInt

class UsersViewModel : ViewModel(), AnkoLogger {
    private var state: SingleLiveEvent<UserState> = SingleLiveEvent()
    private var storageUsers: StorageReference? = null
    private var datauser = MutableLiveData<UsersModel>()

    //firebase
    lateinit var firestoreuser: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var UserId: String? = null
    private var myUrl = ""
    lateinit var mFirebaseStorage: FirebaseStorage

    //profilwarung
    var namawarung: String? = null


    //ambil data warung
    fun ProfilWarung() {
        inisialisasidatabase()
        val docref = firestoreuser.collection("Warung_Akun").document(UserId.toString()).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists() && documentSnapshot != null) {
                    var profil = documentSnapshot.toObject(UsersModel::class.java)
                    datauser.postValue(profil!!)
                }
                val source = if (documentSnapshot.metadata.isFromCache) {
                    var profil = documentSnapshot.toObject(UsersModel::class.java)
                    datauser.postValue(profil!!)
                } else {
                    var profil = documentSnapshot.toObject(UsersModel::class.java)
                    datauser.postValue(profil!!)
                }



            }
    }

    fun inisialisasidatabase() {
        storageUsers =
            FirebaseStorage.getInstance().reference.child("Warung").child(UserId.toString())
                .child("daftar")

        firestoreuser = FirebaseFirestore.getInstance()

        auth = FirebaseAuth.getInstance()
        UserId = auth.currentUser!!.uid
        mFirebaseStorage = FirebaseStorage.getInstance()

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

    //aktifasi warung ketika pendaftaran awal
    fun btn_aktifasi(view: View) {
        inisialisasidatabase()
        state.value = UserState.Isloading(true)
        if (openday.isNullOrEmpty() || jambukawarung.isNullOrEmpty() || jamtutupwarung.isNullOrEmpty() || image_uri == null) {
            state.value = UserState.Isloading(false)
            state.value = UserState.ShowToast("Isi Semua Kolom")
        } else {


            val fileref =
                storageUsers!!.child(
                    System.currentTimeMillis().toString() + ".jpg"
                )
            var uploadTask: StorageTask<*>
            uploadTask = fileref.putFile(image_uri!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw  it
                        state.value = UserState.ShowToast(it.message.toString())
                    }
                }
                return@Continuation fileref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result
                    val key =
                        FirebaseDatabase.getInstance().reference.push().key

                    myUrl = downloadUrl.toString()
                    val usermap: MutableMap<String, Any?> = HashMap()

                    usermap["foto_icon"] = myUrl
                    usermap["hari_tutup"] = openday.toString()
                    usermap["jam_buka"] = jambukawarung.toString()
                    usermap["jam_tutup"] = jamtutupwarung.toString()
                    usermap["status_aktivasi"] = true
                    val docref =
                        firestoreuser.collection("Warung_Akun").document(UserId.toString()).update(
                            usermap
                        ).addOnCompleteListener {
                            if (it.isSuccessful) {
                                state.value = UserState.IsSuccess(1)
                                state.value = UserState.Isloading(false)
                            } else {
                                state.value = UserState.ShowToast(Constant.error)
                                state.value = UserState.Isloading(false)
                            }
                        }
                } else {
                    state.value = UserState.ShowToast(Constant.error)
                    state.value = UserState.Isloading(false)
                }

            }
        }
    }

    //edit makanan
    fun btn_editwarung(view: View) {
        inisialisasidatabase()
        state.value = UserState.Isloading(true)
        if (EditRestoActivity.data==null
        ) {
            val docref = firestoreuser.collection("Warung_Akun")
                .document(UserId.toString()).update(
                    "jam_buka",
                    EditRestoActivity.jambukawarung,
                    "jam_tutup",
                    EditRestoActivity.jamtutupwarung,
                    "hari_tutup",
                    EditRestoActivity.openday).addOnCompleteListener {
                    if (it.isSuccessful) {
                        state.value = UserState.IsSuccess(1)  // 1 sukses
                        state.value = UserState.ShowToast(Constant.input_sukses)
                        state.value = UserState.Isloading(false)
                    }
                }
        } else {
            val fileref =
                storageUsers!!.child(System.currentTimeMillis().toString() + ".jpg")
            var uploadTask: StorageTask<*>
            uploadTask = fileref.putBytes(EditRestoActivity.data!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw  it
                        state.value = UserState.ShowToast(it.message.toString())
                    }
                }
                return@Continuation fileref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result
                    myUrl = downloadUrl.toString()
                    val photoRef: StorageReference =
                        mFirebaseStorage.getReferenceFromUrl(datauser.value!!.foto_icon.toString())
                    photoRef.delete()

                    val docref = firestoreuser.collection("Warung_Akun")
                        .document(UserId.toString()).update(
                            "jam_buka",
                            EditRestoActivity.jambukawarung,
                            "jam_tutup",
                            EditRestoActivity.jamtutupwarung,
                            "hari_tutup",
                            EditRestoActivity.openday,
                            "foto_icon",
                            myUrl
                        ).addOnCompleteListener {
                            if (it.isSuccessful) {
                                state.value = UserState.IsSuccess(1)  // 1 sukses
                                state.value = UserState.ShowToast(Constant.input_sukses)
                                state.value = UserState.Isloading(false)
                            }
                        }

                } else {
                    state.value = UserState.Isloading(false)
                    state.value = UserState.ShowToast(Constant.error)
                }
            }

        }

    }

    fun getState() = state
    fun getProfilWarung() = datauser


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