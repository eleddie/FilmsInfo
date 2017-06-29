package com.proyectosyntax.codingchallenge

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView

class CategoriesAdapter(var context: Context, private var items: List<Pair<Int, String>>) : RecyclerView.Adapter<CategoriesAdapter.MyHolder>() {
    private var inflater: LayoutInflater = LayoutInflater.from(context)
    val selectedItems: SparseBooleanArray = SparseBooleanArray()

    override fun onBindViewHolder(holder: MyHolder?, position: Int) {
        val current = items.toList()[position]
        holder!!.name.text = current.second
        holder.cBox.setChecked(selectedItems.get(position, false))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        val view: View = inflater.inflate(R.layout.categories_item, parent, false)
        val holder = MyHolder(view)
        return holder
    }


    override fun getItemCount(): Int {
        return items.size
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.category_name) as TextView
        var cBox: CheckBox = itemView.findViewById(R.id.selected) as CheckBox

    }

    fun getItem(position: Int): Pair<Int, String> {
        return items[position]
    }
}