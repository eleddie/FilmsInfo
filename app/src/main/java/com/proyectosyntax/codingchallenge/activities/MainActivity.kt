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
import android.content.Intent
import com.proyectosyntax.codingchallenge.utils.CurrentState
import com.proyectosyntax.codingchallenge.utils.ObjectSerializer
import android.support.v4.view.MenuItemCompat




class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, CategoriesFragment.OnCategoryItemSelectedListener {

    lateinit var moviesFragment: MoviesFragment
    lateinit var categoriesFragment: CategoriesFragment
    lateinit var showsFragment: ShowsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)

        val categories: HashMap<Int, String>
        if (intent.extras != null)
            categories = intent.extras.get("categories") as HashMap<Int, String>
        else {
            val preferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
            val categoriesSaved = preferences.getString("categories", null)
            categories = ObjectSerializer.deserialize(categoriesSaved) as HashMap<Int, String>
        }
        Log.i("Categories", categories.toString())

        moviesFragment = MoviesFragment.newInstance()
        showsFragment = ShowsFragment.newInstance()
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

        val queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                Log.i("Search query change", newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.i("Search query submit", query)
                if (query.isNotBlank()) {
                    CurrentState.categories.clear()
                    CurrentState.Movie.page = 1
                    CurrentState.Show.page = 1
                    CurrentState.search = query
                    moviesFragment.updateSearch(query, CurrentState.Movie.page)
                    showsFragment.updateSearch(query, CurrentState.Show.page)
                    searchView.clearFocus()
                }
                return true
            }
        }

        searchView.setOnQueryTextListener(queryTextListener)

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search), object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                Log.i("menuItemActionExpand", "Called")
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                Log.i("menuItemActionCollapse", "Called")
                CurrentState.search = null
                CurrentState.Movie.page = 1
                CurrentState.Show.page = 1
                moviesFragment.updateType(CurrentState.TYPE_POPULAR, CurrentState.Movie.page)
                showsFragment.updateType(CurrentState.TYPE_POPULAR, CurrentState.Show.page)
                return true
            }
        })


        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        CurrentState.search = null
        when (item.itemId) {
            R.id.nav_popular_movies -> {
                tabs.getTabAt(1)?.select()
                CurrentState.Movie.page = 1
                moviesFragment.updateType(CurrentState.TYPE_POPULAR, CurrentState.Movie.page)
            }
            R.id.nav_top_rated_movies -> {
                tabs.getTabAt(1)?.select()
                CurrentState.Movie.page = 1
                moviesFragment.updateType(CurrentState.TYPE_TOP_RATED, CurrentState.Movie.page)
            }
            R.id.nav_upcoming_movies -> {
                tabs.getTabAt(1)?.select()
                CurrentState.Movie.page = 1
                moviesFragment.updateType(CurrentState.TYPE_UPCOMING, CurrentState.Movie.page)
            }
            R.id.nav_popular_shows -> {
                tabs.getTabAt(2)?.select()
                CurrentState.Show.page = 1
                showsFragment.updateType(CurrentState.TYPE_POPULAR, CurrentState.Show.page)
            }
            R.id.nav_top_rated_shows -> {
                tabs.getTabAt(2)?.select()
                CurrentState.Show.page = 1
                showsFragment.updateType(CurrentState.TYPE_TOP_RATED, CurrentState.Show.page)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCategoryItemPicked(item: Pair<Int, String>) {
        Log.i("SelectedCategory", item.toString())
        CurrentState.search = null
        if (CurrentState.categories[item.first] == null) {
            CurrentState.categories.put(item.first, item.second)
        } else {
            CurrentState.categories.remove(item.first)
        }

        if (CurrentState.categories.size > 0) {
            CurrentState.Movie.page = 1
            CurrentState.Show.page = 1
            moviesFragment.updateCategories(CurrentState.categories.toList(), CurrentState.Movie.page)
            showsFragment.updateCategories(CurrentState.categories.toList(), CurrentState.Show.page)
        } else {
            CurrentState.Movie.page = 1
            CurrentState.Show.page = 1
            moviesFragment.updateType(CurrentState.TYPE_POPULAR, CurrentState.Movie.page)
            showsFragment.updateType(CurrentState.TYPE_POPULAR, CurrentState.Show.page)
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
