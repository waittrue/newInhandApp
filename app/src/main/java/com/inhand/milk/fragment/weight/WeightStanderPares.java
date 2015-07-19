package com.inhand.milk.fragment.weight;

import android.util.Xml;

import com.inhand.milk.App;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/19.
 * 解析标准的那个表的，产生相对月份，相对标准的值。
 */
public class WeightStanderPares {
    private static WeightStanderPares instance = null;
    private XmlPullParser parser;
    private static final int TotalMonths = 81 + 1;
    private float[][] boyStander = new float[TotalMonths][2], girlStander = new float[TotalMonths][2];

    private WeightStanderPares() {
        InputStream inputStream = null;
        try {
            inputStream = App.getAppContext().getResources().getAssets().open("StanderData/WeightListSD.xml");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        for (int i = 0; i < TotalMonths; i++) {
            for (int j = 0; j < 2; j++) {
                boyStander[i][j] = 0;
                girlStander[i][j] = 0;
            }
        }
        readXML(inputStream);
    }

    public static WeightStanderPares getInstance() {
        if (instance == null) {
            synchronized (instance) {
                if (instance == null)
                    instance = new WeightStanderPares();
            }
        }
        return instance;
    }

    private void readXML(InputStream inputStream) {
        parser = Xml.newPullParser();
        int position1 = 0, position2 = 0, lastposition = 0;
        float[][] data = null;
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            Map<String, String> map = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT://文档开始事件,可以进行数据初始化处理
                        break;

                    case XmlPullParser.START_TAG://开始元素事件
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("key")) {
                            map = new HashMap<>();
                            String str = parser.nextText();
                            if (str.equals("min"))
                                position2 = 0;
                            else if (str.equals("max"))
                                position2 = 1;
                            else if (str.equals("boy"))
                                data = boyStander;
                            else if (str.equals("girl"))
                                data = girlStander;
                            else {
                                int month = Integer.parseInt(str);
                                lastposition = position1;
                                position1 = month;
                            }
                        } else if (name.equalsIgnoreCase("string")) {

                            String str = parser.nextText();
                            float num = Float.parseFloat(str);
                            data[position1][position2] = num;
                            if (lastposition + 1 != position1) {
                                float start = data[lastposition][position2];
                                float unitDiff = (num - start) / (position1 - lastposition);
                                int end = position1 - lastposition;
                                for (int i = 1; i < end; i++) {
                                    data[lastposition + i][position2] = unitDiff * i + start;
                                }
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG://结束元素事件
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float getBoyMax(int month) {
        if (month >= TotalMonths)
            return 0;
        return boyStander[month][1];
    }

    public float getBoyMin(int month) {
        if (month >= TotalMonths)
            return 0;
        return boyStander[month][0];
    }

    public float getGirlMax(int month) {
        if (month >= TotalMonths)
            return 0;
        return girlStander[month][1];
    }

    public float getGirlMin(int month) {
        if (month >= TotalMonths)
            return 0;
        return girlStander[month][0];
    }
}
