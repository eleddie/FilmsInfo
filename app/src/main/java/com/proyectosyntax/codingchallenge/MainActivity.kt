package com.proyectosyntax.codingchallenge

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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

import android.app.SearchManager
import android.content.Context
import android.support.v7.widget.SearchView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val EXTRA_POPULAR = "popular"
    var EXTRA_SELECTED = EXTRA_POPULAR
    val EXTRA_TOP_RATED = "top_rated"
    val EXTRA_UPCOMING = "upcoming"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

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
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        toolbar.title = item.title
        when (item.itemId) {
            R.id.nav_popular -> {
                EXTRA_SELECTED = EXTRA_POPULAR
            }
            R.id.nav_top_rated -> {
                EXTRA_SELECTED = EXTRA_TOP_RATED
            }
            R.id.nav_upcoming -> {
                EXTRA_SELECTED = EXTRA_UPCOMING
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {


        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> return MoviesFragment.newInstance(EXTRA_SELECTED)
                else -> return ShowsFragment.newInstance(EXTRA_SELECTED)
            }
        }

        override fun getCount(): Int = 2
    }
}
