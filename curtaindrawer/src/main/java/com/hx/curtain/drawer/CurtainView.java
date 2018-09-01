package com.hx.curtain.drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

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
    /**竖直波浪个数*/
    private float hWaveCount;
    /**水平波浪个数*/
    private float vWaveCount;

    private int bitmapwidth;
    private int bitmapheight;

    public void setHWaveCount(float hWaveCount) {
        this.hWaveCount = hWaveCount;
    }

    public void setvVWaveCount(float vWaveCount) {
        this.vWaveCount = vWaveCount;
    }

    public CurtainView(Context context) {
        super(context);
        init();
    }

    public CurtainView(Context context, float hWaveCount, float vWaveCount) {
        super(context);
        this.hWaveCount = hWaveCount;
        this.vWaveCount = vWaveCount;
    }

    public CurtainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurtainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setWaveHeight(float progress) {
        this.progress = progress;
        invalidate();
    }

    public void init() {
        COUNT = (WIDTH + 1) * (HEIGHT + 1);
        verts = new float[COUNT * 2];
        origs = new float[COUNT * 2];
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

                int xIndex = (i*(WIDTH+1)+j)*2;
                int yIndex = (i * (WIDTH + 1) + j) * 2 + 1;

                float waveHeight =  H_MAX_WAVE_HEIGHT * progress;
                float yOffset = waveHeight / 2 + waveHeight / 2 * (float) Math.sin((float)j/WIDTH*hWaveCount*Math.PI+k);
                //垂直方向竖直压缩时的坐标
                float vXPostion = origs[xIndex] + (bitmapwidth - origs[xIndex]) * progress;
                //垂直方向正弦曲线优化后的坐标,1.1->个波峰波谷
                float vXSinPostion = V_MAX_WAVE_HEIGHT / 2 * progress * (float) Math.sin((float)i/WIDTH*vWaveCount*Math.PI + k);
                //x坐标不变
                verts[xIndex]= vXSinPostion *((bitmapwidth - vXPostion) / bitmapwidth) + vXPostion;


                //经过上述扭曲之后整个图片变高了H_MAX_WAVE_HEIGHT像素
                //现在要做的就是把图片中间水平线分割的上下像素位置向中间偏移使得高度不变
                //越靠近中间水平线的像素偏移越小,waveHeight / 2递减为0
                //计算跟水平线的像素距离
                float centerY = (waveHeight + bitmapheight) / 2;
                float scaleyOffset = (centerY - origs[yIndex]) / centerY * yOffset;

                Log.e("scaleyOffset", "centerY: " + centerY
                        + "\nscaleyOffset: " + scaleyOffset
                        + "\ncenterY - origs[yIndex]): " + (centerY - origs[yIndex])
                        + "\norigs[yIndex]: " + origs[yIndex]
                        + "\nbitmapheight: " + bitmapheight);


                //y坐标改变，呈现正弦曲线
                verts[yIndex] = origs[yIndex] + scaleyOffset;//

                int channel = 255 - (int)(yOffset * 3);
                channel = channel < 0 ? 0 : channel;
                channel = channel > 255 ? 255 : channel;
                colors[index] = 0xFF000000 | channel << 16 | channel << 8 | channel;
                index += 1;
            }
        }
        canvas.drawBitmapMesh(mbitmap, WIDTH, HEIGHT, verts, 0, colors, 0, null);
    }
}
