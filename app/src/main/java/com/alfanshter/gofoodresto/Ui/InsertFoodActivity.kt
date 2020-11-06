package com.alfanshter.gofoodresto.Ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.RadioButton
import android.widget.Toast
import com.alfanshter.gofoodresto.MainActivity
import com.alfanshter.gofoodresto.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_insert_food.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.IOException
import java.util.HashMap

class InsertFoodActivity : AppCompatActivity() {
    var nama: String? = null
    var harga: String? = null
    var keterangan: String? = null
    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    lateinit var radiokategori: RadioButton
    lateinit var radiojenis: RadioButton
    var kategori: String? = null
    var jenis: String? = null
    private var storageReference: StorageReference? = null
    private var mDatabase: DatabaseReference? = null

    private var myUrl = ""
    lateinit var auth: FirebaseAuth
    var userID: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_food)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        mDatabase = FirebaseDatabase.getInstance().reference

        nama = edt_nama.text.toString().trim()
        harga = edt_harga.text.toString().trim()
        keterangan = edt_keterangan.text.toString().trim()
        storageReference = FirebaseStorage.getInstance().reference.child("images")

        btn_foto.setOnClickListener {
            pilihfile()
        }

        btnUpload.setOnClickListener {

            val intSelectkategori: Int = radioGroup_kategori!!.checkedRadioButtonId
            radiokategori = findViewById(intSelectkategori)
            kategori = radiokategori.text.toString()

            val intSelectJenis: Int = radioGroup_jenis!!.checkedRadioButtonId
            radiojenis = findViewById(intSelectJenis)
            jenis = radiojenis.text.toString()

            when {
                filePath == null -> toast("ambil gambar telebih dahulu ")
                TextUtils.isEmpty(edt_nama.text.toString()) -> toast("masukkan nama terlebih dahulu")
                TextUtils.isEmpty(edt_harga.text.toString()) -> toast("masukkan harga terlebih dahulu")
                TextUtils.isEmpty(edt_keterangan.text.toString()) -> toast("masukkan nama keterangan dahulu")
                else -> {
                    val progressDialog = ProgressDialog(this)
                    progressDialog.setTitle("Akun Setting")
                    progressDialog.setMessage("Tunggu , sedang update")
                    progressDialog.show()
                    val fileref =
                        storageReference!!.child(System.currentTimeMillis().toString() + ".jpg")
                    var uploadTask: StorageTask<*>
                    uploadTask = fileref.putFile(filePath!!)

                    uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw  it
                                progressDialog.dismiss()
                            }
                        }
                        return@Continuation fileref.downloadUrl
                    }).addOnCompleteListener(OnCompleteListener<Uri> { task ->
                        if (task.isSuccessful) {
                            val downloadUrl = task.result
                            val key =
                                FirebaseDatabase.getInstance().reference.push().key

                            myUrl = downloadUrl.toString()

                            val usermap: MutableMap<String, Any?> = HashMap()
                            usermap["gambar"] = myUrl
                            usermap["harga"] = edt_harga.text.toString()
                            usermap["kategori"] = radiokategori.text.toString()
                            usermap["jenis"] = radiojenis.text.toString()
                            usermap["nama"] = edt_nama.text.toString()
                            mDatabase!!.child("Pandaan")
                                .child("User_Resto").child(userID.toString()).child(key.toString())
                                .setValue(usermap)
                                .addOnCompleteListener(OnCompleteListener<Void?> {
                                    finish()
                                    startActivity<MainActivity>()
                                })

                            toast("upload sukses")
                            progressDialog.dismiss()
                        } else {
                            progressDialog.dismiss()
                            toast("upload gagal")
                        }
                    })

                }

            }
        }

    }

    private fun pilihfile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "PILIH GAMBAR"), PICK_IMAGE_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            filePath = data.data!!
            try {
                Picasso.get().load(filePath).fit().into(gambar_makanan)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}