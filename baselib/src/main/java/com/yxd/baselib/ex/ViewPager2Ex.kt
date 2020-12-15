package com.yxd.baselib.ex

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yxd.baselib.LLLP
import com.yxd.baselib.base.BaseActivity
import com.yxd.baselib.base.BaseFragment
import com.yxd.baselib.utils.DensityUtils
import com.yxd.baselib.view.indicator.ViewPagerHelper
import com.yxd.baselib.view.indicator.YxdIndicator
import com.yxd.baselib.view.indicator.buildins.commonnavigator.CommonNavigator
import com.yxd.baselib.view.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import com.yxd.baselib.view.indicator.buildins.commonnavigator.abs.IPagerIndicator
import com.yxd.baselib.view.indicator.buildins.commonnavigator.abs.IPagerTitleView
import com.yxd.baselib.view.indicator.buildins.commonnavigator.indicators.LinePagerIndicator
import com.yxd.baselib.view.rv.YxdRVAdapter
import com.yxd.baselib.view.rv.YxdRVHolder
import java.lang.reflect.Field

interface ViewPager2Ex: ViewEx, ContextEx {

    /**
     * 设置当前可见页面的两边应该保留的页面数。超过此限制的页面将在需要时从适配器重新创建。
     *
     * ViewPager2默认就实现了懒加载。
     * 但是如果想避免Fragment频繁销毁和创建造成的开销，可以通过setOffscreenPageLimit()方法设置预加载数量，
     * 将数据加载逻辑放到Fragment的onResume()方法中。
     *
     * @param limit Int
     * @return ViewPager2
     */
    fun ViewPager2.setOffscreenPageLimit(limit:Int): ViewPager2 {
        offscreenPageLimit = limit
        return this
    }

    /**
     * 设置ViewPager2是否为垂直方向
     *
     * @param boolean Boolean
     * @return ViewPager2
     */
    fun ViewPager2.setVertical(boolean: Boolean = true): ViewPager2 {
        if (boolean) {
            this.orientation = ViewPager2.ORIENTATION_VERTICAL
        } else {
            this.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
        return this
    }

    /**
     * 设置允许用户滑动ViewPager2
     *
     * @param boolean Boolean
     * @return ViewPager2
     */
    fun ViewPager2.setAllowUserInput(boolean: Boolean = true): ViewPager2 {
        this.isUserInputEnabled = boolean;
        return this
    }

    /**
     * 绑定1~N个View
     *
     * @param data R
     * @param bindItemWithData
     * @param getLayoutIndex
     * @param itemLayoutId
     * @return ViewPager2
     */
    fun <T, R : List<T>> ViewPager2.generate(
            data: R,
            bindItemWithData: (h: YxdRVHolder, i: Int, it: T) -> Unit,
            getLayoutIndex: ((i: Int) -> Int)? = null,
            vararg itemLayoutId: Int
    ): ViewPager2 {
        this.adapter = object : YxdRVAdapter<T>(context, data, *itemLayoutId) {
            override fun onBindData(
                    viewHolder: YxdRVHolder,
                    position: Int,
                    item: T
            ) {
                bindItemWithData.invoke(viewHolder, position, item)
            }

            override fun getLayoutIndex(layoutIndex: Int, item: T): Int {
                return getLayoutIndex?.invoke(layoutIndex) ?: 0
            }
        }
        return this
    }

    /**
     * 绑定1~N个Fragment
     *
     * @param activity BaseActivity
     * @param fragments R
     * @return ViewPager2
     */
    fun <T : BaseFragment, R : List<T>> ViewPager2.bindFragment(
            activity: BaseActivity,
            fragments: R
    ): ViewPager2 {
        adapter = object : FragmentStateAdapter(activity) {
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }

        }
        return this
    }

