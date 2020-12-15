package com.yxd.baselib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;
import com.yxd.baselib.utils.LogUtils;

/**
 * 控制左右滑动的ViewPager
 *
 * @author 86139
 */
public class NoScrollViewPager extends ViewPager {
    /**
     * 能否左右滑动
     */
    private boolean isScrollable = false;

    /**
     * 能否向右滑动
     */
    private boolean isRightScrollable = false;

    /**
     * 能否向左滑动
     */
    private boolean isLeftScrollable = false;

    private float downX;

    private float downY;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    /**
     * 1.dispatchTouchEvent一般情况不做处理
     * ,如果修改了默认的返回值,子孩子都无法收到事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // return true;不行
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否拦截
     * 拦截:会走到自己的onTouchEvent方法里面来
     * 不拦截:事件传递给子孩子
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // return false;//可行,不拦截事件,
        // return true;//不行,孩子无法处理事件
        //return super.onInterceptTouchEvent(ev);//不行,会有细微移动

        if (isScrollable) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    /**
     * 是否消费事件
     * 消费:事件就结束
     * 不消费:往父控件传
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String action = "";
        //在触发时回去到起始坐标
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //将按下时的坐标存储
                downX = x;
                downY = y;
                Log.e("YXD_NVP", "=======按下时X：" + x);
                Log.e("YXD_NVP", "=======按下时Y：" + y);
                break;
            case MotionEvent.ACTION_UP:
                Log.e("YXD_NVP", "=======抬起时X：" + x);
                Log.e("YXD_NVP", "=======抬起时Y：" + y);

                //获取到距离差
                float dx = x - downX;
                float dy = y - downY;
                //防止是按下也判断
                if (Math.abs(dx) > 8 && Math.abs(dy) > 8) {
                    //通过距离差判断方向
                    int orientation = getOrientation(dx, dy);
                    switch (orientation) {
                        case 'r':
                            action = "右";
                            break;
                        case 'l':
                            action = "左";
                            break;
                        case 't':
                            action = "上";
                            break;
                        case 'b':
                            action = "下";
                            break;
                        default:
                    }
                    LogUtils.d("YXD_NVP", "向" + action + "滑动");
                }
                break;
            default:
        }
        //return false;// 可行,不消费,传给父控件
        //return true;// 可行,消费,拦截事件
        //super.onTouchEvent(ev); //不行,
        //虽然onInterceptTouchEvent中拦截了,
        //但是如果viewpage里面子控件不是viewgroup,还是会调用这个方法.
        if (isScrollable) {
            return super.onTouchEvent(event);
        } else {
            // 可行,消费,拦截事件
            return true;
        }
    }

    public void setScroll(boolean scroll) {
        isScrollable = scroll;
    }

    @Override
    public void setCurrentItem(int item) {
        //去除页面切换时的滑动翻页效果
        super.setCurrentItem(item, false);
    }

    /**
     * 根据距离差判断 滑动方向
     *
     * @param dx X轴的距离差
     * @param dy Y轴的距离差
     * @return 滑动的方向
     */
    private int getOrientation(float dx, float dy) {
        LogUtils.e("YXD_NVP", "========X轴距离差：" + dx);
        LogUtils.e("YXD_NVP", "========Y轴距离差：" + dy);
        if (Math.abs(dx) > Math.abs(dy)) {
            //X轴移动
            return dx > 0 ? 'r' : 'l';
        } else {
            //Y轴移动
            return dy > 0 ? 'b' : 't';
        }
    }

}