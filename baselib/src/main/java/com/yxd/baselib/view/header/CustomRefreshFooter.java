package com.yxd.baselib.view.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.yxd.baselib.R;

/**
 * SmartRefreshLayout的自定义Footer
 */

public class CustomRefreshFooter extends LinearLayout implements RefreshFooter {
    private ImageView mImage;
    private Animation mAnim;

    public CustomRefreshFooter(Context context) {
        super(context, null, 0);
    }

    public CustomRefreshFooter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CustomRefreshFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.widget_custom_refresh_footer, this);
        mImage = view.findViewById(R.id.tv_refresh_footer);
        mAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_round_rotate);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        mAnim.setInterpolator(linearInterpolator);
        addView(view);
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        //控制是否稍微上滑动就刷新
        kernel.getRefreshLayout().setEnableAutoLoadMore(false);
    }

    @Override
    public void onPulling(float percent, int offset, int height, int extendHeight) {

    }

    @Override
    public void onReleasing(float percent, int offset, int height, int extendHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        if (mAnim != null && mAnim.hasStarted() && !mAnim.hasEnded()) {
            mAnim.cancel();
            mImage.clearAnimation();
        }
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullUpToLoad:
                if (mAnim != null) {
                    mImage.startAnimation(mAnim);
                }
                break;
            case Loading:

            case LoadReleased:

                break;
            case ReleaseToLoad:
                break;
        }
    }

}