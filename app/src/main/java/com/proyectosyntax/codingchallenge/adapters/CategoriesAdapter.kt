package com.proyectosyntax.codingchallenge.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.proyectosyntax.codingchallenge.R

class CategoriesAdapter(var context: android.content.Context, private var items: List<Pair<Int, String>>) : RecyclerView.Adapter<CategoriesAdapter.MyHolder>() {
    private var inflater: LayoutInflater = LayoutInflater.from(context)
    val selectedItems: android.util.SparseBooleanArray = android.util.SparseBooleanArray()

    override fun onBindViewHolder(holder: CategoriesAdapter.MyHolder?, position: Int) {
        val current = items.toList()[position]
        holder!!.name.text = current.second
        holder.cBox.isChecked = selectedItems.get(position, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoriesAdapter.MyHolder {
        val view: View = inflater.inflate(R.layout.categories_item, parent, false)
        val holder = MyHolder(view)
        return holder
    }

    override fun getItemCount(): Int = items.size

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.category_name) as TextView
        var cBox: CheckBox = itemView.findViewById(R.id.selected) as CheckBox

    }

    fun getItem(position: Int): Pair<Int, String> = items[position]

}