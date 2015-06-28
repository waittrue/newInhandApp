package com.inhand.milk.utils.firstlanunch;

import com.inhand.milk.R;
import com.inhand.milk.R.color;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class SmallDotsTab extends View {
	private float dotsR;
	private float dotsSpacing;
	private int dotsNum;
	private int currentIndex = 0;
	private Canvas mCanvas;
	public SmallDotsTab(Context context,int num,float r, float spacing) {
		// TODO Auto-generated constructor stub
		super(context);
		dotsR = r;
		dotsSpacing = spacing;
		dotsNum = num;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension( (int) ((dotsNum -1)*dotsSpacing + dotsNum * 2 *dotsR + 1), 
					(int)( 2 *dotsR +1));//Ԥ��һ�㳤�ȡ�
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		mCanvas = canvas;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		for (int i=0;i<dotsNum ;i++){
			if (i == currentIndex){
				paint.setColor(getResources().getColor(R.color.small_dots_select_color));
			}
			else {
				paint.setColor(getResources().getColor(R.color.small_dots_noselect_color));
			}
			drawDot(i, paint);
		}
	}
	private void drawDot(int index, Paint paint){
		
		float x  = dotsR + index * dotsSpacing + index * 2* dotsR;
		mCanvas.drawCircle(x, dotsR+0.5f, dotsR, paint);
	}
	
	
	public void setNextDots(){
		currentIndex++;
		if (currentIndex >= dotsNum )
			currentIndex--;
		else 
			invalidate();
	}
	public void setPreDots(){
		currentIndex--;
		if (currentIndex < 0 )
			currentIndex = 0;
		else 
			invalidate();
	}
	
}
