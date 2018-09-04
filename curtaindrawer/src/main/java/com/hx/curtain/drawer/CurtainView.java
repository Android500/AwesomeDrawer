package com.hx.curtain.drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

public class CurtainView extends View {
    private Bitmap mbitmap;
    //将图片划分成40个小格
    private static int WIDTH = 30;
    private static int HEIGHT = 30;
    private float waveHeight = 0;
    //最大水平的波形高度
    private static float H_MAX_WAVE_HEIGHT = 50;
    //最大垂直的波形高度
    private static float V_MAX_WAVE_HEIGHT = 800;
    //小格相交的总的点数
    private int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    private float[] verts = new float[COUNT * 2];
    private float[] origs = new float[COUNT * 2];
    private int[] colors = new int[COUNT * 2];

    private float k;
    private float progress;
    //竖直波浪个数
    private float hWaveCount;
    //水平波浪个数
    private float vWaveCount;
    private int bitmapwidth;
    private int bitmapheight;

    private List<Point> points;
    private float[] pos = new float[2];
    private PathMeasure pathMeasure;

    private float[] xOffsets = new float[WIDTH + 1];
    private float[] yOffsets = new float[WIDTH + 1];

    float[] waveTops = {0, 0.03F, 0.08F, 0.15F, 0.24F, 0.36F, 0.49F, 0.64F, 0.81F, 1.0F};

    public CurtainView(Context context) {
        super(context);
    }

    public CurtainView(Context context, float hWaveCount, float vWaveCount) {
        super(context);
        this.hWaveCount = hWaveCount;
        this.vWaveCount = vWaveCount;
        this.points = new ArrayList<>();
        this.pathMeasure = new PathMeasure();
    }

    public CurtainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CurtainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setWaveHeight(float progress) {
        this.progress = progress;
        for (int t = 0; t < waveTops.length; t++) {
            Point point = points.get(t);
            point.y = t % 2 != 0 ? 0 : (int) (H_MAX_WAVE_HEIGHT * progress);
        }
         BezierUtils.measurePath(pathMeasure, points);
        invalidate();
    }

    public void setTexture(Bitmap bitmap) {
        this.mbitmap = bitmap;
        bitmapwidth = mbitmap.getWidth();
        bitmapheight = mbitmap.getHeight();

        int index = 0;
        for (int i = 0; i < HEIGHT + 1; i++) {
            float fy = bitmapheight / (float) HEIGHT * i;
            for (int j = 0; j < WIDTH + 1; j++) {
                float fx = bitmapwidth / (float) WIDTH * j;
                //偶数位记录x坐标  奇数位记录Y坐标
                origs[index * 2] = verts[index * 2] = fx;
                origs[index * 2 + 1] = verts[index * 2 + 1] = fy;
                index++;
            }
        }

        points.clear();
        for (int i = 0; i < waveTops.length; i++) {
            Point point = new Point();
            point.x = (int) Math.floor((double) (bitmapwidth * waveTops[i]));
            point.y = i % 2 != 0 ? 0 : (int) (H_MAX_WAVE_HEIGHT * progress);
            points.add(point);
        }

        BezierUtils.measurePath(pathMeasure, points);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int j = 0; j < WIDTH + 1; j++) {
            pathMeasure.getPosTan(pathMeasure.getLength() * j / (float) WIDTH, pos, null);
            xOffsets[j] = pos[0];
            yOffsets[j] = pos[1];
        }

        int index = 0;
        for (int i = 0; i < HEIGHT + 1; i++) {
            for (int j = 0; j < WIDTH + 1; j++) {

                int xIndex = (i * (WIDTH + 1) + j) * 2;
                int yIndex = (i * (WIDTH + 1) + j) * 2 + 1;
                //中间水平线y坐标
                float centerY = (waveHeight + bitmapheight) / 2;

                float waveHeight = H_MAX_WAVE_HEIGHT * progress;
                float yOffset = waveHeight / 2 + waveHeight / 2 * (float) Math.sin((float) j / WIDTH * hWaveCount * Math.PI + k);

                //bezier optimize
                yOffset = yOffsets[j];

                //未优化时的x坐标
                float vXPostion = origs[xIndex] + (bitmapwidth - origs[xIndex]) * progress;

                //bezier optimize
                vXPostion = xOffsets[j] + (bitmapwidth - xOffsets[j]) * progress;


                //坐标优化-->垂直方向正弦曲线偏移优化后的坐标,vWaveCount个波峰波谷
                float vXSinPostion = V_MAX_WAVE_HEIGHT / 2 * progress * (float) Math.sin((float) i / WIDTH * vWaveCount * Math.PI + k);

                verts[xIndex] = vXSinPostion * ((bitmapwidth - vXPostion) / bitmapwidth) + vXPostion;

                //经过上述扭曲之后整个图片变高了waveHeight像素
                //现在要做的就是把图片中间水平线分割的上下像素位置向中间偏移使得高度不变
                //越靠近中间水平线的像素偏移越小,从waveHeight / 2递减为0
                //计算跟水平线的像素距离
                //scaleOffset > 0水平线上方,scaleOffset < 0 水平线下方
                float scaleOffset = (centerY - origs[yIndex]) / centerY * yOffset;

                //y坐标改变，呈现正弦曲线
                verts[yIndex] = origs[yIndex] + scaleOffset;//

                //阴影着色
                int channel = 255 - (int) (yOffset * 3);
                channel = channel < 0 ? 0 : channel;
                channel = channel > 255 ? 255 : channel;
                colors[index] = 0xFF000000 | channel << 16 | channel << 8 | channel;
                index += 1;
            }
        }
        canvas.drawBitmapMesh(mbitmap, WIDTH, HEIGHT, verts, 0, colors, 0, null);
    }
}
