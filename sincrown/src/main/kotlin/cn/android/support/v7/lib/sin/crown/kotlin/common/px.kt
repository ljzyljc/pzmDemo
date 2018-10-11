package cn.android.support.v7.lib.sin.crown.kotlin.common

import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Paint
import android.util.DisplayMetrics
import android.util.Log
import android.view.View

object px {
    var statusHeight = 0//状态栏高度
    var baseWidth = 750f//基准宽
    var baseHeight = 1334f//基准高
    var horizontalProportion: Float = 0.toFloat()//真实水平比例大小
    var verticalProportion: Float = 0.toFloat()//真实垂直比例大小
    var density: Float = 0.toFloat()//当前设备dpi密度值比例，即 dpi/160 的比值
    var ignorex: Boolean = false//是否忽悠比例缩放
    var ignorey: Boolean = false//是否忽悠比例缩放
    private var realWidth = 0f//真实屏幕宽(以竖屏为标准，宽度比高度小)
    private var realHeight = 0f//真实屏幕高

    /**
     * 获取当前Activity屏幕方向，true竖屏，false横屏
     */
    fun oritation(activity: Activity): Boolean {
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
            return false
        }
        //ActivityInfo.SCREEN_ORIENTATION_PORTRAIT 竖屏
        return true
    }

    /**
     * fixme 获取屏幕宽，isVertical true以竖屏为标准。默认是。false以横屏为标准
     */
    fun realWidth(isVertical: Boolean = true): Float {
        if (isVertical) {
            return realWidth
        } else {
            return realHeight
        }
    }

    /**
     * fixme 获取屏幕高，isVertical true以竖屏为标准。默认是。false以横屏为标准
     */
    fun realHeight(isVertical: Boolean = true): Float {
        if (isVertical) {
            return realHeight
        } else {
            return realWidth
        }
    }


    init {
        init()
    }

    //初始化，基准宽或高，发生变化时(以竖屏为标准)，可以手动调用，重新初始化
    //fixme 注意：以竖屏为标准，宽度比高度小(高度大于宽度)
    fun init(baseWidth: Float = 750f, baseHeight: Float = 1334f) {
        this.baseWidth = baseWidth
        this.baseHeight = baseHeight
        //真实值
        var displayMetrics: DisplayMetrics? = context()?.resources?.displayMetrics
        realWidth = displayMetrics!!.widthPixels.toFloat()
        realHeight = displayMetrics.heightPixels.toFloat()
        density = displayMetrics.density
        if (realWidth > realHeight) {
            var w = realWidth
            realWidth = realHeight
            realHeight = w
        }
        horizontalProportion = realWidth / baseWidth
        verticalProportion = realHeight / baseHeight
        //获取状态栏的高度
        statusHeight()
        ignorex()
        ignorey()
    }

    private fun ignorex() {
        //防止比例为1的时候做多余的适配
        if (horizontalProportion >= 0.999 && horizontalProportion <= 1.001) { //750/720=1.04166 苹果/安卓
            ignorex = true
        } else {
            ignorex = false
        }
    }

    private fun ignorey() {
        //防止比例为1的时候做多余的适配
        if (verticalProportion >= 0.999 && verticalProportion <= 1.001) { //1334/1280=1.04218 苹果/安卓
            ignorey = true
        } else {
            ignorey = false
        }
    }

    /**
     * fixme 适配x值(默认全屏)，以竖屏为标准。
     */
    fun x(x: Int = baseWidth.toInt()): Int {
        return x(x.toFloat()).toInt()
    }

    //Int类型已经添加了默认参数，Float就不能添加默认参数了。不然无法识别
    fun x(x: Float): Float {
        if (ignorex) {
            return x
        }
        return x * horizontalProportion
    }

    /**
     * fixme 适配y值，始终以竖屏为标准。
     */
    fun y(y: Int = baseHeight.toInt()): Int {
        return y(y.toFloat()).toInt()
    }

    fun y(y: Float): Float {
        if (ignorey) {
            return y;
        }
        return y * verticalProportion
    }

    /**
     * fixme 设置文字大小。以X为标准
     */
    fun textSizeX(x: Float): Float {
        return pixelToDp(x(x))//textView.setTextSize单位是dp,且是float类型。设置文字大小。
    }

    fun textSizeX(x: Int): Float {
        return pixelToDp(x(x.toFloat()))
    }

    /**
     * fixme 设置文字大小。以Y为标准
     */
    fun textSizeY(y: Float): Float {
        return pixelToDp(y(y))//textView.setTextSize单位是dp,且是float类型。设置文字大小。
    }

    fun textSizeY(y: Int): Float {
        return pixelToDp(y(y.toFloat()))
    }

    //与屏幕边缘左边的距离
    fun left(view: View): Int {
        //获取现对于整个屏幕的位置。
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return location[0]
    }

    //与屏幕边缘右边的距离
    fun right(view: View): Int {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return (realWidth - location[0] - view.width).toInt()
    }

    //与屏幕边缘上边的距离
    fun top(view: View): Int {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return location[1]
    }

    //与屏幕边缘下边的距离
    fun bottom(view: View): Int {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return (realHeight - location[1] - view.height).toInt()
    }

    //测量两个View之间的X坐标间距
    fun distanceX(view1: View, view2: View): Float {
        return view2.x - view1.x
    }

    //测量两个View之间的Y坐标间距
    fun distanceY(view1: View, view2: View): Float {
        return view2.y - view1.y
    }

    //获取文本居中Y坐标,height：以这个高度进行对其。即对其高度
    fun centerTextY(paint: Paint, height: Float): Float {
        var baseline = (height - (paint.descent() - paint.ascent())) / 2 - paint.ascent()
        return baseline
    }

    //获取文本居中X坐标，以文本居左为计算标准，即：paint.textAlign=Paint.Align.LEFT
    fun centerTextX(text: String, paint: Paint, width: Float): Float {
        val w = paint.measureText(text, 0, text.length)//测量文本的宽度
        var x = (width - w) / 2
        return x
    }

    //获取位图居中Y坐标
    fun centerBitmapY(bitmap: Bitmap, height: Float): Float {
        var y = (height - bitmap.height) / 2
        return y
    }

    //获取位图居中X坐标，width对其的宽度
    fun centerBitmapX(bitmap: Bitmap, width: Float): Float {
        var x = (width - bitmap.width) / 2
        return x
    }

    //Dp转像素
    fun dpToPixel(dp: Float): Float {
        return dp * density//其中 density就是 dpi/160的比值。
    }

    //像素转Dp
    fun pixelToDp(px: Float): Float {
        return px / density
    }

    var id: Int = 0//id不能小于0，-1表示没有id
        get() = id()
    private var ids = 1000//记录id生成的个数，依次叠加，保证不重复。
    private var map = mutableMapOf<String, Int>()//保存id键值
    fun id(key: Int): Int {
        return id(key.toString())
    }

    //id生成器(xml系统布局id都是从20亿开始的。所以绝对不会和系统id重复。)
    //即能生成id,也能获取id
    fun id(key: String? = null): Int {
        //根据键值获取id
        //id不能小于0，-1表示没有id
        //constraintLayout id找不到时，就以父容器为主。(前提：id不能小于0)
        key?.let {
            map[it]?.let {
                return it//如果该键值的id已经存在，直接返回
            }
        }
        //如果id不存在，就重新创建id
        ids++
        //Log.e("test", "id:\t" + ids)
        key?.let {
            map.put(key, ids)
        }
        return ids
    }

    /**
     * 获得状态栏的高度，单位像素
     *
     * @return
     */
    private fun statusHeight(context: Application? = null): Int {
        if (statusHeight <= 0) {
            try {
                val clazz = Class.forName("com.android.internal.R\$dimen")
                val `object` = clazz.newInstance()
                val height = Integer.parseInt(clazz.getField("status_bar_height")
                        .get(`object`).toString())
                statusHeight = context(context)?.resources?.getDimensionPixelSize(height) ?: 0
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return statusHeight
    }

    //通过反射获取ActivityThread【隐藏类】
    private fun getActivityThread(): Any? {
        try {
            val clz = Class.forName("android.app.ActivityThread")
            val method = clz.getDeclaredMethod("currentActivityThread")
            method.isAccessible = true
            return method.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private var application: Application? = null

    //上下文
    //px.context(this.application) 或 px.context()
    fun context(context: Application? = null): Application? {
        if (application != null) {
            return application
        }
        context?.let {
            application = context
        }
        if (application == null) {
            //如果配置文件没有声明，也没有手动初始化。则通过反射自动初始化。【反射是最后的手段，效率不高】
            //通过反射，手动获取上下文。
            val activityThread = getActivityThread()
            if (null != activityThread) {
                try {
                    val getApplication = activityThread.javaClass.getDeclaredMethod("getApplication")
                    getApplication.isAccessible = true
                    application = getApplication?.invoke(activityThread) as Application ?: null
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        return application
    }

}