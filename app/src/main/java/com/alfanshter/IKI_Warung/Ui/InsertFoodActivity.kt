package com.alfanshter.IKI_Warung.Ui

import android.R.attr
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.alfanshter.IKI_Warung.MainActivity
import com.alfanshter.IKI_Warung.R
import com.alfanshter.IKI_Warung.ViewModels.MakananState
import com.alfanshter.IKI_Warung.ViewModels.MakananViewModel
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_insert_food.*
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.lang.Integer.parseInt
import java.util.*


class InsertFoodActivity : AppCompatActivity() {
    var nama: String? = null
    var harga: String? = null
    var keterangan: String? = null
    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    private var filepathcamera: Uri? = null

    private var filePathGambar : Uri? = null
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

    private lateinit var makananViewModel: MakananViewModel
    lateinit var sessionManager: SessionManager
    lateinit var imagename: MultipartBody.Part

    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_food)
//        auth = FirebaseAuth.getInstance()
//        userID = auth.currentUser!!.uid
//        mDatabase = FirebaseDatabase.getInstance().reference

        sessionManager = SessionManager(this)

//        nama = edt_nama.text.toString().trim()
//        harga = edt_harga.text.toString().trim()
//        keterangan = edt_keterangan.text.toString().trim()
//        storageReference = FirebaseStorage.getInstance().reference.child("Warung").child(userID.toString()).child("resep")

        makananViewModel = ViewModelProviders.of(this).get(MakananViewModel::class.java)
        makananViewModel.getState().observer(this, androidx.lifecycle.Observer {
            handleUIState(it)
        })

        btn_foto.setOnClickListener {
            var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 123)
        }

