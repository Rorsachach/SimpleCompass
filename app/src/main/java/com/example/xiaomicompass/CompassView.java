package com.example.xiaomicompass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class CompassView extends View {

    private Canvas canvas; // 创建一个画布
    private Context context; // 创建一个上下文
//
    private int width; // 存放指南针宽度
    private int centerX, centerY; // 圆心坐标
    private int outSideRadius;  // 外圆半径
    private int inSideRadius; // 内圆半径
    private int textHeight; // 文字框高度


    private float val; // 存储当前的角度值(Z轴旋转角)

    private String text = "北"; // 存放当前的方向值

    private Paint textPaint; // 文字画笔(最上方的)
    private Rect textRect; // 为最上方的文字创建一个矩形
    private Paint outSideTrianglePaint; // 外圆三角画笔
    private Path outSideTriangle; // 外圆三角
    private Paint inSideTrianglePaint; // 内圆三角画笔
    private Path inSideTriangle; // 内圆三角
    private Paint deepGrayPaint; // 深灰色的画笔
    private Paint lightGrayPaint; // 浅灰色画笔
    private Paint deepRedPaint; // 深红色画笔

    // 为每个方向画出一个字母位置
    private Paint directionPaint;
    private Rect directionRect;

    private Paint numberPaint;
    private Rect numberRect;
    private Rect numberRect2;

    private Paint centerTextPaint;
    private Rect centerTextRect;


    public void setVal(float val) {
        this.val = val;
        invalidate();
    }

    /**
     * 初始化
     * @param context
     */
    public CompassView(Context context) {
        super(context);
        initView(context);
    }

    public CompassView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CompassView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }

    /**
     * 具体初始化内容，包括各类画笔和各种图形
     * @param context
     */
    private void initView(Context context) {
        this.context = context;

        // 初始化文字画笔(最上方的)
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL); // 文字风格，填充
        textPaint.setAntiAlias(true); // 抗锯齿
        textPaint.setTextSize(80);
        textPaint.setColor(context.getResources().getColor(R.color.white)); // 文字颜色：白

        textRect = new Rect();// 初始化放文字的矩形(最上方的)

        // 外圆三角画笔
        outSideTrianglePaint = new Paint();
        outSideTrianglePaint.setStyle(Paint.Style.FILL);
        outSideTrianglePaint.setAntiAlias(true);
        outSideTrianglePaint.setColor(context.getResources().getColor(R.color.deepgray));

        outSideTriangle = new Path(); // 外圆三角

        // 内圆三角画笔
        inSideTrianglePaint = new Paint();
        inSideTrianglePaint.setStyle(Paint.Style.FILL);
        inSideTrianglePaint.setAntiAlias(true);
        inSideTrianglePaint.setColor(context.getResources().getColor(R.color.red));

        inSideTriangle = new Path(); // 内圆三角


        // 深红色画笔
        deepRedPaint = new Paint();
        deepRedPaint.setStrokeWidth((float)5);
        deepRedPaint.setStyle(Paint.Style.STROKE);
        deepRedPaint.setAntiAlias(true);
        deepRedPaint.setColor(context.getResources().getColor(R.color.red));

        // 深灰色画笔
        deepGrayPaint = new Paint();
        deepGrayPaint.setStrokeWidth((float)5);
        deepGrayPaint.setStyle(Paint.Style.STROKE);
        deepGrayPaint.setAntiAlias(true);
        deepGrayPaint.setColor(context.getResources().getColor(R.color.deepgray));

        // 浅灰色画笔
        lightGrayPaint = new Paint();
        lightGrayPaint.setStrokeWidth((float)5);
        lightGrayPaint.setStyle(Paint.Style.STROKE);
        lightGrayPaint.setAntiAlias(true);
        lightGrayPaint.setColor(context.getResources().getColor(R.color.lightgray));

        // 方向画笔
        directionPaint = new Paint();
        directionPaint.setStyle(Paint.Style.FILL);
        directionPaint.setTextSize(40);
        directionPaint.setAntiAlias(true);
        directionPaint.setColor(context.getResources().getColor(R.color.red));

        directionRect = new Rect();

        // 数字画笔
        numberPaint = new Paint();
        numberPaint.setStyle(Paint.Style.FILL);
        numberPaint.setTextSize(40);
        numberPaint.setAntiAlias(true);
        numberPaint.setColor(context.getResources().getColor(R.color.deepgray));

        numberRect = new Rect();
        numberRect2 = new Rect();

        // 中心文字画笔
        centerTextPaint = new Paint();
        centerTextPaint.setStyle(Paint.Style.FILL);
        centerTextPaint.setTextSize(120);
        centerTextPaint.setAntiAlias(true);
        centerTextPaint.setColor(context.getResources().getColor(R.color.white));

        centerTextRect = new Rect();
    }


    /**
     * 对屏幕进行测量，并初始化画板大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec); // 获取大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec); // 获取模式
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        width = Math.min(widthSize, heightSize);

        // 上方的 1/3 写字
        textHeight = width / 3;

        // 设置圆心坐标
        centerX = width / 2;
        centerY = width / 2 + textHeight;

        // 设置外部圆的半径
        outSideRadius = width * 3 / 8;
        // 内部圆半径
        inSideRadius = outSideRadius * 4 / 5;

        // View 布局大小
        setMeasuredDimension(width, width + textHeight);

    }

    /**
     * 开始绘制图像
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        drawText(); // 绘制顶端文字
        drawOutCircle(); // 绘制外圆
        drawInnerCircle(); // 绘制内圆
        drawDegreeScale(); // 绘制刻度
        drawCenterText(); // 绘制中心标度值

    }

    /**
     * 绘制上方的度数文字
     */
    private void drawText() {

        // 判断度数大小来判断大致方向
        if(val <= 15 || val >= 345)
            text = "北";
        else if (val > 15 && val <= 75)
            text = "东北";
        else if (val > 75 && val <= 105)
            text = "东";
        else if (val > 105 && val <= 165)
            text = "东南";
        else if (val > 165 && val <= 195)
            text = "南";
        else if (val > 195 && val <= 255 )
            text = "西南";
        else if (val > 255 && val <= 285)
            text = "西";
        else if (val > 285 && val < 345)
            text = "西北";

        if(text == null)
            Log.e("TEXT","null");
        else if(textRect == null)
            Log.e("textRect","null");

        textPaint.getTextBounds(text, 0, text.length(), textRect);
        // 设置文字宽度
        int textWidth = textRect.width();
        // 让文字水平居中
        canvas.drawText(text, width/2 - textWidth/2, textHeight/2, textPaint);

    }


    /**
     * 绘制外部的圆圈
     */
    private void drawOutCircle() {
        canvas.save(); // 保存当前画布状态
        int outsideTriangleHeight = 40; // 外圆小三角大小
        outSideTriangle.moveTo(width/2, textHeight - outsideTriangleHeight); // 确定三角位置
        float sideLength = (float) (outsideTriangleHeight / Math.sqrt(3) * 2); // 40*root(3)
        // 画三角
        outSideTriangle.lineTo(width/2 - sideLength/2, textHeight);
        outSideTriangle.lineTo(width/2 + sideLength/2, textHeight);
        outSideTriangle.close();
        canvas.drawPath(outSideTriangle, outSideTrianglePaint); // 使用深灰色画笔画

        // 画圆弧
        // 最左端，最上端，最右端，最下端，从-80度到-80 + 120(40)度，中间不填充(圆环)，使用浅灰色画笔
        canvas.drawArc(width/2 - outSideRadius,
                textHeight,
                width/2 + outSideRadius,
                textHeight + outSideRadius * 2,
                0,
                -80,
                false,
                deepGrayPaint);
        canvas.drawArc(width/2 - outSideRadius,
                textHeight,
                width/2 + outSideRadius,
                textHeight + outSideRadius * 2,
                -100,
                -80,
                false,
                deepGrayPaint);
        canvas.drawArc(width/2 - outSideRadius,
                textHeight,
                width/2 + outSideRadius,
                textHeight + outSideRadius * 2,
                0,
                180,
                false,
                deepRedPaint);
        canvas.restore();
    }

    /**
     * 绘制内部的圆圈
     */
    private void drawInnerCircle() {
        canvas.save();
        int insideTriangleHeight = (outSideRadius - inSideRadius) /2;

        canvas.rotate(-val, width/2, outSideRadius + textHeight);
        inSideTriangle.moveTo(width/2, textHeight + insideTriangleHeight);
        float sideLength = (float) (insideTriangleHeight / Math.sqrt(3) * 2);
        inSideTriangle.lineTo(width/2 - sideLength/2, textHeight + (outSideRadius - inSideRadius));
        inSideTriangle.lineTo(width/2 + sideLength/2, textHeight + (outSideRadius - inSideRadius));
        inSideTriangle.close();
        canvas.drawPath(inSideTriangle, inSideTrianglePaint);

        canvas.drawArc(width/2 - inSideRadius,
                textHeight + (outSideRadius - inSideRadius),
                width/2 + inSideRadius,
                textHeight + outSideRadius + inSideRadius,
                -80,
                340,
                false,
                lightGrayPaint);

        if(val <= 180 && val >= 10){
            canvas.drawArc(width/2 - inSideRadius,
                    textHeight + (outSideRadius - inSideRadius),
                    width/2 + inSideRadius,
                    textHeight + outSideRadius + inSideRadius,
                    -80,
                    val - 10,
                    false,
                    deepRedPaint);
        }else if(val > 180 && val <= 350) {
            canvas.drawArc(width/2 - inSideRadius,
                    textHeight + (outSideRadius - inSideRadius),
                    width/2 + inSideRadius,
                    textHeight + outSideRadius + inSideRadius,
                    -100,
                    val + 10 - 360,
                    false,
                    deepRedPaint);
        }


        canvas.restore();
    }

    /**
     * 绘制刻度
     */
    private void drawDegreeScale() {
        canvas.save();

        // 先获取一下字跨度
        // 首先先是 N W S E
        directionPaint.getTextBounds("W", 0, 1, directionRect); // 获取W大小进而创造一个矩形
        int directionTextWidth = directionRect.width();
        int directionTextHeight = directionRect.height();

        // 获取两位数字字跨度
        numberPaint.getTextBounds("30", 0,2, numberRect);
        int numberTextWidth = numberRect.width();
        int numberTextHeight = numberRect.height();

        // 获取三位数字字跨度
        numberPaint.getTextBounds("300", 0, 3, numberRect2);
        int numberTextWidth2 = numberRect2.width();
        int numberTextHeight2 = numberRect2.height();

        canvas.rotate(-val, width/2, textHeight + outSideRadius); // 转轴中心旋转一下

        for (int i = 0; i < 240; i++) {

            int start = textHeight + (outSideRadius - inSideRadius) + 10; // 起始点Y坐标，因此画线时只需加入线长即可

            // 先画线
            if(i == 0 || i == 60 || i == 120 || i == 180){
                canvas.drawLine(width / 2,
                         start,
                        width / 2,
                        start + 30,
                        deepRedPaint);
            }else if(i % 20 == 0){
                canvas.drawLine(width / 2,
                        start,
                        width / 2,
                        start + 23,
                        deepGrayPaint);
            }else {
                canvas.drawLine(width / 2,
                        start,
                        width / 2,
                        start + 15,
                        deepGrayPaint);
            }

            // 标字
            if(i == 0){
                canvas.drawText("N",
                        width/2 - directionTextWidth/2,
                        start + 40 + directionTextHeight,
                        directionPaint);
            }else if (i == 60)
                canvas.drawText("E",
                        width/2 - directionTextWidth/2,
                        start + 40 + directionTextHeight,
                        directionPaint);
            else if(i == 120)
                canvas.drawText("S",
                        width/2 - directionTextWidth/2,
                        start + 40 + directionTextHeight,
                        directionPaint);
            else if(i == 180)
                canvas.drawText("W",
                        width/2 - directionTextWidth/2,
                        start + 40 + directionTextHeight,
                        directionPaint);
            else if(i % 20 == 0 && i < 100)
                canvas.drawText(i/20*30+"",
                        width/2 - numberTextWidth/2,
                        start + 33 + numberTextHeight,
                        numberPaint);
            else if(i % 20 == 0 && i >= 100)
                canvas.drawText(i/20*30+"",
                        width/2 - numberTextWidth2/2,
                        start + 33 + numberTextHeight,
                        numberPaint);

            canvas.rotate( 1.5f, width/2, textHeight + outSideRadius); // 旋转
        }
        canvas.restore();
    }

    /**
     * 绘制中间部分的文字
     */
    private void drawCenterText() {
        canvas.save();
        String text = ((int) val) + "°";
        centerTextPaint.getTextBounds(text, 0, text.length(), centerTextRect);
        canvas.drawText(text,
                width/2 - centerTextRect.width()/2,
                textHeight + outSideRadius + centerTextRect.height()/2,
                centerTextPaint);
        canvas.restore();
    }
}
