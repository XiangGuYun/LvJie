package com.yxd.lvjie.test

object Test {

    @JvmStatic
    fun main(args: Array<String>){
        val hex = "424FB146"
        val value = java.lang.Float.intBitsToFloat(Integer.valueOf(hex.trim { it <= ' ' }, 16))
        println(value)

    }
}