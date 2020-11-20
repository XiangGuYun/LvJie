package com.yxd.lvjie.activity

import android.Manifest
import android.Manifest.permission.*
import android.os.Bundle
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.utils.PermissionUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.net.Req
import kotlinx.android.synthetic.main.activity_login.*


@LayoutId(R.layout.activity_login)
class LoginActivity : ProjectBaseActivity() {

    override fun init(bundle: Bundle?) {
        etAccount.txt("admin")
        etPassword.txt("Aa111111")

        btnLogin.click {
            if (etAccount.isEmpty || etPassword.isEmpty) {
                return@click
            }
            Req.login(etAccount.str, etPassword.str) {
                goTo<HomeActivity>(true)
            }
        }

        PermissionUtils.req(this, null, null, null,
            ACCESS_COARSE_LOCATION,
            ACCESS_FINE_LOCATION,
            WRITE_EXTERNAL_STORAGE,
            READ_EXTERNAL_STORAGE)

    }

}