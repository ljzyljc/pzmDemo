package cn.android.support.v7.lib.sin.crown.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import java.util.HashMap;
import java.util.Map;

/**
 * 弹性ScrollView【滑动原理，对scrollview里面的第一个View进行位置上下偏移滑动。】
 * Created by 彭治铭 on 2018/4/25.
 */
public class BounceScrollView extends NestedScrollView {

    private View inner;// 孩子View

    private float y;// 点击时y坐标

    private Rect normal = new Rect();// 矩形(这里只是个形式，只是用于判断是否需要动画.)

    private boolean isCount = false;// 是否开始计算
    private float lastX = 0;
    private float lastY = 0;
    private float currentX = 0;
    private float currentY = 0;
    private float distanceX = 0;
    private float distanceY = 0;
    private boolean upDownSlide = false; //判断上下滑动的flag

    public BounceScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BounceScrollView(@NonNull Context context) {
        super(context);
    }

    /***
     * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
     * 方法，也应该调用父类的方法，使该方法得以执行.
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
    }

    public boolean isSlidingDown = false;//是否下滑。true 下滑，false上滑

    private boolean isDownAnime = true;
    private boolean isUpAnime = true;

    public boolean openUpAnime = true;//fixme 上滑弹性动画开启。
    public boolean openDownAnime = true;//fixme 下滑弹性动画开启

    //解决嵌套滑动冲突。
    @Override
    public boolean startNestedScroll(int axes) {
        //子View滑动时，禁止弹性动画。
        isDownAnime = false;
        isUpAnime = false;
        return super.startNestedScroll(axes);
    }


    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        //Log.e("test", "dyConsumed:\t" + dyConsumed + "\tdyUnconsumed:\t" + dyUnconsumed);
        if (dyConsumed == 0) {
            isDownAnime = true;
            isUpAnime = true;
        } else {
            isDownAnime = false;
            isUpAnime = false;
        }
        if (dyConsumed == 0 && dyUnconsumed == 0) {
            isDownAnime = false;
            isUpAnime = false;
        }
        return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public void stopNestedScroll() {
        super.stopNestedScroll();
        //结束时，开启弹性滑动。
        isDownAnime = true;
        isUpAnime = true;
    }

    boolean isHorizon = false;//是否属于横屏滑动(水平滑动，不具备弹性效果)
    boolean isfirst = true;//是否为第一次滑动。
    byte isfirstDirection = 0;//第一次滑动方向,0初始化，没有方向，1 横屏方法，2 竖屏方向。

    int inerTop = 0;//记录原始的顶部高度。

    Map map = new HashMap<Integer, MPoint>();

    class MPoint {
        public float y;// 点击时y坐标
        public float preY = y;// 按下时的y坐标
        public float nowY = y;// 时时y坐标
        public int deltaY = 0;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (inner == null) {
            if (getChildCount() > 0) {
                inner = getChildAt(0);
            } else {
                return super.dispatchTouchEvent(ev);
            }
        }
        currentX = ev.getX();
        currentY = ev.getY();
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                map.put(ev.getPointerId(ev.getActionIndex()), new MPoint());
//                Log.e("test", "按下:\t" + ev.getPointerCount());
                inerTop = inner.getTop();//原始顶部，不一定都是0，所以要记录一下。
                isfirst = true;
                isfirstDirection = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN://第二个手指按下
                map.put(ev.getPointerId(ev.getActionIndex()), new MPoint());
                break;
            case MotionEvent.ACTION_POINTER_UP:
                map.remove(ev.getPointerId(ev.getActionIndex()));
                break;
            case MotionEvent.ACTION_MOVE:
                distanceX = currentX - lastX;
                distanceY = currentY - lastY;
//                Log.e("test", "x滑动:\t" + distanceX + "\ty滑动:\t" + distanceY);
//                Log.e("test", "currentX:\t" + currentX + "\tcurrentY:\t" + currentY);
                if (distanceY > 0) {
                    isSlidingDown = true;//下滑大于0
                } else {
                    isSlidingDown = false;//上滑小于0
                }
                if ((Math.abs(distanceX) < Math.abs(distanceY)) && Math.abs(distanceY) > 12) {
                    if (isfirstDirection == 0) {
                        isfirstDirection = 2;//上下方向
                    }
                    upDownSlide = true;//表示上下滑动
                } else {
                    if (isfirstDirection == 0 && (Math.abs(distanceX) > Math.abs(distanceY))) {
                        isfirstDirection = 1;//水平滑动方向
                    }
                }
                if (isfirst) {
                    isHorizon = !upDownSlide;//横屏滑动(水平滑动，不具备弹性效果)
                    isfirst = false;
                }
                if (isfirstDirection == 2) {
                    isHorizon = false;//上下方向
                } else if (isfirstDirection == 1) {
                    isHorizon = true;//水平方向
                }
                //Log.e("test", "x:\t" + Math.abs(distanceX) + "\ty:\t" + Math.abs(distanceY) + "\tisHorizon:\t" + isHorizon);
                //Log.e("test","isSlidingDown:\t"+isSlidingDown+"\tisDownAnime:\t"+isDownAnime+"\tisHorizon:\t"+isHorizon+"\tupDownSlide:\t"+upDownSlide+"\tinner:\t"+inner+"\topenDownAnime:\t"+openDownAnime);
                if (isSlidingDown && isDownAnime && !isHorizon) {
                    if (upDownSlide && inner != null && openDownAnime) {
                        commOnTouchEvent(ev);//fixme 开启下拉弹性
                    }
                } else if (!isSlidingDown && isUpAnime && !isHorizon) {
                    if (upDownSlide && inner != null && openUpAnime) {
                        commOnTouchEvent(ev);//fixme 开启上拉弹性
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
//                Log.e("test", "离开");
                //以防万一，恢复原始状态
                isDownAnime = true;
                isUpAnime = true;
                if (upDownSlide && inner != null) {
                    commOnTouchEvent(ev);
                }
                map.remove(ev.getPointerId(ev.getActionIndex()));
                map.clear();
                break;
            default:
                break;
        }
        lastX = currentX;
        lastY = currentY;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        //Log.e("test", "顶部Y:\t" + inner.getTop());
        if (inner != null && inner.getTop() != inerTop) {
            return true;//子View在移动的时候，拦截对子View的事件处理。
        }
        return super.onInterceptTouchEvent(e);
    }

    /***
     * 监听touch
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }


    boolean bAnime = false;//是否开始动画

    /***
     * 触摸事件
     *
     * @param ev
     */
    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                // 手指松开.
                if (isNeedAnimation()) {
                    animation();
                    isCount = false;
                }
                clear0();
                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = y;// 按下时的y坐标
                float nowY = ev.getY();// 时时y坐标
                int deltaY = (int) (preY - nowY);// 滑动距离

