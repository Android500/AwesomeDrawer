package com.hx.material.ui.folding

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.hx.materail.ui.R
import kotlinx.android.synthetic.main.acitvity_fold_content.*

class FoldContentActivity : AppCompatActivity() {


    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitvity_fold_content)
        setSupportActionBar(toolbar)

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

}
