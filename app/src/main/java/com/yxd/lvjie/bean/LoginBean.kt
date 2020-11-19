package com.yxd.lvjie.bean

data class LoginBean(
    val code: Int? = null,
    val `data`: Data? = null,
    val message: String? = null
) {
    data class Data(
        val token: String? = null,
        val type: String? = null,
        val user: User? = null
    ) {
        data class User(
            val createTime: Long? = null,
            val id: Int? = null,
            val loginTime: Long? = null,
            val name: String? = null,
            val password: String? = null,
            val roleName: String? = null,
            val superPower: Int? = null,
            val username: String? = null
        )
    }
}