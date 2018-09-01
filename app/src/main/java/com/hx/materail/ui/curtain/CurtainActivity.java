package com.hx.materail.ui.curtain;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hx.curtain.drawer.CurtainContentLayout;
import com.hx.materail.ui.R;


public class CurtainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain);
        final ImageView imageView = findViewById(R.id.imageView);
        final View menuLayout = findViewById(R.id.menu_layout);

        final CurtainContentLayout curtain_layout = findViewById(R.id.curtain_layout);
        curtain_layout.setCurtainLayoutListener(new CurtainContentLayout.OnCurtainLayoutListener() {
            @Override
            public void onSlide(View caurtainLayout, float slideOffset) {
                imageView.setRotation(360 * slideOffset);
                Log.e("CurtainActivity", "slideOffset: " + slideOffset );
                menuLayout.setAlpha(slideOffset);
                menuLayout.setScaleX(slideOffset);
                menuLayout.setScaleY(slideOffset);
                menuLayout.setTranslationX(- menuLayout.getLeft() * slideOffset);
            }
        });

        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curtain_layout.toggle();
            }
        });
    }
}
