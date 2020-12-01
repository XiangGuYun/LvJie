package com.yxd.lvjie.activity

import android.Manifest.permission.*
import android.os.Bundle
import android.os.Environment
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.annotation.Permission
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.net.Req
import kotlinx.android.synthetic.main.activity_login.*
import java.io.File

@Permission([ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE])
@LayoutId(R.layout.activity_login)
class LoginActivity : ProjectBaseActivity() {

    override fun init(bundle: Bundle?) {
        etAccount.txt("admin")
        etPassword.txt("Aa111111")

        btnLogin.click {
            val file = File(Environment.getExternalStorageDirectory().toString(), "test1.txt")
            file.createNewFile().toast()
//            if (etAccount.isEmpty || etPassword.isEmpty) {
//                return@click
//            }
//            Req.login(etAccount.str, etPassword.str) {
//                goTo<HomeActivity>(true)
//            }
        }
    }

}