package com.alfanshter.iki_warung.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.iki_warung.MainActivity
import com.alfanshter.iki_warung.Model.MakananModels
import com.alfanshter.iki_warung.R
import com.alfanshter.iki_warung.Ui.DetailActivity
import com.alfanshter.iki_warung.Utils.RoundedCornersTransformation
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.list_item.view.*
import org.jetbrains.anko.*
import java.util.ArrayList

class MakananAdapter(list: ArrayList<MakananModels>, context: Context) :
    RecyclerView.Adapter<MakananAdapter.ViewHolder>() {
    var list: ArrayList<MakananModels>
    var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val radius = 30
        val margin = 10
        val transformation: Transformation =
            RoundedCornersTransformation(radius, margin)

        val mylist: MakananModels = list[position]


        Picasso.get().load(mylist.getgambar()).transform(transformation).centerCrop().fit()
            .into(holder.makanan)
        holder.nama.text = mylist.getname()
        holder.harga.text = mylist.getprice()
        holder.itemView.setOnClickListener {
            context.startActivity(
                context.intentFor<DetailActivity>(
                    "gambar_makanan" to mylist.getgambar(),
                    "harga" to mylist.getprice(),
                    "nama" to mylist.getname(),
                    "id_makanan" to mylist.getidmakanan()
                ).newTask()
            )

        }

/*        holder.itemView.setOnClickListener {
            Toast.makeText(context, "${holder.id}", Toast.LENGTH_SHORT).show()
        }*/
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var makanan: ImageView
        var nama: TextView
        var harga: TextView

        init {
            makanan = itemView.findViewById(R.id.gambar_makanan)
            nama = itemView.findViewById(R.id.name)
            harga = itemView.findViewById(R.id.harga)
        }
    }

    init {
        this.list = list
        this.context = context
    }


}