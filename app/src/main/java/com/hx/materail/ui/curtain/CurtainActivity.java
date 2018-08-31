package com.hx.materail.ui.curtain;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;

import com.hx.curtain.drawer.CurtainContentLayout;
import com.hx.materail.ui.R;


public class CurtainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain);

        CurtainContentLayout curtain_layout = findViewById(R.id.curtain_layout);
        curtain_layout.setCurtainLayoutListener(new CurtainContentLayout.OnCurtainLayoutListener() {
            @Override
            public void onSlide(View caurtainLayout, float slideOffset) {
                Log.e("CurtainActivity", "slideOffset: " + slideOffset);
            }
        });
    }
}
