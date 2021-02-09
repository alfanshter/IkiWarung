package com.alfanshter.IKI_Warung

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edit_resto.*
import org.jetbrains.anko.*

class EditRestoActivity : AppCompatActivity(),AnkoLogger {

    lateinit var ref : DatabaseReference
    lateinit var auth : FirebaseAuth
    var UserID : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_resto)

        auth = FirebaseAuth.getInstance()
        UserID = auth.currentUser!!.uid
//        ambildata()
    }

//    private fun ambildata(){
//        ref = FirebaseDatabase.getInstance().reference.child("Pandaan")
//        ref.child("Resto").child(UserID.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val jam_buka = snapshot.child("jam_buka").value.toString()
//                val jam_tutup = snapshot.child("jam_tutup").value.toString()
//                val open_warungday = snapshot.child("tutup_warungday").value.toString()
//
//                edit_openwarung.setText(jam_buka.toString())
//                edit_tutupwarung.setText(jam_tutup.toString())
//                edt_keterangan.setText(open_warungday.toString())
//
//                btnUpload.setOnClickListener {
//                    val  tutup_warungday=
//                        FirebaseDatabase.getInstance().reference
//                            .child("Pandaan").child("Resto").child(UserID.toString()).child("tutup_warungday").setValue(edt_keterangan.text.toString())
//
//                    val  jam_buka=
//                        FirebaseDatabase.getInstance().reference
//                            .child("Pandaan").child("Resto").child(UserID.toString()).child("jam_buka").setValue(edit_tutupwarung.text.toString())
//
//                    val  jam_tutup=
//                        FirebaseDatabase.getInstance().reference
//                            .child("Pandaan").child("Resto").child(UserID.toString()).child("jam_tutup").setValue(edit_openwarung.text.toString()).addOnCompleteListener {
//                                if (it.isSuccessful){
//                                    toast("berhasil update")
//                                    startActivity(intentFor<MainActivity>().clearTask().newTask())
//                                }
//                            }
//
//                }
//            }
//
//        })
//    }
}