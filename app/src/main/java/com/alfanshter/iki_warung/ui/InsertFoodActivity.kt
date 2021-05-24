package com.alfanshter.iki_warung.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alfanshter.iki_warung.MainActivity
import com.alfanshter.iki_warung.R
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.iki_warung.Utils.CustomProgressDialog
import com.alfanshter.iki_warung.databinding.ActivityInsertFoodBinding
import com.alfanshter.iki_warung.viewmodel.FoodViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_insert_food.*
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream


class InsertFoodActivity : AppCompatActivity(),AnkoLogger {
    var nama: String? = null
    var harga: String? = null
    lateinit var radiokategori: RadioButton
    lateinit var auth: FirebaseAuth

    //FOTO
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2

    lateinit var binding: ActivityInsertFoodBinding
    lateinit var insertfoodmodel: FoodViewModel

    private var progressdialog  = CustomProgressDialog()

    companion object {
        var kategori_insert: String? = null
         var filePath: Uri? = null
         var data : ByteArray? = null


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_insert_food)
        insertfoodmodel = ViewModelProviders.of(this).get(FoodViewModel::class.java)
        binding.viewmodel = insertfoodmodel

        insertfoodmodel.getState().observer(this, Observer {
            handleUiState(it)
        })



        binding.btnFoto.setOnClickListener {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
            filePath =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            //camera intent
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, filePath)
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)

        }

        binding.btnGalery.setOnClickListener {
            pilihfile()
        }


    }

    private fun handleUiState(it: FoodViewModel.FoodState?) {
        when(it){
            is FoodViewModel.FoodState.ShowToast -> toast(it.message)
            is FoodViewModel.FoodState.IsLoading -> loading(it.loading)
            is FoodViewModel.FoodState.IsSukses -> sukses(it.sukses!!)
        }
    }

    private fun pilihfile() {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
             if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    dialog_permis(
                        "External storage",  Manifest.permission.READ_EXTERNAL_STORAGE

                    )
                } else {
                    ActivityCompat
                        .requestPermissions(
                           this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_PICK_IMAGE
                        )
                }

            }
        }
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PICK_IMAGE)

    }

    fun dialog_permis(
        msg: String,
        permission: String
    ) {
        val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permission necessary")
        alertBuilder.setMessage("$msg permission is necessary")
        alertBuilder.setPositiveButton(android.R.string.yes,
            DialogInterface.OnClickListener { dialog, which ->
                ActivityCompat.requestPermissions(
                    (this as Activity?)!!, arrayOf(permission),
                    REQUEST_PICK_IMAGE
                )
            })
        val alert: AlertDialog = alertBuilder.create()
        alert.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Picasso.get().load(filePath).fit().centerCrop().into(gambar_makanan)
                convert()
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                filePath = data?.data
                Picasso.get().load(filePath).fit().centerCrop().into(gambar_makanan)
                convert()
            }
        }

    }

    fun convert(){
        val bmp = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        data = baos.toByteArray()

    }

    fun sukses(state : Int){
        if (state == 1){
            startActivity(intentFor<MainActivity>().clearTask().newTask())
            finish()
        }
    }

    fun loading(state : Boolean){
        if (state){
        progressdialog.show(this@InsertFoodActivity, Constant.tunggu)
        }else{
            progressdialog.dialog.dismiss()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}