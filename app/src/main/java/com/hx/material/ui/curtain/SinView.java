package com.hx.material.ui.curtain;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class SinView extends View {

    private int height;
    private int width;

    private Path path;
    private Paint paint;

    private float waveHeight;

    private int lineColor;//线的颜色
    private int backColor;//背景色
    private float amplitude;//振幅
    private float frequency;//频率

    private float startAngle = 90;

    private int i = 0;

    public SinView(Context context) {
        this(context, null);
    }

    public SinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public SinView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void initView(Context context, AttributeSet attrs) {

        amplitude = 100;
        frequency = 360;
        lineColor = Color.GREEN;
        backColor = Color.YELLOW;

        paint = new Paint();
        paint.setColor(lineColor);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        path = new Path();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        if (height == 0) {
            height = getMeasuredHeight();
            waveHeight = height;
            Log.e("KFJKFJ", "高度" + waveHeight + "getMeasuredHeight()" + getMeasuredHeight() + "amplitude" + amplitude);
        }
        if (width == 0) {
            width = getMeasuredWidth();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.drawColor(backColor);
        for (i = 0; i < height; i += 5) {
            float y = (float) (waveHeight/2 + waveHeight/2 * Math.sin(i * 5 + startAngle)) + 10;//
            if (i == 0) {
                //设置path的起点
                path.moveTo(y, 0);
            } else {
                //连线
                path.lineTo(y, i);
            }

        }

//        postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                i = 0;
//                path.reset();
//                startAngle+=(Math.PI/4);
//                postInvalidate();
//            }
//        }, 200);

        canvas.drawPath(path, paint);
    }
}
