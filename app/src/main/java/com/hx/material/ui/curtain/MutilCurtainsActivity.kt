package com.hx.material.ui.curtain

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_mutil_curtains.*
import ui.material.hx.com.hxmaterial.R

/**
 * Created by Administrator on 2018/8/27.
 */
class MutilCurtainsActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mutil_curtains)
        setSupportActionBar(toolbar)
    }
}