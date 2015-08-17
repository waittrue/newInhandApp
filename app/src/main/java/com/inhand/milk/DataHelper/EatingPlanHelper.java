package com.inhand.milk.DataHelper;

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
        for (int i = 0; i < 8; i++) {
            int[] time = new int[2];
            time[0] = 3 * i;
            time[1] = 0;
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
