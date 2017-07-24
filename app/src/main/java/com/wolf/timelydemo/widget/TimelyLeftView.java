package com.wolf.timelydemo.widget;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wolf.timelydemo.R;

/**
 * Created by dahongwudi on 17/7/15.
 */

public class TimelyLeftView extends View{

    /**
     * 屏幕的宽度
     */
    public static int widthPixels;

    /**
     * 屏幕的高度
     */
    public static int heightPixels;

    /**
     * 左侧未选中颜色
     */
    private int leftNormalColor;

    /**
     * 左侧选中颜色
     */
    private int leftSelectColor;

    /**
     * 默认字体大小
     */
    private float textSize;

    /**
     * 上下间距
     */
    private float topBottomMargin;

    /**
     * 选中状态的等差距离
     */
    private float clockDiff;

    /**
     * 时间展示的颜色
     */
    private int showTimeColor;

    /**
     * 时间展示的背景颜色
     */
    private int showTimeBgColor;


    /**
     * 时间展示字体大小
     */
    private float showTimeSize;

    /**
     *左侧时间刻度画笔
     */
    private Paint leftPaint;

    /**
     *选中背景画笔颜色
     */
    private Paint selectBgPaint;

    /**
     * 时间显示画笔
     */
    private Paint timePaint;

    private int progress = 0;

    private final int PROGRESS_MIN = 0;

    private final int PROGRESS_MAX = 6*24;

    private int selectHour;

    private int selectMin;

    String[] seq = new String[]{"-", "-", "-", "-", "-", "-", "-06", "-", "-",
            "-", "-", "-", "-12", "-", "-", "-", "-", "-",
            "-18", "-", "-", "-", "-", "-", "-"};

    /**
     * 是否正在滑动
     */
    private boolean isOn;

    /**
     * 初始化
     */
    private boolean isInit;

    /**
     * 当前文字显示位置
     */
    TextValue currentText;

    Paint.FontMetrics fm;
    public TimelyLeftView(Context context) {
        super(context);
    }

