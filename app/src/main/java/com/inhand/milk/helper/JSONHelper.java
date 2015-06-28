package com.inhand.milk.helper;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JSONHelper
 * Desc:JSON处理帮助类
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-03
 * Time: 16:01
 */
public class JSONHelper {

    /**
     * 从文件解析JSON数组为对象列表
     *
     * @param ctx   上下文环境
     * @param file  文件名
     * @param clazz 返回对象类型
     * @return 对象列表
     */
    public static <T> List<T> parseJSONArray(Context ctx, String file, Class<T> clazz) {
        InputStream is = null;
        try {
            is = ctx.getResources().getAssets().open(file);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String json = new String(buffer, "utf-8");
            is.close();
            return JSON.parseArray(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 校正JSON字串，解决低版本Android
     * 的json序列化问题
     *
     * @param JSON 待修正JSON字串
     * @return
     */
    public static String getValidJSON(String JSON){
        if (Build.VERSION.SDK_INT<17){
            JSON=JSON.replace("\"[","[");
            JSON=JSON.replace("\\","");
            JSON=JSON.replace("]\"","]");
        }
        return JSON;
    }

    public  static  String getValidCloudJSON(String JSON){
        if (Build.VERSION.SDK_INT<17&&JSON.indexOf("[\"{")==0){
            Log.d("cloudjsons0",JSON);
            JSON=JSON.replace("[\"{","[{\"");
            JSON=JSON.replace("=","\":");
            JSON=JSON.replace(", ",",\"");
            JSON=JSON.replace(",\"{",",{\"");
            JSON=JSON.replace("}\"]","}]");
            JSON=JSON.replace(":[{",":[{\"");
            JSON=JSON.replace("}\",","},");
            String regEx="\\d+:\\d+";
            Pattern pattern=Pattern.compile(regEx);
            Matcher matcher=pattern.matcher(JSON);
            int index=0;
            while (matcher.find()) {
                index=JSON.indexOf(matcher.group(),index);
                String rpstr="\"" + matcher.group() + "\"";
                JSON = JSON.substring(0,index)+rpstr+JSON.substring(index+matcher.group().length());
                index=index+matcher.group().length();
            }
            Log.d("cloudjsons1",JSON);
        }
        //Log.d("cloudjsons",JSON);
        return  JSON;
    }
}
