package com.alfanshter.iki_warung

import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.alfanshter.iki_warung.Ui.InsertFoodActivity
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.iki_warung.Utils.CustomProgressDialog
import com.alfanshter.iki_warung.databinding.ActivityEditRestoBinding
import com.alfanshter.iki_warung.viewmodel.UsersViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_resto.*
import kotlinx.android.synthetic.main.activity_edit_resto.edit_openwarung
import kotlinx.android.synthetic.main.activity_edit_resto.edit_tutupwarung
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class EditRestoActivity : AppCompatActivity(),AnkoLogger {

     var progressDialog = CustomProgressDialog()
    lateinit var binding : ActivityEditRestoBinding
    lateinit var usersViewModel: UsersViewModel

    //FOTO
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    //Date
    var openwarung: Date? = null
    companion object{
        var image_uri: Uri? = null
        var openday: String? = null
        var jambukawarung: String? = null
        var jamtutupwarung: String? = null
        var data : ByteArray? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_resto)
        usersViewModel = ViewModelProviders.of(this).get(UsersViewModel::class.java)
        binding.viewmodel = usersViewModel

        usersViewModel.ProfilWarung()
        usersViewModel.getProfilWarung().observe(this, androidx.lifecycle.Observer {
            binding.editOpenwarung.setText(it.jam_buka)
            binding.editTutupwarung.setText(it.jam_tutup)
            Picasso.get().load(it.foto_icon).fit().into(binding.gambarWarung)
        })
        usersViewModel.getState().observer(this, androidx.lifecycle.Observer {
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

        binding.btnFoto.setOnClickListener {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
            image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            //camera intent
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        }



        binding.btnGalery.setOnClickListener {
            pilihfile()
        }

        binding.editOpenwarung.setOnClickListener {
            openwarungtime()
        }

        binding.editTutupwarung.setOnClickListener {
            tutupwarungtime()
        }
    }

    private fun handleUiState(it: UsersViewModel.UserState?) {
        when(it){
            is UsersViewModel.UserState.Isloading -> loading(it.userstate)
            is UsersViewModel.UserState.IsSuccess -> isSukses(it.what)
            is UsersViewModel.UserState.ShowToast -> toast(it.message)
        }

    }

    private fun isSukses(what: Int?) {
        if (what==1){
            startActivity(intentFor<MainActivity>().clearTask().newTask())
            finish()
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Picasso.get().load(image_uri).fit().into(gambar_warung)
                convert()
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                image_uri = data?.data
                Picasso.get().load(image_uri).fit().into(gambar_warung)
                convert()
            }
        }

    }

    fun loading(state : Boolean){
        if (state){
            progressDialog.show(this@EditRestoActivity, Constant.tunggu)
        }else{
            progressDialog.dialog.dismiss()
        }
    }

    fun convert(){
        val bmp = MediaStore.Images.Media.getBitmap(contentResolver, image_uri)
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        data = baos.toByteArray()

    }


}