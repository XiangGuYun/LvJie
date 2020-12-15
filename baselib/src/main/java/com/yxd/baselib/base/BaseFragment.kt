package com.yxd.baselib.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.yxd.baselib.annotation.Bus
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.ex.BaseEx
import com.yxd.baselib.utils.DensityUtils
import org.greenrobot.eventbus.EventBus

/**
 * Fragment的基类
 */
abstract class BaseFragment : Fragment(), BaseEx {

    companion object {
        lateinit var gson:Gson
    }

    private var startEventBus = false
    private var viewInject: LayoutId? = null
    lateinit var fragView:View

    /**
     * 获取托管Activity并指定具体类型
     */
    fun <T : Activity> act(): T {
        return activity!! as T
    }

    /**
     * 获取托管Activity但不指定具体类型
     */
    fun getAct(): BaseActivity {
        return activity as BaseActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val annotations = this::class.annotations
        annotations.forEachIndexed { _, it->
            when(it.annotationClass){
                LayoutId::class->{
                    viewInject = it as LayoutId
                }
                Bus::class->{
                    startEventBus = true
                    EventBus.getDefault().register(this)
                }
            }
        }
        fragView = if(viewInject!=null){
            inflater.inflate(viewInject!!.id,container,false)
        }else{
            inflater.inflate(getFragLayoutId(),container,false)
        }
        // 防止点击穿透
        fragView.rootView.setOnTouchListener { v, event -> true }
        gson = BaseActivity.gson
        return fragView
    }

    /**
     * 如果不使用注解，可以重写此方法来返回布局
     */
    protected open fun getFragLayoutId(): Int {
        return 0
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //初始化代码需要放在onActivityCreated中，否则容易发生空指针异常
        init()
    }

    abstract fun init()


    /**
     * 跳转新的Activity
     */
    inline fun <reified T: Activity> goTo(vararg pairs:Pair<String,Any>, anims:Pair<Int, Int> = 0 to 0){
        getAct().goTo<T>(*pairs, anims = anims)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(startEventBus){
            EventBus.getDefault().unregister(this)
        }
    }

}