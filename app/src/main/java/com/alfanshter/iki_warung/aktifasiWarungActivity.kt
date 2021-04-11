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
import com.alfanshter.iki_warung.databinding.ActivityAktifasiwarungBinding
import com.alfanshter.iki_warung.viewmodel.UsersViewModel
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_aktifasiwarung.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class aktifasiWarungActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    var nama: String? = null
    var harga: String? = null
    var keterangan: String? = null

    //FOTO
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2


    private var mDatabase: DatabaseReference? = null
    lateinit var auth: FirebaseAuth
    var userID: String? = null
    lateinit var database: DatabaseReference
    var openwarung: Date? = null

    companion object {
        var openday: String? = null
        var jambukawarung: String? = null
        var jamtutupwarung: String? = null
        var image_uri: Uri? = null

    }

    //Loading
    lateinit var progressDialog: ProgressDialog

    //binding
    lateinit var binding: ActivityAktifasiwarungBinding
    lateinit var userviewmodel: UsersViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_aktifasiwarung)
        userviewmodel = ViewModelProviders.of(this).get(UsersViewModel::class.java)
        binding.viewmodels = userviewmodel

        userviewmodel.getState().observer(this, androidx.lifecycle.Observer {
            handleUiState(it)
        })
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

    private fun handleUiState(it: UsersViewModel.UserState?) {
        when (it) {
            is UsersViewModel.UserState.ShowToast -> toast(it.message)
            is UsersViewModel.UserState.Isloading -> loading(it.userstate)
            is UsersViewModel.UserState.IsSuccess -> aktifasi_warung(it.what)
        }

    }


    private fun aktifasi_warung(sukses: Int?) {
        if (sukses == 1) {
            startActivity(
                intentFor<MainActivity>().clearTask().newTask()
            )
            finish()
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


    fun loading(status: Boolean) {
        if (status) {
            progressDialog.setTitle(Constant.tunggu)
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}