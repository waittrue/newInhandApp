package com.inhand.milk.fragment.temperature_amount.details;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PinnedHeaderListView  extends ListView{
	
	private  boolean drawHead = true;
	private View headView;
	private PinnedListViewAdapter myAdapter;
	private int mMeasureWidth;
	private int mMeasureHeight;
	
	
	public PinnedHeaderListView(Context context) {
		super(context);
	}
	
	public PinnedHeaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public PinnedHeaderListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		super.setAdapter(adapter);
		myAdapter =	(PinnedListViewAdapter) adapter;
	}

	public void setHeadView(View v){
		headView = v;
		Log.i("setHeadView", "1111111");
		requestLayout();
	}
	
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.i("onmeasure", "1111111");
		if ( headView != null){
			measureChild(headView, widthMeasureSpec, heightMeasureSpec);
			mMeasureHeight = headView.getMeasuredHeight();
			mMeasureWidth  = headView.getMeasuredWidth();
		}
			
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		Log.i("onlayout", "1111111");
		if (headView != null){
			controlHeadView( getFirstVisiblePosition() );
		}
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.dispatchDraw(canvas);
		Log.i("dispatchDraw", "1111111");
		if (headView != null && drawHead){
			drawChild(canvas, headView, getDrawingTime());
			controlHeadView( getFirstVisiblePosition() );
		}
	}
	
	private void controlHeadView(int position){
		if (headView == null){
			drawHead = false;
			return ;
		}
		int state = myAdapter.getHeadViewState(position);
		
		switch (state) {
		case HeadViewManager.HEAD_VIEW_GONE:
			 drawHead = false;
			 break;	 
		case HeadViewManager.HEAD_VIEW_VISIBLE:
			 myAdapter.configureHeadView(headView, position);	
			 headView.layout(0, 0, mMeasureWidth, mMeasureHeight);
			 drawHead = true;
			 break;
		case HeadViewManager.HEAD_VIEW_MOVE:
		     myAdapter.configureHeadView(headView, position);	
			 drawHead = true;
			 View child =  getChildAt(0);
			 if (child != null){
				 int bottom = child.getBottom();
				 int height = headView.getHeight();//����Ҫ�� ���ⳤ��   С��   ���ݳ���
				 int y = bottom - height;
				 if ( y > 0 )
					 y = 0;
				 headView.layout(0, y, mMeasureWidth, mMeasureHeight + y);
			 }
				 
			break;
		}
	}
	

	public interface HeadViewManager{
		public static final int HEAD_VIEW_GONE = 0;
		public static final int HEAD_VIEW_VISIBLE = 1;
		public static final int HEAD_VIEW_MOVE = 2;
		int getHeadViewState(int position);
		void configureHeadView(View view, int position);
	}
}