    public TimelyLeftView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        widthPixels = dm.widthPixels;
        heightPixels = dm.heightPixels;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimelyLeftView);
        if (typedArray != null) {
            leftNormalColor = typedArray.getColor(R.styleable.TimelyLeftView_timely_left_normal, Color.parseColor("#999999"));
            leftSelectColor = typedArray.getColor(R.styleable.TimelyLeftView_timely_left_checked, Color.parseColor("#ffffff"));
            textSize = typedArray.getDimension(R.styleable.TimelyLeftView_timely_text_size, 10);
            topBottomMargin = typedArray.getDimension(R.styleable.TimelyLeftView_timely_top_bottom_margin, 10);
            clockDiff = typedArray.getDimension(R.styleable.TimelyLeftView_timely_diff, 5);
            showTimeColor = typedArray.getColor(R.styleable.TimelyLeftView_timely_show_time, Color.parseColor("#e48f20"));
            showTimeSize = typedArray.getDimension(R.styleable.TimelyLeftView_timely_show_time_size, 40);
            showTimeBgColor = typedArray.getColor(R.styleable.TimelyLeftView_timely_show_time_bg, Color.parseColor("#33ffffff"));
            typedArray.recycle();
        }

        leftPaint = new Paint();
        leftPaint.setAntiAlias(true);
        leftPaint.setStyle(Paint.Style.FILL);
        leftPaint.setStrokeWidth(2);

        //时间展示画笔
        timePaint = new Paint();
        timePaint.setAntiAlias(true);
        timePaint.setStyle(Paint.Style.FILL);
        timePaint.setColor(showTimeColor);
        timePaint.setTextSize(showTimeSize);

        selectBgPaint = new Paint();
        selectBgPaint.setAntiAlias(true);
        selectBgPaint.setStyle(Paint.Style.FILL);
        selectBgPaint.setColor(showTimeBgColor);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!isInit){
                    currentText = new TextValue(1f,getWidth()/4);

                    fm = timePaint.getFontMetrics();
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                isInit = true;

                currentText.setScale(1f);
                currentText.setScrollerX(getWidth()/4);

                if (event.getY() < topBottomMargin) {
                    setProgress(PROGRESS_MIN);
                    return true;
                } else if (event.getY() > getHeight() - topBottomMargin) {
                    setProgress(PROGRESS_MAX);
                    return true;
                }
                setProgress(calculProgress(event.getY()));
                isOn = true;
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isOn = false;
//                invalidate();
//                Log.d("wanghj","--------->cancel");
                startAnimate();
                break;
        }

        return super.onTouchEvent(event);
    }

    private void startAnimate(){
        TextValue startValue = new TextValue(1f, getWidth()/4);
        TextValue endValue = new TextValue(1.2f, getWidth()/3);
        ValueAnimator anim = ValueAnimator.ofObject(new TextEvaluator(), startValue, endValue);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentText = (TextValue) animation.getAnimatedValue();
                Log.d("wanghj","currentText----->"+currentText.getScale()+"----->"+currentText.getScrollerX());
                invalidate();
            }
        });
        anim.setDuration(100);
        anim.start();
    }

    class TextValue{
        float scale;
        float scrollerX;

        public TextValue(float scale, float scrollerX) {
            this.scale = scale;
            this.scrollerX = scrollerX;
        }

        public float getScale() {
            return scale;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }

        public float getScrollerX() {
            return scrollerX;
        }

        public void setScrollerX(float scrollerX) {
            this.scrollerX = scrollerX;
        }
    }
    class TextEvaluator implements TypeEvaluator {

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            TextValue startText = (TextValue) startValue;
            TextValue endText = (TextValue) endValue;
            float scale = startText.getScale() + fraction * (endText.getScale() - startText.getScale());
            float scrollerX = startText.getScrollerX() + fraction * (endText.getScrollerX() - startText.getScrollerX());
            TextValue text = new TextValue(scale, scrollerX);
            Log.d("wanghj","text----->"+text.getScale()+"----->"+text.getScrollerX());
            return text;
        }

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        for(int i = 0; i < seq.length; i++){
            float posY = (getHeight() - topBottomMargin * 2) / 24 * (24 - i)+topBottomMargin;
            if(isOn){
                if(i < selectHour-3 || i > selectHour+3) {
                    leftPaint.setTextSize(textSize);
                    leftPaint.setColor(leftNormalColor);
                    canvas.drawText(seq[i], 0,posY , leftPaint);
                }else if(i == selectHour+3 || i == selectHour-3){
                    leftPaint.setTextSize(textSize*1.5f);
                    leftPaint.setColor(leftSelectColor);
                    canvas.drawText(seq[i], 0 + clockDiff, posY, leftPaint);
                }else if(i == selectHour+2 || i == selectHour-2){
                    leftPaint.setTextSize(textSize*2.0f);
                    leftPaint.setColor(leftSelectColor);
                    canvas.drawText(seq[i], 0 + 2*clockDiff, posY, leftPaint);
                }else if(i == selectHour+1 || i == selectHour-1){
                    leftPaint.setTextSize(textSize*2.5f);
                    leftPaint.setColor(leftSelectColor);
                    canvas.drawText(seq[i], 0 + 3*clockDiff, posY, leftPaint);
                }else{
                    leftPaint.setTextSize(textSize*3.0f);
                    leftPaint.setColor(leftSelectColor);
                    canvas.drawText(seq[i], 0 + 4*clockDiff, posY, leftPaint);
                }
            }else{
                leftPaint.setTextSize(textSize);
                leftPaint.setColor(leftNormalColor);
                canvas.drawText(seq[i], 0, posY, leftPaint);
            }

        }

        if(isInit){
            int textHeight = (int) (Math.ceil(fm.descent - fm.ascent) + 2);
            timePaint.setTextSize(showTimeSize*currentText.getScale());
            canvas.drawText(progressToTime(PROGRESS_MAX - progress),currentText.getScrollerX(), getCx(), timePaint);
            canvas.drawRect(0,getCx()+fm.ascent-textHeight/4,getWidth(),getCx()+textHeight/4,selectBgPaint);
        }

    }


    /**
     * 获取文字坐标
     * @return
     */
    private float getCx() {
        float cx = 0.0f;
        cx = (getHeight() - topBottomMargin * 2);
        return cx / PROGRESS_MAX * (progress) + topBottomMargin;
    }

    /**
     * 设置百分比
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    /**
     * 计算触摸点的百分比
     */
    private int calculProgress(float eventY) {
        float proResult = (eventY - topBottomMargin) / (getHeight() - topBottomMargin * 2);
        return (int) (proResult * PROGRESS_MAX);
    }


    /**
     * 以五分钟为单位计时
     * @param progress
     * @return
     */
    private String progressToTime(int progress){

        selectHour = progress / 6;
        selectMin = progress % 6;


        return Integer.toString(selectHour) + ": "+ Integer.toString(selectMin) + "0";
//        return Integer.toString(selectHour) + ": "+ Integer.toString(selectMin/2) + (selectMin%2 == 1?"0":"5");
    }

}
