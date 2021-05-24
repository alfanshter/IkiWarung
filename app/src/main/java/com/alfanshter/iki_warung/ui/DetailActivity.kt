package com.alfanshter.iki_warung.ui

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alfanshter.iki_warung.EditActivity
import com.alfanshter.iki_warung.MainActivity
import com.alfanshter.iki_warung.Model.MakananModels
import com.alfanshter.iki_warung.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.*


class DetailActivity : AppCompatActivity(), AnkoLogger {
    var gambar: String? = null
    var harga: String? = null
    var nama: String? = null
    var latitude: String? = null
    var longitude: String? = null
    var kodesales: String? = null
    var penjual: String? = null
    var id_makanan: String? = null
    var status: Boolean = false
    lateinit var progressDialog: ProgressDialog
    lateinit var dialog: AlertDialog
    lateinit var getswitchlistener: ValueEventListener
    lateinit var database: DatabaseReference
    lateinit var refinfo: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var mFirebaseStorage: FirebaseStorage
    var userID: String? = null

    //firestore
    lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        progressDialog = ProgressDialog(this)
        firestore = FirebaseFirestore.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Pandaan")
        refinfo = FirebaseDatabase.getInstance().getReference("Pandaan")
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        val bundle: Bundle? = intent.extras
        gambar = bundle!!.getString("gambar_makanan")
        id_makanan = bundle.getString("id_makanan")
        harga = bundle.getString("harga")
        nama = bundle.getString("nama")
        mFirebaseStorage = FirebaseStorage.getInstance()
        
        //Tampilkan Data dari MenuFragment
        Picasso.get().load(gambar).fit().centerCrop().into(gambar_detail)
        name.text = nama.toString()
        price.text = harga.toString()

        editmakanan.setOnClickListener {
            startActivity<EditActivity>("id_makanan" to id_makanan.toString())
        }
        btn_back.setOnClickListener {
            finish()
        }
        getswitch()
        switch()
        btn_delete.setOnClickListener {
            hapusmakanan()
        }
    }

    private fun hapusmakanan() {
        val builder = AlertDialog.Builder(this@DetailActivity)
        builder.setTitle("Apakah anda ingin menghapus makaanan ini ? ")
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    val photoRef: StorageReference =
                        mFirebaseStorage.getReferenceFromUrl(gambar.toString())
                    photoRef.delete().addOnSuccessListener {
                        val docref = firestore.collection("Warung_Resep").document(id_makanan.toString()).delete()
                        docref.addOnCompleteListener {
                            if (it.isSuccessful){
                                startActivity(intentFor<MainActivity>().clearTask().newTask())
                                finish()
                            }
                        }

                    }
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                }
                DialogInterface.BUTTON_NEUTRAL -> {
                }
            }
        }
        // Set the alert dialog positive/yes button
        builder.setPositiveButton("YES", dialogClickListener)

        // Set the alert dialog negative/no button
        builder.setNegativeButton("NO", dialogClickListener)

        // Set the alert dialog neutral/cancel button
        builder.setNeutralButton("CANCEL", dialogClickListener)


        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }


    private fun getswitch() {
        val docref = firestore.collection("Warung_Resep").document(id_makanan.toString()).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val data = document.toObject(MakananModels::class.java)
                    if (data!!.status_makanan.equals("buka")) {
                        switch1.isChecked = true
                    } else if (data.status_makanan.equals("tutup")) {
                        switch1.isChecked = false
                    }
                }
            }
    }

    private fun switch() {
        switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //ubah ke posisi buka
                firestore.collection("Warung_Resep").document(id_makanan.toString())
                    .update("status_makanan", "buka").addOnCompleteListener {
                        if (it.isSuccessful) {
                            toast("berhasil")
                        }
                    }.addOnFailureListener {
                        toast(it.message.toString())
                    }

            } else {
                //ubah ke posisi tutup
                firestore.collection("Warung_Resep").document(id_makanan.toString())
                    .update("status_makanan", "tutup").addOnCompleteListener {
                        if (it.isSuccessful) {
                            toast("berhasil")
                        }
                    }.addOnFailureListener {
                        toast(it.message.toString())
                    }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}


