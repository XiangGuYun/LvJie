package com.yp.baselib.ex

import android.support.v7.widget.*
import android.text.Html
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.kotlinlib.common.Holder
import com.kotlinlib.common.StringEx
import com.yp.baselib.utils.RVUtils


interface RvEx : StringEx {
    /**
     * 设置适配器
     * @receiver RVUtils
     * @param data ArrayList<T> 数据集合
     * @param fun1 (holder: EasyRVHolder, pos:Int)->Unit 绑定数据
     * @param itemId Int 列表项ID
     * @return RVUtils
     */
    fun <T> RVUtils.rvAdapter(data: ArrayList<T>?,
                              fun1: (holder: Holder, pos: Int) -> Unit,
                              itemId: Int): RVUtils {
        adapter(data, RVUtils.onBindData(fun1),
                RVUtils.SetMultiCellView { 0 }, itemId)
        return this
    }

    fun <T> RVUtils.rvAdapter(data: List<T>?,
                              fun1: (holder: Holder, pos: Int) -> Unit,
                              itemId: Int): RVUtils {
        adapter(data, RVUtils.onBindData(fun1),
                RVUtils.SetMultiCellView { 0 }, itemId)
        return this
    }


    /**
     * 遍历RecyclerView的子视图
     * @receiver RecyclerView
     * @param fun1 (i:Int,it:View)->Unit
     */
    fun RecyclerView.foreachIndexed(fun1: (i: Int, it: View) -> Unit) {
        for (i in 0 until childCount) {
            fun1.invoke(i, getChildAt(i))
        }
    }

    fun RecyclerView.foreach(fun1: (it: View) -> Unit) {
        for (i in 0 until childCount) {
            fun1.invoke(getChildAt(i))
        }
    }

    /**
     * 设置多个列表项布局的适配器
     * @receiver RVUtils
     * @param data ArrayList<T>
     * @param fun1 (holder: com.kotlinlib.common.Holder, pos:Int)->Unit
     * @param fun2 (Int)->Int
     * @param itemId IntArray 传入可变长度的ID数组
     * @return RVUtils
     */
    fun <T> RVUtils.rvMultiAdapter(data: ArrayList<T>,
                                   fun1: (holder: Holder, pos: Int) -> Unit,
                                   fun2: (pos: Int) -> Int,
                                   vararg itemId: Int): RVUtils {
        adapter(data, RVUtils.onBindData(fun1),
                RVUtils.SetMultiCellView(fun2), *itemId)
        return this
    }

    fun <T> RVUtils.rvMultiAdapter(data: List<T>,
                                   fun1: (holder: Holder, pos: Int) -> Unit,
                                   fun2: (pos: Int) -> Int,
                                   vararg itemId: Int): RVUtils {
        adapter(data, RVUtils.onBindData(fun1),
                RVUtils.SetMultiCellView(fun2), *itemId)
        return this
    }

    /**
     * 设置带有HeaderView的适配器
     * @receiver RVUtils
     * @param data List<T>
     * @param headerViewId Int
     * @param handleHeaderView (holder:Holder)->Unit
     * @param handleNormalView (holder:Holder,pos:Int)->Unit
     * @param handleNormalLayoutIndex (pos:Int)->Int
     * @param itemId IntArray
     */
    fun <T> RVUtils.rvAdapterH(data: List<T>,
                               headerViewId: Int,
                               handleHeaderView: (holder: Holder) -> Unit,
                               handleNormalView: (holder: Holder, pos: Int) -> Unit,
                               handleNormalLayoutIndex: (pos: Int) -> Int,
                               vararg itemId: Int) {
        needHeader = true
        rvMultiAdapter(data, { holder, pos ->
            when (pos) {
                0 -> {
                    handleHeaderView.invoke(holder)
                }
                else -> {
                    handleNormalView.invoke(holder, pos)
                }
            }
        }, {
            when (it) {
                0 -> 0
                else -> handleNormalLayoutIndex.invoke(it) + 1
            }
        }, headerViewId, *itemId)
    }

