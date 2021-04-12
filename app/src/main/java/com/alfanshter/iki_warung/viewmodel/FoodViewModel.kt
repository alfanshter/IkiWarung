package com.alfanshter.iki_warung.viewmodel

import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfanshter.iki_warung.Model.UsersModel
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.iki_warung.Utils.SingleLiveEvent
import com.alfanshter.iki_warung.Ui.InsertFoodActivity
import com.alfanshter.iki_warung.Ui.MenuFragment
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt

class FoodViewModel : ViewModel(), AnkoLogger {
    private var state: SingleLiveEvent<FoodState> = SingleLiveEvent()
    private var dataswitch = MutableLiveData<UsersModel>()
    //firebase
    private var storageReference: StorageReference? = null
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    var UserId: String? = null

    var nama_makanan: String? = null
    var harga_makanan: String? = null
    var keterangan_makanan: String? = null

    private var myUrl = ""

    fun inisialisasifirebase() {
        auth = FirebaseAuth.getInstance()
        UserId = auth.currentUser!!.uid
        storageReference =
            FirebaseStorage.getInstance().reference.child("Warung").child(UserId.toString())
                .child("resep")
        firestore = FirebaseFirestore.getInstance()
    }


    //tambah menu
    fun btn_tambahkan(view: View) {
        state.value = FoodState.IsLoading(true)
        inisialisasifirebase()

        if (nama_makanan.isNullOrEmpty() || harga_makanan.isNullOrEmpty() || keterangan_makanan.isNullOrEmpty() || InsertFoodActivity.filePath == null
            || InsertFoodActivity.kategori_insert == null
        ) {
            info { "dinda $nama_makanan $harga_makanan $keterangan_makanan ${InsertFoodActivity.filePath} ${InsertFoodActivity.kategori_insert}" }
            state.value = FoodState.ShowToast(Constant.input)
            state.value = FoodState.IsLoading(false)

        } else {
            var hargamakanan = harga_makanan.toString().toInt()
            var hargappn = ((hargamakanan * 15) / 100)
            var hargatotal = hargamakanan + hargappn

            val fileref =
                storageReference!!.child(
                    System.currentTimeMillis().toString() + ".jpg"
                )
            var uploadTask: StorageTask<*>
            uploadTask = fileref.putFile(InsertFoodActivity.filePath!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw  it
                        state.value = FoodState.ShowToast(it.message.toString())
                    }
                }
                return@Continuation fileref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result
                    val key =
                        FirebaseFirestore.getInstance().collection("Warung_Resep").document().id

                    myUrl = downloadUrl.toString()
                    var harga = (hargatotal.toDouble() / 1000).roundToInt() * 1000
                    val usermap: MutableMap<String, Any?> = HashMap()

                    usermap["gambar_makanan"] = myUrl
                    usermap["harga"] = harga_makanan.toString()
                    usermap["harga_ppn"] = hargappn.toString()
                    usermap["harga_total"] = harga.toString()
                    usermap["kategori"] = InsertFoodActivity.kategori_insert!!.toString()
                    usermap["nama"] = nama_makanan.toString()
                    usermap["id_makanan"] = key.toString()
                    usermap["keterangan"] = keterangan_makanan.toString()
                    usermap["uid"] = UserId.toString()
                    usermap["tanggal_tambah"] = Timestamp(Date())

                    val docref = firestore.collection("Warung_Resep").document().set(usermap)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                state.value = FoodState.IsSukses(1)
                                state.value = FoodState.IsLoading(false)
                                state.value = FoodState.ShowToast(Constant.input_sukses)
                            } else {
                                state.value = FoodState.IsLoading(false)
                                state.value = FoodState.ShowToast(Constant.error)
                            }
                        }
                }
            }
            state.value = FoodState.IsLoading(false)
        }
    }

    //ambil data switch warung
    fun ambilDataSwitchWarung(){
        inisialisasifirebase()
        state.value = FoodState.IsLoading(true)
        firestore.collection("Warung_Akun").document(UserId.toString()).get().addOnSuccessListener {document ->
            if (document.exists() && document!=null){
                val data = document.toObject(UsersModel::class.java)
                if (data!!.status == false){
                    state.value = FoodState.IsSuksesMenu(1)   // satu artinya Switch terdeteksi Tutup
                    state.value = FoodState.IsLoading(false)
                }
                if (data.status == true){
                    state.value = FoodState.IsSuksesMenu(2)   // dua artinya Switch terdeteksi buka
                    state.value = FoodState.IsLoading(false)
                }
                state.value = FoodState.IsLoading(false)
            }
        }
    }

    fun setDataSwitchWarung(){
        inisialisasifirebase()
        state.value = FoodState.IsLoading(true)
        val docref = firestore.collection("Warung_Akun").document(UserId.toString())
        if (MenuFragment.status_switch ==true){
            docref.update("status",true).addOnCompleteListener {
                if (it.isSuccessful){
                    state.value = FoodState.IsLoading(false)
                }
            }
        }else if (MenuFragment.status_switch ==false){
            docref.update("status", false).addOnCompleteListener {
                if (it.isSuccessful){
                    state.value = FoodState.IsLoading(false)
                }
            }
        }
    }

    fun getState() = state
    fun getSwitch() = dataswitch


    sealed class FoodState {
        data class IsLoading(var loading: Boolean = false) : FoodState()
        data class ShowToast(var message: String) : FoodState()
        data class IsSukses(var sukses: Int? = null) : FoodState()
        data class IsSuksesMenu ( var sukses : Int? = null) : FoodState()
    }
}




