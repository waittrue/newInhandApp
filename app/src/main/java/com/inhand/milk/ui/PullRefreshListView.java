package com.inhand.milk.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inhand.milk.App;
import com.inhand.milk.R;

public class PullRefreshListView extends ListView implements AbsListView.OnScrollListener {

    private static final int TAP_TO_REFRESH = 1;
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    private static final int REFRESHING = 4;
    private static final int DOWN_PULL = 5;
    private static final String TAG = "PullRefreshListView";
    private static final String LOADING_STRING = "加载中... ", DOWN_REFRESH_STRING = "下拉刷新",
            RELASE_REFRESH_STRING = "释放刷新";
    private static final int LOADING_DRAW = R.drawable.ic_launcher;
    private static final int PULL_DRAW = R.drawable.pullto_refresh_icon;
    private static final int SMOOTH_SCROLL_TIME = 1000;
    private final Handler scrollHander = new Handler();
    private int mCurrentScrollState;
    private int mRefreshState;
    private RelativeLayout mRefreshView;
    private RelativeLayout mBottomRefreshView;
    private TextView mRefreshViewText, mBottomRefreshText;
    private ImageView mRefreshViewImage, mBottomRefreshImage;
    private RotateAnimation mFlipAnimation;
    private RotateAnimation mReverseFlipAnimation;
    private RotateAnimation mRotation;
    private int mRefreshViewHeight, mBottomViewHeight, mBottomInnormalHeight;
    private OnScrollListener mOnScrollListener;
    private int mLastMotionY;
    private boolean normalFooter = true;
    private OnRefreshListener mOnRefreshListener;
    private boolean ableRefrsh = true;
    private boolean dataChanged = true;
    private boolean smoothScroll;

    public PullRefreshListView(Context context) {
        super(context);
        init();
    }

    public PullRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public PullRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PullRefreshListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mFlipAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(250);
        mFlipAnimation.setFillAfter(true);


        mReverseFlipAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(250);
        mReverseFlipAnimation.setFillAfter(true);

