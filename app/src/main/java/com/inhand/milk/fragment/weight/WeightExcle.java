package com.inhand.milk.fragment.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.inhand.milk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/7.
 */
public class WeightExcle extends View {
    private static final String TAG = "WeightExcle";
    private int width, height;
    private int maxDate = MINDATE, minDate = MAXDATE;
    private float leftTextCenter, lineWidth, textLineWidth, leftTextSize, bottomTextSize, pointShowTextSize,
            bottomTextMargin, startX, endX, circleR, maxY, leftLineMargin;
    private float maxValue = MINVALUE, minValue = MAXVALUE, maxPointValue = MINVALUE, minPointValue = MAXVALUE;
    private final static float MAXVALUE = 10000, MINVALUE = -2;
    private final static int MAXDATE = 40, MINDATE = -1, CLICKLARGER = 10;
    private static float bottomHeight;
    private int leftTextColor = Color.GREEN, standerAreaColor = Color.BLACK, lineColor = Color.WHITE,
            shaderStartColor = 0xff00ff00, shaderEndColor = 0x0000ff00, bottomTextColor = Color.WHITE,
            pointShowColor = Color.WHITE;
    private static String[] leftTexts, bottomText;
    private float[] standerLeft, standerRight;
    private int MonthDays, showPoint;
    private List<List<Object>> points;

    public WeightExcle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public WeightExcle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WeightExcle(Context context) {
        super(context);
        init();
    }

    private void init() {
        bottomText = this.getResources().getStringArray(R.array.weight_excle_bottom_texts);
        leftTexts = this.getResources().getStringArray(R.array.weight_excle_left_texts);
        bottomHeight = this.getResources().getDimension(R.dimen.weight_fragment_excle_bottom_height);
        leftLineMargin = this.getResources().getDimension(R.dimen.weight_fragment_excle_left_line_margin);
        bottomTextSize = bottomHeight / 3;
        bottomTextMargin = bottomHeight / 8;
        setLisetner();
    }

