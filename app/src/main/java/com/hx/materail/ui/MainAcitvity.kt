package com.hx.materail.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hx.materail.ui.curtain.CurtainActivity
import com.hx.materail.ui.curtain.MutilCurtainsActivity
import com.hx.materail.ui.folding.FoldContentActivity
import com.hx.materail.ui.folding.FoldDrawerMenu2Activity
import com.hx.materail.ui.folding.FoldDrawerMenuActivity
import com.hx.materail.ui.tutorial.CurtainDemoActivity
import kotlinx.android.synthetic.main.acitvity_main.*

class MainAcitvity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitvity_main)
        setSupportActionBar(toolbar)
        fold_cotnent.setOnClickListener {
            startActivity(Intent(this, FoldContentActivity::class.java))
        }

        fold_menu.setOnClickListener {
            startActivity(Intent(this, FoldDrawerMenuActivity::class.java))
        }

        fold_menu2.setOnClickListener {
            startActivity(Intent(this, FoldDrawerMenu2Activity::class.java))
        }

        curtain_menu.setOnClickListener {
            startActivity(Intent(this, CurtainActivity::class.java))
        }

        curtain_view.setOnClickListener {
            startActivity(Intent(this, MutilCurtainsActivity::class.java))
        }

        curtain_tutorial.setOnClickListener {
            startActivity(Intent(this, CurtainDemoActivity::class.java))
        }
    }
}
