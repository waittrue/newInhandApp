package com.inhand.milk.utils.firstlanunch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.inhand.milk.R;

public class Ruler extends View {

    private int startNum;
    private int endNum;
    private int spacing, spacingnum;
    private float width, height;
    private boolean vertical;
    private int offsetstart;
    private float lineShortLen, lineLongLen, lineMiddleLen;
    private float textsize = 20;
    private int totalNum;

    public Ruler(Context context, float containerSize, float w, float h, int s, int e, int spa, int spanum, boolean v) {
        super(context);
        // TODO Auto-generated constructor stub
        if (s > e) {
            Log.i("ruler", "s > e");
            return;
        }
        if (spanum > e - s) {
            Log.i("ruler", "spanum > e-s");
            return;
        }
        startNum = s;
        spacing = spa;
        spacingnum = spanum;
        totalNum = (int) ((e - s) / spacingnum + 0.5f);
        endNum = totalNum * spacingnum + startNum;
        if (w == 0) {
            width = totalNum * spacing;
            height = h;
            lineShortLen = h / 4;
            lineLongLen = h / 4 + h / 8;
        } else if (h == 0) {
            height = totalNum * spacing;
            width = w;
            lineShortLen = w / 4;
            lineLongLen = w / 4 + w / 8;
        }
        lineMiddleLen = (lineLongLen + lineShortLen) / 2;
        vertical = v;
        offsetstart = (int) containerSize / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        if (vertical)
            setMeasuredDimension((int) width + offsetstart * 2 + 1, (int) height + 1);
        else
            setMeasuredDimension((int) width + 1, (int) (height + offsetstart * 2 + 1));
    }

    @Override

    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        Paint paint = new Paint();

        paint.setColor(getResources().getColor(R.color.ruler_color));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(spacing / 6);
        Log.i("ruler", String.valueOf(totalNum));
        if (vertical) {
            for (int i = 0; i <= totalNum; i++) {
                drawLine(canvas, offsetstart + i * spacing, 0, paint);
            }
        } else {
            for (int i = 0; i <= totalNum; i++) {
                drawLine(canvas, (int) width, offsetstart + i * spacing, paint);
            }
        }
    }

    private void drawLine(Canvas canvas, int x, int y, Paint paint) {
        Log.i("ruler", String.valueOf(vertical));
        if (vertical) {
            if ((x - offsetstart) % (spacing * 10) == 0) {
                paint.setAlpha(255);
                canvas.drawLine(x, y, x, y + lineLongLen, paint);
                String num = String.valueOf((x - offsetstart) / spacing * spacingnum / 10);
                paint.setTextSize(textsize);
                float offset = paint.measureText(num);
                canvas.drawText(num, x - offset / 2, y + lineLongLen + textsize, paint);
            } else if ((x - offsetstart) % (spacing * 5) == 0) {
                paint.setAlpha(125);
                canvas.drawLine(x, y, x, y + lineMiddleLen, paint);
            } else {
                paint.setAlpha(125);
                canvas.drawLine(x, y, x, y + lineShortLen, paint);
            }

        } else {

            if ((y - offsetstart) % (spacing * 10) == 0) {
                canvas.drawLine(x, y, x - lineLongLen, y, paint);
                paint.setAlpha(255);
                String num = String.valueOf((y - offsetstart) / spacing * spacingnum / 10);
                paint.setTextSize(textsize);
                float offset = paint.measureText(num);
                canvas.drawText(num, x - lineLongLen - textsize, y - offset / 2, paint);
            } else if ((y - offsetstart) % (spacing * 5) == 0) {
                paint.setAlpha(125);
                canvas.drawLine(x, y, x - lineMiddleLen, y, paint);
            } else {
                paint.setAlpha(125);
                canvas.drawLine(x, y, x - lineShortLen, y, paint);
            }

        }
    }


}