        mRotation = new RotateAnimation(0, 359,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mRotation.setInterpolator(new LinearInterpolator());
        mRotation.setDuration(1000);
        mRotation.setFillAfter(true);
        mRotation.setRepeatCount(-1);


        mRefreshState = TAP_TO_REFRESH;
        super.setOnScrollListener(this);

        setHeadView();
        measureView(mRefreshView);
        mRefreshViewHeight = mRefreshView.getMeasuredHeight();

        setFooterView();
        measureView(mBottomRefreshView);
        mBottomViewHeight = mBottomRefreshView.getMeasuredHeight();

        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private void setHeadView() {
        int width = App.getWindowWidth(getContext());
        int height = App.getWindowHeight(getContext());
        int parentHeight = height / 6;
        int imageHeight = parentHeight / 3;
        int textSize = imageHeight / 2;
        mRefreshView = new RelativeLayout(getContext());
        // mRefreshView.setBackgroundColor(Color.RED);

        AbsListView.LayoutParams mRefreshViewLP = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mRefreshView.setMinimumHeight(parentHeight);
        // Log.i(TAG, "height" + String.valueOf(parentHeight));
        mRefreshView.setLayoutParams(mRefreshViewLP);

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams linearLayoutLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        linearLayoutLp.bottomMargin = parentHeight / 2 - imageHeight / 2;
        linearLayoutLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mRefreshView.addView(linearLayout, linearLayoutLp);


        mRefreshViewImage = new ImageView(getContext());
        mRefreshViewImage.setImageDrawable(getResources().getDrawable(PULL_DRAW));
        mRefreshViewImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        LinearLayout.LayoutParams imageLP = new LinearLayout.LayoutParams(imageHeight, imageHeight);
        linearLayout.addView(mRefreshViewImage, imageLP);

        mRefreshViewText = new TextView(getContext());
        mRefreshViewText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mRefreshViewText.setText(DOWN_REFRESH_STRING);
        LinearLayout.LayoutParams textLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textLP.gravity = Gravity.CENTER_VERTICAL;
        linearLayout.addView(mRefreshViewText, textLP);

        addHeaderView(mRefreshView);

    }

    private void setFooterView() {
        int width = App.getWindowWidth(getContext());
        int height = App.getWindowHeight(getContext());
        int parentHeight = height / 6;
        int imageHeight = parentHeight / 3;
        int textSize = imageHeight / 2;
        mBottomRefreshView = new RelativeLayout(getContext());
        //mBottomRefreshView.setBackgroundColor(Color.RED);

        AbsListView.LayoutParams mRefreshViewLP = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mBottomRefreshView.setMinimumHeight(parentHeight);
        //    Log.i(TAG, "height" + String.valueOf(parentHeight));
        mBottomRefreshView.setLayoutParams(mRefreshViewLP);

        //���ֺ�ͼƬ�ĸ���ܣ���Ҫ�Ǻ��������
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams linearLayoutLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        linearLayoutLp.topMargin = parentHeight / 2 - imageHeight / 2;
        linearLayoutLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mBottomRefreshView.addView(linearLayout, linearLayoutLp);

        mBottomRefreshImage = new ImageView(getContext());
        mBottomRefreshImage.setImageDrawable(getResources().getDrawable(LOADING_DRAW));
        mBottomRefreshImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        LinearLayout.LayoutParams imageLP = new LinearLayout.LayoutParams(imageHeight, imageHeight);
        linearLayout.addView(mBottomRefreshImage, imageLP);


        mBottomRefreshText = new TextView(getContext());
        mBottomRefreshText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mBottomRefreshText.setText(LOADING_STRING);
        LinearLayout.LayoutParams textLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textLP.gravity = Gravity.CENTER_VERTICAL;
        linearLayout.addView(mBottomRefreshText, textLP);

        addFooterView(mBottomRefreshView);
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,
                0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;

        if (lpHeight > 0) {  //�����ͼ�ĸ߶ȴ���0
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private void onFresh() {
        Log.i(TAG, "onfresh");
        if (mRefreshState != REFRESHING || ableRefrsh == false)
            return;
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.doInBackground();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                // ((BaseAdapter)getAdapter()).notifyDataSetChanged();
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.Refresh();
                }
                //做刷新完成后的工作
                dataChanged = true;
                if (getFirstVisiblePosition() == 0)
                    setSelection(1);
                resetHeaderFooter();
            }
        };
        task.execute();
    }

    public void prepareForRefresh() {
        resetHeaderPadding();
        Log.i(TAG, "prepareForREfresh");
        // We need this hack, otherwise it will keep the previous drawable.
        if (ableRefrsh == true) {
            mRefreshViewImage.setImageDrawable(getResources().getDrawable(LOADING_DRAW));
            mRefreshViewImage.startAnimation(mRotation);
            if (normalFooter)
                mBottomRefreshImage.startAnimation(mRotation);
            // Set refresh view text to the refreshing label
            mRefreshViewText.setText(LOADING_STRING);
            mRefreshState = REFRESHING;
        }

    }

    private void resetHeaderFooter() {
        if (dataChanged) {
            int visibleCount = getChildCount();
            int count = getCount() - 2;
            if (count < visibleCount) {
                normalFooter = false;
                int totalHeight = 0;
                for (int i = count; i >= 1; i--)
                    totalHeight += getChildAt(i).getMeasuredHeight();
                mBottomInnormalHeight = getHeight() - totalHeight;
                Log.i(TAG, String.valueOf(mBottomInnormalHeight));
            } else {
                normalFooter = true;
            }
        }
        dataChanged = false;
        if (normalFooter) {
            resetFooterPadding();
        } else if (ableRefrsh == false) {
            mBottomRefreshView.getLayoutParams().height = mBottomInnormalHeight + mBottomViewHeight;
            mBottomRefreshView.requestLayout();
        } else if (ableRefrsh == true) {
            mBottomRefreshView.getLayoutParams().height = mBottomInnormalHeight;
            mBottomRefreshView.requestLayout();
        }
        resetHeader();

        if (ableRefrsh == true) {
            if (normalFooter == true) {
                mBottomRefreshImage.setVisibility(VISIBLE);
                mBottomRefreshText.setVisibility(VISIBLE);
            } else {
                mBottomRefreshImage.setVisibility(GONE);
                mBottomRefreshText.setVisibility(GONE);
            }
            mRefreshViewImage.setVisibility(VISIBLE);
            mRefreshViewText.setVisibility(VISIBLE);
        } else {
            mBottomRefreshImage.setVisibility(GONE);
            mBottomRefreshText.setVisibility(GONE);
            mRefreshViewImage.setVisibility(GONE);
            mRefreshViewText.setVisibility(GONE);
        }
        mRefreshState = TAP_TO_REFRESH;
        Log.i(TAG, "TAP_TO_REFRESH");
    }

