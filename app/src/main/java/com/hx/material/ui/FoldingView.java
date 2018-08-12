package com.hx.material.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import ui.material.hx.com.hxmaterial.R;

public class FoldingView extends ViewGroup {

    private int folderCount = 4;
    //偏移的百分比
    private int slideOffset;

    private static final int NUM_OF_POINT = 8;
    /**
     * 图片的折叠后的总宽度
     */
    private int mTranslateDis;

    /**
     * 折叠后的总宽度与原图宽度的比例
     */
    private float mFactor = 0.5f;
    /**
     * 折叠块的个数
     */
    private int mNumOfFolds = 2;

    private Matrix[] mMatrices = new Matrix[mNumOfFolds];


    /**
     * 绘制黑色透明区域
     */
    private Paint mSolidPaint;

    /**
     * 绘制阴影
     */
    private Paint mShadowPaint;
    private Matrix mShadowGradientMatrix;
    private LinearGradient mShadowGradientShader;

    /***
     * 原图每块的宽度
     */
    private int mFlodWidth;
    /**
     * 折叠时，每块的宽度
     */
    private int mTranslateDisPerFlod;

    private int originalWidth;
    private int originalHeight;

    public FoldingView(Context context) {
        super(context);
    }

    public FoldingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FoldingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View child = getChildAt(0);
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child = getChildAt(0);
        child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        originalWidth = getMeasuredWidth();
        originalHeight = getMeasuredHeight();
        init();
    }

    public void setFactor(int mFactor){
        if(this.mFactor == mFactor)
            return;
        this.mFactor = mFactor;
        init();
        invalidate();
    }

    private void init(){

        //折叠后的总宽度
        mTranslateDis = (int) (originalWidth * mFactor);
        //原图每块的宽度
        mFlodWidth = originalWidth / mNumOfFolds;
        //折叠时，每块的宽度
        mTranslateDisPerFlod = mTranslateDis / mNumOfFolds;

        //初始化matrix
        for (int i = 0; i < mNumOfFolds; i++) {
            mMatrices[i] = new Matrix();
        }

        mSolidPaint = new Paint();
        int alpha = (int) (255 * mFactor * 0.8f);
        mSolidPaint
                .setColor(Color.argb((int) (alpha * 0.8F), 0, 0, 0));

        mShadowPaint = new Paint();
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowGradientShader = new LinearGradient(0, 0, 0.5f, 0,
                Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
        mShadowPaint.setShader(mShadowGradientShader);
        mShadowGradientMatrix = new Matrix();
        mShadowGradientMatrix.setScale(mFlodWidth, 1);
        mShadowGradientShader.setLocalMatrix(mShadowGradientMatrix);
        mShadowPaint.setAlpha(alpha);

        //纵轴减小的那个高度，用勾股定理计算下
        int depth = (int) Math.sqrt(mFlodWidth * mFlodWidth
                - mTranslateDisPerFlod * mTranslateDisPerFlod) / 2;

        //转换点
        float[] src = new float[NUM_OF_POINT];
        float[] dst = new float[NUM_OF_POINT];

        /**
         * 原图的每一块，对应折叠后的每一块，方向为左上、右上、右下、左下，大家在纸上自己画下
         */
        for (int i = 0; i < mNumOfFolds; i++) {
            src[0] = i * mFlodWidth;
            src[1] = 0;

            src[2] = src[0];
            src[3] = originalHeight;

            src[4] = src[0] + mFlodWidth;
            src[5] = src[3];

            src[6] = src[4];
            src[7] = src[1];

            boolean isEven = i % 2 == 0;

            dst[0] = i * mTranslateDisPerFlod;
            dst[1] = isEven ? 0 : depth;

            dst[2] = dst[0];
            dst[3] = isEven ? originalHeight : originalHeight - depth;

            dst[4] = dst[0] + mTranslateDisPerFlod;
            dst[5] = isEven ? originalWidth - depth : originalHeight;

            dst[6] = dst[4];
            dst[7] = isEven ? depth : 0;

            //setPolyToPoly
            mMatrices[i].setPolyToPoly(src, 0, dst, 0, src.length >> 1);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        for (int i = 0; i < mNumOfFolds; i++) {

            canvas.save();
            //将matrix应用到canvas
            canvas.concat(mMatrices[i]);
            //控制显示的大小
            canvas.clipRect(mFlodWidth * i, 0, mFlodWidth * i + mFlodWidth, originalHeight);
            //移动绘制阴影
            canvas.translate(mFlodWidth * i, 0);

            super.dispatchDraw(canvas);

//            if (i % 2 == 0) {
//                //绘制黑色遮盖
//                canvas.drawRect(0, 0, mFlodWidth, originalHeight, mSolidPaint);
//            } else {
//                //绘制阴影
//                canvas.drawRect(0, 0, mFlodWidth, originalHeight, mShadowPaint);
//            }
            canvas.restore();
        }

        //绘制mNumOfFolds次
    }
}
