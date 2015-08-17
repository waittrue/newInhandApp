package com.inhand.milk.fragment.user_info_settings;

import android.content.Context;
import android.util.Xml;

import com.inhand.milk.App;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/6.
 * city--Xml的解析，并用这个对象提供返回标题string和内容string
 * 两个公用的方法。
 */
public class UserInfoCityXmlPares {
    private static final String title = "title", content = "content";
    private static UserInfoCityXmlPares instance = null;
    List<Map<String, String>> data;
    private XmlPullParser parser;

    private UserInfoCityXmlPares(Context context) {
        data = new ArrayList<>();
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open("StanderData/CityList.xml");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        readXML(inputStream);
        /*
        for(Map<String,String> map:data){
            if(map.containsKey(title))
                Log.i("xml",map.get(title));
            Log.i("xml",map.get(content));
        }
        */
    }

    private static synchronized UserInfoCityXmlPares init() {
        if (instance == null)
            instance = new UserInfoCityXmlPares(App.getAppContext());
        return instance;
    }

    public static UserInfoCityXmlPares getInstance() {
        if (instance == null)
            init();
        return instance;
    }

    private boolean hasTitle(int position) {
        if (position < 0 || position > data.size() - 1)
            return false;
        Map<String, String> map = data.get(position);
        return map.containsKey(title);
    }

    public String getTitle(int position) {
        if (position < 0 || position > data.size() - 1)
            return null;
        Map<String, String> map = data.get(position);
        if (map.containsKey(title)) {
            return map.get(title);
        }
        return null;
    }

    public String getContent(int position) {
        if (position < 0 || position > data.size() - 1)
            return null;
        Map<String, String> map = data.get(position);
        return map.get(content);

    }

    public int getCount() {
        return data.size();
    }

    private void readXML(InputStream inputStream) {
        parser = Xml.newPullParser();
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
                            map.put(title, str);
                        } else if (name.equalsIgnoreCase("string")) {
                            if (map == null)
                                map = new HashMap<>();
                            String str = parser.nextText();
                            map.put(content, str);
                            data.add(map);
                            map = null;
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
}
