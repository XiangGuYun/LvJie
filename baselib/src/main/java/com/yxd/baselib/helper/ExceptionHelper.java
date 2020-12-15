package com.yxd.baselib.helper;

import android.os.Environment;

import com.yxd.baselib.GlobalConfig;
import com.yxd.baselib.utils.EquipmentUtils;
import com.yxd.baselib.utils.FileUtils;
import com.yxd.baselib.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 异常处理帮助类
 */
public class ExceptionHelper implements Thread.UncaughtExceptionHandler {

    private static volatile ExceptionHelper INSTANCE;
    
    private static final String TAG ="YXD_EX";

    private ExceptionHelper() {
    }

    public static ExceptionHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (ExceptionHelper.class) {
                if (INSTANCE == null) {
                    synchronized (ExceptionHelper.class) {
                        INSTANCE = new ExceptionHelper();
                    }
                }
            }
        }
        return INSTANCE;
    }

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * 初始化默认异常捕获
     */
    public void init() {
        // 获取默认异常处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将当前类设为默认异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (handleException(e)) {
            // 已经处理,APP重启

        } else {
            // 如果不处理,则调用系统默认处理异常,弹出系统强制关闭的对话框
            if (mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(t, e);
            }
        }
    }

    private boolean handleException(Throwable e) {
        if (e == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String currentTime = sdf.format(System.currentTimeMillis());

        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        pw.close();
        String result = writer.toString();

        // 打印出错误日志
        LogUtils.e(TAG, "异常发生时间："+currentTime + "\n异常原因：" +result+"\n发生异常的设备信息：");
        LogUtils.e(TAG, "手机厂商：" + EquipmentUtils.getDeviceBrand());
        LogUtils.e(TAG, "手机型号：" + EquipmentUtils.getSystemModel());
        LogUtils.e(TAG, "手机当前系统语言：" + EquipmentUtils.getSystemLanguage());
        LogUtils.e(TAG, "Android系统版本号：" + EquipmentUtils.getSystemVersion());
        LogUtils.e(TAG, "手机设备名：" + EquipmentUtils.getSystemDevice());
        LogUtils.e(TAG, "主板名：" + EquipmentUtils.getDeviceBoard());
        LogUtils.e(TAG, "手机厂商名：" + EquipmentUtils.getDeviceManufacturer());

        if(GlobalConfig.isWriteExceptionFile()){
            File fileException = FileUtils.newSDCardFile("应用异常日志.txt");
            StringBuilder sb = new StringBuilder("");
            sb.append("异常发生时间：").append(currentTime).append("\n异常原因：").append(result).append("\n发生异常的设备信息：").append("\n");;
            sb.append("手机厂商：").append(EquipmentUtils.getDeviceBrand()).append("\n");;
            sb.append("手机型号：").append(EquipmentUtils.getSystemModel()).append("\n");;
            sb.append("手机当前系统语言：").append(EquipmentUtils.getSystemLanguage()).append("\n");;
            sb.append("Android系统版本号：").append(EquipmentUtils.getSystemVersion()).append("\n");;
            sb.append("手机设备名：").append(EquipmentUtils.getSystemDevice()).append("\n");;
            sb.append("主板名：").append(EquipmentUtils.getDeviceBoard()).append("\n");;
            sb.append("手机厂商名：").append(EquipmentUtils.getDeviceManufacturer()).append("\n");;
            FileUtils.writeTextToSDCard(fileException, sb.toString());
        }


        return true;
    }

}