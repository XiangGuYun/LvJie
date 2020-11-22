package com.yxd.lvjie.bean

/*
{
	"code": 0,
	"data": {
		"id": 1,
		"version": "0.0.1",
		"type": 1, // 0-诱导更新 1-强制更新
		"notes": "测试版本",
		"downloadUrl": "下载链接还没有",
		"createTime": "2020-11-20 09:39:29"
	},
	"message": "成功"
}
 */
data class ApkUpdateBean(
    val code: Int,
    val `data`: Data,
    val message: String
){
    data class Data(
        val createTime: String,
        val downloadUrl: String,
        val id: Int,
        val notes: String,
        val type: Int,
        val version: String
    )
}