    /**
     * 绑定TabLayout
     *
     * @param tl TabLayout
     * @param isScroll Boolean
     * @param leftRightMargin Pair<Int, Int>
     * @param listener Function2<Tab, Int, Unit>
     */
    @Deprecated("老版本")
    fun ViewPager2.bindTL(
            tl: TabLayout,
            isScroll: Boolean = false,
            leftRightMargin: Pair<Int, Int> = 0 to 0,
            listener: (TabLayout.Tab, Int) -> Unit
    ) {
        val vp = this
        val fragments = vp.adapter?.itemCount?:0
        if (isScroll) {
            tl.tabMode = TabLayout.MODE_SCROLLABLE //设置滑动Tab模式
        } else {
            tl.tabMode = TabLayout.MODE_FIXED //设置固定Tab模式
        }
        setIndicator(tl, leftRightMargin.first, leftRightMargin.second)
        for (i in 0 until fragments) {
            val tab = tl.newTab()
            tl.addTab(tab)
        }
        //将TabLayout和ViewPager关联起来
        tl.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                vp.currentItem = tab?.position!!
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tl.selectTab(tl.getTabAt(position))
            }
        })
        //Tab属性必须在关联ViewPager之后设置
        for (i in 0 until fragments) {
            listener.invoke(tl.getTabAt(i)!!, i)
        }
    }

    /**
     * 绑定TabLayout，支持刷新（刷新需要先detach，再重新attach）
     * @receiver ViewPager2
     * @param tabs TabLayout
     * @param listener Function2<Tab, Int, Unit>
     */
    fun ViewPager2.bindTL(tabs: TabLayout, listener: (TabLayout.Tab, Int) -> Unit){
        val tlMediator = TabLayoutMediator(tabs, this){tab, i ->
            listener.invoke(tab, i)
        }
        tlMediator.attach()
    }

    /**
     * 绑定YxdIndicator
     * @receiver ViewPager2
     * @param indicator YxdIndicator
     * @param tabSize Int
     * @param indicatorColor Int
     * @param indicatorWidth Int
     * @param indicatorHeight Int
     * @param cornerRadius Int
     * @param tabWidth Int
     * @param getTabView Function1<[@kotlin.ParameterName] Int, View>
     */
    fun ViewPager2.bindIndicator(
            indicator: YxdIndicator,
            tabSize: Int,
            indicatorColor: Int,
            indicatorWidth: Int = -1,
            indicatorHeight: Int = 2,
            cornerRadius: Int = 0,
            tabWidth: Int = -1,
            getTabView: (index: Int) -> View
    ) {
        val vp = this
        indicator.navigator = CommonNavigator(indicator.context).apply {
            adapter = object : CommonNavigatorAdapter() {
                override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                    val tabView = getTabView.invoke(index)
                    if (tabView !is IPagerTitleView) {
                        throw ClassCastException("${tabView.javaClass.name}未实现IPagerTitleView接口")
                    }
                    tabView.setOnClickListener {
                        vp.currentItem = index
                    }
                    tabView.post {
                        tabView.doLP<LLLP> {
                            if (tabWidth == -1) {
                                it.width = context!!.srnWidth / tabSize
                            } else {
                                it.width = tabWidth
                            }
                        }
                    }
                    return tabView
                }

                override fun getCount(): Int {
                    return tabSize
                }

                override fun getIndicator(context: Context?): IPagerIndicator {
                    return LinePagerIndicator(context).apply {
                        mode = if (indicatorWidth == -1) {
                            LinePagerIndicator.MODE_MATCH_EDGE
                        } else {
                            LinePagerIndicator.MODE_EXACTLY
                        }
                        roundRadius =
                                DensityUtils.dp2px(cornerRadius.toFloat()).toFloat()
                        if (indicatorWidth != -1)
                            lineWidth =
                                    DensityUtils.dp2px(indicatorWidth.toFloat()).toFloat()
                        lineHeight =
                                DensityUtils.dp2px(indicatorHeight.toFloat()).toFloat()
                        setColors(indicatorColor)
                    }
                }

            }
        }
        vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                indicator.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                indicator.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
            ) {
                indicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        })
    }

    /**
     * 设置TabLayout指示器的左右边距
     * @param tabs TabLayout
     * @param leftDip Int
     * @param rightDip Int
     */
    private fun setIndicator(tabs: TabLayout, leftDip: Int, rightDip: Int) {
        val tabLayout: Class<*> = tabs.javaClass
        var tabStrip: Field? = null
        try {
            tabStrip = tabLayout.getDeclaredField("slidingTabIndicator")
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        tabStrip!!.isAccessible = true
        var llTab: LinearLayout? = null
        try {
            llTab = tabStrip[tabs] as LinearLayout
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        val left = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                leftDip.toFloat(),
                Resources.getSystem().displayMetrics
        ).toInt()
        val right = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                rightDip.toFloat(),
                Resources.getSystem().displayMetrics
        ).toInt()
        for (i in 0 until llTab!!.childCount) {
            val child = llTab.getChildAt(i)
            child.setPadding(0, 0, 0, 0)
            val params =
                    LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1f
                    )
            params.leftMargin = left
            params.rightMargin = right
            child.layoutParams = params
            child.invalidate()
        }
    }

}