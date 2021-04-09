package com.alfanshter.iki_warung

import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.iki_warung.databinding.ActivityFirstaddfoodBinding
import com.alfanshter.iki_warung.viewmodel.FoodViewModel
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_firstaddfood.*
import kotlinx.android.synthetic.main.activity_firstaddfood.btn_foto
import kotlinx.android.synthetic.main.activity_firstaddfood.btn_galery
import kotlinx.android.synthetic.main.activity_firstaddfood.gambar_makanan
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class firstaddfoodActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    var nama: String? = null
    var harga: String? = null
    var keterangan: String? = null
    private val PICK_IMAGE_REQUEST = 1

    //FOTO
    private var filepathcamera: Uri? = null
    private val REQUEST_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2



    var jenis: String? = null
    private var storageReference: StorageReference? = null
    private var mDatabase: DatabaseReference? = null
    private var myUrl = ""
    lateinit var auth: FirebaseAuth
    var userID: String? = null
    lateinit var database: DatabaseReference
    var openwarung: Date? = null
    lateinit var radiokategori: RadioButton
    companion object {
        var openday: String? = null
        var jambukawarung: String? = null
        var jamtutupwarung: String? = null
        var image_uri: Uri? = null
        var kategori: String? = null

    }

    //Loading
    lateinit var progressDialog: ProgressDialog
    //binding
    lateinit var binding: ActivityFirstaddfoodBinding
    lateinit var foodViewModel: FoodViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_firstaddfood)
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel::class.java)
        binding.viewmodels = foodViewModel

        foodViewModel.getState().observer(this, androidx.lifecycle.Observer {
            handleUiState(it)
        })
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        val hari = resources.getStringArray(R.array.hari)
        val spinner = findViewById<Spinner>(R.id.spinner_buka)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, hari
            )
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    openday = hari[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

        }
        mDatabase = FirebaseDatabase.getInstance().reference
        database = FirebaseDatabase.getInstance().getReference("Pandaan")

//        nama = edt_nama.text.toString().trim()
//        harga = edt_harga.text.toString().trim()
//        keterangan = edt_keterangan.text.toString().trim()
//        storageReference =
//            FirebaseStorage.getInstance().reference.child("Warung").child(userID.toString())
//                .child("resep")

        btn_foto.setOnClickListener {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
            image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            //camera intent
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)

        }

                        val intSelectkategori: Int = radioGroup_kategori!!.checkedRadioButtonId
                radiokategori = findViewById(intSelectkategori)
                kategori = radiokategori.text.toString()