    private void setLisetner() {
        OnTouchListener onTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int date = pointsAt(event.getX(), event.getY());
                    if (date == 0)
                        return false;
                    updateNumShow(date);
                    //showPoint  = date;
                    //invalidate();
                }
                return false;
            }
        };
        this.setOnTouchListener(onTouchListener);
    }

    //根据坐标返回点击的那个点
    private int pointsAt(float x, float y) {
        int date = xToDate(x);
        float centerY = 0;
        if (date == 0)
            return 0;
        for (List<Object> list : points) {
            if ((int) list.get(0) == date) {
                centerY = (float) list.get(1);
                break;
            }
        }
        centerY = valueToY(centerY);
        if (Math.abs(centerY - y) <= circleR * CLICKLARGER) {
            return date;
        }
        return 0;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initVaribles();
    }

    private void initVaribles() {
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        maxY = height - bottomHeight;
        leftTextCenter = width / 16;
        leftTextSize = leftTextCenter / 2;
        textLineWidth = leftTextSize / 9;
        lineWidth = textLineWidth * 1.7f;
        circleR = lineWidth * 2f;
        startX = width / 8;
        endX = width - startX / 1.5f;
        pointShowTextSize = leftTextSize;
    }

    public void setMonthDays(int monthDays) {
        this.MonthDays = monthDays - 1;
    }

    public void addPoint(int date, float y) {
        if (date <= 0)
            return;
        if (this.points == null)
            this.points = new ArrayList<>();
        List<Object> list = new ArrayList<>();
        list.add(date);
        list.add(y);
        minValue = minValue < y ? minValue : y;
        maxValue = maxValue > y ? maxValue : y;
        minPointValue = minPointValue < y ? minPointValue : y;
        maxPointValue = maxPointValue > y ? maxPointValue : y;
        maxDate = maxDate > date ? maxDate : date;
        minDate = minDate < date ? minDate : date;
        showPoint = maxDate;
        points.add(list);
    }

    public void clearPoints() {
        if (this.points == null)
            this.points = new ArrayList<>();
        else
            this.points.clear();

        minDate = MAXDATE;
        maxDate = MINDATE;
        minValue = MAXVALUE;
        maxValue = MINVALUE;
        minPointValue = MAXVALUE;
        maxPointValue = MINVALUE;
        if (standerLeft != null) {
            minValue = minValue < standerLeft[0] ? minValue : standerLeft[0];
            maxValue = maxValue > standerLeft[1] ? maxValue : standerLeft[1];
        }
        if (standerRight != null) {
            minValue = minValue < standerRight[0] ? minValue : standerRight[0];
            maxValue = maxValue > standerRight[1] ? maxValue : standerRight[1];
        }

    }

    public void clearStander() {
        standerLeft = null;
        standerRight = null;
        minValue = MAXVALUE;
        maxValue = MINVALUE;
        float y;
        for (List<Object> list : points) {
            y = (float) list.get(1);
            minValue = minValue < y ? minValue : y;
            maxValue = maxValue > y ? maxValue : y;
        }
    }

    public void setStanderLeft(float min, float max) {
        if (min > max)
            return;
        if (min <= 0 || max <= 0)
            return;
        if (standerLeft == null) {
            standerLeft = new float[2];
        }
        standerLeft[0] = min;
        standerLeft[1] = max;
        minValue = minValue < standerLeft[0] ? minValue : standerLeft[0];
        maxValue = maxValue > standerLeft[1] ? maxValue : standerLeft[1];
    }

    public void setStanderRight(float min, float max) {
        if (min > max)
            return;
        if (min <= 0 || max <= 0)
            return;
        if (standerRight == null) {
            standerRight = new float[2];
        }
        standerRight[0] = min;
        standerRight[1] = max;
        minValue = minValue < standerRight[0] ? minValue : standerRight[0];
        maxValue = maxValue > standerRight[1] ? maxValue : standerRight[1];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (maxValue == MINVALUE || minValue == MAXVALUE)
            return;
        drawStanderArea(canvas);
        drawLeftText(canvas);
        drawLine(canvas);
        drawShader(canvas);
        drawPoints(canvas);
        drawBottomText(canvas);
        drawNum(canvas, showPoint);

    }

    private void updateNumShow(int date) {
        invalidateNum(date);
        invalidateNum(showPoint);
        showPoint = date;
    }

    //这个地方老是出问题，所以先直接全部重画。虽然感觉不好;
    private void invalidateNum(int date) {
        float value = 0, y, x, height;
        Paint paint = new Paint();
        paint.setTextSize(pointShowTextSize);

        for (List<Object> list : points) {
            if (date == (int) list.get(0)) {
                value = (float) list.get(1);
                break;
            }
        }
        y = valueToY(value);
        x = dateToX(date);
        Paint.FontMetrics fm = paint.getFontMetrics();
        height = (float) Math.ceil(fm.descent - fm.ascent);
        invalidate((int) x, (int) ((y - circleR * 1.2f) - height),
                (int) (x + paint.measureText(String.valueOf(value))), (int) (y - circleR * 1.2f) + 1);
    }


    private void drawNum(Canvas canvas, int date) {
        float value = 0, y, x;
        for (List<Object> list : points) {
            if (date == (int) list.get(0)) {
                value = (float) list.get(1);
                break;
            }
        }
        y = valueToY(value);
        x = dateToX(date);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(pointShowColor);
        paint.setTextSize(pointShowTextSize);
        canvas.drawText(String.valueOf(value), x, y - circleR * 1.2f, paint);
    }

    private void drawBottomText(Canvas canvas) {
        Paint paint = new Paint();
        float start, centerY;
        paint.setAntiAlias(true);
        paint.setTextSize(bottomTextSize);
        paint.setColor(bottomTextColor);
        centerY = maxY + bottomTextSize + bottomTextMargin;
        for (int i = 0; i < 4; i++) {
            start = (endX - startX) / 3 * i + startX - paint.measureText(bottomText[i]) / 2;
            canvas.drawText(bottomText[i], start, centerY, paint);
        }
    }

    private void drawShader(Canvas canvas) {
        Shader shader = new LinearGradient(dateToX(minDate),
                valueToY(maxPointValue),
                //maxY * ( 1-(this.maxValue-minValue)/(maxValue -minValue)),
                dateToX(minDate),
                maxY,
                new int[]{shaderStartColor, shaderEndColor}, null, Shader.TileMode.REPEAT);

        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Path path = new Path();

        float x, y;
        int count = points.size();
        List list;
        list = points.get(0);
        x = dateToX((int) list.get(0));
        path.moveTo(x, maxY);
        for (int i = 0; i < count; i++) {
            list = points.get(i);
            x = dateToX((int) list.get(0));
            y = valueToY((float) list.get(1));
            path.lineTo(x, y);
        }
        path.lineTo(x, maxY);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawPoints(Canvas canvas) {
        float x, y;
        Paint paint = new Paint();
        paint.setColor(lineColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(lineWidth);
        paint.setAntiAlias(true);
        int count = points.size();
        List list;
        for (int i = 0; i < count; i++) {
            list = points.get(i);
            x = dateToX((int) list.get(0));
            y = valueToY((float) list.get(1));
            drawPoint(canvas, x, y, paint);
        }
    }

    private void drawPoint(Canvas canvas, float x, float y, Paint paint) {
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, circleR, paint);
        paint.setColor(Color.RED);
        canvas.drawCircle(x, y, circleR / 2, paint);
    }

    private void drawLine(Canvas canvas) {
        if (points == null)
            return;
        if (points.size() < 2)
            return;
        Paint paint = new Paint();
        paint.setColor(lineColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(lineWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        Path path = new Path();

        float x, y;
        int count = points.size();
        List list;
        list = points.get(0);
        x = dateToX((int) list.get(0));
        y = valueToY((float) list.get(1));
        path.moveTo(x, y);
        for (int i = 1; i < count; i++) {
            list = points.get(i);
            x = dateToX((int) list.get(0));
            y = valueToY((float) list.get(1));
            path.lineTo(x, y);
        }
        canvas.drawPath(path, paint);
    }

    private void drawStanderArea(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(standerAreaColor);

        setStanderLeft((maxValue - minValue) / 3 + minValue, (maxValue - minValue) / 3 * 2 + minValue);
        setStanderRight((maxValue - minValue) / 2 + minValue, (maxValue - minValue) * 3 / 4 + minValue);

        if (standerRight == null || standerLeft == null)
            return;

        float leftBottom, rightBottom, leftTop, rightTop;
        leftBottom = valueToY(standerLeft[0]);
        rightBottom = valueToY(standerRight[0]);
        leftTop = valueToY(standerLeft[1]);
        rightTop = valueToY(standerRight[1]);


        leftBottom = calculateLine(startX, leftBottom, endX, rightBottom, 0);
        rightBottom = calculateLine(startX, leftBottom, endX, rightBottom, width);
        leftTop = calculateLine(startX, leftTop, endX, rightTop, 0);
        rightTop = calculateLine(startX, leftTop, endX, rightTop, width);

        Path path = new Path();
        path.moveTo(0, leftTop);
        path.lineTo(width, rightTop);
        path.lineTo(width, rightBottom);
        path.lineTo(0, leftBottom);
        path.close();
        canvas.drawPath(path, paint);

    }

    //这个函数夹杂了画最下面一条线的功能。
    private void drawLeftText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(leftTextColor);
        paint.setStrokeWidth(textLineWidth);
        paint.setTextSize(leftTextSize);

        float maxValue = this.maxValue * 1.2f;
        float minValue = this.minValue * 0.8f;

        float tempWidth, tempSpace = 0;
        float[] divides = new float[4];
        float leftBottom, rightBottom, leftTop, rightTop, space;
        space = maxValue - minValue;
        leftBottom = maxY * (1 - (standerLeft[0] - minValue) / space);
        leftTop = maxY * (1 - (standerLeft[1] - minValue) / space);
        rightBottom = maxY * (1 - (standerRight[0] - minValue) / space);
        rightTop = maxY * (1 - (standerRight[1] - minValue) / space);

        divides[2] = calculateLine(startX, leftBottom, endX, rightBottom, leftTextCenter);
        divides[1] = calculateLine(startX, leftTop, endX, rightTop, leftTextCenter);
        divides[0] = 0;
        divides[3] = maxY;
        for (int i = 0; i < 3; i++) {
            tempWidth = paint.measureText(leftTexts[i]);
            canvas.drawLine(leftTextCenter, tempSpace + leftLineMargin, leftTextCenter,
                    (divides[i] + divides[i + 1]) / 2 - leftTextSize / 2, paint);
            canvas.drawText(leftTexts[i], leftTextCenter - tempWidth / 2,
                    (divides[i] + divides[i + 1]) / 2 + leftTextSize / 2, paint);
            tempSpace = (divides[i] + divides[i + 1]) / 2 + leftTextSize / 2;
        }
        canvas.drawLine(leftTextCenter, tempSpace + leftLineMargin, leftTextCenter,
                maxY - leftLineMargin, paint);
        canvas.drawLine(0, maxY, width, maxY, paint);
    }

    //根据两点确定直线，并返回x出的y值
    private float calculateLine(float x1, float y1, float x2, float y2, float x) {
        return (y2 - y1) * (x - x1) / (x2 - x1) + y1;
    }

    private float dateToX(int date) {
        return (float) (date - 1) / MonthDays * (endX - startX) + startX;
    }

    private int xToDate(float x) {
        float center;
        for (List<Object> list : points) {
            center = dateToX((int) list.get(0));
            if (Math.abs(x - center) <= circleR * CLICKLARGER)
                return (int) list.get(0);
        }
        return 0;
    }

    private float valueToY(float value) {
        float maxValue = this.maxValue * 1.2f;
        float minValue = this.minValue * 0.8f;
        return maxY * (1 - (value - minValue) / (maxValue - minValue));
    }


}
