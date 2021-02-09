package com.alfanshter.IKI_Warung.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.IKI_Warung.R
import com.alfanshter.IKI_Warung.Utils.MakananResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class MakananAdapter(private var makanan : MutableList<MakananResponse>, private var context: Context) : RecyclerView.Adapter<MakananAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(makananResponse: MakananResponse, context: Context){
            itemView.name.text = makananResponse.nama
            itemView.harga.text = "Rp. " + makananResponse.harga.toString()
            Picasso.get().load(makananResponse.foto).into(itemView.gambar_makanan)
        }
    }

    fun setMakanan(r:List<MakananResponse>){
        makanan.clear()
        makanan.addAll(r)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(makanan[position], context)
    }

    override fun getItemCount(): Int {
        return makanan.size
    }
}