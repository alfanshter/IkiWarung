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
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_edit.btn_foto
import kotlinx.android.synthetic.main.activity_edit.btn_galery
import kotlinx.android.synthetic.main.activity_edit.edt_harga
import kotlinx.android.synthetic.main.activity_edit.edt_keterangan
import kotlinx.android.synthetic.main.activity_edit.edt_nama
import kotlinx.android.synthetic.main.activity_edit.gambar_makanan
import kotlinx.android.synthetic.main.activity_insert_food.*
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.HashMap
import kotlin.math.roundToInt

class EditActivity : AppCompatActivity(),AnkoLogger {
    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    private var filepathcamera: Uri? = null
    lateinit var progressDialog: ProgressDialog
    lateinit var ref : DatabaseReference
    lateinit var reflistener : ValueEventListener
    lateinit var auth: FirebaseAuth
    var keywarung : String? = null
    var logic = 0
    private var myUrl = ""
    lateinit var mFirebaseStorage : FirebaseStorage

    private var storageReference: StorageReference? = null

    var userID : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        progressDialog = ProgressDialog(this)
        val bundle: Bundle? = intent.extras
        keywarung = bundle!!.getString("firebase_keywarung")
        mFirebaseStorage = FirebaseStorage.getInstance()

        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        ref = FirebaseDatabase.getInstance().reference.child("Pandaan").child("Resto_Detail").child(userID.toString()).child(keywarung.toString())
        storageReference = FirebaseStorage.getInstance().reference.child("Warung").child(userID.toString()).child("resep")

        reflistener = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var nama = snapshot.child("nama").value.toString()
                    var harga = snapshot.child("harga").value.toString()
                    var keterangan = snapshot.child("keterangan").value.toString()
                    var foto = snapshot.child("gambar").value.toString()
                    var kategori = snapshot.child("kategori").value.toString()


                    edt_nama.setText(nama)
                    edt_harga.setText(harga)
//                    edt_keterangan.setText(keterangan)
                    Picasso.get().load(foto).into(gambar_makanan)



                    btn_edit.setOnClickListener {
                        var hargamakanan = edt_harga.text.toString().toInt()
                        var hargappn = ((hargamakanan *15 )/100)
                        var hargatotal = hargamakanan + hargappn
                        var harga_total = (hargatotal.toDouble()/1000).roundToInt() * 1000

                        if (logic ==1){
                            progressDialog.setTitle("Upload Makanan")
                            progressDialog.setMessage("Tunggu , sedang update")
                            progressDialog.show()
                            val bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath)
                            val baos = ByteArrayOutputStream()
                            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos)
                            val data = baos.toByteArray()
                            val fileref =
                                storageReference!!.child(System.currentTimeMillis().toString() + ".jpg")
                            var uploadTask: StorageTask<*>
                            uploadTask = fileref.putBytes(data)
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
                                    myUrl = downloadUrl.toString()
                                    ref.child("nama").setValue(edt_nama.text.toString())
                                    ref.child("harga").setValue(edt_harga.text.toString())
                                    ref.child("harga_ppn").setValue(hargappn.toString())
                                    ref.child("harga_total").setValue(harga_total.toString())
                                    ref.child("keterangan").setValue(edt_keterangan.text.toString())
                                    ref.child("gambar").setValue(myUrl)
                                    val photoRef: StorageReference =
                                        mFirebaseStorage.getReferenceFromUrl(foto)
                                    photoRef.delete()
                                    finish()
                                    startActivity(intentFor<MainActivity>().clearTask().newTask())

                                } else {
                                    progressDialog.dismiss()
                                    toast("upload gagal")
                                }
                            })

                        }
                        else if (logic==2){
                            progressDialog.setTitle("Upload Makanan")
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
                                    myUrl = downloadUrl.toString()
                                    ref.child("nama").setValue(edt_nama.text.toString())
                                    ref.child("harga").setValue(edt_harga.text.toString())
                                    ref.child("harga_ppn").setValue(hargappn.toString())
                                    ref.child("harga_total").setValue(harga_total.toString())
                                    ref.child("keterangan").setValue(edt_keterangan.text.toString())
                                    ref.child("gambar").setValue(myUrl)
                                    val photoRef: StorageReference =
                                        mFirebaseStorage.getReferenceFromUrl(foto)
                                    photoRef.delete()
                                    finish()
                                    startActivity(intentFor<MainActivity>().clearTask().newTask())

                                } else {
                                    progressDialog.dismiss()
                                    toast("upload gagal")
                                }
                            })

                        }
                        else{
                            ref.child("nama").setValue(edt_nama.text.toString())
                            ref.child("harga").setValue(edt_harga.text.toString())
                            ref.child("harga_ppn").setValue(hargappn.toString())
                            ref.child("harga_total").setValue(harga_total.toString())
                            ref.child("keterangan").setValue(edt_keterangan.text.toString())
                            finish()
                            startActivity(intentFor<MainActivity>().clearTask().newTask())

                        }
                     }
                }
            }

        }

        ref.addListenerForSingleValueEvent(reflistener)

        btn_galery.setOnClickListener {
            pilihfile()
        }
        btn_foto.setOnClickListener {
            var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 123)

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        ref.removeEventListener(reflistener)
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
        inImage.compress(Bitmap.CompressFormat.JPEG, 25, bytes)
        val path: String =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

}