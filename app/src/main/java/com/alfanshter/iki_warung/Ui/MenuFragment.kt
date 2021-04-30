package com.alfanshter.iki_warung.Ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.iki_warung.*
import com.alfanshter.iki_warung.Model.MakananModels
import com.alfanshter.iki_warung.Model.UsersModel
import com.alfanshter.iki_warung.R
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.iki_warung.Utils.CustomProgressDialog
import com.alfanshter.iki_warung.adapter.MakananAdapter
import com.alfanshter.iki_warung.viewmodel.FoodViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.fragment_menu.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class MenuFragment : Fragment(), AnkoLogger {
    var dialog_bidding: Dialog? = null
    private lateinit var rvMakanan: RecyclerView
    private lateinit var rvMinuman: RecyclerView
    var refinfo: DatabaseReference? = null
    var image_list: HashMap<String, String>? = null
    lateinit var tambahbarang: RelativeLayout
    lateinit var dialog: AlertDialog
    lateinit var switch: SwitchMaterial
    lateinit var getswitchlistener: ValueEventListener
    var refid: String? = null

    lateinit var foodViewModel: FoodViewModel
    private var progressdialog = CustomProgressDialog()

    private lateinit var adapter: MakananAdapter
    private lateinit var adapterminuman: MakananAdapter
    lateinit var root: View

    var arrayList = ArrayList<MakananModels>()
    var arrayListMinuman = ArrayList<MakananModels>()

    //database
    lateinit var auth: FirebaseAuth
    var userID: String? = null
    lateinit var firestore: FirebaseFirestore

    companion object {
        var status_switch: Boolean? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_menu, container, false)
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel::class.java)

        dialog_bidding = Dialog(context!!)
        dialog_bidding!!.setContentView(R.layout.layout_editharga)
        switch = root.find(R.id.swt_buka)
        rvMakanan = root.find(R.id.rv_makanan)
        rvMinuman = root.find(R.id.rv_minuman)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        tambahbarang = root.find(R.id.rv_menu1)
        getswitch()
        switch()
        getmakanan()
        getminuman()
        tambahbarang.setOnClickListener {
            startActivity<InsertFoodActivity>()
        }

        root.rv_menu2.setOnClickListener {
            startActivity<EditRestoActivity>()
        }

        if (auth == null) {
            userID = null
            startActivity<SplashScreen>()
            activity!!.finish()
        }


//        switch()


        return root
    }

    //ambil data makanan
    private fun getmakanan() {
        rvMakanan.layoutManager = LinearLayoutManager(context!!.applicationContext)
        rvMakanan.setHasFixedSize(true)
        (rvMakanan.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.HORIZONTAL
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        val firestore = FirebaseFirestore.getInstance()
        val setiings = firestore.firestoreSettings.isPersistenceEnabled
        val docref = firestore.collection("Warung_Resep").whereEqualTo("uid", userId.toString())
            .whereEqualTo("kategori", "Makanan").get().addOnSuccessListener { doc ->
                if (doc.isEmpty){

                }else{
                    for (document in doc) {
                        try {
                            val data = document.toObject(MakananModels::class.java)
                            val mylist = MakananModels()
                            mylist.setgambar(data.gambar_makanan.toString())
                            mylist.setname(data.nama.toString())
                            mylist.setprice(data.harga)
                            mylist.setidmakanan(data.id_makanan.toString())
                            arrayList.add(mylist)
                            adapter = MakananAdapter(arrayList, context!!.applicationContext)

                            rvMakanan.adapter = adapter
                            adapter.notifyDataSetChanged()

                        }catch (e: Exception){

                        }

                    }
                }
            }.addOnFailureListener {

            }
    }

    //ambil data minuman
    private fun getminuman() {
        rvMinuman.layoutManager = LinearLayoutManager(context!!.applicationContext)
        rvMinuman.setHasFixedSize(true)
        (rvMinuman.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.HORIZONTAL


        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        val firestore = FirebaseFirestore.getInstance()
        val docref = firestore.collection("Warung_Resep").whereEqualTo("uid", userId.toString())
            .whereEqualTo("kategori", "Minuman").get().addOnSuccessListener { doc ->
                for (document in doc) {
                    val data = document.toObject(MakananModels::class.java)
                    val mylist = MakananModels()
                    mylist.setgambar(data.gambar_makanan.toString())
                    mylist.setname(data.nama.toString())
                    mylist.setprice(data.harga)
                    mylist.setidmakanan(data.id_makanan.toString())
                try {
                    arrayListMinuman.add(mylist)
                    adapterminuman = MakananAdapter(arrayListMinuman, context!!.applicationContext)

                    rvMinuman.adapter = adapterminuman
                    adapterminuman.notifyDataSetChanged()

                }catch (e:Exception){

                }

                }
            }.addOnFailureListener {

            }
    }

    private fun inisialisasifirebase() {
        firestore = FirebaseFirestore.getInstance()

    }

    private fun getswitch() {
        inisialisasifirebase()
        val docref = firestore.collection(Constant.warung_akun).document(userID.toString()).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val data = document.toObject(UsersModel::class.java)
                    if (data!!.status == true) {
                        switch.isChecked = true
                        switch.text = "Buka"
                    } else if (data.status == false) {
                        switch.isChecked = false
                        switch.text = "Tutup"
                    }
                }
            }
    }

    private fun switch() {
        inisialisasifirebase()
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //ubah ke posisi buka
                firestore.collection(Constant.warung_akun).document(userID.toString())
                    .update("status", true).addOnCompleteListener {
                        if (it.isSuccessful) {
                        }
                    }.addOnFailureListener {
                        toast(it.message.toString())
                    }

            } else {
                //ubah ke posisi tutup
                firestore.collection(Constant.warung_akun).document(userID.toString())
                    .update("status", false).addOnCompleteListener {
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