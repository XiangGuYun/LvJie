package com.yxd.lvjie.test

import com.yxd.lvjie.constant.Utils
import com.yxd.lvjie.utils.CmdUtils
import com.yxd.lvjie.utils.Crc16Utils

object Test {



    @JvmStatic
    fun main(args: Array<String>){
//        val result = Crc16Utils.calcCrc16(Utils.hexStringToByteArray("021000fe00020442C80000"))
//        println(result.toString(16))

        println(CmdUtils.hex2Float("42C80000"))

    }


}