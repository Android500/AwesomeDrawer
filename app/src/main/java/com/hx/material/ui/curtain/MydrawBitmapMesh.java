package com.hx.material.ui.curtain;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import ui.material.hx.com.hxmaterial.R;

public class MydrawBitmapMesh extends View {
    private Bitmap mbitmap;
    //将图片划分成200个小格
    private static final int WIDTH=200;
    private static final int HEIGHT=200;


    //小格相交的总的点数
    private int COUNT=(WIDTH+1)*(HEIGHT+1);
    private float[] verts=new float[COUNT*2];
    private float[] origs=new float[COUNT*2];
    private float k;
    public MydrawBitmapMesh(Context context) {
        super(context);
        init();
    }
    public MydrawBitmapMesh(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int i=0;i<HEIGHT+1;i++){
            for(int j=0;j<WIDTH+1;j++){
                //x坐标不变
                verts[(i*(WIDTH+1)+j)*2+0]+=0;
                //增加k值是为了让相位产生移动，从而可以飘动起来
                float offset=(float)Math.sin((float)j/WIDTH*2*Math.PI+k);
                //y坐标改变，呈现正弦曲线
                verts[(i*(WIDTH+1)+j)*2+1]=origs[(i*(WIDTH+1)+j)*2+1]+offset*5;
            }
        }
        //k+=0.4f;

        canvas.drawBitmapMesh(mbitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
        canvas.drawBitmap(mbitmap, 100, 300, null);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    public void init(){
        int index=0;
        mbitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.timg);
        float bitmapwidth=mbitmap.getWidth();
        float bitmapheight=mbitmap.getHeight();
        for(int i=0;i<HEIGHT+1;i++){
            float fy=bitmapwidth/HEIGHT*i;
            for(int j=0;j<WIDTH+1;j++){
                float fx=bitmapheight/WIDTH*j;
                //偶数位记录x坐标  奇数位记录Y坐标
                origs[index*2+0]=verts[index*2+0]=fx;
                origs[index*2+1]=verts[index*2+1]=fy;
                index++;
            }
        }

    }
}