//        btnUpload.setOnClickListener {
//
//            val intSelectkategori: Int = radioGroup_kategori!!.checkedRadioButtonId
//            radiokategori = findViewById(intSelectkategori)
//            kategori = radiokategori.text.toString()
//
//
//            var hargamakanan = edt_harga.text.toString().toInt()
//            var hargappn = ((hargamakanan *15 )/100)
//            var hargatotal = hargamakanan + hargappn
//
//            if (logic==1){
//                when {
//                    filePath == null -> toast("ambil gambar telebih dahulu ")
//                    TextUtils.isEmpty(edt_nama.text.toString()) -> toast("masukkan nama terlebih dahulu")
//                    TextUtils.isEmpty(edt_harga.text.toString()) -> toast("masukkan harga terlebih dahulu")
//                    TextUtils.isEmpty(edt_keterangan.text.toString()) -> toast("masukkan nama keterangan dahulu")
//                    else -> {
//                        val progressDialog = ProgressDialog(this)
//                        progressDialog.setTitle("Upload Makanan")
//                        progressDialog.setMessage("Tunggu , sedang update")
//                        progressDialog.show()
//                        val bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath)
//                        val baos = ByteArrayOutputStream()
//                        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos)
//                        val data = baos.toByteArray()
//                        val fileref =
//                            storageReference!!.child(System.currentTimeMillis().toString() + ".jpg")
//                        var uploadTask: StorageTask<*>
//                        uploadTask = fileref.putBytes(data)
//                        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
//                            if (!task.isSuccessful) {
//                                task.exception?.let {
//                                    throw  it
//                                    progressDialog.dismiss()
//                                }
//                            }
//                            return@Continuation fileref.downloadUrl
//                        }).addOnCompleteListener(OnCompleteListener<Uri> { task ->
//                            if (task.isSuccessful) {
//                                val downloadUrl = task.result
//                                val key =
//                                    FirebaseDatabase.getInstance().reference.push().key
//
//                                myUrl = downloadUrl.toString()
//                                var harga = (hargatotal.toDouble()/1000).roundToInt() * 1000
//
//                                val usermap: MutableMap<String, Any?> = HashMap()
//                                usermap["gambar"] = myUrl
//                                usermap["harga"] = edt_harga.text.toString()
//                                usermap["harga_ppn"] = hargappn.toString()
//                                usermap["harga_total"] = harga.toString()
//                                usermap["kategori"] = radiokategori.text.toString()
//                                usermap["nama"] = edt_nama.text.toString()
//                                usermap["id"] = key.toString()
//                                usermap["keterangan"] = edt_keterangan.text.toString()
//                                usermap["kode_makanan"] = kode
//                                mDatabase!!.child("Pandaan")
//                                    .child("Resto_Detail").child(userID.toString()).child(key.toString())
//                                    .setValue(usermap)
//                                    .addOnCompleteListener(OnCompleteListener<Void?> {
//                                        finish()
//                                        startActivity<MainActivity>()
//                                    })
//
//                                toast("upload sukses")
//                                progressDialog.dismiss()
//                            } else {
//                                progressDialog.dismiss()
//                                toast("upload gagal")
//                            }
//                        })
//
//                    }
//
//                }
//            }
//            else if (logic==2){
//                when {
//                    filepathcamera == null -> toast("ambil gambar telebih dahulu ")
//                    TextUtils.isEmpty(edt_nama.text.toString()) -> toast("masukkan nama terlebih dahulu")
//                    TextUtils.isEmpty(edt_harga.text.toString()) -> toast("masukkan harga terlebih dahulu")
//                    TextUtils.isEmpty(edt_keterangan.text.toString()) -> toast("masukkan nama keterangan dahulu")
//                    else -> {
//                        val progressDialog = ProgressDialog(this)
//                        progressDialog.setTitle("Akun Setting")
//                        progressDialog.setMessage("Tunggu , sedang update")
//                        progressDialog.show()
//                        val fileref =
//                            storageReference!!.child(System.currentTimeMillis().toString() + ".jpg")
//                        var uploadTask: StorageTask<*>
//                        uploadTask = fileref.putFile(filepathcamera!!)
//                        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
//                            if (!task.isSuccessful) {
//                                task.exception?.let {
//                                    throw  it
//                                    progressDialog.dismiss()
//                                }
//                            }
//                            return@Continuation fileref.downloadUrl
//                        }).addOnCompleteListener(OnCompleteListener<Uri> { task ->
//                            if (task.isSuccessful) {
//                                val downloadUrl = task.result
//                                val key =
//                                    FirebaseDatabase.getInstance().reference.push().key
//
//                                myUrl = downloadUrl.toString()
//                                var harga = (hargatotal.toDouble()/1000).roundToInt() * 1000
//
//                                val usermap: MutableMap<String, Any?> = HashMap()
//                                usermap["gambar"] = myUrl
//                                usermap["harga"] = edt_harga.text.toString()
//                                usermap["harga_ppn"] = hargappn.toString()
//                                usermap["harga_total"] = harga.toString()
//                                usermap["kategori"] = radiokategori.text.toString()
//                                usermap["nama"] = edt_nama.text.toString()
//                                usermap["id"] = key.toString()
//                                usermap["keterangan"] = edt_keterangan.text.toString()
//                                usermap["kode_makanan"] = kode
//                                mDatabase!!.child("Pandaan")
//                                    .child("Resto_Detail").child(userID.toString()).child(key.toString())
//                                    .setValue(usermap)
//                                    .addOnCompleteListener(OnCompleteListener<Void?> {
//                                        finish()
//                                        startActivity<MainActivity>()
//                                    })
//
//                                toast("upload sukses")
//                                progressDialog.dismiss()
//                            } else {
//                                progressDialog.dismiss()
//                                toast("upload gagal")
//                            }
//                        })
//
//                    }
//
//                }
//
//            }
//            else{
//                toast("ambil gambar telebih dahulu ")
//            }
//        }

        btnUpload.setOnClickListener {
            tambahMakanan(sessionManager.getAuthToken().toString())
        }

        btn_galery.setOnClickListener {
            pilihfile()
        }

    }


    private fun folder(){
        val path: File = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "FotoWarung"
        )

    }

    private fun tambahMakanan(token: String){
        val nama = edt_nama.text.toString()
        val harga = edt_harga.text.toString()
        val keterangan = edt_keterangan.text.toString()

        val intSelectkategori: Int = radioGroup_kategori!!.checkedRadioButtonId
        radiokategori = findViewById(intSelectkategori)
        val kategori = radiokategori.text.toString()

        val foto = File(currentPhotoPath)
        val requestBody = RequestBody.create(MediaType.parse("image/*"), foto)
        imagename = MultipartBody.Part.createFormData("file", foto.name, requestBody)
        val name = RequestBody.create(MediaType.parse("text/plain"), nama)
        val cate = RequestBody.create(MediaType.parse("text/plain"), kategori)
        val price =  RequestBody.create(MediaType.parse("text/plain"), harga)
        val description = RequestBody.create(MediaType.parse("text/plain"), keterangan)
        val status = RequestBody.create(MediaType.parse("text/plain"), "false")

        if (makananViewModel.validate(nama, parseInt(harga), keterangan, currentPhotoPath)){
//            toast(currentPhotoPath)
            makananViewModel.addMakanan(token, name, cate, price, description, status, imagename)
        }
    }

    private fun handleUIState(it: MakananState){
        when(it){
//            is UserState.Reset -> {
//                setEmailError(null)
//                setPasswordError(null)
//            }
            is MakananState.Error -> {
                isLoading(false)
                toast(it.err)
            }

            is MakananState.ShowToast -> toast(it.message)
            is MakananState.Failed -> {
                isLoading(false)
                toast(it.message)
            }

            is MakananState.Success -> {
//                sessionManager.setAuthToken(it.token)
                startActivity(intentFor<MainActivity>().clearTask().newTask())
//                                        progressDialog.dismiss()
                finish()
                toast(it.message)
//                toast("Login Berhasil")
            }

            is MakananState.IsLoading -> isLoading(it.state)
        }
    }

    private fun isLoading(state: Boolean){
        if (state){
            btnUpload.isEnabled = false
//            progressDialog.setTitle("Tunggu sebentar")
//            progressDialog.show()
        }else{
//            progressDialog.dismiss()
            btnUpload.isEnabled = true
        }
    }

    private fun toast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun pilihfile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "PILIH GAMBAR"), PICK_IMAGE_REQUEST)

    }

    fun kodeorder(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val outputStrLength = (20..26).shuffled().first()

        return (1..outputStrLength)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    val kode = kodeorder()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            logic = 1

            filePath = data.data!!
            filePathGambar = data.data!!

            val selectedImage: Uri = filePathGambar!!
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = contentResolver.query(
                selectedImage, filePathColumn,
                null, null, null
            )

            if(cursor != null){
                cursor.moveToFirst()
                val columnIndex : Int = cursor.getColumnIndex(filePathColumn[0])
                currentPhotoPath = cursor.getString(columnIndex)
            }

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
            filePathGambar = getImageUri(applicationContext, bmp)

            val selectedImage: Uri = filePathGambar!!
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = contentResolver.query(
                selectedImage, filePathColumn,
                null, null, null
            )

            if(cursor != null){
                cursor.moveToFirst()
                val columnIndex : Int = cursor.getColumnIndex(filePathColumn[0])
                currentPhotoPath = cursor.getString(columnIndex)
            }

            try {
                Picasso.get().load(filepathcamera).fit().into(gambar_makanan)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 25, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    override fun onDestroy() {
        super.onDestroy()
        logic = 0
    }
}