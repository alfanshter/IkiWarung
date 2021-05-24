package com.alfanshter.iki_warung

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.iki_warung.Utils.CustomProgressDialog
import com.alfanshter.iki_warung.databinding.ActivityEditBinding
import com.alfanshter.iki_warung.viewmodel.FoodViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_edit.gambar_makanan
import kotlinx.android.synthetic.main.activity_insert_food.*
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream

class EditActivity : AppCompatActivity(), AnkoLogger {
    private var filePath: Uri? = null
    lateinit var progressDialog: ProgressDialog
    lateinit var auth: FirebaseAuth
    lateinit var mFirebaseStorage: FirebaseStorage


    companion object {
        var id_makanan: String? = null
        var data: ByteArray? = null
        var gambarmakanan: String? = null
        var idmakanan: String? = null
    }

    private var progressdialog = CustomProgressDialog()

    lateinit var foodViewModel: FoodViewModel
    lateinit var binding: ActivityEditBinding
    var userID: String? = null

    //FOTO
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle(Constant.tunggu)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel::class.java)
        binding.viewmodel = foodViewModel

        val bundle: Bundle? = intent.extras
        id_makanan = bundle!!.getString("id_makanan")
        mFirebaseStorage = FirebaseStorage.getInstance()

        foodViewModel.getdatamakanan_id()
        foodViewModel.getState().observer(this, Observer {
            handleUiState(it)
        })

        foodViewModel.getMakanan_Id().observe(this, Observer {
            progressDialog.dismiss()
            binding.edtNama.setText(it.nama.toString())
            binding.edtHarga.setText(it.harga.toString())
            binding.edtKeterangan.setText(it.keterangan)
            Picasso.get().load(it.gambar_makanan).into(binding.gambarMakanan)
            gambarmakanan = it.gambar_makanan
            idmakanan = it.id_makanan
        })

        binding.btnGalery.setOnClickListener {
            pilihfile()
        }
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

    }

    private fun handleUiState(it: FoodViewModel.FoodState?) {
        when (it) {
            is FoodViewModel.FoodState.ShowToast -> toast(it.message)
            is FoodViewModel.FoodState.IsLoading -> loading(it.loading)
            is FoodViewModel.FoodState.IsSukses -> sukses(it.sukses)
        }

    }

    private fun sukses(sukses: Int?) {
        if (sukses == 1) {
            startActivity(intentFor<MainActivity>().clearTask().newTask())
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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
                Picasso.get().load(filePath).fit().into(gambar_makanan)
                convert()
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                filePath = data?.data
                Picasso.get().load(filePath).fit().into(gambar_makanan)
                convert()
            }
        }

    }

    fun convert() {
        val bmp = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        data = baos.toByteArray()
    }


    fun loading(state: Boolean) {
        if (state) {
            progressdialog.show(this@EditActivity, Constant.tunggu)
        } else {
            progressdialog.dialog.dismiss()
        }
    }

}