package cn.android.support.v7.lib.sin.crown.kotlin.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import cn.android.support.v7.lib.sin.crown.kotlin.base.BaseView

/**
 * 颜色渐变视图
 */
open class GradientView : View {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    init {
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    //fixme 水平渐变颜色数组值【均匀渐变】
    var horizontalColors: IntArray? = null

    fun horizontalColors(vararg color: Int) {
        horizontalColors = color
    }

    fun horizontalColors(vararg color: String) {
        horizontalColors = IntArray(color.size)
        horizontalColors?.apply {
            if (color.size > 1) {
                for (i in 0..color.size - 1) {
                    this[i] = Color.parseColor(color[i])
                }
            } else {
                this[0] = Color.parseColor(color[0])
            }
        }

    }

    //fixme 垂直渐变颜色数组值【均匀】
    var verticalColors: IntArray? = null

    fun verticalColors(vararg color: Int) {
        verticalColors = color
    }

    //fixme 如：verticalColors("#00dedede","#dedede") 向上的阴影线
    fun verticalColors(vararg color: String) {
        verticalColors = IntArray(color.size)
        verticalColors?.apply {
            if (color.size > 1) {
                for (i in 0..color.size - 1) {
                    this[i] = Color.parseColor(color[i])
                }
            } else {
                this[0] = Color.parseColor(color[0])
            }
        }

    }

    var top_color = Color.TRANSPARENT//fixme 上半部分颜色
    fun top_color(top_color: Int) {
        this.top_color = top_color
    }

    fun top_color(top_color: String) {
        this.top_color = Color.parseColor(top_color)
    }

    var bottom_color = Color.TRANSPARENT//fixme 下半部分颜色
    fun bottom_color(bottom_color: Int) {
        this.bottom_color = bottom_color
    }

    fun bottom_color(bottom_color: String) {
        this.bottom_color = Color.parseColor(bottom_color)
    }

    var left_color = Color.TRANSPARENT//fixme 左半部分颜色
    fun left_color(left_color: Int) {
        this.left_color = left_color
    }

    fun left_color(left_color: String) {
        this.left_color = Color.parseColor(left_color)
    }

    var right_color = Color.TRANSPARENT//fixme 右半部分颜色
    fun right_color(right_color: Int) {
        this.right_color = right_color
    }

    fun right_color(right_color: String) {
        this.right_color = Color.parseColor(right_color)
    }

    var left_top_color = Color.TRANSPARENT//fixme 左上角部分颜色
    fun left_top_color(left_top_color: Int) {
        this.left_top_color = left_top_color
    }

    fun left_top_color(left_top_color: String) {
        this.left_top_color = Color.parseColor(left_top_color)
    }

    var right_top_color = Color.TRANSPARENT//fixme 右上角部分颜色
    fun right_top_color(right_top_color: Int) {
        this.right_top_color = right_top_color
    }

    fun right_top_color(right_top_color: String) {
        this.right_top_color = Color.parseColor(right_top_color)
    }

    var left_bottom_color = Color.TRANSPARENT//fixme 左下角部分颜色
    fun left_bottom_color(left_bottom_color: Int) {
        this.left_bottom_color = left_bottom_color
    }

    fun left_bottom_color(left_bottom_color: String) {
        this.left_bottom_color = Color.parseColor(left_bottom_color)
    }

    var right_bottom_color = Color.TRANSPARENT//fixme 右下角部分颜色
    fun right_bottom_color(right_bottom_color: Int) {
        this.right_bottom_color = right_bottom_color
    }

    fun right_bottom_color(right_bottom_color: String) {
        this.right_bottom_color = Color.parseColor(right_bottom_color)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.apply {
            var paint = Paint()
            paint.isAntiAlias = true
            paint.isDither = true
            paint.style = Paint.Style.FILL_AND_STROKE

            //上半部分颜色
            if (top_color != Color.TRANSPARENT) {
                paint.color = top_color
                drawRect(RectF(0f, 0f, width.toFloat(), height / 2f), paint)
            }

            //下半部分颜色
            if (bottom_color != Color.TRANSPARENT) {
                paint.color = bottom_color
                drawRect(RectF(0f, height / 2f, width.toFloat(), height.toFloat()), paint)
            }


            //左半部分颜色
            if (left_color != Color.TRANSPARENT) {
                paint.color = left_color
                drawRect(RectF(0f, 0f, width.toFloat() / 2, height.toFloat()), paint)
            }

            //右半部分颜色
            if (right_color != Color.TRANSPARENT) {
                paint.color = right_color
                drawRect(RectF(width / 2f, 0f, width.toFloat(), height.toFloat()), paint)
            }

            //左上角部分颜色
            if (left_top_color != Color.TRANSPARENT) {
                paint.color = left_top_color
                drawRect(RectF(0f, 0f, width.toFloat() / 2, height.toFloat() / 2), paint)
            }

            //右上角部分颜色
            if (right_top_color != Color.TRANSPARENT) {
                paint.color = right_top_color
                drawRect(RectF(width / 2f, 0f, width.toFloat(), height.toFloat() / 2), paint)
            }

            //左下角部分颜色
            if (left_bottom_color != Color.TRANSPARENT) {
                paint.color = left_bottom_color
                drawRect(RectF(0f, height / 2f, width.toFloat() / 2, height.toFloat()), paint)
            }

            //右下角部分颜色
            if (right_bottom_color != Color.TRANSPARENT) {
                paint.color = right_bottom_color
                drawRect(RectF(width / 2f, height / 2f, width.toFloat(), height.toFloat()), paint)
            }

            //水平渐变
            horizontalColors?.let {
                var shader = LinearGradient(0f, 0f, width.toFloat(), 0f, it, null, Shader.TileMode.MIRROR)
                paint.setShader(shader)
                drawPaint(paint)
            }

            //fixme 水平渐变 和 垂直渐变 效果会叠加。垂直覆盖在水平的上面。

            //垂直渐变
            verticalColors?.let {
                var shader = LinearGradient(0f, 0f, 0f, height.toFloat(), it, null, Shader.TileMode.MIRROR)
                paint.setShader(shader)
                drawPaint(paint)
            }
            canvas?.let {
                draw?.let {
                    var paint = Paint()
                    paint.isAntiAlias = true
                    paint.isDither = true
                    paint.style=Paint.Style.FILL_AND_STROKE
                    paint.strokeWidth=0f
                    it(canvas, paint)
                }
            }
        }
    }
    //自定义画布，根据需求。自主实现
    open var draw: ((canvas: Canvas, paint: Paint) -> Unit)? = null

    //自定义，重新绘图
    open fun draw(draw: ((canvas: Canvas, paint: Paint) -> Unit)? = null): GradientView {
        this.draw = draw
        postInvalidate()//刷新
        return this
    }

    //画自己【onDraw在draw()的流程里面，即在它的前面执行】
    var onDraw: ((canvas: Canvas, paint: Paint) -> Unit)? = null

    //画自己
    fun onDraw_(onDraw: ((canvas: Canvas, paint: Paint) -> Unit)? = null): GradientView {
        this.onDraw = onDraw
        postInvalidate()//刷新
        return this
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            onDraw?.let {
                var paint = Paint()
                paint.isAntiAlias = true
                paint.isDither = true
                paint.style=Paint.Style.FILL_AND_STROKE
                paint.strokeWidth=0f
                it(canvas, paint)
            }
        }
    }

}