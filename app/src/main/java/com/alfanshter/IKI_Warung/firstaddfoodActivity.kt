package com.alfanshter.IKI_Warung

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.RadioButton
import com.alfanshter.udinlelangfix.Session.SessionManager
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
import kotlinx.android.synthetic.main.activity_firstaddfood.*
import kotlinx.android.synthetic.main.activity_firstaddfood.btnUpload
import kotlinx.android.synthetic.main.activity_firstaddfood.btn_foto
import kotlinx.android.synthetic.main.activity_firstaddfood.btn_galery
import kotlinx.android.synthetic.main.activity_firstaddfood.edt_harga
import kotlinx.android.synthetic.main.activity_firstaddfood.edt_keterangan
import kotlinx.android.synthetic.main.activity_firstaddfood.edt_nama
import kotlinx.android.synthetic.main.activity_firstaddfood.gambar_makanan
import kotlinx.android.synthetic.main.activity_firstaddfood.radioGroup_kategori
import kotlinx.android.synthetic.main.activity_firstaddfood.radio_jenismakanan
import kotlinx.android.synthetic.main.activity_insert_food.*
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.HashMap

class firstaddfoodActivity : AppCompatActivity() {
    lateinit var sessionManager :SessionManager
    var nama: String? = null
    var harga: String? = null
    var keterangan: String? = null
    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    private var filepathcamera: Uri? = null
    lateinit var radiokategori: RadioButton
    lateinit var radiojenis: RadioButton
    var kategori: String? = null
    var jenis: String? = null
    private var storageReference: StorageReference? = null
    private var mDatabase: DatabaseReference? = null
    var logic = 0
    private var myUrl = ""
    lateinit var auth: FirebaseAuth
    var userID: String? = null
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firstaddfood)
        sessionManager = SessionManager(this)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        mDatabase = FirebaseDatabase.getInstance().reference
        database = FirebaseDatabase.getInstance().getReference("Pandaan")

        nama = edt_nama.text.toString().trim()
        harga = edt_harga.text.toString().trim()
        keterangan = edt_keterangan.text.toString().trim()
        storageReference = FirebaseStorage.getInstance().reference.child("Warung").child(userID.toString()).child("resep")

        btn_foto.setOnClickListener {
            var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 123)

        }

        btnUpload.setOnClickListener {

            val intSelectkategori: Int = radioGroup_kategori!!.checkedRadioButtonId
            radiokategori = findViewById(intSelectkategori)
            kategori = radiokategori.text.toString()

            val intSelectJenis: Int = radio_jenismakanan!!.checkedRadioButtonId
            radiojenis = findViewById(intSelectJenis)
            jenis = radiojenis.text.toString()

            var hargamakanan = edt_harga.text.toString().toInt()
            var hargappn = ((hargamakanan *15 )/100)
            var hargatotal = hargamakanan + hargappn

            if (logic==1){
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
                                usermap["harga_ppn"] = hargappn.toString()
                                usermap["harga_total"] = hargatotal.toString()
                                usermap["kategori"] = radiokategori.text.toString()
                                usermap["jenis"] = radiojenis.text.toString()
                                usermap["nama"] = edt_nama.text.toString()
                                usermap["id"] = key.toString()

                                val uploadgambar =
                                    FirebaseDatabase.getInstance().reference
                                        .child("Pandaan").child("Resto").child(userID.toString()).child("gambar").setValue(myUrl.toString())
                                val uploadnama =
                                    FirebaseDatabase.getInstance().reference
                                        .child("Pandaan").child("Resto").child(userID.toString()).child("nama").setValue(edt_nama.text.toString())

                                mDatabase!!.child("Pandaan")
                                    .child("Resto_Detail").child(userID.toString()).child(key.toString())
                                    .setValue(usermap)
                                    .addOnCompleteListener(OnCompleteListener<Void?> {
                                        finish()
                                        startActivity(intentFor<MainActivity>().clearTask().newTask())

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
            else if (logic==2){
                when {
                    filepathcamera == null -> toast("ambil gambar telebih dahulu ")
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
                        uploadTask = fileref.putFile(filepathcamera!!)
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
                                usermap["id"] = key.toString()
                                val uploadgambar =
                                    FirebaseDatabase.getInstance().reference
                                        .child("Pandaan").child("Resto").child(userID.toString()).child("gambar").setValue(myUrl.toString())
                                val uploadnama =
                                    FirebaseDatabase.getInstance().reference
                                        .child("Pandaan").child("Resto").child(userID.toString()).child("nama").setValue(edt_nama.text.toString())

                                mDatabase!!.child("Pandaan")
                                    .child("Resto_Detail").child(userID.toString()).child(key.toString())
                                    .setValue(usermap)
                                    .addOnCompleteListener(OnCompleteListener<Void?> {
                                        finish()
                                        startActivity(intentFor<MainActivity>().clearTask().newTask())
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
            else{
                toast("ambil gambar telebih dahulu ")
            }
        }

        btn_galery.setOnClickListener {
            pilihfile()
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
            logic = 1
            filePath = data.data!!
            try {
                Picasso.get().load(filePath).fit().into(gambar_makanan)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        else if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            logic = 2
            var bmp = data!!.extras!!.get("data") as Bitmap
            filepathcamera = getImageUri(applicationContext, bmp)

            try {
                Picasso.get().load(filepathcamera).fit().into(gambar_makanan)
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    override fun onDestroy() {
        super.onDestroy()
        logic = 0
    }
}