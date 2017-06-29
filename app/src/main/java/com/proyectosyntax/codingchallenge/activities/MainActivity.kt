package com.proyectosyntax.codingchallenge.activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.app.SearchManager
import android.content.Context
import android.support.v7.widget.SearchView
import android.util.Log
import com.proyectosyntax.codingchallenge.fragments.CategoriesFragment
import com.proyectosyntax.codingchallenge.fragments.MoviesFragment
import com.proyectosyntax.codingchallenge.R
import com.proyectosyntax.codingchallenge.fragments.ShowsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, CategoriesFragment.OnCategoryItemSelectedListener {

    val EXTRA_POPULAR = "popular"
    val EXTRA_TOP_RATED = "top_rated"
    val EXTRA_UPCOMING = "upcoming"

    lateinit var moviesFragment: MoviesFragment
    lateinit var categoriesFragment: CategoriesFragment
    lateinit var showsFragment: ShowsFragment
    var selectedCategories = HashMap<Int, String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val categories: HashMap<Int, String> = intent.extras.get("categories") as HashMap<Int, String>

        Log.i("Categories", categories.toString())

        moviesFragment = MoviesFragment.newInstance(EXTRA_POPULAR)
        showsFragment = ShowsFragment.newInstance(this, EXTRA_POPULAR)
        categoriesFragment = CategoriesFragment.newInstance(categories)


        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)
        navigationView.menu.getItem(0).setChecked(true)

        toolbar.title = navigationView.menu.getItem(0).title
        val mSectionsPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter
        container!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        tabs.getTabAt(1)?.select()
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_popular_movies -> {
                tabs.getTabAt(1)?.select()
                moviesFragment.updateList(EXTRA_POPULAR)
            }
            R.id.nav_top_rated_movies -> {
                tabs.getTabAt(1)?.select()
                moviesFragment.updateList(EXTRA_TOP_RATED)
            }
            R.id.nav_upcoming_movies -> {
                tabs.getTabAt(1)?.select()
                moviesFragment.updateList(EXTRA_UPCOMING)
            }
            R.id.nav_popular_shows -> {
                tabs.getTabAt(2)?.select()
                showsFragment.updateList(EXTRA_POPULAR)
            }
            R.id.nav_top_rated_shows -> {
                tabs.getTabAt(2)?.select()
                showsFragment.updateList(EXTRA_TOP_RATED)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        toolbar.title = item.title
        return true
    }

    override fun onCategoryItemPicked(item: Pair<Int, String>) {
        Log.i("SelectedCategory", item.toString())
        if (selectedCategories[item.first] == null) {
            selectedCategories.put(item.first, item.second)
        } else {
            selectedCategories.remove(item.first)
        }
        if (selectedCategories.size > 0) {
            moviesFragment.search(selectedCategories.toList())
            showsFragment.search(selectedCategories.toList())
        }else{
            moviesFragment.updateList(EXTRA_POPULAR)
            showsFragment.updateList(EXTRA_POPULAR)
        }
    }

    inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> return categoriesFragment
                1 -> return moviesFragment
                else -> return showsFragment
            }
        }

        override fun getCount(): Int = 3
    }
}
