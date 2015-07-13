package com.inhand.milk.fragment.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.view.View;

import com.inhand.milk.App;
import com.inhand.milk.R;

public class Circle extends View {

    private final float[] originalColorWeight = {0, 0.33f, 0.66f, 1.0f};//һ��������0.25�����������ĳ�0.245
    private final int[] originalRGB = {0x04, 0x98, 0xa2,
            0x02, 0xb9, 0xb4,
            0x05, 0x7e, 0x9b,
            0x05, 0x7e, 0x9b};
    private float sweepAngle = 0;
    private Handler handler;
    private Runnable runnabel;
    private int time, maxScore, score;
    private float maxSweepAngle;
    private String scoreString;
    ;
    private float accurateScore;
    private float x, y, r, wr, paintWidth, textSize;
    private int[] color;
    private float[] fixedColorWeight;
    private int[] fixedRGB;

    public Circle(Context context, float rr) {
        super(context);
        time = 300;
        score = 10;
        scoreString = String.valueOf(score);
        maxScore = 0;
        maxSweepAngle = maxScore * 3.6f;

        x = rr;//Բ��
        y = rr;//Բ��
        wr = rr;//�뾶 ��Ȧ
        paintWidth = wr * 2 / 366 * 5;//��Բ��ʱ��ıʵĿ��
        x = wr + paintWidth;//Ԥ����һ��width�ĳ���
        y = wr + paintWidth;
        r = wr - paintWidth / 2;
        textSize = wr * 2 * 150 / 366;

        fixColor();
        sortWeight();
    }

    public void setScore(int s) {
        maxScore = s;
        maxSweepAngle = maxScore * 3.6f;
    }

    public void setAnimalTime(int t) {
        time = t;
    }


    private void drawBackground(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(paintWidth);

        paint.setColor(getResources().getColor(R.color.home_circle_outermost));
        paint.setStyle(Style.STROKE);
        canvas.drawCircle(x, y, r, paint);

        paint.setStyle(Style.FILL);
        paint.setStrokeWidth(1);


        paint.setColor(getResources().getColor(R.color.home_circle_inner_outermost));
        canvas.drawCircle(x, y, r - paintWidth / 2, paint);


        float tempr = wr * 312f / 366;
        paint.setColor(getResources().getColor(R.color.public_main_b_color));
        paint.setAlpha((int) (0.03 * 256));
        canvas.drawCircle(x, y, tempr, paint);


        tempr = wr * 266f / 366;
        paint.setColor(getResources().getColor(R.color.public_main_b_color));
        paint.setAlpha((int) (0.07 * 256));
        canvas.drawCircle(x, y, tempr, paint);


    }

    private void fixColor() {
        int i, tempi;
        boolean a;
        float temp, w;
        a = false;
        fixedColorWeight = new float[originalColorWeight.length + 2];
        fixedRGB = new int[3 * (originalColorWeight.length + 2)];
        for (i = 0; i < originalColorWeight.length + 2; i++) {
            if (a == false)
                temp = originalColorWeight[i] - (float) 0.25;
            else if (i == originalColorWeight.length + 2 - 1) {
                temp = originalColorWeight[i - 2] - (float) 0.001 - (float) 0.25;
            } else
                temp = originalColorWeight[i - 2] - (float) 0.25;
            if (temp < 0) {
                fixedColorWeight[i] = temp + 1;
                fixedRGB[i * 3] = originalRGB[i * 3];
                fixedRGB[i * 3 + 1] = originalRGB[i * 3 + 1];
                fixedRGB[i * 3 + 2] = originalRGB[i * 3 + 2];
            } else if (temp >= 0 && a == false) {
                w = originalColorWeight[i] - originalColorWeight[i - 1];
                w = ((float) 0.25 - originalColorWeight[i - 1]) / w;
                for (tempi = i; tempi < i + 2; tempi++) {
                    fixedRGB[tempi * 3] = (int) (originalRGB[3 * (i - 1)] * (1 - w) + originalRGB[3 * i] * w);
                    fixedRGB[tempi * 3 + 1] = (int) (originalRGB[3 * (i - 1) + 1] * (1 - w) + originalRGB[3 * i + 1] * w);
                    fixedRGB[tempi * 3 + 2] = (int) (originalRGB[3 * (i - 1) + 2] * (1 - w) + originalRGB[i * 3 + 2] * w);
                }
                fixedColorWeight[i] = 0;
                fixedColorWeight[i + 1] = 1;
                i = i + 2;


                fixedColorWeight[i] = temp;
                fixedRGB[i * 3] = originalRGB[(i - 2) * 3];
                fixedRGB[i * 3 + 1] = originalRGB[(i - 2) * 3 + 1];
                fixedRGB[i * 3 + 2] = originalRGB[(i - 2) * 3 + 2];

                a = true;
            } else {
                fixedColorWeight[i] = temp;
                fixedRGB[i * 3] = originalRGB[(i - 2) * 3];
                fixedRGB[i * 3 + 1] = originalRGB[(i - 2) * 3 + 1];
                fixedRGB[i * 3 + 2] = originalRGB[(i - 2) * 3 + 2];
            }
        }

    }

