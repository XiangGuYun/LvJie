package com.yxd.lvjie.test

import com.yxd.lvjie.utils.CmdUtils

object Test {

    @JvmStatic
    fun main(args: Array<String>){
//        val result = Crc16Utils.calcCrc16(Utils.hexStringToByteArray("021000fe00020442C80000"))
//        println(result.toString(16))

        println(CmdUtils.hex2Float("3F800000C4CF"))

    }


}