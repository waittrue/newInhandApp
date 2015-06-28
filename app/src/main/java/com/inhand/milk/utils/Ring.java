package com.inhand.milk.utils;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.view.View;

public class Ring extends View{

	private float sweepAngle = 0;
	private Handler handler ;
	private Runnable runnabel;
	private int  time;
    private boolean first = true;
	private float  maxSweepAngle;
	private float x,y,r,wr,paintWidth;
	private int backgroundColor;
	
	private  float[] originalColorWeight = {0, 0.33f, 0.66f, 1.0f};//&#x4bb;&#xfffd;&#xfffd;&#xfffd;&#xfffd;&#xfffd;&#xfffd;&#xfffd;&#xfffd;0.25&#xfffd;&#xfffd;&#xfffd;&#xfffd;&#xfffd;&#xfffd;&#xfffd;&#xfffd;&#xfffd;&#xfffd;&#xfffd;&#x133;&#xfffd;0.245
	private   int[] originalRGB  = {0x04, 0x98, 0xa2,
								 0x02, 0xb9, 0xb4,
								 0x05,0x7e,0x9b,
								 0x05, 0x7e, 0x9b} ;
	private int[]   color;
	private float[] fixedColorWeight ;
	private int[]   fixedRGB;
	
	public Ring(Context context,float rr,int color) {
		super(context);
		// TODO Auto-generated constructor stub
		time = 10000;
        handler = new Handler();
		maxSweepAngle = 360;

		x=rr;//Բ��
		y=rr;//Բ��
	    wr = rr;//�뾶 ��Ȧ
	    paintWidth = wr /8;//��Բ��ʱ��ıʵĿ��
	    x = wr+paintWidth;//Ԥ����һ��width�ĳ���
	    y = wr+paintWidth;
		r = wr - paintWidth/2;
		backgroundColor = color;
		fixColor();
		sortWeight();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension( (int) (wr+wr+paintWidth*2 + 0.5f), (int)(wr+wr+paintWidth*2 + 0.5f));
	}
	
	
	public void setColors(int[] colors , float[] colorsWeight){
		if (colors.length/3 != colorsWeight.length)
			return;
		originalColorWeight = colorsWeight;
		originalRGB = colors;
		fixColor();
		sortWeight();
	}

	public void setAnimalTime(int t){
		time = t;
	}
	
	

	private void drawBackground(Canvas canvas){
		Paint paint  = new Paint();
		paint.setAlpha(255);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(paintWidth);
		
		paint.setColor(backgroundColor);
		paint.setStyle(Style.STROKE ); 
		canvas.drawCircle(x, y, r, paint);

	}
	private void fixColor(){
		int i,tempi;
		boolean a;
		float temp,w;
		a =  false;
		fixedColorWeight = new float[originalColorWeight.length+2];
		fixedRGB  = new int[3*(originalColorWeight.length+2)];
		for(i=0; i<originalColorWeight.length+2; i++){
			if(a ==false)
				temp = originalColorWeight[i] - (float)0.25;
			else if(i == originalColorWeight.length+2-1){
				temp = originalColorWeight[i-2]-(float)0.001-(float)0.25;
			}
			else
				temp = originalColorWeight[i-2]-(float)0.25;
			if(temp < 0 ){
				fixedColorWeight[i] = temp+1;
				fixedRGB[i*3] = originalRGB[i*3];
				fixedRGB[i*3+1] = originalRGB[i*3+1];
				fixedRGB[i*3+2] = originalRGB[i*3+2];
			}
			else if(temp >= 0 && a ==false){
				w =  originalColorWeight[i] - originalColorWeight[i-1];
				w =  ((float)0.25 - originalColorWeight[i-1])/w;
				for(tempi = i ;tempi <i+2 ;tempi++){	
					fixedRGB[tempi*3] = (int)(originalRGB[3*(i-1)]*(1-w) +originalRGB[3*i]*w);
					fixedRGB[tempi*3+1] =(int) (originalRGB[3*(i-1)+1]*(1-w) +originalRGB[3*i+1]*w);
					fixedRGB[tempi*3+2] = (int)(originalRGB[3*(i-1)+2]*(1-w) +originalRGB[i*3+2]*w);
					}
				fixedColorWeight[i]= 0;
				fixedColorWeight[i+1] = 1;
				i= i+2;
				
				
				fixedColorWeight[i] = temp;
				fixedRGB[i*3] = originalRGB[(i-2)*3];
				fixedRGB[i*3+1] = originalRGB[(i-2)*3+1];
				fixedRGB[i*3+2] = originalRGB[(i-2)*3+2];
				
				a = true;
			}
			else {
				fixedColorWeight[i] = temp;
				fixedRGB[i*3] = originalRGB[(i-2)*3];
				fixedRGB[i*3+1] = originalRGB[(i-2)*3+1];
				fixedRGB[i*3+2] = originalRGB[(i-2)*3+2];
			}
		}
		
	}
	private void sortWeight(){
		int i,j;
		float temp;
		int colortemp;
		for(i=0;i<fixedColorWeight.length-1;i++){
			for(j=i+1;j<fixedColorWeight.length;j++){
				if(fixedColorWeight[j] < fixedColorWeight[i]){
					temp  = fixedColorWeight[i];
					fixedColorWeight[i] = fixedColorWeight[j];
					fixedColorWeight[j] = temp;
					
					colortemp =  fixedRGB[3*i];
					fixedRGB[3*i] = fixedRGB[3*j];
					fixedRGB[3*j] = colortemp;
					
					colortemp =  fixedRGB[3*i+1];
					fixedRGB[3*i+1] = fixedRGB[3*j+1];
					fixedRGB[3*j+1] = colortemp;
					
					colortemp =  fixedRGB[3*i+2];
					fixedRGB[3*i+2] = fixedRGB[3*j+2];
					fixedRGB[3*j+2] = colortemp;
				}
						
			}
		}
		
		color = new int[originalColorWeight.length+2];
		for(i=0;i<originalColorWeight.length+2;i++)
			color[i] = (int)Color.rgb(fixedRGB[i*3], fixedRGB[i*3+1], fixedRGB[i*3+2]);
	}
	
