package com.inhand.milk.fragment.temperature_amount;

import com.inhand.milk.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.view.View;

public class MilkCircle extends View{

	private float wr ;//pix
	private float lineWidth ;
	private float width ;//pix
	private float  r;//pix
	private float x,y;
	private float lineToCircle  ;
	private float lineSweepAngleOffset ;//��λ��

	private int   currentAmount=200;
	private int   adviseAmount =300;
	private float BottomNumSize ;
	private Paint paint =new Paint();
	
	public MilkCircle(Context context,float rr) {
		super(context);	
		lineWidth = getResources().getDimension(R.dimen.milk_circle_line_width);
		lineToCircle = getResources().getDimension(R.dimen.milk_circle_line_to_circle);
		
		wr = rr;
		width = rr*2 + lineToCircle *2; 
		r = wr - lineWidth/2;
		x = wr + lineToCircle;
		y = wr + lineToCircle;
		BottomNumSize = 2*wr/8;
		
		max_score = (int)((float)currentAmount/adviseAmount*100);
		num = String.valueOf(max_score)+"%";
		paint.setTextSize(BottomNumSize);
		lineSweepAngleOffset =  (float)  (Math.atan( paint.measureText(num)/2/(r + lineToCircle) ) /Math.PI* 180  +2);//��2���е��϶
		max_sweepAngle = (float)currentAmount/adviseAmount * (360 - 2*lineSweepAngleOffset);
		sweepAngle = max_sweepAngle;
	}
	
	public void setCurrentAmount(int cAmount){
		currentAmount = cAmount;
		if (adviseAmount !=0)
			max_score = (int)((float)currentAmount/adviseAmount*100);
	}
	public void setAdviseAmount(int adAmount){
		adviseAmount = adAmount;
		if (adviseAmount !=0)
			max_score = (int)((float)currentAmount/adviseAmount*100);
	}
    private void drawText(Canvas canvas){
        Paint paint = new Paint();
        float numsize = 2*wr/10;
        paint.setColor(Color.WHITE);
        paint.setTextSize(numsize);
        paint.setAntiAlias(true);

        String textup = "建议量: "+String.valueOf(adviseAmount)+"ml";
        String textdown = "当前量: "+String.valueOf(currentAmount)+"ml";
        canvas.drawText(textup, x - paint.measureText(textup)/2, y - numsize/3, paint);
        canvas.drawText(textdown, x - paint.measureText(textdown)/2, y + numsize/3 + numsize, paint);
    }
	
	private void drawBackground(Canvas canvas){
		Paint paint = new Paint();
		int color = getResources().getColor(R.color.milk_circle_double_line_color);
		paint.setColor( color );
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(lineWidth);
		RectF rectf =  new RectF(x - r,y - r,x + r,y + r);
		canvas.drawArc(rectf,0,360,false, paint);
		
	}
	
	protected void onDraw(Canvas canvas){
		drawBackground(canvas);
		drawText(canvas);
		//drawCircle(canvas);
		drawDoubleLine(canvas);
		drawBottomText(canvas);
		
	}
	
	private void drawDoubleLine(Canvas canvas){
		float rr;
		int color = getResources().getColor(R.color.milk_circle_double_line_color);
		Paint paint = new Paint();
		paint.setColor( color );
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(lineWidth);
		
		rr = r + lineToCircle;
		RectF rectf =  new RectF(x - rr,y - rr,x + rr,y + rr);	
		canvas.drawArc(rectf, 90 - lineSweepAngleOffset, -sweepAngle/2,false, paint);
		canvas.drawArc(rectf, 90 + lineSweepAngleOffset, sweepAngle/2,false, paint);
		
	}
	private void drawBottomText(Canvas canvas){
		float xx,yy;
		if( !num.endsWith("%"))
			num = num +"%";
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(lineWidth);
		paint.setTextSize(BottomNumSize);
		xx = x - paint.measureText(num)/2;
		yy = y + (r + lineToCircle) *(float) Math.cos(lineSweepAngleOffset/180 *Math.PI) + BottomNumSize/2;
		canvas.drawText(num, xx, yy, paint);
		
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension((int)( width ), (int)( (wr+wr+wr*14/30) + lineToCircle));//����һ�㡣��ʵ��Ч��
	}
	
	private  float sweepAngle,max_sweepAngle;
	private  int score ,max_score;
	private  float  score_float;
	private Handler handler ;
	private int  time;
	private String num;
	private Runnable runnabel;
	
	public void start(){	
		handler = new Handler();
		sweepAngle = 0;
		score = 0;
		score_float = 0;
		time = 100;
		runnabel = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				num = String.valueOf(score);
				invalidate();
				if (sweepAngle== max_sweepAngle )
					handler.removeCallbacks(this);
				
				else 
					handler.postDelayed(this, 2);
				    
			 
				 sweepAngle += max_sweepAngle / (time/2);
			     score_float=  score_float + (float)(max_score)/(time/2);	     
			     score = (int)score_float;
			     if (sweepAngle > max_sweepAngle || score > max_score){
						sweepAngle = max_sweepAngle;
						score = max_score;
				 }
				
				}
		};
		handler.postDelayed(runnabel, 2);	
	}
	public void stop(){
		if (runnabel != null)
			handler.removeCallbacks(runnabel);
	}
	
}
