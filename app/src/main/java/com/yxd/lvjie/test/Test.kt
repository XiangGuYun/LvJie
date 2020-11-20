package com.yxd.lvjie.test

import com.yxd.lvjie.constant.Utils

object Test {

    @JvmStatic
    fun main(args: Array<String>){
//        val hex = "424FB146"
//        val value = java.lang.Float.intBitsToFloat(Integer.valueOf(hex.trim { it <= ' ' }, 16))
//        println(value)
        val hex = "[02 03 28 47 52 45 41 4E 2D 49 4E 49 54 21 21 62 79 20 7A 6A 79 00 00 38 36 34 33 33 33 30 34 32 38 38 34 34 37 38 00 00 00 00 00 9F 98]"
        println(Utils.byteToASCII(Utils.hexStringToByteArray(hex)))
    }
}