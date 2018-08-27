package com.hx.material.ui.tutorial;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import ui.material.hx.com.hxmaterial.R;

public class CurtainView extends View {
    private Bitmap mbitmap;
    private static int WIDTH = 30;
    private static int HEIGHT = 30;
    //最大水平的波形高度
    private float WAVE_HEIGHT = 50;

    //小格相交的总的点数
    private int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    private float[] verts = new float[COUNT * 2];
    private float[] origs = new float[COUNT * 2];

    private float k;

    private float progress;


    public CurtainView(Context context) {
        super(context);
        init();
    }

    public CurtainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurtainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < HEIGHT + 1; i++) {
            for (int j = 0; j < WIDTH + 1; j++) {
                //把每一个水平像素通过正弦公式转换成正弦曲线
                //WAVE_HEIGHT表示波峰跟波低的垂直距离，皱褶后会王桑超过水平线，所以往下偏移WAVE_HEIGHT / 2
                //5表示波浪的密集度，表示波峰波谷总共有五个,对应上面左图的1,2,3,4,5
                //j就是水平像的X轴坐标
                //K决定正弦曲线起始点(x=0)点的Y坐标，k=0就是从波峰波谷的中间开始左->右绘制曲线
                float yOffset = WAVE_HEIGHT / 2  + WAVE_HEIGHT / 2 * (float) Math.sin((float) j / WIDTH * 5 * Math.PI + k);
                verts[(i * (WIDTH + 1) + j) * 2 + 1] = origs[(i * (WIDTH + 1) + j) * 2 + 1] + yOffset;//
            }
        }

        canvas.drawBitmapMesh(mbitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
    }

    int bitmapwidth;
    int bitmapheight;

    public void init() {
        mbitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg);
        bitmapwidth = mbitmap.getWidth();
        bitmapheight = mbitmap.getHeight();

        COUNT = (WIDTH + 1) * (HEIGHT + 1);
        verts = new float[COUNT * 2];
        origs = new float[COUNT * 2];

        int index = 0;
        for (int i = 0; i < HEIGHT + 1; i++) {
            float fy = bitmapheight / (float) HEIGHT * i;
            for (int j = 0; j < WIDTH + 1; j++) {
                float fx = bitmapwidth / (float) WIDTH * j;
                //偶数位记录x坐标  奇数位记录Y坐标
                origs[index * 2 + 0] = verts[index * 2 + 0] = fx;
                origs[index * 2 + 1] = verts[index * 2 + 1] = fy;
                index++;
            }
        }
    }
}
