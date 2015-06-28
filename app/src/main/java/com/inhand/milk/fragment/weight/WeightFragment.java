package com.inhand.milk.fragment.weight;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.utils.RingWithText;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2015/6/6.
 */
public class WeightFragment extends TitleFragment {
    private WeightExcle weightExcle;
    private static final String TAG = "weightFragment";
    private int lastPositon = -1;
    private RingWithText ringWithText;
    private static final DecimalFormat decimalFormat = new DecimalFormat("###.##");
    private Adder adder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_weight, container, false);
        setTitleview(getResources().getString(R.string.weight_fragment_title), 1);
        initViews(mView);
        return mView;
    }
    private void initViews(View view){
        initWeightTab(view);
        initLine(view);
        initWeightExcle(view);
        initRelativeContent(view);
        initAdder(view);
    }

    private void initAdder(View view) {
        adder = (Adder) view.findViewById(R.id.weight_fragment_adder);
        adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click");
                inAnimation();
            }
        });

    }
    private void inAnimation() {
        AdderPupupWindow floatAdderWindow = new AdderPupupWindow(getActivity());
        floatAdderWindow.show();
    }


    private void initRelativeContent(View view) {
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.weight_fragment_ring_container);
        initRingWithText();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        relativeLayout.addView(initRingWithText(), lp);
        initRelativeLeftTexts(relativeLayout);
        initRelativeRightTexts(relativeLayout);
        initBottomTextView(view);

    }

    private void initBottomTextView(View view) {
        TextView textView = (TextView) view.findViewById(R.id.weight_fragment_bottom_text);
        textView.setText(getLastTime());
    }

    private String getLastTime() {
        String str = getResources().getString(R.string.weight_fragment_bottome_text);
        return str + ":2015-2-3";
    }

    private void initRelativeLeftTexts(RelativeLayout relativeLayout) {
        TextView leftUp = new TextView(getActivity());
        TextView leftDown = new TextView(getActivity());
        String upString = decimalFormat.format(getCurrentWeight());
        String downString = getResources().getString(R.string.weight_left_down_text);

        float upLeftMargin, downLeftMargin, upTopMargin, downTopMargin;
        int color = getResources().getColor(R.color.weight_fragment_left_text_color);
        float upTextSize = getResources().getDimension(R.dimen.weight_fragment_left_up_text_size);
        float downTextSize = getResources().getDimension(R.dimen.weight_fragment_left_down_text_size);
        float space = getResources().getDimension(R.dimen.weight_fragment_text_space_size);

        leftUp.setTextColor(color);
        leftUp.setText(upString);
        leftUp.setTextSize(TypedValue.COMPLEX_UNIT_PX, upTextSize);
        leftDown.setTextColor(color);
        leftDown.setText(downString);
        leftDown.setTextSize(TypedValue.COMPLEX_UNIT_PX, downTextSize);

        upLeftMargin = App.getWindowWidth(getActivity()) / 2 - ringWithText.getR() - leftUp.getPaint().measureText(upString);
        upLeftMargin = upLeftMargin / 2;
        downLeftMargin = App.getWindowWidth(getActivity()) / 2 - ringWithText.getR() - leftDown.getPaint().measureText(downString);
        downLeftMargin = downLeftMargin / 2;

        upTopMargin = ringWithText.getR() * 2 - upTextSize - downTextSize - space;
        upTopMargin = upTopMargin / 2;
        downTopMargin = upTopMargin + upTextSize + space;

        RelativeLayout.LayoutParams upLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams downLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        upLp.leftMargin = (int) upLeftMargin;
        upLp.topMargin = (int) upTopMargin;
        downLp.leftMargin = (int) downLeftMargin;
        downLp.topMargin = (int) downTopMargin;

        relativeLayout.addView(leftUp, upLp);
        relativeLayout.addView(leftDown, downLp);
    }

    private void initRelativeRightTexts(RelativeLayout relativeLayout) {
        TextView rightUp = new TextView(getActivity());
        TextView rightDown = new TextView(getActivity());
        String upString = getCurrentStander();
        String downString = getResources().getString(R.string.weight_left_down_text);

        float upRightMargin, downRightMargin, upTopMargin, downTopMargin;
        int color = getResources().getColor(R.color.weight_fragment_right_text_color);
        float upTextSize = getResources().getDimension(R.dimen.weight_fragment_right_up_text_size);
        float downTextSize = getResources().getDimension(R.dimen.weight_fragment_right_down_text_size);
        float space = getResources().getDimension(R.dimen.weight_fragment_text_space_size);
        int width = App.getWindowWidth(getActivity());

        rightUp.setTextColor(color);
        rightUp.setText(upString);
        rightUp.setTextSize(TypedValue.COMPLEX_UNIT_PX, upTextSize);
        rightDown.setTextColor(color);
        rightDown.setText(downString);
        rightDown.setTextSize(TypedValue.COMPLEX_UNIT_PX, downTextSize);

        upRightMargin = App.getWindowWidth(getActivity()) / 2 - ringWithText.getR() - rightUp.getPaint().measureText(upString);
        upRightMargin = upRightMargin / 2;
        downRightMargin = App.getWindowWidth(getActivity()) / 2 - ringWithText.getR() - rightDown.getPaint().measureText(downString);
        downRightMargin = downRightMargin / 2;

        upTopMargin = ringWithText.getR() * 2 - upTextSize - downTextSize - space;
        upTopMargin = upTopMargin / 2;
        downTopMargin = upTopMargin + upTextSize + space;

        RelativeLayout.LayoutParams upLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams downLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        upLp.leftMargin = (int) (width - upRightMargin - rightUp.getPaint().measureText(upString));
        upLp.topMargin = (int) upTopMargin;
        downLp.leftMargin = (int) (width - downRightMargin - rightDown.getPaint().measureText(downString));
        ;
        downLp.topMargin = (int) downTopMargin;

        relativeLayout.addView(rightUp, upLp);
        relativeLayout.addView(rightDown, downLp);
    }

    private float getCurrentWeight() {

        return 12.30f;
    }

    private String getCurrentStander() {
        return "13.4~15.5";
    }

    private RingWithText initRingWithText() {
        float r;
        int height = App.getWindowHeight(getActivity());  // 屏幕高度（像素）
        r = height - getResources().getDimension(R.dimen.weight_fragment_tab_height) -
                getResources().getDimension(R.dimen.weight_fragment_line_height) -
                getResources().getDimension(R.dimen.weight_fragment_excle_down_divider_height) -
                getResources().getDimension(R.dimen.footer_navigation_fragment_height) -
                getResources().getDimension(R.dimen.temperature_milk_title_height) -
                getResources().getDimension(R.dimen.milk_amount_title_divider) - App.getStatusHeight(getActivity());
        r = r / 2 / 5 * 3 / 2;//这里要跟布局图里面的那个权重对应好;


        ringWithText = new RingWithText(this.getActivity(), r);

        ringWithText.setTexts(getRingWithTextStrings(0), getRingWithTextSizes(r));
        // ringWithText.setRingWidth(r/6);
        ringWithText.setTextColor(getResources().getColor(R.color.weight_fragment_ring_text_color));
        ringWithText.setRingBgColor(getResources().getColor(R.color.weight_fragment_ring_bg_color));
        ringWithText.setBackgroundColor(getResources().getColor(R.color.weight_fragment_ring_inner_bg_color));
        return ringWithText;
    }

    private float[] getRingWithTextSizes(float r) {
        float[] result = new float[2];
        result[0] = r / 5;
        result[1] = r / 2.5f;
        return result;
    }

    private String[] getRingWithTextStrings(int weight) {
        String[] result = new String[2];
        result[0] = getResources().getString(R.string.weight_ring_text_up);
        //这里要根据实际情况反馈出最后的结果
        result[1] = getResources().getStringArray(R.array.weight_ring_text_status)[1];
        return result;
    }
    private void initWeightExcle(View view) {
        weightExcle = (WeightExcle) view.findViewById(R.id.weight_fragment_excle);
        addPoints(weightExcle);
        weightExcle.setMonthDays(6);
    }

    private void addPoints(WeightExcle weightExcle) {
        weightExcle.clearPoints();
        for (int i = 0; i < 6; i++) {
            weightExcle.addPoint(i + 1, (float) Math.random() * 30 + 20);
        }
    }

    private void initWeightTab(View view) {
        WeightTab weightTab = (WeightTab)view.findViewById(R.id.weight_tabs);
        weightTab.setTabNum(10);
        lastPositon = 9;
        weightTab.initTabs();
        weightTab.setStopLisetner(new WeightTab.StopLisetner() {
            @Override
            public void stopLisetner(int position) {
                if (lastPositon == position)
                    return;
                lastPositon = position;
                monthToWeightExcle(weightExcle, position);
                weightExcle.invalidate();
            }
        });
    }

    private void monthToWeightExcle(WeightExcle weightExcle, int position) {
        weightExcle.clearPoints();
        //weightExcle.clearStander();
        addPoints(weightExcle);
        //weightExcle.setStanderLeft();
        //weightExcle.setStanderRight();
        weightExcle.setMonthDays(6);
    }
    private void initLine(View view){
        ImageView imageView = new ImageView(this.getActivity());
        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.weight_fragment_line_container);
        WindowManager wm = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);

        int lineWidth = wm.getDefaultDisplay().getWidth() /4;
        imageView.setLayoutParams(new LinearLayout.LayoutParams(lineWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setBackgroundColor(Color.RED);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.addView(imageView);

    }

}
