package com.yxd.baselib.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.yxd.baselib.base.BaseApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * App工具类
 *
 * @author YeXuDong
 */
public class AppUtils {

    /**
     * 获取设备的IMEI值
     * 注意：需要添加READ_PRIVILEGED_PHONE_STATE权限
     *
     * @param ctx
     * @return
     */
    public static String getIMEI(Context ctx) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            TelephonyManager TelephonyMgr = (TelephonyManager) ctx.getSystemService(TELEPHONY_SERVICE);
            String szImei = TelephonyMgr.getDeviceId();
            return szImei;
        } else {
            return Settings.System.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

    }

    /**
     * 获取包名
     *
     * @return
     */
    public static String getPackageName() {
        return BaseApplication.getInstance().getContext().getPackageName();
    }

    /**
     * 获取缓存路径
     *
     * @return
     */
    public static String getCachePath() {
        return String.format("/data/data/%s/cache", getPackageName());
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode() {
        Context context = BaseApplication.getInstance().getContext();
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 获取版本名称
     */
    public static String getVersionName() {
        Context context = BaseApplication.getInstance().getContext();
        PackageManager manager = context.getPackageManager();
        String name = "";
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 安装APK
     *
     * @param activity
     * @param providerAuthorities 提供者证书，对应provider节点下的authorities属性值
     * @param filePath            APK文件路径
     *                            <p>
     *                            需要将以下信息插入到AndroidManifest.xml的application节点下面
     *                            <provider
     *                            android:name="androidx.core.content.FileProvider"
     *                            android:authorities="包名.fileprovider"
     *                            android:exported="false"
     *                            android:grantUriPermissions="true">
     *                            <meta-data
     *                            android:name="android.support.FILE_PROVIDER_PATHS"
     *                            android:resource="@xml/file_paths" />
     *                            </provider>
     *                            <p>
     *                            在res文件夹中新建xml文件夹和file_paths.xml
     *                            <?xml version="1.0" encoding="utf-8"?>
     *                            <paths xmlns:android="http://schemas.android.com/apk/res/android">
     *                            <external-path path="android/data/包名/" name="files_root" />
     *                            <external-path path="." name="external_storage_root" />
     *                            <external-path name="external_files" path="."/>
     *                            </paths>
     */
    public static void install(Activity activity, String providerAuthorities, String filePath) {
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //是否获取安装未知应用权限
            boolean hasInstallPermission = activity.getPackageManager().canRequestPackageInstalls();
            if (hasInstallPermission) {
                //安装应用的逻辑
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(activity, providerAuthorities, apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                //当为允许安装未知来源应用时
                ToastUtils.toast("请允许安装未知来源应用");
                //跳转至“安装未知应用”权限界面，引导用户开启权限，可以在onActivityResult中接收权限的开启结果
                Uri packageURI = Uri.parse("package:" + activity.getPackageName());
                Intent intent1 = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivityForResult(intent1, REQUEST_CODE_UNKNOWN_APP);
                return;
            }
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        activity.startActivity(intent);
        activity.finish();
    }

    public static final int REQUEST_CODE_UNKNOWN_APP = 12;

    /**
     * 当允许安装位置来源APK之后的回调
     *
     * @param ctx
     * @param providerAuthorities
     * @param filePath
     * @param requestCode
     */
    public static void doInOnActivityResult(Context ctx, String providerAuthorities, String filePath, int requestCode) {
        if (requestCode == REQUEST_CODE_UNKNOWN_APP) {
            File apkFile = new File(filePath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(ctx, providerAuthorities, apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            ctx.startActivity(intent);
        }
    }

    /**
     * 判断手机是否有root权限
     */
    public static boolean haveRootPermission() {
        PrintWriter PrintWriter;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return getResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    private static boolean getResult(int value) {
        switch (value) {
            // 代表成功
            case 0:
                return true;
            // 失败
            case 1:
                return false;
            // 未知情况
            default:
                return false;
        }
    }

    /**
     * 重启APP
     */
    public static void restartApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        final Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }

    /**
     * 启动app
     * com.exmaple.client/.MainActivity
     * com.exmaple.client/com.exmaple.client.MainActivity
     */
    public static boolean startApp(String packageName, String activityName) {
        boolean isSuccess = false;
        String cmd = "am start -n " + packageName + "/" + activityName + " \n";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            int value = process.waitFor();
            return getResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return isSuccess;
    }

    /**
     * 卸载APP
     */
    public static boolean uninstall(String packageName,Context context){
        if(haveRootPermission()){
            // 有root权限，利用静默卸载实现
            return clientUninstall(packageName);
        }else{
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,packageURI);
            uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(uninstallIntent);
            return true;
        }
    }

    /**
     * 静默安装
     */
    public static boolean clientInstall(Context context, String apkPath){
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            Log.d("silent", Thread.currentThread().getName());
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("chmod 777 "+apkPath);
            PrintWriter.println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
            PrintWriter.println("pm install -r "+apkPath);
//            PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return getResult(value);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("silent", e.getLocalizedMessage());
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 将文件复制到system/app 目录
     * @param apkPath 特别注意格式：该路径不能是：/storage/emulated/0/app/QDemoTest4.apk 需要是：/sdcard/app/QDemoTest4.apk
     * @return
     */
    public static boolean copy2SystemApp(String apkPath){
        PrintWriter PrintWriter = null;
        Process process = null;
        String appName = "chetou.apk",cmd;

        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            cmd = "mount -o remount,rw -t yaffs2 /dev/block/mtdblock3 /system";
            Log.e("copy2SystemApp", cmd);
            PrintWriter.println(cmd);

            cmd = "cat "+apkPath+" > /system/app/"+appName;
            Log.e("copy2SystemApp", cmd);
            PrintWriter.println(cmd);

            cmd = "chmod 777 /system/app/"+appName +" -R";
            Log.e("copy2SystemApp", cmd);
            PrintWriter.println(cmd);

            cmd = "mount -o remount,ro -t yaffs2 /dev/block/mtdblock3 /system";
            Log.e("copy2SystemApp", cmd);
            PrintWriter.println(cmd);
            PrintWriter.println("reboot");  //重启
            PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return getResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 安装
     */
    public static boolean install(Context context, String apkPath){
        // 先判断手机是否有root权限
        if(haveRootPermission()){
            // 有root权限，利用静默安装实现
//            return clientInstall(context, apkPath);
            return executeSuCMD(context, apkPath);
        }else{
            // 没有root权限，利用意图进行安装
            File file = new File(apkPath);
            if(!file.exists())
                return false;
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
            context.startActivity(intent);
            return true;
        }
    }

    public static boolean executeSuCMD(Context context, String currentTempFilePath) {
        Process process = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            // 请求root
            process = Runtime.getRuntime().exec("su");
            out = process.getOutputStream();
            // 调用安装
            out.write(("pm install -r " + currentTempFilePath + "\n").getBytes());

            Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, restartIntent);
            Log.d("silent", "服务在启动");

            in = process.getInputStream();
            int len = 0;
            byte[] bs = new byte[256];
            while (-1 != (len = in.read(bs))) {
                String state = new String(bs,len);
                if (state.equals("Success\n")) {
                    //安装成功后的操作
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 静默卸载
     */
    public static boolean clientUninstall(String packageName){
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("LD_LIBRARY_PATH=/vendor/lib:/system/lib ");
            PrintWriter.println("pm uninstall "+packageName);
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return getResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }



}
