package com.hx.material.ui.curtain;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.LongDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import ui.material.hx.com.hxmaterial.R;

public class CurtainView extends View {
    private Bitmap mbitmap;
    //将图片划分成200个小格
    private static int WIDTH = 30;
    private static int HEIGHT = 30;
    private float waveHeight = 0;
    private float startAngle = 0;
    //最大水平的波形高度
    private float H_MAX_WAVE_HEIGHT = 50;

    //最大垂直的波形高度
    private float V_MAX_WAVE_HEIGHT = 500;

    //小格相交的总的点数
    private int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    private float[] verts = new float[COUNT * 2];
    private float[] origs = new float[COUNT * 2];
    private int[] colors = new int[COUNT * 2];
    private int maxAlpha = 0xFF;

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


    public void setWaveHeight(float progress) {
        this.progress = progress;
        Log.e("TAG" , "progress: " + progress);
        invalidate();
    }

    public void setTexture(Bitmap bitmap){
        this.mbitmap = bitmap;
        bitmapwidth = mbitmap.getWidth();
        bitmapheight = mbitmap.getHeight();
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
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int index = 0;
        for (int i = 0; i < HEIGHT + 1; i++) {
            for (int j = 0; j < WIDTH + 1; j++) {

                float yOffset = H_MAX_WAVE_HEIGHT / 2 * progress + H_MAX_WAVE_HEIGHT / 2 * progress * (float) Math.sin((float)j/WIDTH*5*Math.PI+k);

                //垂直方向竖直压缩时的坐标
                float vXPostion = origs[(i*(WIDTH+1)+j)*2+0] + (bitmapwidth - origs[(i*(WIDTH+1)+j)*2+0]) * progress;
                //垂直方向正弦曲线优化后的坐标,1.1->个波峰波谷
                float vXSinPostion = V_MAX_WAVE_HEIGHT / 2 * progress * (float) Math.sin((float)i/WIDTH*1.1*Math.PI + k);
                //Log.e("TAG" , "vXSinPostion: " + vXSinPostion);
                //x坐标不变
                verts[(i*(WIDTH+1)+j)*2+0]= vXSinPostion *((bitmapwidth - vXPostion) / bitmapwidth) + vXPostion;
                //增加k值是为了让相位产生移动，从而可以飘动起来
                //y坐标改变，呈现正弦曲线
                //verts[(i*(WIDTH+1)+j)*2+1]=origs[(i*(WIDTH+1)+j)*2+1]+offset*50;

                verts[(i * (WIDTH + 1) + j) * 2 + 1] = origs[(i * (WIDTH + 1) + j) * 2 + 1] + yOffset;//

                int channel = 255 - (int)(yOffset * 3);
                channel = channel < 0 ? 0 : channel;
                channel = channel > 255 ? 255 : channel;
                colors[index] = 0xFF000000 | channel << 16 | channel << 8 | channel;
                index += 1;
            }
        }
        //k+=0.4f;

        canvas.drawBitmapMesh(mbitmap, WIDTH, HEIGHT, verts, 0, colors, 0, null);
        //invalidate();

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);


    }

    int bitmapwidth;
    int bitmapheight;

    public void init() {
        //mbitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg2);
        //bitmapwidth = mbitmap.getWidth();
        //bitmapheight = mbitmap.getHeight();

        COUNT = (WIDTH + 1) * (HEIGHT + 1);
        verts = new float[COUNT * 2];
        origs = new float[COUNT * 2];
    }
}
