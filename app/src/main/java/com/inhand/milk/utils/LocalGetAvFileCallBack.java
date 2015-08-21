package com.inhand.milk.utils;

import com.avos.avoscloud.AVException;

/**
 * Created by Administrator on 2015/8/21.
 * 作者：大力
 * 时间：2015/8/21
 * 描述：从云端取到文件的接口
 */
public interface LocalGetAvFileCallBack {
    public void done(final byte[] data, AVException e);
}
