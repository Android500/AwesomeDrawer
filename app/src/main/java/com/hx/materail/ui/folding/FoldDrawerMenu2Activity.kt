package com.hx.materail.ui.folding

import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import com.hx.materail.ui.R
import kotlinx.android.synthetic.main.activity_fold_menu2.*

/**
 * Created by Administrator on 2018/8/23.
 */
class FoldDrawerMenu2Activity : AppCompatActivity() {


    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fold_menu2)
        setSupportActionBar(toolbar)
        slide_layout.sliderFadeColor = Color.TRANSPARENT
    }
}