    private void resetHeader() {

        resetHeaderPadding();
        // Set refresh view text to the pull label
        if (ableRefrsh) {
            mRefreshViewText.setText(DOWN_REFRESH_STRING);
            // Replace refresh drawable with arrow drawable
            //���ó�ʼͼƬ
            mRefreshViewImage.setImageResource(R.drawable.pullto_refresh_icon);
            // Clear the full rotation animation
            mRefreshViewImage.clearAnimation();
        }
        // Hide progress bar and arrow.
        // Log.i(TAG,"chu shi hua reset header");
    }

    private void applyHeaderPadding(MotionEvent ev) {

        int pointerCount = ev.getHistorySize();
        for (int p = 0; p < pointerCount; p++) {
            if (ableRefrsh == false || ableRefrsh == true) {
                if (normalFooter || !normalFooter) {
                    if (mRefreshState == RELEASE_TO_REFRESH) {
                        int historicalY = (int) ev.getHistoricalY(p);
                        int topPadding = (int) (((historicalY - mLastMotionY)
                                - mRefreshViewHeight) / 1.7);
                        mRefreshView.getLayoutParams().height = mRefreshViewHeight + topPadding;
                        mRefreshView.requestLayout();
                    }
                }
            }
        }
    }

    private void applyFooterHeight(MotionEvent ev) {
        int pointerCount = ev.getHistorySize();
        int padding;
        for (int p = 0; p < pointerCount; p++) {
            if (!ableRefrsh) {
                if (normalFooter) {
                    if (mRefreshState == DOWN_PULL) {
                        int historicalY = (int) ev.getHistoricalY(p);
                        Log.i(TAG, "footer:" + String.valueOf(historicalY));
                        padding = (int) ((mLastMotionY - historicalY - mBottomViewHeight) / 1.7f);
                        mBottomRefreshView.getLayoutParams().height = mBottomViewHeight + padding;
                        mBottomRefreshView.requestLayout();
                        setSelection(getCount() - 1);
                    }
                } else if (!normalFooter && mRefreshState == DOWN_PULL) {
                    int historicalY = (int) ev.getHistoricalY(p);
                    padding = (int) ((mLastMotionY - historicalY - mBottomViewHeight) / 1.7f);
                    Log.i(TAG, "footer:" + String.valueOf(padding));
                    mBottomRefreshView.getLayoutParams().height = mBottomInnormalHeight + mBottomViewHeight + padding;
                    mBottomRefreshView.requestLayout();
                    Log.i(TAG, "padding:" + String.valueOf(padding));
                    setSelection(getCount() - 1);
                }

            }
        }
    }

    private void resetHeaderPadding() {
        mRefreshView.getLayoutParams().height = mRefreshViewHeight;
        mRefreshView.requestLayout();
    }

    private void resetFooterPadding() {
        mBottomRefreshView.getLayoutParams().height = mBottomViewHeight;
        mBottomRefreshView.requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int y = (int) event.getY();
        if (ableRefrsh == false) {
            if (normalFooter || !normalFooter) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_DOWN:
                        mLastMotionY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        applyHeaderPadding(event);
                        applyFooterHeight(event);
                        break;
                }
            }
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (!isVerticalScrollBarEnabled()) {
                        setVerticalScrollBarEnabled(true);
                    }

