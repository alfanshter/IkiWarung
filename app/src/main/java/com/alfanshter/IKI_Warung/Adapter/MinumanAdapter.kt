package com.alfanshter.IKI_Warung.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.IKI_Warung.R
import com.alfanshter.IKI_Warung.Utils.MakananResponse
import com.alfanshter.IKI_Warung.Utils.MinumanResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class MinumanAdapter(private var minuman : MutableList<MinumanResponse>, private var context: Context) : RecyclerView.Adapter<MinumanAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(minumanResponse: MinumanResponse, context: Context){
            itemView.name.text = minumanResponse.nama
            itemView.harga.text = "Rp. " + minumanResponse.harga.toString()
            Picasso.get().load(minumanResponse.foto).into(itemView.gambar_makanan)
        }
    }

    fun setMinuman(r:List<MinumanResponse>){
        minuman.clear()
        minuman.addAll(r)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(minuman[position], context)
    }

    override fun getItemCount(): Int {
        return minuman.size
    }
}