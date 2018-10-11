package cn.android.support.v7.lib.sin.crown.kotlin.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.graphics.RectF
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import cn.android.support.v7.lib.sin.crown.kotlin.R
import cn.android.support.v7.lib.sin.crown.kotlin.base.BaseView


/**
 * 自定义圆角相对布局
 * Created by 彭治铭 on 2018/5/20.
 */
open class RoundRelativeLayout : RelativeLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.RoundCornersRect)
        typedArray?.let {
            var all_radius = typedArray?.getDimension(R.styleable.RoundCornersRect_radian_all, 0f)
            left_top = typedArray?.getDimension(R.styleable.RoundCornersRect_radian_left_top, all_radius)
            left_bottom = typedArray?.getDimension(R.styleable.RoundCornersRect_radian_left_bottom, all_radius)
            right_top = typedArray?.getDimension(R.styleable.RoundCornersRect_radian_right_top, all_radius)
            right_bottom = typedArray?.getDimension(R.styleable.RoundCornersRect_radian_right_bottom, all_radius)
        }
    }

    var all_radius: Float = 0F//默认，所有圆角的角度
    var left_top: Float = 0f//左上角
    var left_bottom: Float = 0f//左下角
    var right_top = 0f//右上角
    var right_bottom = 0f//右下角

    var strokeWidth = 0f//边框宽度
    var strokeColor = Color.TRANSPARENT//边框颜色

    init {
        setLayerType(View.LAYER_TYPE_HARDWARE, null)//开启硬件加速
    }

    override fun dispatchDraw(canvas: Canvas?) {
        //背景
        canvas?.let {
            onDraw?.let {
                var paint = Paint()
                paint.isAntiAlias = true
                paint.isDither = true
                paint.style = Paint.Style.FILL_AND_STROKE
                paint.strokeWidth = 0f
                it(canvas, paint)
            }
        }

        super.dispatchDraw(canvas)

        canvas?.let {
            if (left_top <= 0) {
                left_top = all_radius
            }
            if (left_bottom <= 0) {
                left_bottom = all_radius
            }
            if (right_top <= 0) {
                right_top = all_radius
            }
            if (right_bottom <= 0) {
                right_bottom = all_radius
            }
            //利用内补丁画圆角。只对负补丁有效(防止和正补丁冲突，所以取负)
            var paint = Paint()
            paint.isDither = true
            paint.isAntiAlias = true
            paint.style = Paint.Style.FILL
            paint.strokeWidth = 0f
            paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))//取下面的交集
            // 矩形弧度
            val radian = floatArrayOf(left_top!!, left_top!!, right_top, right_top, right_bottom, right_bottom, left_bottom, left_bottom)
            // 画矩形
            var rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
            var path = Path()
            path.addRoundRect(rectF, radian, Path.Direction.CW)
            canvas.drawPath(path, paint)

            //画矩形边框
            if (strokeWidth > 0) {

                rectF = RectF(0f + strokeWidth / 2F, 0f + strokeWidth / 2F, width.toFloat() - strokeWidth / 2F, height.toFloat() - strokeWidth / 2F)
                path.reset()
                path.addRoundRect(rectF, radian, Path.Direction.CW)
                paint.strokeWidth = strokeWidth
                paint.style = Paint.Style.FILL
                paint.color = strokeColor
                paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))//取下面的交集
                paint.style = Paint.Style.FILL_AND_STROKE
                canvas.drawPath(path, paint)

                //画边框
                paint.style = Paint.Style.STROKE
                paint.setXfermode(null)//正常
                canvas.drawPath(path, paint)

            }

        }
        //前景
        canvas?.let {
            draw?.let {
                var paint = Paint()
                paint.isAntiAlias = true
                paint.isDither = true
                paint.style = Paint.Style.FILL_AND_STROKE
                paint.strokeWidth = 0f
                it(canvas, paint)
            }
        }
    }

    //自定义画布，根据需求。自主实现
    open var draw: ((canvas: Canvas, paint: Paint) -> Unit)? = null

    //自定义，重新绘图
    open fun draw(draw: ((canvas: Canvas, paint: Paint) -> Unit)? = null): RoundRelativeLayout {
        this.draw = draw
        postInvalidate()//刷新
        return this
    }

    //画自己【onDraw在draw()的流程里面，即在它的前面执行】
    var onDraw: ((canvas: Canvas, paint: Paint) -> Unit)? = null

    //画自己[onDraw与系统名冲突，所以加一个横线]
    fun onDraw_(onDraw: ((canvas: Canvas, paint: Paint) -> Unit)? = null): RoundRelativeLayout {
        this.onDraw = onDraw
        postInvalidate()//刷新
        return this
    }

}