                if (ev.getPointerCount() > 1) {
                    //多指滑动
                    for (int i = 0; i < ev.getPointerCount(); i++) {
                        MPoint m = (MPoint) map.get(ev.getPointerId(i));
                        m.preY = m.y;
                        m.nowY = ev.getY(i);
                        m.deltaY = (int) (m.preY - m.nowY);
                        if (i == 0) {
                            deltaY = (int) m.deltaY;
                        } else {
                            //移动距离取最大的
                            if (Math.abs(deltaY) < Math.abs(m.deltaY)) {
                                deltaY = (int) m.deltaY;
                            }
                        }
                        m.y = m.nowY;
                    }
                }

                if (!isCount) {
                    deltaY = 0; // 在这里要归0.
                }
                //Log.e("test","按下y：\t"+preY+"\tnowY：\t"+nowY+"\t距离:\t"+deltaY+"\t是否移动:\t"+isNeedMove());
                y = nowY;
//                Log.e("test", "deltaY滑动:\t" + deltaY);
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局/速度大于了200都是异常。不做移动处理
                if (isNeedMove() && Math.abs(deltaY) < 200) {
                    // 初始化头部矩形
                    if (normal.isEmpty()) {
                        // 保存正常的布局位置
                        normal.set(inner.getLeft(), inner.getTop(),
                                inner.getRight(), inner.getBottom());
                    }
                    // 移动布局
                    int top = inner.getTop() - deltaY / 2;
                    int bottom = inner.getBottom() - deltaY / 2;
                    //移动最大不能超过总高度的一半
                    if (top < getHeight() / 2 && bottom > getHeight() / 2 && !bAnime) {
                        inner.layout(inner.getLeft(), top,
                                inner.getRight(), bottom);
                    } else {
                        animation();//恢复原状
                    }
                }
                isCount = true;
                break;

            default:
                break;
        }
    }

    /***
     * 回缩动画
     */
    public void animation() {
        if (!bAnime) {
            bAnime = true;//开始动画
            int top = inner.getTop();
            // 开启移动动画
            TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
                    normal.top);
            ta.setDuration(200);
            ta.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bAnime = false;//动画结束
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    bAnime = false;
                }
            });
            inner.startAnimation(ta);
            // 设置回到正常的布局位置
            inner.layout(normal.left, normal.top, normal.right, normal.bottom);

            normal.setEmpty();
        }
    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    /***
     * 是否需要移动布局 inner.getMeasuredHeight():获取的是控件的总高度
     *
     * getHeight()：获取的是屏幕的高度
     *
     * @return
     */
    public boolean isNeedMove() {
        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        // 0是顶部，后面那个是底部
        if (scrollY == 0 || scrollY == offset) {
            return true;
        }
        return false;
    }

    private void clear0() {
        lastX = 0;
        lastY = 0;
        distanceX = 0;
        distanceY = 0;
        upDownSlide = false;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewForTabListener != null) {
            scrollViewForTabListener.onScrollChanged(this, l, t, oldl, oldt);
        }

    }

    private ScrollViewForTabListener scrollViewForTabListener;

    public void setScrollViewForTabListener(ScrollViewForTabListener scrollViewForTabListener) {
        this.scrollViewForTabListener = scrollViewForTabListener;
    }

    public interface ScrollViewForTabListener {
        void onScrollChanged(BounceScrollView bounceScrollView, int x, int y, int oldx, int oldy);
    }

}