//        btnUpload.setOnClickListener {
//            var edtnama = edt_nama.text.toString()
//            var edtharga = edt_harga.text.toString()
//            var edtketerangan = edt_keterangan.text.toString()
//
//            if (!TextUtils.isEmpty(edtnama) && !TextUtils.isEmpty(edtharga) && !TextUtils.isEmpty(
//                    edtketerangan
//                ) && myUrl == "" && !TextUtils.isEmpty(edit_tutupwarung.text.toString()) && !TextUtils.isEmpty(
//                    edit_openwarung.text.toString()
//                ) && openday != null
//            ) {
//
//                val intSelectkategori: Int = radioGroup_kategori!!.checkedRadioButtonId
//                radiokategori = findViewById(intSelectkategori)
//                kategori = radiokategori.text.toString()
//
//
//                var hargamakanan = edt_harga.text.toString().toInt()
//                var hargappn = ((hargamakanan * 15) / 100)
//                var hargatotal = hargamakanan + hargappn
//                when {
//                    filepathcamera == null -> toast("ambil gambar telebih dahulu ")
//                    TextUtils.isEmpty(edt_nama.text.toString()) -> toast("masukkan nama terlebih dahulu")
//                    TextUtils.isEmpty(edt_harga.text.toString()) -> toast("masukkan harga terlebih dahulu")
//                    TextUtils.isEmpty(edt_keterangan.text.toString()) -> toast("masukkan nama keterangan dahulu")
//                    TextUtils.isEmpty(edit_tutupwarung.text.toString()) -> toast("masukkan jam dahulu")
//                    TextUtils.isEmpty(edit_openwarung.text.toString()) -> toast("masukkan jam dahulu")
//                    else -> {
//                        val progressDialog = ProgressDialog(this)
//                        progressDialog.setTitle("Akun Setting")
//                        progressDialog.setMessage("Tunggu , sedang update")
//                        progressDialog.show()
//                        val fileref =
//                            storageReference!!.child(
//                                System.currentTimeMillis().toString() + ".jpg"
//                            )
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
//                                var harga = (hargatotal.toDouble() / 1000).roundToInt() * 1000
//                                val usermap: MutableMap<String, Any?> = HashMap()
//
//                                usermap["gambar"] = myUrl
//                                usermap["harga"] = edt_harga.text.toString()
//                                usermap["harga_ppn"] = hargappn.toString()
//                                usermap["harga_total"] = harga.toString()
//                                usermap["kategori"] = radiokategori.text.toString()
//                                usermap["nama"] = edt_nama.text.toString()
//                                usermap["id"] = key.toString()
//                                usermap["kode_makanan"] = kode.toString()
//                                usermap["tutup_warungday"] = openday.toString()
//                                usermap["jam_buka"] = jambukawarung.toString()
//                                usermap["jam_tutup"] = jamtutupwarung.toString()
//
//
//                                val uploadgambar =
//                                    FirebaseDatabase.getInstance().reference
//                                        .child("Pandaan").child("Resto")
//                                        .child(userID.toString()).child("gambar")
//                                        .setValue(myUrl.toString())
//                                val uploadnama =
//                                    FirebaseDatabase.getInstance().reference
//                                        .child("Pandaan").child("Resto")
//                                        .child(userID.toString()).child("nama")
//                                        .setValue(edt_nama.text.toString())
//                                val uploadstatus =
//                                    FirebaseDatabase.getInstance().reference
//                                        .child("Pandaan").child("Resto")
//                                        .child(userID.toString()).child("status")
//                                        .setValue("Tutup")
//
//                                val tutup_warungday =
//                                    FirebaseDatabase.getInstance().reference
//                                        .child("Pandaan").child("Resto")
//                                        .child(userID.toString()).child("tutup_warungday")
//                                        .setValue(openday.toString())
//
//                                val jam_buka =
//                                    FirebaseDatabase.getInstance().reference
//                                        .child("Pandaan").child("Resto")
//                                        .child(userID.toString()).child("jam_buka")
//                                        .setValue(jambukawarung.toString())
//
//                                val jam_tutup =
//                                    FirebaseDatabase.getInstance().reference
//                                        .child("Pandaan").child("Resto")
//                                        .child(userID.toString()).child("jam_tutup")
//                                        .setValue(jamtutupwarung.toString())
//
//                                mDatabase!!.child("Pandaan")
//                                    .child("Resto_Detail").child(userID.toString())
//                                    .child(key.toString())
//                                    .setValue(usermap)
//                                    .addOnCompleteListener(OnCompleteListener<Void?> {
//                                        finish()
//                                        startActivity(
//                                            intentFor<MainActivity>().clearTask().newTask()
//                                        )
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
//            } else {
//                toast("Masukkan makanan terlebih dahulu")
//            }
//        }

        btn_galery.setOnClickListener {
            pilihfile()
        }
        edit_openwarung.setOnClickListener {
            openwarungtime()
        }
        edit_tutupwarung.setOnClickListener {
            tutupwarungtime()
        }


    }

    private fun handleUiState(it: FoodViewModel.FoodState?) {
        when(it){
            is FoodViewModel.FoodState.IsLoading -> loading(it.loading)
            is FoodViewModel.FoodState.ShowToast -> toast(it.message)
            is FoodViewModel.FoodState.IsSukses -> insertfirstfood(it.sukses)
        }

    }

    private fun insertfirstfood(sukses: Int?) {
        if (sukses==1){
            startActivity(
                intentFor<MainActivity>().clearTask().newTask()
            )
        }
    }

    private fun openwarungtime() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            edit_openwarung.setText(SimpleDateFormat("HH:mm").format(cal.time))
            openwarung = cal.time
            jambukawarung = (SimpleDateFormat("HH:mm").format(cal.time))

        }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun tutupwarungtime() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            edit_tutupwarung.setText(SimpleDateFormat("HH:mm").format(cal.time))
            openwarung = cal.time
            jamtutupwarung = (SimpleDateFormat("HH:mm").format(cal.time))

        }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun pilihfile() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PICK_IMAGE)

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
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Picasso.get().load(image_uri).fit().into(gambar_makanan)
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                image_uri = data?.data

                Picasso.get().load(image_uri).fit().into(gambar_makanan)
            }
        }

    }


    fun loading(status: Boolean){
        if (status){
            progressDialog.setTitle(Constant.tunggu)
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
        }else{
            progressDialog.dismiss()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}