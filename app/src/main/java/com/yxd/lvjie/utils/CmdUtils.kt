package com.yxd.lvjie.utils

import com.yp.baselib.utils.BusUtils
import com.yxd.lvjie.constant.MsgWhat

object CmdUtils {

    private fun hex2Float(hex:String): Float {
        return java.lang.Float.intBitsToFloat(Integer.valueOf(hex.trim { it <= ' ' }, 16))
    }

    /**
     * 获取电量
     */
    fun getElectricQuantity(){
        BusUtils.post(MsgWhat.SEND_COMMAND, "02 03 00 d2 00 02 64 01")
    }

    /**
     * 解析电量
     * HEX:02 03 04 41 05 D2 90 91 C2
     */
    fun decodeElectricQuantity(hex:String): Float {
        return hex2Float(hex.substring(6, 14))
    }

    /**
     * 获取强度和频率
     */
    fun getStrengthAndFrequency(){
        BusUtils.post(MsgWhat.SEND_COMMAND, "02 03 00 fa 00 04 64 0b")
    }

    /**
     * 解析强度和频率
     * @param hex String
     * @return Pair<Float, Float>
     */
    fun decodeStrengthAndFrequency(hex:String): Pair<Float, Float> {

        val newHex = hex.replace(" ", "")
        val hexStrength = newHex.substring(6, 14)
        val hexFrequency = newHex.substring(14, 22)
        return hex2Float(hexStrength) to hex2Float(hexFrequency)
    }

}