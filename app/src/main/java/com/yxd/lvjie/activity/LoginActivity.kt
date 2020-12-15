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
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.annotation.Permission
import com.yxd.baselib.utils.SnackBarUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import kotlinx.android.synthetic.main.activity_login.*

@Permission([ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE])
@LayoutId(R.layout.activity_login)
class LoginActivity : ProjectBaseActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun init(bundle: Bundle?) {
        etAccount.txt("admin")
        etPassword.txt("Aa111111")

        drawer.setLeftMenu(R.layout.menu_left, 300.dp)

        btnLogin.click {
            drawer.openLeftMenu()
        }

//        btnLogin.click {
//            if (etAccount.isEmpty || etPassword.isEmpty) {
//                return@click
//            }
//            Req.login(etAccount.str, etPassword.str) {
//                goTo<HomeActivity>(true)
//            }
//        }
    }

}