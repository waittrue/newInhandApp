package com.inhand.milk.fragment.temperature_amount;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.view.View;

import com.inhand.milk.R;

public class TempratureCircle extends View{
	private float wr ;
	private float lineWidth ;
	private float width ;
	private float  r;
	private float x1,x2,y1,y2;
	private float bottomTextSize ;

	public TempratureCircle(Context context,float rr,float line) {
		super(context);
		lineWidth = getResources().getDimension(R.dimen.temperature_circle_line_width);
		bottomTextSize = getResources().getDimension(R.dimen.temperature_circle_bottom_text_size);
		
		wr = rr;
		width = rr*4 +line;
		r = wr - lineWidth/2;
		x1 = wr;
		y1 = wr;
		x2 = width - wr;
		y2 = wr;
		
		max_score1 = 39;
		max_sweepAngle1 = (float)max_score1/60 * 360;
		max_score2 = 33;
		max_sweepAngle2 = (float)max_score2 /60 *360;
		sweepAngle1 = max_sweepAngle1;
		sweepAngle2 = max_sweepAngle2;
		num1 = String.valueOf(max_score1);
		num2 = String.valueOf(max_score2);
		// TODO Auto-generated constructor stub
	}
	public void setMaxTemperature(int maxTemperature){
		max_score1 = maxTemperature;
		max_sweepAngle1 = (float)max_score1/60 * 360;
	}
	
	public void setMinTemperature(int minTemperature) {
		max_score2 = minTemperature;
		max_sweepAngle2 = (float)max_score2 /60 *360;
	}
	private void drawText(Canvas canvas){
		
		Paint paint = new Paint();
		float numsize = (float)2*wr*13/(22+26);
		paint.setColor(Color.WHITE);
		paint.setTextSize(numsize);
		paint.setAntiAlias(true);

        String text = "C°";
		String text11 = getResources().getString(R.string.temerature_circle_bottome_left_title);
		String text22 = getResources().getString(R.string.temerature_circle_bottome_right_title);
		float leftStringLength = paint.measureText(num1);
		float rightStringLength = paint.measureText(num2);
		canvas.drawText(num1, x1-leftStringLength/2, y1+numsize/4, paint);
		canvas.drawText(num2, x2-rightStringLength/2, y2+numsize/4, paint);
		paint.setTextSize(numsize/2);
		canvas.drawText(text, x1+leftStringLength/2, y1+numsize/4, paint);
		canvas.drawText(text, x2+rightStringLength/2, y2+numsize/4, paint);
		
		paint.setTextSize(bottomTextSize);
		float  offsetx = paint.measureText(text11)/2;
		canvas.drawText(text11, x1 - offsetx, y1+wr+bottomTextSize, paint);
		canvas.drawText(text22, x2 - offsetx, y2+wr+bottomTextSize, paint);
		
		
		
	}
	
	private void drawBackground(Canvas canvas){
		Paint paint = new Paint();
		int color = getResources().getColor(R.color.temperature_circle_line_background_color);
		paint.setColor( color );
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(lineWidth);
		paint.setAlpha(0x5f);
		RectF rectf =  new RectF(x1 - r,y1 - r,x1 + r,y1 + r);
		canvas.drawArc(rectf,0,360,false, paint);
		rectf =  new RectF(x2 - r, y2 - r, x2 + r, y2 +r);
		canvas.drawArc(rectf,0,360,false, paint);
		canvas.drawLine(2*wr, wr, width-2*wr, wr, paint);
	}
	
	protected void onDraw(Canvas canvas){
		drawBackground(canvas);
		drawText(canvas);
		drawCircle(canvas);
		
	}
	
	private void drawCircle(Canvas canvas){
		Paint paint = new Paint();
		int colorLeft = getResources().getColor(R.color.temperature_circle_left_line_color);
		int colorRight = getResources().getColor(R.color.temperature_circle_right_line_color);
		paint.setColor( colorLeft );
		paint.setStyle(Style.STROKE); 
		paint.setAntiAlias(true);
		paint.setStrokeWidth(lineWidth);
		RectF rectf =  new RectF(x1-r,y1-r,x1+r,y1+r);
		canvas.drawArc(rectf,270,sweepAngle1,false, paint);
		
		
		paint.setColor(colorRight);
	    rectf =  new RectF(x2-r,y2-r,x2+r,y2+r);
		canvas.drawArc(rectf,270,sweepAngle2,false, paint);
		
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension((int)width, (int)( wr+wr+wr*14/30 ));//����һ�㡣��ʵ��Ч��
	}
	
	private  float sweepAngle1,max_sweepAngle1,sweepAngle2,max_sweepAngle2;
	private  int score1 ,max_score1,score2,max_score2;
	private  float  score_float1,score_float2;
	Handler handler ;
	private int  time;
	String num1,num2;
	
	
	public void start(){
        if (handler == null)
		    handler = new Handler();
		sweepAngle1 = 0;
		score1 = 0;
		score_float1 = 0 ;
		time = 100;
		sweepAngle2 = 0;
		score2 = 0;
		score_float2 = 0 ;
	
		Runnable runnabel = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 num1 = String.valueOf(score1);
				 num2 = String.valueOf(score2);
				invalidate();
			//	Toast.makeText(getContext(), String.valueOf(score1), Toast.LENGTH_SHORT).show();
				if (sweepAngle1 == max_sweepAngle1 && sweepAngle2 == max_sweepAngle2)
					handler.removeCallbacks(this);
				else 
					handler.postDelayed(this, 2);
				    
				 sweepAngle1 += max_sweepAngle1 / (time/2);
				 sweepAngle2 += max_sweepAngle1 / (time/2);
			     score_float1 =  score_float1 + (float)(max_score1)/(time/2);
			     score_float2 =  score_float2 + (float)(max_score1)/(time/2);
			     score1 = (int)score_float1;
			     score2 = (int)score_float2;
			     if (sweepAngle1 > max_sweepAngle1 || score1 > max_score1){
						sweepAngle1 = max_sweepAngle1;
						score1 = max_score1;
				 }
				
			     if (sweepAngle2 > max_sweepAngle2 || score2 > max_score2){
						sweepAngle2 = max_sweepAngle2;
						score2 = max_score2;
				 }
			 }
		};
		handler.postDelayed(runnabel, 2);
		
	}
}
