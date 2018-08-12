package com.hx.material.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActionBarDrawerToggle
import android.support.v4.view.GravityCompat
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import com.ptr.folding.FoldingDrawerLayout
import kotlinx.android.synthetic.main.activity_drawer.*
import ui.material.hx.com.hxmaterial.R

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
    {


        protected override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_drawer)
            setSupportActionBar(toolbar)



            nav_view.setNavigationItemSelectedListener(this)

            fold_view.anchorFactor = 1f
            drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                    fold_view.foldFactor = slideOffset;
                }
                override fun onDrawerOpened(drawerView: View) {}
                override fun onDrawerClosed(drawerView: View) {
                }

                override fun onDrawerStateChanged(newState: Int) {}
            })
        }

        override fun onBackPressed() {
            val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }


        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            // Handle navigation view item clicks here.
            val id = item.itemId

            if (id == R.id.nav_camera) {
                // Handle the camera action
            } else if (id == R.id.nav_gallery) {

            } else if (id == R.id.nav_slideshow) {

            } else if (id == R.id.nav_manage) {

            } else if (id == R.id.nav_share) {

            } else if (id == R.id.nav_send) {

            }

            drawer_layout.closeDrawer(GravityCompat.START)
            return true
        }
    }
