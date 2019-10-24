package com.example.pagingwithcontentprovider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val listDataProvider: PagedListDataProvider): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CustomViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val text = listDataProvider.getItemAt(position).toString()
        (holder as CustomViewHolder).onBind(text, position)
    }

    override fun getItemCount(): Int {
        return listDataProvider.getItemCount()
    }


    class CustomViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val textView: TextView by lazy { view.findViewById<TextView>(R.id.textView) }
        fun onBind(text: String, position: Int) {
            textView.text = "Position: $position \t \t Item: $text"
        }

        companion object {
            fun create(parent: ViewGroup): CustomViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_item, parent, false)
                return CustomViewHolder(view)
            }
        }
    }
}