package com.yp.baselib.utils;

import android.util.Log;

/**
 * 日志工具类
 */
public class LogUtils {
    /**
     * 截断输出日志
     * @param msg
     */
    public static void logE(String tag, String msg) {
        if (tag == null || tag.length() == 0 || msg == null || msg.length() == 0) {
            return;
        }
        int segmentSize = 3 * 1024;
        long length = msg.length();
        // 长度小于等于限制直接打印
        if (length <= segmentSize ) {
            Log.e(tag, msg);
        }else {
            // 循环分段打印日志
            while (msg.length() > segmentSize ) {
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "");
                Log.e(tag,"-------------------"+ logContent);
            }
            // 打印剩余日志
            Log.e(tag,"-------------------"+ msg);
        }
    }

}