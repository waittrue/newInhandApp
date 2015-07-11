package com.inhand.milk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/7/2.
 */
public class CircleImageView extends ImageView{
    private int boundWidth = 10,boundColor = Color.GREEN;
    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setScaleType(ScaleType.CENTER_CROP);
        Drawable drawable = getDrawable();
        if (null == drawable) {
            return;
        }
        Bitmap bitmap = drawableToBitmap(drawable);

        float cx = getWidth() / 2;
        float cy = getHeight() / 2;

        float radius = Math.min(getWidth(), getHeight()) / 2;

        Paint borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(boundColor);

        canvas.drawCircle(cx, cy, radius, borderPaint);


        Paint paint = new Paint();
        paint.setAntiAlias(true);
        int bitmapRadius = (int)radius*2 - boundWidth;
        bitmap = getCroppedBitmap(bitmap,bitmapRadius);
        canvas.drawBitmap(bitmap,cx- bitmapRadius/2,cy - bitmapRadius/2,paint);

    }
    public  Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);


        return bitmap;
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap p;
        //判断图片的大小与传入radius是否相等，如果不相等，那么
        //将图片设置成长 宽都是radius的图片
        if(bmp.getWidth() != radius || bmp.getHeight() != radius)
            p = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            p = bmp;
        //最后输出的图片信息
        Bitmap output = Bitmap.createBitmap(p.getWidth(),
                p.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, p.getWidth(), p.getHeight());
        //画笔加上  抗锯齿标志，图像更加平滑
        paint.setAntiAlias(true);
        //如果该项设置为true,则图像在动画进行中会滤掉对Bitmap图像的优化操作,加快显示
        paint.setFilterBitmap(true);
        //防抖动
        paint.setDither(true);
// 透明色
        canvas.drawARGB(0, 0, 0, 0);
        //画笔的颜色
        paint.setColor(Color.parseColor("#BAB399"));
        //画出一个圆形
        canvas.drawCircle(p.getWidth() / 2+0.7f, p.getHeight() / 2+0.7f,
                p.getWidth() / 2+0.1f, paint);
        //设置两张图片相交时的模式 ，就是在画布上遮上圆形的图片信息
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(p, rect, rect, paint);
        return output;
    }
}
