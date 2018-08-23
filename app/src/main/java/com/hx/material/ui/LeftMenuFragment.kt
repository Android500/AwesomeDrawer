package com.hx.material.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ui.material.hx.com.hxmaterial.R

/**
 * Created by Administrator on 2018/8/23.
 */
class LeftMenuFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_left_menu, container, false);
    }
}