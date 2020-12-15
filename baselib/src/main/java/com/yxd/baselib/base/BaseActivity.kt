package com.yxd.baselib.base

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.*
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
import android.widget.Toast
import com.githang.statusbar.StatusBarCompat
import com.google.gson.Gson
import com.yxd.baselib.GlobalConfig
import com.yxd.baselib.annotation.*
import com.yxd.baselib.ex.BaseEx
import com.yxd.baselib.utils.*
import com.yxd.baselib.utils.StatusBarUtils.hideStatusBar
import com.yxd.baselib.utils.StatusBarUtils.setStatusBarTextBlack
import me.yokeyword.fragmentation.SupportActivity
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.collections.ArrayList


/**
 * 最基类Activity
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseActivity : SupportActivity(), BaseEx {

    /**
     * 是否启用EventBus
     */
    var startEventBus = false

    /**
     * 布局ID注解
     */
    var viewInject: LayoutId? = null

    /**
     * 状态栏颜色注解
     */
    var colorInject: StatusBarColor? = null

    /**
     * 是否是竖直方向
     */
    var orientationInject: Orientation? = null

    var permission: Permission? = null

    var dont_request_orientation = false

    var immersion = false

    var topBarId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        //初始化注解
        initAnnotation()

        //设置屏幕方向
        if (!dont_request_orientation) {
            try {
                requestedOrientation = if (orientationInject == null) {
                    //默认是竖直方向
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                } else {
                    if (orientationInject?.isVertical!!)
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    else
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
            } catch (e: IllegalStateException) {

            }
        }

        //加载布局
        if (viewInject != null) {
            setContentView(viewInject!!.id)
        }

        if (colorInject != null) {
            StatusBarCompat.setStatusBarColor(this, Color.parseColor(colorInject?.color))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarCompat.setStatusBarColor(this, Color.WHITE)
        }

        val winContent = findViewById<ViewGroup>(android.R.id.content)
        winContent.setBackgroundColor(GlobalConfig.getGlobalBgColor())

        beforeInit(savedInstanceState)

        init(savedInstanceState)

        if (startEventBus) {
            EventBus.getDefault().register(this)
        }

        actList.add(this)
    }


    private fun initAnnotation() {
        val annotations = this::class.annotations
        annotations.forEachIndexed { _, it ->
            when (it.annotationClass) {
                LayoutId::class -> {
                    viewInject = it as LayoutId
                }
                StatusBarColor::class -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) colorInject =
                        it as StatusBarColor
                }
                Orientation::class -> {
                    orientationInject = it as Orientation
                }
                Permission::class -> {
                    permission = it as Permission
                    val permissions = permission?.permissions
                    permissions?.let {
                        PermissionUtils.req(this, {
                            granted()
                        }, {
                            shouldShowRequestPermissionRationale()
                        }, {
                            needGoToSettingsPage()
                        }, *it)
                    }
                }
                Bus::class -> {
                    startEventBus = true
                }
                StatusBarBlackText::class -> {
                    setStatusBarTextBlack(this, true)
                }
                FullScreen::class -> {
                    hideStatusBar(this, true)
                }
                NoReqOrientation::class -> {
                    dont_request_orientation = true
                }
                Immersion::class -> {
                    immersion = true
                    topBarId = (it as Immersion).topBarId
                }
            }
        }
    }

    open fun shouldShowRequestPermissionRationale() {}

    open fun needGoToSettingsPage() {}

    open fun granted() {}

    protected open fun beforeInit(savedInstanceState: Bundle?) {}

    protected abstract fun init(bundle: Bundle?)

    companion object {
        /**
         * 所有的Activity共享一个gson
         */
        var gson = Gson()

        /**
         * 用于储存所有的Activity实例
         */
        var actList = ArrayList<BaseActivity>()

        /**
         * 判断在Activity栈中是否包含某个Activity，传入简名即可
         */
        fun containActivity(simpleName: String): Boolean {
            return actList.find { it.javaClass.simpleName == simpleName } != null
        }

        /**
         * 获取位于栈顶的Activity
         * @return BaseActivity
         */
        @JvmStatic
        fun getStackTopActivity(): BaseActivity {
            return actList.last()
        }

        /**
         * 根据Activity的SimpleName来关闭它
         * @param actName String
         */
        fun finishActivityByName(actName: String) {
            for (activity in actList) {
                if (activity.javaClass.simpleName == actName) {
                    activity.finish()
                    //return
                }
            }
        }

        /**
         * 根据1~N个Activity的SimpleName来关闭它（们）
         * @param actName Array<out String>
         */
        fun finishActivityByName(vararg actName: String) {
            for (activity in actList) {
                if (activity.javaClass.simpleName in actName) {
                    activity.finish()
                }
            }
        }

        /**
         * 关闭所有的Activity
         */
        fun finishAllActivities() {
            LogUtils.d("ActivityName", "size is " + actList.size)
            for (activity in actList) {
                LogUtils.d("ActivityName", activity.javaClass.simpleName)
                activity.finish()
            }
        }

        /**
         * 关闭所有的Activity除了指定的Activity
         */
        fun finishAllActivitiesExcept(vararg actName: String) {
            val list = actName.toMutableList()
            for (activity in actList) {
                if (!list.contains(activity.javaClass.simpleName)) {
                    activity.finish()
                }
            }
        }

        /**
         * 关闭栈中最上一层Activity
         */
        fun finishLast() {
            actList.last().finish()
        }

        /**
         * 根据类名来在栈中查找Activity，可能为空，可替代EventBus来直接调用要查找的Activity的方法
         */
        fun <T : BaseActivity> findActivity(simpleName: String): T? {
            return actList.find { it.javaClass.simpleName == simpleName } as? T
        }

    }

    /**
     * 用于耗时操作前显示loading
     */
    var showLoadingCallback: (() -> Unit)? = null

    /**
     * 用于在耗时操作后结束loading
     */
    var hideLoadingCallback: (() -> Unit)? = null

    /**
     * 当HTTP请求失败时触发的回调
     */
    var onHttpErrorCallback: ((message: String) -> Unit)? = null

    fun onHttpError(message: String) {
        onHttpErrorCallback?.invoke(message)
    }

    fun showLoading() {
        showLoadingCallback?.invoke()
    }

    fun hideLoading() {
        hideLoadingCallback?.invoke()
    }

    /**
     * 启动Activity
     */
    inline fun <reified T : Activity> goTo(needFinish: Boolean = false) {
        startActivity(Intent(this, T::class.java))
        if (needFinish) finish()
    }

    /**
     * 启动Activity，可带参数和跳转动画
     */
    inline fun <reified T : Activity> goTo(
        vararg pairs: Pair<String, Any>,
        anims: Pair<Int, Int> = 0 to 0,
        needFinish: Boolean = false
    ) {

        val intent = Intent(this, T::class.java)
        pairs.forEach {
            when (it.second) {
                is String -> {
                    intent.putExtra(it.first, it.second.toString())
                }
                is Int -> {
                    intent.putExtra(it.first, it.second as Int)
                }
                is Boolean -> {
                    intent.putExtra(it.first, it.second as Boolean)
                }
                is Double -> {
                    intent.putExtra(it.first, it.second as Double)
                }
                is java.io.Serializable -> intent.putExtra(
                    it.first,
                    it.second as java.io.Serializable
                )
                is Parcelable -> intent.putExtra(it.first, it.second as Parcelable)
            }
            LogUtils.d("T_BUNDLE", ("${it.first} ${it.second}"))
        }
        startActivity(intent)
        if (anims != 0 to 0) overridePendingTransition(anims.first, anims.second)
        if (needFinish) finish()
    }

    fun inflate(id: Int): View {
        return LayoutInflater.from(this).inflate(id, null)
    }

    private var handler: Handler? = null

    /**
     * 执行延迟任务（使用Handler实现）
     * @param delay Long
     * @param callback Function0<Unit>
     */
    fun doDelayTask(delay: Long, callback: () -> Unit) {
        if (handler == null) {
            handler = Handler()
        }
        handler?.postDelayed({
            callback.invoke()
        }, delay)
    }

    /**
     * 执行延迟任务(使用Timer实现)
     * @param delay Long
     * @param callback Function0<Unit>
     */
    fun doDelayTask1(delay: Long, callback: () -> Unit) {
        var countDownTimer: Timer? = null
        countDownTimer = TimerUtils.schedule(delay) {
            runOnUiThread {
                callback.invoke()
                countDownTimer?.cancel()
            }
        }
    }

    override fun onDestroy() {
        LogUtils.d("ActivityName", "移除了 " + javaClass.simpleName)
        actList.remove(this)
        if (startEventBus) {
            EventBus.getDefault().unregister(this)
        }
        handler?.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    var exitTime = 0L

    /**
     * 退出应用验证
     * @param msg String
     * @param time Long
     */
    fun doExitVerify(msg: String = "再按一次退出应用", time: Long = 2000) {
        if ((System.currentTimeMillis() - exitTime) > time) {
            msg.toast()
            exitTime = System.currentTimeMillis()
        } else {
            finishAllActivities()
        }
    }

}