	private void selecteColor(float sweepAngle,Paint paint){
		if(sweepAngle - 90 < 0) 
				sweepAngle = sweepAngle +270;
		else 
			sweepAngle = sweepAngle - 90;
		
		float weight =  sweepAngle / 360;
		
		for(int i= 1; i<fixedColorWeight.length ;i++)
		if(weight <= fixedColorWeight[i] && weight >=fixedColorWeight[i-1]){
			float w = ( weight-fixedColorWeight[i-1] )/(fixedColorWeight[i] - fixedColorWeight[i-1]);
			int  red  =(int) (fixedRGB[i*3] * w +fixedRGB[i*3-3]*(1-w));
			int  green = (int) (fixedRGB[i*3+1] * w +fixedRGB[i*3-2]*(1-w));
			int  blue = (int) (fixedRGB[i*3+2] * w +fixedRGB[i*3-1]*(1-w));
			paint.setColor(Color.rgb(red, green, blue));
			return;
		}
		
	}
	private  void drawEdge(Canvas canvas,float sweepAngle,boolean end){
		float xx,yy,rr;
		float temp = 270 ;
		Paint paint =  new Paint();
		paint.setStrokeWidth(1);
        paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
        if (end == true)
            sweepAngle = sweepAngle -1;
        else
            sweepAngle = sweepAngle +1 ;
		selecteColor(sweepAngle, paint);

		xx = (float)(  x+ r * Math.cos(  (temp+sweepAngle) / 180 * Math.PI ) );
		yy = (float)(  y+ r * Math.sin(   (temp+sweepAngle) / 180 * Math.PI ) ) ;
		rr =  paintWidth/2;
        RectF rectF = new RectF(xx-rr,yy-rr,xx+rr,yy+rr);
        if (end == true)
		    canvas.drawArc(rectF,temp+sweepAngle,180,false,paint);
        else
            canvas.drawArc(rectF,temp+sweepAngle,-180,false,paint);
	}
	
	protected void onDraw(Canvas canvas){
		drawBackground(canvas);
	
		Paint paint = new Paint();
		paint.setStyle(Style.STROKE); 
		paint.setAntiAlias(true);
		paint.setStrokeWidth(paintWidth);
		RectF rectf =  new RectF(x-r,y-r,x+r,y+r);
		Shader shader = new SweepGradient(x, y,color,fixedColorWeight);
		paint.setShader(shader);
		canvas.drawArc(rectf,270,sweepAngle,false, paint);
        if (!first){
		    drawEdge(canvas,sweepAngle,true);
            drawEdge(canvas,0,false);
        }
	}


	public void start(){

        first = false;
		sweepAngle = 0;
		runnabel = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				invalidate();
				if (sweepAngle == maxSweepAngle)
					handler.removeCallbacks(this);
				
				else 
					handler.postDelayed(this, 2);
				 sweepAngle  += maxSweepAngle / (time/2);
			     if (sweepAngle > maxSweepAngle){
						sweepAngle = maxSweepAngle;
                        first = true;
				 }
			}
		};
		handler.postDelayed(runnabel, 2);
	}
    public void accelerateAnimator( ){
        first = false;
        handler.removeCallbacks(runnabel);
        runnabel = new Runnable() {
        float temp  = maxSweepAngle - sweepAngle;
            @Override
            public void run() {
                // TODO Auto-generated method stub
                invalidate();
                if (sweepAngle == maxSweepAngle)
                    handler.removeCallbacks(this);
                else
                    handler.postDelayed(this, 2);
                sweepAngle  += temp /(100/2);
                if (sweepAngle > maxSweepAngle){
                    sweepAngle = maxSweepAngle;
                    first = true;
                }
            }
        };
        handler.postDelayed(runnabel, 2);
    }
}