                    if (getFirstVisiblePosition() == 0 && mRefreshState != REFRESHING) {
                        if ((mRefreshView.getBottom() >= mRefreshViewHeight
                                || mRefreshView.getTop() >= 0)
                                && mRefreshState == RELEASE_TO_REFRESH) {
                            // Initiate the refresh
                            mRefreshState = REFRESHING;
                            Log.i(TAG, "loading_ing");
                            prepareForRefresh();
                            onFresh();
                        } else if (mRefreshView.getBottom() < mRefreshViewHeight
                                || mRefreshView.getTop() <= 0) {
                            // Abort refresh and scroll down below the refresh view
                            //  resetHeaderFooter();
                            Log.i(TAG, String.valueOf(mRefreshView.getBottom()));
                            //setSelection(1);
                            mySmoothScrollBy(mRefreshView.getBottom(), SMOOTH_SCROLL_TIME);
                        }
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    mLastMotionY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    applyHeaderPadding(event);
                    applyFooterHeight(event);
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private void mySmoothScrollBy(final int distance, final int duration) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                smoothScrollBy(distance, duration);
            }
        };
        scrollHander.post(runnable);
        smoothScroll = true;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mCurrentScrollState = scrollState;
        if (ableRefrsh == false) {
            if (normalFooter) {
                if (mCurrentScrollState == SCROLL_STATE_IDLE) {
                    Log.i(TAG, "SCROLL_STATE_IDLE");
                    if (getFirstVisiblePosition() == 0 && mRefreshView.getBottom() > 0) {
                        mySmoothScrollBy(mRefreshView.getBottom(), SMOOTH_SCROLL_TIME);
                        mRefreshState = TAP_TO_REFRESH;
                        Log.i(TAG, "head visible scroll");
                    } else if (getLastVisiblePosition() == getCount() - 1 && mBottomRefreshView.getTop() < getHeight()) {
                        mySmoothScrollBy(mBottomRefreshView.getTop() - getHeight(), SMOOTH_SCROLL_TIME);
                        Log.i(TAG, "footer visible scroll");
                        mRefreshState = TAP_TO_REFRESH;
                    } else {
                        Log.i(TAG, "head footer invisible and reset header footer:" + String.valueOf(mRefreshView.getBottom()));
                        if (getFirstVisiblePosition() == 0 && mRefreshView.getBottom() == 0) {
                            resetHeaderFooter();
                            setSelection(1);
                        } else
                            resetHeaderFooter();
                    }

                }
            } else {
                if (mCurrentScrollState == SCROLL_STATE_IDLE) {
                    Log.i(TAG, "innormal SCROLL_STATE_IDLE");
                    Log.i(TAG, "innormal head bottom scroll:" + String.valueOf(mRefreshView.getBottom()));
                    if (getFirstVisiblePosition() == 0 && mRefreshView.getBottom() > 0) {
                        mySmoothScrollBy(mRefreshView.getBottom(), SMOOTH_SCROLL_TIME);
                        Log.i(TAG, "innormal head visible scroll:" + String.valueOf(mRefreshView.getBottom()));
                        mRefreshState = TAP_TO_REFRESH;
                    } else if (getLastVisiblePosition() == getCount() - 1
                            && getHeight() - mBottomRefreshView.getTop() > mBottomInnormalHeight) {
                        mySmoothScrollBy(-getHeight() + mBottomRefreshView.getTop() + mBottomInnormalHeight,
                                SMOOTH_SCROLL_TIME);
                        mRefreshState = TAP_TO_REFRESH;
                        Log.i(TAG, "innormal footer visible scroll:" + String.valueOf(-getHeight() + mBottomRefreshView.getTop() + mBottomInnormalHeight));
                    } else {
                        Log.i(TAG, "head footer invisible and reset header footer:" + String.valueOf(mRefreshView.getBottom()));
                        resetHeaderFooter();
                        setSelection(1);
                    }
                }
            }
        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // Log.i(TAG, "onscroll");
        if (!ableRefrsh) {
            if (normalFooter) {
                if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    if (firstVisibleItem == 0 && mRefreshView.getBottom() >= mRefreshViewHeight
                            && mRefreshState != RELEASE_TO_REFRESH) {
                        mRefreshState = RELEASE_TO_REFRESH;
                        Log.i(TAG, "Relase_to_refersh");
                    } else if (getLastVisiblePosition() == totalItemCount - 1
                            && getHeight() - mBottomRefreshView.getTop() >= mBottomViewHeight
                            && mRefreshState != DOWN_PULL) {
                        mRefreshState = DOWN_PULL;
                        Log.i(TAG, "down_pull");
                    }
                }
            } else {
                // Log.i(TAG,"onscroll");
                //  Log.i(TAG,"down_pull_can:"+String.valueOf(getHeight() - mBottomRefreshView.getTop() >= mBottomInnormalHeight));
                if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    //Log.i(TAG,"scroll_scroll:donw_pull status:"+String.valueOf(mRefreshView.getBottom()));
                    if (firstVisibleItem == 0 && mRefreshView.getBottom() >= mRefreshViewHeight
                            && mRefreshState != RELEASE_TO_REFRESH) {
                        mRefreshState = RELEASE_TO_REFRESH;
                        Log.i(TAG, "Relase_to_refersh");
                    } else if (getLastVisiblePosition() == totalItemCount - 1
                            && getHeight() - mBottomRefreshView.getTop() >= mBottomInnormalHeight + mBottomViewHeight
                            && mRefreshState != DOWN_PULL) {
                        mRefreshState = DOWN_PULL;
                        Log.i(TAG, "down_pull");
                    }
                }
            }
        } else {
            if ((mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL)
                    && mRefreshState != REFRESHING) {
                if (firstVisibleItem == 0) {
                    if ((mRefreshView.getBottom() >= mRefreshViewHeight
                            || mRefreshView.getTop() >= 0)
                            && mRefreshState != RELEASE_TO_REFRESH) {
                        mRefreshViewText.setText(RELASE_REFRESH_STRING);
                        mRefreshViewImage.clearAnimation();
                        mRefreshViewImage.startAnimation(mFlipAnimation);
                        mRefreshState = RELEASE_TO_REFRESH;
                        Log.i(TAG, "RELASE_TO_REFRESH");
                    } else if (mRefreshView.getBottom() < mRefreshViewHeight
                            && mRefreshState != PULL_TO_REFRESH) {
                        mRefreshViewText.setText(DOWN_REFRESH_STRING);
                        if (mRefreshState != TAP_TO_REFRESH) {
                            mRefreshViewImage.clearAnimation();
                            mRefreshViewImage.startAnimation(mReverseFlipAnimation);
                        }
                        Log.i(TAG, "pull_to_refresh");
                        mRefreshState = PULL_TO_REFRESH;
                    }
                } else {
                    resetHeaderFooter();
                }
            } else if (mCurrentScrollState == SCROLL_STATE_FLING
                    && firstVisibleItem == 0
                    && mRefreshState == TAP_TO_REFRESH) {
                setSelection(1);
            }
            if ((mCurrentScrollState == SCROLL_STATE_FLING || mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL) &&
                    getLastVisiblePosition() == totalItemCount - 1 && mRefreshState != REFRESHING) {
                int visibleHeight = getHeight() - mBottomRefreshView.getTop();
                //Log.i(TAG,"visibleheihgt"+String.valueOf(visibleHeight));
                if (normalFooter && visibleHeight > mBottomViewHeight / 3) {
                    prepareForRefresh();
                    onFresh();
                }
            }

        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        //����������ʾͷ��
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (getHeight() <= 0) ;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resetHeaderFooter();
                        setSelection(1);
                    }
                });
            }
        });
        thread.start();

    }


    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
        if (listener == null)
            ableRefrsh = false;
        else
            ableRefrsh = true;
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }

    public interface OnRefreshListener {
        public void doInBackground();

        public void Refresh();
    }
}
