package com.hx.material.ui.curtain;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by caifangmao on 15/3/26.
 */
public class CurtainMenuActivity extends Activity {

    private View menu;
    private CurtainContentLayout contentLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentLayout = new CurtainContentLayout(this);

        super.setContentView(contentLayout, layoutParams);


    }

    @Override
    public void setContentView(int layoutResID){
        if(contentLayout != null){
            View content = LayoutInflater.from(this).inflate(layoutResID, null, false);

            setContentView(content);
        }
    }

    @Override
    public void setContentView(View view){
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        setContentView(view, layoutParams);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams){
        contentLayout.addContent(view);
    }

    public void setMenu(int layoutRes){
        if(menu != null){
            contentLayout.removeView(menu);
        }
        menu = LayoutInflater.from(this).inflate(layoutRes, null ,false);

        contentLayout.addMenu(menu);
    }

    protected CurtainContentLayout getCurtainLayout(){
        return this.contentLayout;
    }
}
