package com.proyectosyntax.codingchallenge.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.proyectosyntax.codingchallenge.adapters.CategoriesAdapter
import com.proyectosyntax.codingchallenge.R
import com.proyectosyntax.codingchallenge.utils.RecyclerViewListener
import com.proyectosyntax.codingchallenge.utils.SpacesItemDecoration


class CategoriesFragment : Fragment() {

    var mListAdapter: CategoriesAdapter? = null
    var categoriesList: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val categories = (arguments.get("categories") as HashMap<Int, String>).toList()
        mListAdapter = CategoriesAdapter(activity, categories.sortedWith(compareBy({ it.second }, { it.first })))

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.categories_list, container, false)
        categoriesList = rootView.findViewById(R.id.categoriesList) as RecyclerView

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.item_spacing)
        categoriesList!!.addItemDecoration(SpacesItemDecoration(spacingInPixels))

        categoriesList!!.addOnItemTouchListener(RecyclerViewListener(context, categoriesList!!, object : RecyclerViewListener.ClickListener {
            override fun onClick(view: View, position: Int) {
                if (mListAdapter!!.selectedItems.get(position, false)) {
                    mListAdapter!!.selectedItems.delete(position)
                } else {
                    mListAdapter!!.selectedItems.put(position, true)
                }
                mListAdapter!!.notifyItemChanged(position)
                (activity as OnCategoryItemSelectedListener).onCategoryItemPicked(mListAdapter!!.getItem(position))
            }
        }))
        categoriesList!!.layoutManager = GridLayoutManager(activity, 2)
        categoriesList!!.adapter = mListAdapter
        return rootView
    }


    companion object {
        fun newInstance(cat: HashMap<Int, String>): CategoriesFragment {
            val fragment = CategoriesFragment()
            val args = Bundle()
            args.putSerializable("categories", cat)
            fragment.arguments = args
            return fragment
        }
    }

    interface OnCategoryItemSelectedListener {
        fun onCategoryItemPicked(item: Pair<Int,String>)
    }

}
