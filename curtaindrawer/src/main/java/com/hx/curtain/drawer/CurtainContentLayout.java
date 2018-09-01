package com.hx.curtain.drawer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.OvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.hx.curtain.drawer.nineoldandroids.animation.Animator;
import com.hx.curtain.drawer.nineoldandroids.animation.ValueAnimator;

import static com.hx.curtain.drawer.R.styleable.CurtainMenu;


/**
 * Created by caifangmao on 15/3/26.
 */
public class CurtainContentLayout extends FrameLayout {

    public interface OnCurtainLayoutListener{
        void onSlide(View caurtainLayout, float slideOffset);
    }

    private static final int DEFAULT_INTERCEPT_LENGTH = 30;
    private int defaultMenuWidth;

    private View content;
    private View menu;
    private CurtainView curtainView;

    private Bitmap curtainTexture;

    private int initX;
    private int initY;

    private int currentX;
    private int currentY;

    private int vx;

    private Paint paint;

    private boolean inSlidingMode = false;

    private static final int SLIDING_MODE_CLOSED = 0;
    private static final int SLIDING_MODE_OPENED = 1;
    private static final int SLIDING_MODE_SLIDING = 2;

    private int slidingMode = 0;

    private int minVelocity;
    private int maxVY;
    private VelocityTracker velocityTracker;

    private ValueAnimator slidingAnimator;
    private float maxScrollRate;
    private OnCurtainLayoutListener curtainLayoutListener;
    private GestureDetector gestureDetector;

