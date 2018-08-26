package com.hx.material.ui.curtain;

import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;

import ui.material.hx.com.hxmaterial.R;

public class CurtainActivity extends  CurtainMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain);
        setMenu(R.layout.menu_left);



    }


}
