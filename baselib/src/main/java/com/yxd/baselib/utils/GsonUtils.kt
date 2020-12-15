package com.yxd.baselib.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonUtils {

    /**
     * 将JSON字符串转换为数据列表
     *
     * 如果发生转换异常，请用如下方式代替
     *  val list = JsonParser().parse(cardOrder.list).asJsonArray
     *  val newList = list.map {
     *      val jo = it.asJsonObject
     *      AllCaiModel.DataBean.ResultsBean().apply {
     *          price = jo.get("price").asInt
     *          id = jo.get("id").asInt
     *          name = jo.get("name").asString
     *          count = jo.get("count").asInt
     *      }
     *  }
     * @return List<T>
     */
    fun <T> fmtJsonList(jsonArrayString: String): List<T> {
        return Gson().fromJson(jsonArrayString, object : TypeToken<List<T>>() {}.type) as List<T>
    }

}