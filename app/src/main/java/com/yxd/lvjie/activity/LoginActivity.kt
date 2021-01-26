package com.yxd.lvjie.activity

import android.Manifest.permission.*
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.tencent.bugly.crashreport.CrashReport
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.annotation.Permission
import com.yxd.baselib.utils.SnackBarUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.helper.SPHelper
import com.yxd.lvjie.net.Req
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 登录页
 */
@Permission([ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE])
@LayoutId(R.layout.activity_login)
class LoginActivity : ProjectBaseActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun init(bundle: Bundle?) {

        if(SPHelper.getToken().isNotEmpty()){
            goTo<HomeActivity>(true)
        }

        etAccount.txt(SPHelper.getAccount())
        etAccount.select(SPHelper.getAccount().length)

        btnLogin.click {
            if (etAccount.isEmpty) {
                "请输入账号".toast()
                return@click
            }
            if(etPassword.isEmpty){
                "请输入密码".toast()
                return@click
            }
            Req.login(etAccount.str, etPassword.str) {
                SPHelper.putAccount(etAccount.str)
                SPHelper.putPassword(etPassword.str)
                goTo<HomeActivity>(true)
            }
        }
    }

}