    public void setCurtainLayoutListener(OnCurtainLayoutListener curtainLayoutListener) {
        this.curtainLayoutListener = curtainLayoutListener;
    }

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent event) {

            if (slidingMode != SLIDING_MODE_OPENED) {
                slidingAnimator.cancel();

                inSlidingMode = true;

                initX = (int) event.getX();
                initY = (int) event.getY();

                if (curtainTexture != null && !curtainTexture.isRecycled()) {
                    curtainTexture.recycle();

                }

                curtainTexture = snapshot(CurtainContentLayout.this.content);
                curtainView.setTexture(curtainTexture);
                curtainView.setVisibility(View.VISIBLE);
                CurtainContentLayout.this.content.setVisibility(View.GONE);

                return true;
            } else {
                if (event.getX() < defaultMenuWidth) {
                    return false;
                } else {
                    slidingAnimator.cancel();

                    inSlidingMode = true;

                    initX = (int) event.getX();
                    initY = (int) event.getY();

                    vx = defaultMenuWidth;

                    if (curtainTexture != null && !curtainTexture.isRecycled()) {
                        curtainTexture.recycle();

                    }

                    curtainTexture = snapshot(CurtainContentLayout.this.content);
                    curtainView.setTexture(curtainTexture);
                    curtainView.setVisibility(View.VISIBLE);
                    CurtainContentLayout.this.content.setVisibility(View.GONE);

                    return true;
                }
            }

        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {

            if (slidingMode == SLIDING_MODE_OPENED && event.getX() >= defaultMenuWidth) {
                slidingAnimator.setIntValues(vx, 0);
                slidingAnimator.setDuration(500);
                slidingAnimator.start();

                return true;
            } else {
                return false;
            }


        }

        @Override
        public boolean onScroll(MotionEvent ev1, MotionEvent ev2, float distanceX, float distanceY) {

            if (CurtainContentLayout.this.content != null) {
                currentX = (int) ev2.getX();
                currentY = (int) ev2.getY();

                vx = slidingMode == SLIDING_MODE_CLOSED ? currentX - initX : defaultMenuWidth - (initX - currentX);

                if (vx < 0) {
                    vx = 0;
                }

                if (vx > CurtainContentLayout.this.menu.getWidth()) {
                    vx = CurtainContentLayout.this.menu.getWidth();
                }

                curtainView.setWaveHeight(vx / (float) curtainView.getMeasuredWidth());
                if(curtainLayoutListener != null)
                    curtainLayoutListener.onSlide(CurtainContentLayout.this, vx / (float) curtainView.getMeasuredWidth() / maxScrollRate);
            }

            return true;
        }

        @Override
        public boolean onFling(MotionEvent ev1, MotionEvent ev2, float velocityX, float velocityY) {

            if (Math.abs(velocityX) > minVelocity * 5) {
                if (velocityX > 0) {
                    slidingAnimator.setIntValues(vx, defaultMenuWidth);
                    slidingAnimator.setDuration((long) (Math.abs(defaultMenuWidth - vx) / (float) defaultMenuWidth * 500L));
                } else {
                    slidingAnimator.setDuration(500);//(long) (((float) vx / Math.abs(velocityX)) * 1000.0F)
                    slidingAnimator.setIntValues(vx, 0);
                }

                slidingAnimator.start();
            } else {
                if (ev2.getX() > defaultMenuWidth / 2) {
                    slidingAnimator.setIntValues(vx, defaultMenuWidth);
                    slidingAnimator.setDuration((long) (Math.abs(defaultMenuWidth - vx) / (float) defaultMenuWidth * 500L));
                } else {
                    slidingAnimator.setIntValues(vx, 0);
                    slidingAnimator.setDuration(500);
                }

                slidingAnimator.start();
            }

            return true;
        }
    };


    public CurtainContentLayout(Context context) throws Exception {
        this(context, null);
    }

    public CurtainContentLayout(Context context, AttributeSet attrs) throws Exception {
        this(context, attrs, 0);
    }

    public CurtainContentLayout(Context context, AttributeSet attrs, int defStyle) throws Exception {
        super(context, attrs, defStyle);


        TypedArray ta = context.obtainStyledAttributes(attrs, CurtainMenu);
        Float hWaveCount = ta.getFloat(R.styleable.CurtainMenu_h_waveCount, 5f);
        Float vWaveCount = ta.getFloat(R.styleable.CurtainMenu_v_waveCount, 1.1f);
        int menuLayoutId = ta.getResourceId(R.styleable.CurtainMenu_behind_menu, -1);
        int contentLayoutId = ta.getResourceId(R.styleable.CurtainMenu_content, -1);
        maxScrollRate = ta.getFloat(R.styleable.CurtainMenu_maxRate, 0.75f);
        ta.recycle();
        //相对于父view最大划开区域
        defaultMenuWidth = (int) (getResources().getDisplayMetrics().widthPixels * maxScrollRate);
        curtainView = new CurtainView(context, hWaveCount, vWaveCount);
        //curtainView.setDirection(CurtainView.DIRECTION_LEFT);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(curtainView, 0, layoutParams);
        curtainView.setVisibility(View.GONE);


        if (menuLayoutId != -1) {
            addMenu(LayoutInflater.from(context).inflate(menuLayoutId, null, false));
        }

        if (contentLayoutId != -1) {
            addContent(LayoutInflater.from(context).inflate(contentLayoutId, null, false));
        } else {
            throw new Exception("you must set curtain content layout!!!");
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        gestureDetector = new GestureDetector(context, gestureListener);

        minVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();

        slidingAnimator = ValueAnimator.ofInt(vx, 1);
        slidingAnimator.setDuration(500L);
        slidingAnimator.setInterpolator(new OvershootInterpolator());
        slidingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                setSlidingFactor(value);
            }
        });
        slidingAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (vx > defaultMenuWidth / 2) {
                    slidingMode = SLIDING_MODE_OPENED;
                } else {
                    slidingMode = SLIDING_MODE_CLOSED;
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }

        });
    }

    public void toggle() {
        switch (slidingMode) {
            case SLIDING_MODE_CLOSED:
                slidingAnimator.setIntValues(vx, defaultMenuWidth);
                slidingAnimator.setDuration(500L);
                slidingAnimator.setInterpolator(new OvershootInterpolator());
                slidingAnimator.start();
                break;
            case SLIDING_MODE_OPENED:
                slidingAnimator.setIntValues(vx, 0);
                slidingAnimator.setDuration(500L);
                slidingAnimator.setInterpolator(new OvershootInterpolator());
                slidingAnimator.start();
                break;
        }


    }


    void setSlidingFactor(int slidingFactor) {
        this.vx = slidingFactor;

        if (this.vx <= 0) {
            this.content.setVisibility(View.VISIBLE);
            this.curtainView.setVisibility(View.GONE);
            inSlidingMode = false;
        } else {
            this.content.setVisibility(View.GONE);
            this.curtainView.setVisibility(View.VISIBLE);
        }

        this.curtainView.setWaveHeight(this.vx / (float) curtainView.getMeasuredWidth());
        if(curtainLayoutListener != null)
            curtainLayoutListener.onSlide(CurtainContentLayout.this, vx / (float) curtainView.getMeasuredWidth() / maxScrollRate);
    }

    Integer getSlidingFactor() {
        return this.vx;
    }

    public void addContent(View content) {

        if (content != null) {
            this.content = content;
            this.content.setBackgroundColor(0xFFFFFFFF);

            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            addView(content, this.menu == null ? 0 : 1, layoutParams);
        }
    }

    public void addMenu(View menu) {
        if (menu != null) {
            this.menu = menu;

            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            addView(menu, 0, layoutParams);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        defaultMenuWidth = (int) (getMeasuredWidth() * maxScrollRate);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN && event.getX() <= DEFAULT_INTERCEPT_LENGTH) {
            return true;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                }

                velocityTracker.addMovement(event);

                break;
            case MotionEvent.ACTION_MOVE:


                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                currentX = (int) event.getX();
                currentY = (int) event.getY();

                final VelocityTracker velocityTracker = this.velocityTracker;
                final int pointerId = event.getPointerId(0);
                velocityTracker.computeCurrentVelocity(1000, ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity());
                final float velocityY = velocityTracker.getYVelocity(pointerId);
                final float velocityX = velocityTracker.getXVelocity(pointerId);

                if (Math.abs(velocityX) <= 0 && initX != currentX) {
                    if (event.getX() > defaultMenuWidth / 2) {
                        slidingAnimator.setIntValues(vx, defaultMenuWidth);
                        slidingAnimator.setDuration((long) (Math.abs(defaultMenuWidth - vx) / (float) defaultMenuWidth * 500L));
                        slidingAnimator.setInterpolator(new OvershootInterpolator());
                        slidingAnimator.start();
                    } else {
                        slidingAnimator.setIntValues(vx, 0);
                        slidingAnimator.setDuration((long) (vx / (float) defaultMenuWidth * 500L));
                        slidingAnimator.setInterpolator(new OvershootInterpolator());
                        slidingAnimator.start();
                    }
                }


                break;
        }

        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void setBackgroundColor(int color) {
    }

    private Bitmap snapshot(View v) {

        Bitmap b = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Drawable bg = getBackground();
        if (bg != null) {
            bg.setBounds(0, 0, getWidth(), getHeight());
            bg.draw(c);
        } else {
            paint.setColor(0xFFFFFFFF);
            c.drawRect(0, 0, getWidth(), getHeight(), paint);
        }
        v.draw(c);
        //make sure the color on the top will continue to the status bar area
//        paint.setColor(b.getPixel(0, getStatusBarHeight() + 1));
//        c.drawRect(0, 0, getContext().getResources().getDisplayMetrics().widthPixels, getStatusBarHeight(), paint);
        return b;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        if (inSlidingMode) {
            paint.setColor(0xFF000000);
            canvas.drawRect(menu.getRight(), 0, getWidth(), getHeight(), paint);
        }


        super.dispatchDraw(canvas);
    }
}