    /**
     * 设置带有HeaderView和FooterView的适配器
     * @receiver RVUtils
     * @param data List<T>
     * @param headerViewId Int
     * @param handleHeaderView (holder:Holder)->Unit
     * @param handleNormalView (holder:Holder,pos:Int)->Unit
     * @param handleNormalLayoutIndex (pos:Int)->Int
     * @param itemId IntArray
     */
    fun <T> RVUtils.rvAdapterHF(data: List<T>,
                                headerViewId: Int,
                                handleHeaderView: (headerHolder: Holder) -> Unit,
                                footerViewId: Int,
                                handleFooterView: (footerHolder: Holder) -> Unit,
                                handleNormalView: (normalHolder: Holder, pos: Int) -> Unit,
                                handleNormalLayoutIndex: (pos: Int) -> Int,
                                vararg itemId: Int): RVUtils {
        needHeader = true
        needFooter = true
        rvMultiAdapter(data, { holder, pos ->
            when (pos) {
                0 -> {
                    handleHeaderView.invoke(holder)
                }
                data.lastIndex -> {
                    handleFooterView.invoke(holder)
                }
                else -> {
                    handleNormalView.invoke(holder, pos)
                }
            }
        }, {
            when (it) {
                0 -> 0
                data.lastIndex -> 1
                else -> handleNormalLayoutIndex.invoke(it) + 2
            }
        }, headerViewId, footerViewId, *itemId)
        return this
    }

    /**
     * 简化ViewHolder的view获取，无需指定泛型
     */
    fun Holder.v(id: Int): View {
        return getView(id)
    }

    fun Holder.vNull(id: Int): View? {
        return getView(id)
    }

    /**
     * 简化ViewHolder的view获取，需要指定泛型
     */
    fun <T : View> Holder.view(id: Int): T {
        return getView(id)
    }

    /**
     * 简化ViewHolder的ImageView获取
     */
    fun Holder.iv(id: Int): ImageView {
        return getView(id)
    }

    /**
     * 简化ViewHolder的TextView获取
     */
    fun Holder.tv(id: Int): TextView {
        return getView(id)
    }

    fun Holder.tvNull(id: Int): TextView? {
        return getView(id)
    }

    /**
     * 简化ViewHolder的RecyclerView获取
     */
    fun Holder.rv(id: Int): RecyclerView {
        return getView(id)
    }

    /**
     * 简化ViewHolder的EditText获取
     */
    fun Holder.et(id: Int): EditText {
        return getView(id)
    }

    /**
     *  简化setImageResource，且返回Holder
     */
    fun Holder.ir(ivId: Int, imgId: Int): Holder {
        setImageResource(ivId, imgId)
        return this
    }

    /**
     *  简化setText，且返回Holder
     */
    fun Holder.text(id: Int, text: String?): Holder {
        if (text.isNullOrEmpty()) {
            setText(id, "")
        } else {
            setText(id, text)
        }

        return this
    }

    /**
     *  简化setTextColor，且返回Holder
     */
    fun Holder.color(id: Int, color: Int): Holder {
        setTextColor(id, color)
        return this
    }

    /**
     *  简化setOnClickListener，且返回Holder
     */
    fun Holder.click(id: Int, onClick: (View) -> Unit): Holder {
        v(id).setOnClickListener(onClick)
        return this
    }

    /**
     *  简化setOnItemViewClickListener，且返回Holder
     */
    fun Holder.itemClick(click: (view: View) -> Unit): Holder {
        setOnItemViewClickListener(click)
        return this
    }

    /**
     *  简化ItemView的setOnLongClickListener，且返回Holder
     */
    fun Holder.itemLongClick(click: (view: View) -> Unit): Holder {
        getItemView().setOnLongClickListener {
            click.invoke(it)
            return@setOnLongClickListener true
        }
        return this
    }

    /**
     *  简化设置Html格式文本，且返回Holder
     */
    fun Holder.htmlText(id: Int, html: String): Holder {
        getView<TextView>(id).text = Html.fromHtml(html)
        return this
    }

