package com.alfanshter.iki_warung.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfanshter.iki_warung.EditActivity
import com.alfanshter.iki_warung.Model.MakananModels
import com.alfanshter.iki_warung.Model.UsersModel
import com.alfanshter.iki_warung.R
import com.alfanshter.iki_warung.Ui.InsertFoodActivity
import com.alfanshter.iki_warung.Ui.InsertFoodActivity.Companion.filePath
import com.alfanshter.iki_warung.Ui.MenuFragment
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.iki_warung.Utils.SingleLiveEvent
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt


class FoodViewModel : ViewModel(), AnkoLogger {
    private var state: SingleLiveEvent<FoodState> = SingleLiveEvent()
    private var dataswitch = MutableLiveData<UsersModel>()
    private var datamakanan = MutableLiveData<List<MakananModels>>()
    private var datafood_id = MutableLiveData<MakananModels>()

    //firebase
    private var storageReference: StorageReference? = null
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    var UserId: String? = null
    lateinit var mFirebaseStorage: FirebaseStorage

    var nama_makanan: String? = null
    var harga_makanan: String? = null
    var keterangan_makanan: String? = null
    var kategori_insert: String? = null
    private var myUrl = ""

    fun inisialisasifirebase() {
        auth = FirebaseAuth.getInstance()
        UserId = auth.currentUser!!.uid
        storageReference =
            FirebaseStorage.getInstance().reference.child("Warung").child(UserId.toString())
                .child("resep")
        firestore = FirebaseFirestore.getInstance()
        mFirebaseStorage = FirebaseStorage.getInstance()
    }


    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_makanan ->
                    if (checked) {
                        kategori_insert = "Makanan"
                        // Pirates are the best
                    }
                R.id.radio_minuman ->
                    if (checked) {
                        kategori_insert = "Minuman"
                    }
            }
        }

    }

    //tambah menu
    fun btn_tambahkan(view: View) {
        state.value = FoodState.IsLoading(true)
        inisialisasifirebase()

        if (nama_makanan.isNullOrEmpty() || harga_makanan.isNullOrEmpty() || keterangan_makanan.isNullOrEmpty() || InsertFoodActivity.data == null
            || kategori_insert == null
        ) {
            info { "dinda $nama_makanan $harga_makanan $keterangan_makanan ${InsertFoodActivity.filePath} ${kategori_insert}" }
            state.value = FoodState.ShowToast(Constant.input)
            state.value = FoodState.IsLoading(false)

        } else {
            val key =
                FirebaseFirestore.getInstance().collection("Warung_Resep").document().id
            info { "dinda $nama_makanan $harga_makanan $keterangan_makanan ${InsertFoodActivity.filePath} ${kategori_insert}" }
            var hargamakanan = harga_makanan.toString().toInt()
            var hargappn = ((hargamakanan * 15) / 100)
            var hargatotal = hargamakanan + hargappn


            val fileref =
                storageReference!!.child(System.currentTimeMillis().toString() + ".jpg")
            var uploadTask: StorageTask<*>
            uploadTask = fileref.putBytes(InsertFoodActivity.data!!)
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

                    myUrl = downloadUrl.toString()
                    var harga = (hargatotal.toDouble() / 1000).roundToInt() * 1000
                    val usermap: MutableMap<String, Any?> = HashMap()

                    usermap["gambar_makanan"] = myUrl
                    usermap["harga"] = harga_makanan.toString()
                    usermap["harga_ppn"] = hargappn.toString()
                    usermap["harga_total"] = harga.toString()
                    usermap["kategori"] = kategori_insert.toString()
                    usermap["nama"] = nama_makanan.toString()
                    usermap["id_makanan"] = key.toString()
                    usermap["keterangan"] = keterangan_makanan.toString()
                    usermap["status_makanan"] = "tutup"
                    usermap["uid"] = UserId.toString()
                    usermap["tanggal_tambah"] = Timestamp(Date())

                    val docref = firestore.collection("Warung_Resep").document(key).set(usermap)
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
        }
    }

    //edit makanan
    fun btn_edit(view: View) {
        inisialisasifirebase()
        state.value = FoodState.IsLoading(true)
        if (EditActivity.data==null
        ) {
            val docref = firestore.collection("Warung_Resep")
                .document(EditActivity.idmakanan.toString()).update(
                    "nama",
                    nama_makanan,
                    "harga",
                    harga_makanan,
                    "keterangan",
                    keterangan_makanan).addOnCompleteListener {
                    if (it.isSuccessful) {
                        state.value = FoodState.IsSukses(1)  // 1 sukses
                        state.value = FoodState.ShowToast(Constant.input_sukses)
                        state.value = FoodState.IsLoading(false)
                    }
                }
        } else {
                val fileref =
                    storageReference!!.child(System.currentTimeMillis().toString() + ".jpg")
                var uploadTask: StorageTask<*>
                uploadTask = fileref.putBytes(EditActivity.data!!)
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
                        myUrl = downloadUrl.toString()
                        val photoRef: StorageReference =
                            mFirebaseStorage.getReferenceFromUrl(EditActivity.gambarmakanan.toString())
                        photoRef.delete()

                        val docref = firestore.collection("Warung_Resep")
                            .document(EditActivity.idmakanan.toString()).update(
                                "nama",
                                nama_makanan,
                                "harga",
                                harga_makanan,
                                "keterangan",
                                keterangan_makanan,
                                "gambar_makanan",
                                myUrl.toString()
                            ).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    state.value = FoodState.IsSukses(1)  // 1 sukses
                                    state.value = FoodState.ShowToast(Constant.input_sukses)
                                    state.value = FoodState.IsLoading(false)
                                }
                            }

                    } else {
                        state.value = FoodState.IsLoading(false)
                        state.value = FoodState.ShowToast(Constant.error)
                    }
                }

        }

    }

    fun getdatamakanan_id() {
        inisialisasifirebase()
        state.value = FoodState.IsLoading(true)
        info { "dinda ${EditActivity.id_makanan}" }
        val docref =
            firestore.collection("Warung_Resep").document(EditActivity.id_makanan.toString()).get()
                .addOnSuccessListener { document ->
                    if (document.exists() && document != null) {
                        val data = document.toObject(MakananModels::class.java)
                        datafood_id.postValue(data!!)
                        state.value = FoodState.IsLoading(false)
                    } else {
                        state.value = FoodState.ShowToast(Constant.error)
                        state.value = FoodState.IsLoading(false)
                    }
                }
    }

    fun ambilDataMakanan() {
        inisialisasifirebase()
        val docref = firestore.collection("Warung_Resep").whereEqualTo("uid", UserId.toString())
            .whereEqualTo("kategori", "Makanan").get().addOnSuccessListener { doc ->
                val snapshotsList: MutableList<MakananModels> =
                    doc.toObjects(MakananModels::class.java)
                datamakanan.postValue(snapshotsList)
            }.addOnFailureListener {

            }

    }

    fun getState() = state
    fun getSwitch() = dataswitch
    fun getMakanan() = datamakanan
    fun getMakanan_Id() = datafood_id


    sealed class FoodState {
        data class IsLoading(var loading: Boolean = false) : FoodState()
        data class ShowToast(var message: String) : FoodState()
        data class IsSukses(var sukses: Int? = null) : FoodState()
        data class IsSuksesMenu(var sukses: Int? = null) : FoodState()
    }
}




