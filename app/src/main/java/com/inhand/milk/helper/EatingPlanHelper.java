package com.inhand.milk.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/28.
 */
public class EatingPlanHelper {
    private List<int[]> planTime = new ArrayList<>();
    private boolean[] isMilk;

    public EatingPlanHelper() {
        initPlanTime();
    }

    private void initPlanTime() {
        planTime.clear();
        for (int i = 0; i < 24 * 12; i++) {
            int[] time = new int[2];
            time[0] = i / 12;
            time[1] = i % 12 * 5;
            planTime.add(time);
        }
        int count = planTime.size();
        isMilk = new boolean[count];
        for (int i = 0; i < count; i++) {
            if (i % 2 == 0)
                isMilk[i] = true;
            else
                isMilk[i] = false;
        }
    }

    public List<int[]> getPlanTime() {
        return planTime;
    }

    public boolean[] getIsMilk() {
        return isMilk;
    }
}
