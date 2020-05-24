package com.example.xiaomicompass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class SpiritView extends View {
    private Context context;
    private Canvas canvas;

    private int width, height; // 存放View 的宽与高
    private int centerX, centerY; // 存放圆心中心
    private int textHeight; // 上方的字的大小
    private int outSideRadius;
    private int inSideRadius;
    private int innerCircleRadius; // 可移动内圆的半径

    private int coneHeight; // 圆锥高

    private int innerCircleX, innerCircleY; // 内圆偏移位置;
    private float angle; // 当前偏移角度

    private float valX, valY; // 存放X/Y轴与平面夹角

    private Paint textPaint;
    private Rect textRect;

    private Paint deepGrayPaint; // 深灰色的画笔
    private Paint deepRedPaint; // 深红色画笔
    private Paint lightGreenPaint; // 浅绿色画笔

    public void setValX(float valX) {
        this.valX = valX;
        invalidate();
    }

    public void setValY(float valY) {
        this.valY = valY;
        invalidate();
    }

    public SpiritView(Context context) {
        super(context);
        initView(context);
    }

    public SpiritView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SpiritView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(80);
        textPaint.setColor(context.getResources().getColor(R.color.white));

        textRect = new Rect();

        // 深灰色画笔
        deepGrayPaint = new Paint();
        deepGrayPaint.setStrokeWidth((float)8);
        deepGrayPaint.setStyle(Paint.Style.STROKE);
        deepGrayPaint.setAntiAlias(true);
        deepGrayPaint.setColor(context.getResources().getColor(R.color.deepgray));

        // 深红色画笔
        deepRedPaint = new Paint();
        deepRedPaint.setStrokeWidth((float)8);
        deepRedPaint.setStyle(Paint.Style.STROKE);
        deepRedPaint.setAntiAlias(true);
        deepRedPaint.setColor(context.getResources().getColor(R.color.red));

        // 浅绿色画笔
        lightGreenPaint = new Paint();
        lightGreenPaint.setStrokeWidth((float)8);
        lightGreenPaint.setStyle(Paint.Style.STROKE);
        lightGreenPaint.setAntiAlias(true);
        lightGreenPaint.setColor(context.getResources().getColor(R.color.green));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec); // 获取大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec); // 获取模式
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        width = Math.min(widthSize, heightSize);

        textHeight = width / 3;

        centerX = width / 2;
        centerY = centerX + textHeight;

        outSideRadius = width * 3/8;
        inSideRadius = outSideRadius / 40;
        innerCircleRadius = outSideRadius /10;
        coneHeight = outSideRadius - innerCircleRadius;

        setMeasuredDimension(width, width + textHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;

        getCurrentCircle();
        drawText();
        drawOutCircle();
        drawInCircle();
        drawCurrentCircle();// 画可动圆
    }

    /**
     * 绘制顶端文字
     */
    private void drawText() {

        String text = String.format("%d°", (int) (90 - angle)); // 求得角的余角

        textPaint.getTextBounds(text, 0, text.length(), textRect);

        int textWidth = textRect.width();
        canvas.drawText(text, width/2 - textWidth/2, textHeight/2, textPaint);

    }

    /**
     * 获取当前圆信息
     */
    private void getCurrentCircle() {
        // 获取当前X Y偏移大小
        innerCircleX = (int) (coneHeight * Math.sin(valX * Math.PI/180));
        innerCircleY = (int) (coneHeight * Math.sin(valY * Math.PI/180));

        // 获取当前偏移角度
        double tmp = Math.sqrt(Math.pow(innerCircleX, 2) + Math.pow(innerCircleY, 2)) / coneHeight;
        angle = (float)( Math.acos(tmp) * 180 / Math.PI);
    }

    private void drawOutCircle() {
        canvas.save();

        canvas.drawArc(centerX - outSideRadius,
                textHeight,
                centerX + outSideRadius,
                textHeight + 2*outSideRadius,
                0,
                360,
                false,
                deepGrayPaint);
        canvas.restore();
    }

    private void drawInCircle() {
        canvas.save();

        int iAngle = (int) (90 - angle);
        Log.e("angle", iAngle+"");
        if(iAngle == 0)
            canvas.drawArc(centerX - inSideRadius,
                    textHeight + (outSideRadius - inSideRadius),
                    centerX + inSideRadius,
                    textHeight + (outSideRadius + inSideRadius),
                    0,
                    360,
                    false,
                    lightGreenPaint);
        else
            canvas.drawArc(centerX - inSideRadius,
                textHeight + (outSideRadius - inSideRadius),
                centerX + inSideRadius,
                textHeight + (outSideRadius + inSideRadius),
                0,
                360,
                false,
                deepGrayPaint);
        canvas.restore();
    }

    private void drawCurrentCircle() {
        canvas.save();

        canvas.drawArc(centerX + innerCircleX - innerCircleRadius,
                textHeight + outSideRadius + innerCircleY - innerCircleRadius,
                centerX + innerCircleX + innerCircleRadius,
                textHeight + outSideRadius + innerCircleY + innerCircleRadius,
                0,
                360,
                false,
                deepRedPaint);
        canvas.restore();
    }
}