    private void sortWeight() {
        int i, j;
        float temp;
        int colortemp;
        for (i = 0; i < fixedColorWeight.length - 1; i++) {
            for (j = i + 1; j < fixedColorWeight.length; j++) {
                if (fixedColorWeight[j] < fixedColorWeight[i]) {
                    temp = fixedColorWeight[i];
                    fixedColorWeight[i] = fixedColorWeight[j];
                    fixedColorWeight[j] = temp;

                    colortemp = fixedRGB[3 * i];
                    fixedRGB[3 * i] = fixedRGB[3 * j];
                    fixedRGB[3 * j] = colortemp;

                    colortemp = fixedRGB[3 * i + 1];
                    fixedRGB[3 * i + 1] = fixedRGB[3 * j + 1];
                    fixedRGB[3 * j + 1] = colortemp;

                    colortemp = fixedRGB[3 * i + 2];
                    fixedRGB[3 * i + 2] = fixedRGB[3 * j + 2];
                    fixedRGB[3 * j + 2] = colortemp;
                }

            }
        }

        color = new int[originalColorWeight.length + 2];
        for (i = 0; i < originalColorWeight.length + 2; i++)
            color[i] = (int) Color.rgb(fixedRGB[i * 3], fixedRGB[i * 3 + 1], fixedRGB[i * 3 + 2]);
    }

    private void selecteColor(float sweepAngle, Paint paint) {
        if (sweepAngle - 90 < 0)
            sweepAngle = sweepAngle + 270;
        else
            sweepAngle = sweepAngle - 90;

        float weight = sweepAngle / 360;

        for (int i = 1; i < fixedColorWeight.length; i++)
            if (weight <= fixedColorWeight[i] && weight >= fixedColorWeight[i - 1]) {
                float w = (weight - fixedColorWeight[i - 1]) / (fixedColorWeight[i] - fixedColorWeight[i - 1]);
                int red = (int) (fixedRGB[i * 3] * w + fixedRGB[i * 3 - 3] * (1 - w));
                int green = (int) (fixedRGB[i * 3 + 1] * w + fixedRGB[i * 3 - 2] * (1 - w));
                int blue = (int) (fixedRGB[i * 3 + 2] * w + fixedRGB[i * 3 - 1] * (1 - w));
                paint.setColor(Color.rgb(red, green, blue));
                return;
            }

    }

    private void drawEdge(Canvas canvas, float sweepAngle, boolean right) {
        float xx, yy, rr;
        float temp = 270;
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        /*
        if(right  == true){
			temp =temp + 1;
			selecteColor(sweepAngle+1, paint);
			}
		else {
			temp =temp +1;
			selecteColor(sweepAngle+1, paint);
			}
			*/
        xx = (float) (x + r * Math.cos((temp + sweepAngle) / 180 * Math.PI));
        yy = (float) (y + r * Math.sin((temp + sweepAngle) / 180 * Math.PI));
        rr = paintWidth;
        //Shader shader = new SweepGradient(x, y,color,fixedColorWeight);
        Shader shader = new LinearGradient(x - r, y + r, x + r, y - r,
                getResources().getColor(R.color.public_main_b_color),
                getResources().getColor(R.color.public_main_a_color), Shader.TileMode.MIRROR);
        paint.setShader(shader);
        canvas.drawCircle(xx, yy, rr, paint);
    }


    private void drawText(Canvas canvas) {
        Paint paint = new Paint();
        float strLen;
        String doc = getResources().getString(R.string.home_circle_doc);
        int lowScore = getResources().getColor(R.color.public_caution_color);
        int highScore = getResources().getColor(R.color.public_main_b_color);
        int fen = getResources().getColor(R.color.home_fen_color);
        int docColor = getResources().getColor(R.color.public_main_b_color);

        paint.setTextSize(textSize);

        paint.setTypeface(App.Typeface_arial);
        paint.setAntiAlias(true);
        if (score < 70)
            paint.setColor(lowScore);
        else
            paint.setColor(highScore);
        strLen = paint.measureText(scoreString);
        canvas.drawText(scoreString, x - strLen * 23 / 40, y + textSize / 5f, paint);

        paint.setColor(fen);
        paint.setTextSize(textSize / 3.5f);
        canvas.drawText("分", x - strLen * 23 / 40 + strLen, y + textSize / 5f, paint);//��ͼ���ڳ����Ĳ���

        float temp = (textSize);
        float docTextsize = temp / 4.8f;
        paint.setTextSize(docTextsize);
        paint.setColor(docColor);
        strLen = paint.measureText(doc);
        float yy = y + docTextsize + textSize / 4.5f + docTextsize / 3;
        canvas.drawText(doc, x - strLen / 2, yy, paint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        setMeasuredDimension((int) (wr + wr + paintWidth * 2 + 0.5f), (int) (wr + wr + paintWidth * 2 + 0.5f));//Ԥ��һ�㳤�ȡ�
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);

        Paint paint = new Paint();
        paint.setStyle(Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(paintWidth);
        RectF rectf = new RectF(x - r, y - r, x + r, y + r);
        //Shader shader = new SweepGradient(x, y,color,fixedColorWeight);
        Shader shader = new LinearGradient(x - r, y + r, x + r, y - r,
                getResources().getColor(R.color.public_main_b_color),
                getResources().getColor(R.color.public_main_a_color), Shader.TileMode.MIRROR);
        paint.setShader(shader);
        canvas.drawArc(rectf, 270, sweepAngle, false, paint);

        drawEdge(canvas, sweepAngle, true);
        drawText(canvas);

    }


    public void start() {
        handler = new Handler();
        sweepAngle = 0;
        score = 0;
        accurateScore = 0;
        runnabel = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (maxScore == -1 || maxScore == 0)
                    scoreString = "无";
                else
                    scoreString = String.valueOf(score);
                invalidate();
                if (sweepAngle == maxSweepAngle)
                    handler.removeCallbacks(this);

                else
                    handler.postDelayed(this, 2);


                sweepAngle += maxSweepAngle / (time / 5);
                accurateScore = accurateScore + (float) (maxScore) / (time / 5);
                score = (int) accurateScore;
                if (sweepAngle > maxSweepAngle) {
                    sweepAngle = maxSweepAngle;
                    score = maxScore;
                }

            }

        };
        handler.postDelayed(runnabel, 4);
    }

}
