//package com.yxd.baselib.view.header;
//
//import android.content.Context;
//import android.graphics.drawable.AnimationDrawable;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.scwang.smartrefresh.layout.api.RefreshHeader;
//import com.scwang.smartrefresh.layout.api.RefreshKernel;
//import com.scwang.smartrefresh.layout.api.RefreshLayout;
//import com.scwang.smartrefresh.layout.constant.RefreshState;
//import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
//import com.yxd.baselib.R;
//
///**
// * SmartRefreshLayout 的自定义下拉刷新 Header
// */
//
//public class CustomRefreshHeader extends LinearLayout implements RefreshHeader {
//
//    private ImageView mImage;
//    private AnimationDrawable pullDownAnim;
//    private AnimationDrawable refreshingAnim;
//
//    /**
//     * 构造方法
//     */
//    public CustomRefreshHeader(Context context) {
//        this(context, null, 0);
//    }
//
//    public CustomRefreshHeader(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public CustomRefreshHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        View view = View.inflate(context, R.layout.widget_custom_refresh_header, this);
//        mImage = view.findViewById(R.id.iv_refresh_header);
//    }
//
//    /**
//     * 2，获取真实视图（必须返回，不能为null）一般就是返回当前自定义的view
//     */
//    @NonNull
//    @Override
//    public View getView() {
//        return this;
//    }
//
//    /**
//     * 3，获取变换方式（必须指定一个：平移、拉伸、固定、全屏）,Translate指平移，大多数都是平移
//     */
//    @Override
//    public SpinnerStyle getSpinnerStyle() {
//        return SpinnerStyle.Translate;
//    }
//
//    /**
//     * 4，执行下拉的过程
//     *
//     * @param isDragging
//     * @param percent
//     * @param offset
//     * @param height
//     * @param maxDragHeight
//     */
//    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
////        if (percent < 1) {
////            mImage.setScaleX(percent);
////            mImage.setScaleY(percent);
////        }
//    }
//
//    @Override
//    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {
//
//    }
//
//    /**
//     * 5，一般可以理解为一下case中的三种状态，在达到相应状态时候开始改变
//     * 注意：这三种状态都是初始化的状态
//     */
//    @Override
//    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
//        switch (newState) {
//            case PullDownToRefresh: //下拉刷新开始。正在下拉还没松手时调用
//                //每次重新下拉时，将图片资源重置为小人的大脑袋
//                mImage.setImageResource(R.drawable.activity_donghua);
//                break;
//            case Refreshing: //正在刷新。只调用一次
//                //状态切换为正在刷新状态时，设置图片资源为小人卖萌的动画并开始执行
//                mImage.setImageResource(R.drawable.activity_donghua);
//                refreshingAnim = (AnimationDrawable) mImage.getDrawable();
//                refreshingAnim.start();
//                break;
//            case ReleaseToRefresh:
//                mImage.setImageResource(R.drawable.activity_donghua);
//                pullDownAnim = (AnimationDrawable) mImage.getDrawable();
//                pullDownAnim.start();
//                break;
//        }
//    }
//
//    /**
//     * 动画结束后调用
//     */
//    @Override
//    public int onFinish(RefreshLayout layout, boolean success) {
//        // 结束动画
//        if (refreshingAnim != null && refreshingAnim.isRunning()) {
//            refreshingAnim.stop();
//        }
//        if (pullDownAnim != null && pullDownAnim.isRunning()) {
//            pullDownAnim.stop();
//        }
//        return 0;
//    }
//    @Override
//    public void setPrimaryColors(int... colors) {
//
//    }
//
//    @Override
//    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
//
//    }
//
//    @Override
//    public void onPulling(float percent, int offset, int height, int extendHeight) {
//
//    }
//
//    @Override
//    public void onReleasing(float percent, int offset, int height, int extendHeight) {
//
//    }
//
//
//    @Override
//    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
//
//    }
//
//    @Override
//    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
//
//    }
//
//    @Override
//    public boolean isSupportHorizontalDrag() {
//        return false;
//    }
//}
