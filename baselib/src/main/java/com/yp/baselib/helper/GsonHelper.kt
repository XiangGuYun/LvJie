package com.yp.baselib.helper

import com.google.gson.Gson
import com.google.gson.JsonParser

class GsonHelper {

    companion object {

        private var helper: GsonHelper? = null

        fun getInstance(): GsonHelper {
            if (helper == null) {
                helper = GsonHelper()
            }
            return helper!!
        }
    }

    val jsonParser = JsonParser()

    val gson = Gson()

}