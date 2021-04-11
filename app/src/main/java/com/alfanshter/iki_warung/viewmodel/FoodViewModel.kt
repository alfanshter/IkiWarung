package com.alfanshter.iki_warung.viewmodel

import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModel
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.iki_warung.Utils.SingleLiveEvent
import com.alfanshter.iki_warung.firstaddfoodActivity
import com.alfanshter.iki_warung.firstaddfoodActivity.Companion.image_uri
import com.alfanshter.iki_warung.firstaddfoodActivity.Companion.jambukawarung
import com.alfanshter.iki_warung.firstaddfoodActivity.Companion.jamtutupwarung
import com.alfanshter.iki_warung.firstaddfoodActivity.Companion.kategori
import com.alfanshter.iki_warung.firstaddfoodActivity.Companion.openday
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import kotlin.math.roundToInt

class FoodViewModel : ViewModel(), AnkoLogger {
    private var state: SingleLiveEvent<FoodState> = SingleLiveEvent()

    //firebase
    private var storageReference: StorageReference? = null
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    var UserId: String? = null

    var nama_makanan: String? = null
    var harga_makanan: String? = null
    var keterangan_makanan: String? = null

    private var myUrl = ""

    val kode = kodeorder()
    fun inisialisasifirebase() {
        auth = FirebaseAuth.getInstance()
        UserId = auth.currentUser!!.uid
        storageReference =
            FirebaseStorage.getInstance().reference.child("Warung").child(UserId.toString())
                .child("resep")
        firestore = FirebaseFirestore.getInstance()
    }

    fun btn_tambahkan(view: View) {
        state.value = FoodState.IsLoading(true)
        inisialisasifirebase()

        if (nama_makanan.isNullOrEmpty() || harga_makanan.isNullOrEmpty() || keterangan_makanan.isNullOrEmpty() || openday.isNullOrEmpty()
            || jambukawarung.isNullOrEmpty() || jamtutupwarung.isNullOrEmpty() || image_uri == null
            || kategori == null
        ) {
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
            uploadTask = fileref.putFile(image_uri!!)
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
                        FirebaseDatabase.getInstance().reference.push().key

                    myUrl = downloadUrl.toString()
                    var harga = (hargatotal.toDouble() / 1000).roundToInt() * 1000
                    val usermap: MutableMap<String, Any?> = HashMap()

                    usermap["gambar"] = myUrl
                    usermap["harga"] = harga_makanan.toString()
                    usermap["harga_ppn"] = hargappn.toString()
                    usermap["harga_total"] = harga.toString()
                    usermap["kategori"] = kategori!!.toString()
                    usermap["nama"] = nama_makanan.toString()
                    usermap["id"] = key.toString()
                    usermap["kode_makanan"] = kode.toString()
                    usermap["tutup_warungday"] = firstaddfoodActivity.openday.toString()
                    usermap["jam_buka"] = firstaddfoodActivity.jambukawarung.toString()
                    usermap["jam_tutup"] = firstaddfoodActivity.jamtutupwarung.toString()
                    usermap["uid"] = UserId.toString()
                    info { "dinda $UserId" }
                    val docref =
                        firestore.collection("Warung_Akun").document(UserId.toString()).update(
                            "gambar", myUrl,
                            "tutup_warungday", openday,
                            "jam_buka", jambukawarung,
                            "jam_tutup", jamtutupwarung
                        ).addOnCompleteListener {
                            if (it.isSuccessful) {
                                val ref = firestore.collection("Warung_Detail").document().set(usermap).addOnCompleteListener {
                                    if (it.isSuccessful){
                                        state.value = FoodState.IsSukses(1)
                                    }
                                }
                            }
                        }
                }
            }
                state.value = FoodState.IsLoading(false)
        }
    }
        //ambil data ketika gak ada makanan sama sekali langsung diarahkan ke firstfood
    fun IsGetData(){
        inisialisasifirebase()
        val docref = firestore.collection("Warung_Detail").whereEqualTo("alfan","dinda")
                if (docref.get().isSuccessful){
                    info { "dinda benar" }
                }else{
                    info { "dinda salah" }
                }



        }


//        } .get().addOnSuccessListener {
//            document ->
//            if (document.contains("alfan")){
//                info { "dinda ayu benar" }
//                state.value = FoodState.ShowToast("Selamat Bekerja!!!")
//            }else{
//                info { "dinda ayu salah" }
//                state.value = FoodState.IsSukses(1)  // 1 berarti Sukses dan pindah halaman
//            }
//        }.addOnFailureListener {
//            info { "dinda error ${it.message}" }
//            state.value = FoodState.ShowToast(Constant.error)
//        }

    fun kodeorder(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val outputStrLength = (20..26).shuffled().first()

        return (1..outputStrLength)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }




    fun getState() = state


    sealed class FoodState {
        data class IsLoading(var loading: Boolean = false) : FoodState()
        data class ShowToast(var message: String) : FoodState()
        data class IsSukses(var sukses: Int? = null) : FoodState()
    }
    }