    /**
     * 添加分割线
     */
    fun RVUtils.decorate(drawableId: Int, isVertical: Boolean = true): RVUtils {
        val divider = DividerItemDecoration(context, if (isVertical) DividerItemDecoration.VERTICAL else DividerItemDecoration.HORIZONTAL)
        divider.setDrawable(context.resources.getDrawable(drawableId))
        rv.addItemDecoration(divider)
        return this
    }

    fun RVUtils.decorate(isVertical: Boolean = true): RVUtils {
        rv.addItemDecoration(DividerItemDecoration(context, if (isVertical) DividerItemDecoration.VERTICAL else DividerItemDecoration.HORIZONTAL))
        return this
    }

    fun RVUtils.decorate(decoration: RecyclerView.ItemDecoration): RVUtils {
        rv.addItemDecoration(decoration)
        return this
    }

    /**
     * 设置线性吸附
     */
    fun RVUtils.snapLinear(): RVUtils {
        val helper = LinearSnapHelper()
        helper.attachToRecyclerView(rv)
        return this
    }

    /**
     * 设置页面吸附
     */
    fun RVUtils.snapPager(): RVUtils {
        pagerHelper = PagerSnapHelper()
        pagerHelper.attachToRecyclerView(rv)
        return this
    }

    /**
     * 设置自定义吸附
     */
    fun RVUtils.customSnap(set: (rv: RecyclerView) -> Unit): RVUtils {
        set.invoke(rv)
        return this
    }

    fun RVUtils.customSnap(snapHelper: SnapHelper): RVUtils {
        snapHelper.attachToRecyclerView(rv)
        return this
    }

    /**
     * 设置Item增删动画
     *
    Cool
    LandingAnimator

    Scale
    ScaleInAnimator, ScaleInTopAnimator, ScaleInBottomAnimator
    ScaleInLeftAnimator, ScaleInRightAnimator

    Fade
    FadeInAnimator, FadeInDownAnimator, FadeInUpAnimator
    FadeInLeftAnimator, FadeInRightAnimator

    Flip
    FlipInTopXAnimator, FlipInBottomXAnimator
    FlipInLeftYAnimator, FlipInRightYAnimator

    Slide
    SlideInLeftAnimator, SlideInRightAnimator, OvershootInLeftAnimator, OvershootInRightAnimator
    SlideInUpAnimator, SlideInDownAnimator
     */
    fun <T : RecyclerView.ItemAnimator> RVUtils.anim(anim: T?): RVUtils {
        if (anim == null) {
            rv.itemAnimator = DefaultItemAnimator()
        } else {
            rv.itemAnimator = anim
        }
        return this
    }

    /**
     * 滚动到指定位置，指定位置会完整地出现在屏幕的最下方
     */
    fun <T> RecyclerView.scrollTo(position: Int, list: List<T>) {
        if (position >= 0 && position <= list.size - 1) {
            val firstItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            val lastItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            if (position <= firstItem) {
                scrollToPosition(position)
            } else if (position <= lastItem) {
                val top = getChildAt(position - firstItem).top
                scrollBy(0, top)
            } else {
                scrollToPosition(position)
            }
        }
    }

    /**
     * 删除动画
     */
    fun <T> RecyclerView.deleteAnim(pos: Int, list: MutableList<T>) {
        list.removeAt(pos)
        adapter?.notifyItemRemoved(pos)
        adapter?.notifyItemRangeChanged(pos, list.size - pos)
    }

    fun <T : RecyclerView.LayoutManager> RecyclerView.lm(): T {
        return layoutManager as T
    }

    fun RecyclerView.onScroll(callback: (dx: Int, dy: Int) -> Unit) {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                callback.invoke(dx, dy)
            }
        })
    }

    val RecyclerView.wrap: RVUtils get() = RVUtils(this)

    /**
     * 刷新RecyclerView
     * @receiver RecyclerView
     */
    fun RecyclerView.update() {
        this.adapter?.notifyDataSetChanged()
    }

}