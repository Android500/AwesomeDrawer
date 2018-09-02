package com.hx.materail.ui.tutorial;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hx.materail.ui.R;

import java.util.ArrayList;
import java.util.List;


public class CurtainView extends View {
    private Bitmap mbitmap;
    private static int WIDTH = 30;
    private static int HEIGHT = 30;
    //最大垂直的波形高度
    private static float V_MAX_WAVE_HEIGHT = 700;
    //最大垂直的波形高度
    private static float H_MAX_WAVE_HEIGHT = 50;

    //小格相交的总的点数
    private int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    private float[] verts = new float[COUNT * 2];
    private float[] origs = new float[COUNT * 2];
    private int[] colors = new int[COUNT * 2];
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
    PathMeasure pathMeasure;
    public void setProgress(float progress){
        this.progress = progress;
        invalidate();
    }
    float[] pos = new float[2];
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int index = 0;
        for (int i = 0; i < HEIGHT + 1; i++) {
            for (int j = 0; j < WIDTH + 1; j++) {

                //把每一个水平像素通过正弦公式转换成正弦曲线
                //H_MAX_WAVE_HEIGHT表示波峰跟波低的垂直距离，皱褶后会王桑超过水平线，所以往下偏移WAVE_HEIGHT / 2
                //5表示波浪的密集度，表示波峰波谷总共有五个,对应上面左图的1,2,3,4,5
                //j就是水平像的X轴坐标
                //K决定正弦曲线起始点(x=0)点的Y坐标，k=0就是从波峰波谷的中间开始左->右绘制曲线
                float yOffset = H_MAX_WAVE_HEIGHT / 2 * progress + H_MAX_WAVE_HEIGHT / 2 * progress * (float) Math.sin((float)j/WIDTH*5*Math.PI+k);

                //垂直方向竖直压缩时的坐标
                float xPostion = origs[(i*(WIDTH+1)+j)*2+0] + (bitmapwidth - origs[(i*(WIDTH+1)+j)*2+0]) * progress;
                //垂直方向正弦曲线优化后的坐标,1.1->个波峰波谷
                float vXSinPostion = V_MAX_WAVE_HEIGHT / 2 * progress * (float) Math.sin((float)i/WIDTH*1.1*Math.PI + k);
                //每个像素扭曲后的x坐标
                //origs[(i*(WIDTH+1)+j)*2+0] 原图x坐标
                verts[(i*(WIDTH+1)+j)*2+0]= vXSinPostion *((bitmapwidth - xPostion) / bitmapwidth) + xPostion;
                //每个像素扭曲后的Y坐标
                //origs[(i*(WIDTH+1)+j)*2+1] 原图y坐标
                verts[(i * (WIDTH + 1) + j) * 2 + 1] = origs[(i * (WIDTH + 1) + j) * 2 + 1] + yOffset;//

                int channel = 255 - (int)(yOffset * 3);
                channel = channel < 0 ? 0 : channel;
                channel = channel > 255 ? 255 : channel;
                colors[index] = 0xFF000000 | channel << 16 | channel << 8 | channel;
                index += 1;
            }
        }
        canvas.drawBitmapMesh(mbitmap, WIDTH, HEIGHT, verts, 0, colors, 0, null);
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
