package com.argump.visitstore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.argump.visitstore.databinding.ListKunjunganBinding
import com.argump.visitstore.model.DataStore

class ListStoreAdapter(private val listStores: ArrayList<DataStore>, private val listener: OnAdapterListener): RecyclerView.Adapter<ListStoreAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListKunjunganBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListKunjunganBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store = listStores[position]
        holder.binding.tvNamaToko.text = store.store_name

        holder.binding.checkStoreDone.visibility = if (store.isVisit) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            val itemIndex = holder.adapterPosition
            listener.onClick(store, itemIndex)
        }
    }

    override fun getItemCount() = listStores.size

    interface OnAdapterListener {
        fun onClick(store: DataStore, position: Int)
    }
}