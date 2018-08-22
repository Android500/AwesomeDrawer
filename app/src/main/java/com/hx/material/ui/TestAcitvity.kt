package com.hx.material.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.test.*
import ui.material.hx.com.hxmaterial.R

class TestAcitvity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test)
        fold_cotnent.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
