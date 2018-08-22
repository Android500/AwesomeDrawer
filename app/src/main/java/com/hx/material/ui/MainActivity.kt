package com.hx.material.ui

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_drawer.*
import ui.material.hx.com.hxmaterial.R

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(toolbar)



        nav_view.setNavigationItemSelectedListener(this)

        val metrics = DisplayMetrics()
        val wdm = getSystemService(Activity.WINDOW_SERVICE) as WindowManager
        wdm.defaultDisplay.getMetrics(metrics)
        //screenW = metrics.widthPixels
        //screenH = metrics.heightPixels

        fold_view.anchorFactor = 1f
        drawer_layout.setScrimColor(Color.TRANSPARENT);
        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                fold_view.foldFactor = drawerView.measuredWidth.toFloat() / metrics.widthPixels.toFloat() * slideOffset
            }

            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })
    }

    override fun onBackPressed() {
        val drawer = drawer_layout as DrawerLayout
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
