package com.yxd.lvjie.bean

data class HistoryDataBean(
    val code: Int? = null,
    val `data`: Data? = null,
    val message: String? = null
) {
    data class Data(
        val list: List<History>? = null,
        val total: Int? = null
    ) {
        data class History(
            val frequency: Int? = null,
            val strength: Double? = null,
            val time: Long? = null
        )
    }
}