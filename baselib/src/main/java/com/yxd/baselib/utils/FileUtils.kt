package com.yxd.baselib.utils

import android.Manifest
import android.content.Context
import android.os.Environment
import com.google.gson.JsonParser
import java.io.*

/**
 * 文件工具类
 */
object FileUtils {

    /**
     * 新建SD卡文件
     * @param fileName String
     * @return File
     */
    @JvmStatic
    fun newSDCardFile(fileName:String): File {
        val file = File(Environment.getExternalStorageDirectory().toString(), fileName)
        if(!file.exists()) {
            val isSuccess = file.createNewFile()
            if(!isSuccess){
                LogUtils.e("YXD_", "SD卡文件创建失败！")
            }
        }
        return file
    }

    /**
     * 新建缓存文件
     * @param fileName String
     * @return File
     */
    @JvmStatic
    fun newCacheFile(fileName: String): File {
        val file = File(AppUtils.getCachePath(), fileName)
        if(!file.exists()) {
            val isSuccess = file.createNewFile()
            if(!isSuccess){
                LogUtils.e("YXD_", "缓存文件创建失败！")
            }
        }
        return file
    }

    /**
     * 将assets下的文件复制到sd指定目录下
     * @param context     上下文
     * @param assetsPath  assets下的路径
     */
    fun copyAssetsFileToSDCard(context: Context, assetsPath: String) {
        
        val inputStream = context.resources.assets.open(assetsPath)
        val file = File(Environment.getExternalStorageDirectory().toString(), assetsPath)
        if(!file.exists()){
            file.createNewFile()
        }
        val fos = file.outputStream()
        inputStream.copyTo(fos)
        inputStream.close()
        fos.close()
    }

    /**
     * 获取资源文件文本
     */
    fun readAssetsText(ctx: Context, fileName: String?): String? {
        val stringBuilder = StringBuilder()
        try {
            val inputReader = InputStreamReader(fileName?.let { ctx.resources.assets.open(it) })
            val bufReader = BufferedReader(inputReader)
            var line: String?
            while (bufReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            return stringBuilder.toString()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 读取raw文件夹下的文本
     */
    fun readRawText(ctx: Context, id: Int): String {
        val inputStream = ctx.resources.openRawResource(id)
        return inputStream.bufferedReader().readText()
    }

    /**
     * 读取cache文件夹下的文本
     */
    fun readCacheText(ctx: Context, fileName: String): String {
        val file = File("/data/data/${ctx.packageName}/cache", fileName)
        return file.readText()
    }

    /**
     * 读取SD卡中的文本
     */
    fun readSDCardText(fileName: String): String {
        if(!PermissionUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
            throw Exception("没有文件读写权限！")
        }
        val file = File(Environment.getExternalStorageDirectory(), fileName)
        return file.readText()
    }

    /**
     * 写入对象到文件
     */
    fun writeObject(file: File, obj: Serializable) {
        if (!file.exists()) file.createNewFile()
        val stream = ObjectOutputStream(file.outputStream())
        stream.writeObject(obj)
        stream.close()
    }

    /**
     * 从文件读取对象
     */
    fun <T> readObject(file: File): T? {
        if (!file.exists())
            return null
        val stream = ObjectInputStream(file.inputStream())
        @Suppress("UNCHECKED_CAST")
        val t = stream.readObject() as T?
        stream.close()
        return t
    }

    @JvmStatic
    fun writeTextToSDCard(file:File, text:String){
        if(!PermissionUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
            throw Exception("没有文件读写权限！")
        }
        file.writeText(text)
    }

}