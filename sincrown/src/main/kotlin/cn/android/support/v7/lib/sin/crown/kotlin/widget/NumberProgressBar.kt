package cn.android.support.v7.lib.sin.crown.kotlin.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import cn.android.support.v7.lib.sin.crown.kotlin.base.BaseView

/**
 * 数字进度条
 */
open class NumberProgressBar : View {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    val paint: Paint by lazy { Paint() }
    //进度值（范围0~100）
    var progress: String = "0%"
    var p: Float = 0f//比例
    var txt_width: Int = 0//文本的宽度

    fun setProgress(value: Int): NumberProgressBar {
        progress = value.toString() + "%"
        p = value.toFloat() / 100f
        if (value < 0) {
            progress = "0%"
            p = 0f
        } else if (value > 100) {
            progress = "100%"
            p = 1f
        }
        txt_width = paint.measureText(progress).toInt()
        postInvalidate()
        return this
    }

    //字体颜色
    var textColor: Int = Color.parseColor("#418fde")

    fun setTextColor(value: Int): NumberProgressBar {
        textColor = value
        invalidate()
        return this
    }

    //进度条颜色（默认和字体颜色相同）
    var progressColor: Int = textColor

    fun setProgressColor(value: Int): NumberProgressBar {
        invalidate()
        return this
    }

    //底部颜色
    var dstColor: Int = Color.parseColor("#dedede")

    fun setDstColor(value: Int): NumberProgressBar {
        invalidate()
        return this
    }

    init {
        paint.isAntiAlias = true
        paint.isDither = true
        paint.style = Paint.Style.FILL
        setLayerType(View.LAYER_TYPE_HARDWARE, paint)
    }

    //底部高度
    var dstHeight: Int = 0
    var dst_y = 0f
    var dst_y2 = 0f
    //进度条高度
    var progressHeight: Int = 0
    var pro_y = 0f
    var pro_y2 = 0f
    //文本的字体大小
    var textSize: Float = 0f
    //文本的高度
    var txt_height = 0f
    var txt_y = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //底部高度
        dstHeight = h / 8
        dst_y = ((h - dstHeight) / 2).toFloat()
        dst_y2 = dst_y + dstHeight
        //进度条高度
        progressHeight = h / 6
        pro_y = ((h - progressHeight) / 2).toFloat()
        pro_y2 = pro_y + progressHeight
        //文本的大小就等于控件的高度
        textSize = h.toFloat()
        paint.textSize = textSize
        paint.setTypeface(Typeface.DEFAULT)
        txt_height = paint.descent() - paint.ascent()
        txt_y = (h - txt_height) / 2 - paint.ascent();
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.let {
            //画底部
            paint.color = dstColor
            canvas.drawRect(0f, dst_y, width.toFloat(), dst_y2, paint)
            //画进度条
            var w = width - txt_width//总长度
            var proWidth = w * p//进度条长度
            paint.color = progressColor
            canvas.drawRect(0f, pro_y, proWidth, pro_y2, paint)
            paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
            canvas.drawRect(proWidth, 0f, proWidth + txt_width, height.toFloat(), paint)
            paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC))
            //画进度数值
            paint.color = textColor
            canvas.drawText(progress, proWidth, txt_y, paint)
            paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_OVER))
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
    //自定义画布，根据需求。自主实现
    open var draw: ((canvas: Canvas, paint: Paint) -> Unit)? = null

    //自定义，重新绘图
    open fun draw(draw: ((canvas: Canvas, paint: Paint) -> Unit)? = null): NumberProgressBar {
        this.draw = draw
        postInvalidate()//刷新
        return this
    }

    //画自己【onDraw在draw()的流程里面，即在它的前面执行】
    var onDraw: ((canvas: Canvas, paint: Paint) -> Unit)? = null

    //画自己
    fun onDraw_(onDraw: ((canvas: Canvas, paint: Paint) -> Unit)? = null): NumberProgressBar {
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