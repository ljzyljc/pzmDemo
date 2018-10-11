package cn.android.support.v7.lib.sin.crown.kotlin.anko

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.TextView
import cn.android.support.v7.lib.sin.crown.kotlin.R
import cn.android.support.v7.lib.sin.crown.kotlin.common.px
import cn.android.support.v7.lib.sin.crown.kotlin.https.Bitmaps
import cn.android.support.v7.lib.sin.crown.utils.CacheUtils
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

object Titlebar {

    //顶部标题栏
    class TopBar {

        //最外层容器
        var view: View? = null
        //左边返回键
        var leftTextView: TextView? = null
        //中间标题
        var centerTextView: TextView? = null
        //右边文本(也可以放图片)
        var rightTextView: TextView? = null
        //底部阴影分割线
        var bottomShadowView: View? = null

        //是否显示阴影分割线，true 显示，false不显示。默认不显示
        var shadow: Boolean = false
            set(value) {
                field = value
                if (value) {
                    bottomShadowView?.visibility = View.VISIBLE
                } else {
                    bottomShadowView?.visibility = View.INVISIBLE
                }
            }

        //与左边或右边的间距
        var offset = px.x(24)
        //标题栏高度
        var height = px.y(88)

        //返回键图片颜色
        var backColor = Color.WHITE

        //标题栏标题
        var title: String? = null
            set(value) {
                field = value
                centerTextView?.text = value
            }
        //默认字体大小
        var textSize = px.textSizeY(32)
            set(value) {
                field = value
                leftTextView?.setTextSize(value)
                centerTextView?.setTextSize(value)
                rightTextView?.setTextSize(value)
            }
        //默认字体颜色
        var textColor = Color.WHITE
            set(value) {
                field = value
                leftTextView?.setTextColor(value)
                centerTextView?.setTextColor(value)
                rightTextView?.setTextColor(value)
            }
    }

    /**
     * 获取标题栏
     * title 标题（默认空）
     * backColor 返回键颜色（默认白色）,Color.TRANSPARENT透明色什么都不做
     */
    fun topBar(ui: Activity, title: String? = null, backColor: Int = Color.WHITE): TopBar {
        var topBar = TopBar()
        topBar.title = title
        topBar.backColor = backColor
        var view = view(ui, topBar)
        topBar.view = view
        topBar.leftTextView = view.findViewById(px.id("txt_left"))
        topBar.centerTextView = view.findViewById(px.id("txt_center"))
        topBar.rightTextView = view.findViewById(px.id("txt_right"))
        topBar.bottomShadowView = view.findViewById(px.id("shadow_view_bottom"))
        return topBar
    }

    //fixme 注意，必须是Context上下文。Activity不行。引用时，会报错。
    private fun view(ui: Activity, topBar: TopBar): View = with(ui.baseContext) {
        frameLayout {
            backgroundColor = Color.TRANSPARENT//背景默认透明
            relativeLayout {
                //返回键
                textView {
                    id = px.id("txt_left")
                    textSize = topBar.textSize
                    setTextColor(topBar.textColor)
                    if (topBar.backColor != Color.TRANSPARENT) {//颜色透明就什么都不做
                        if (topBar.backColor == Color.WHITE) {
                            //返回键图片默认就是白色
                            background = resources.getDrawable(R.mipmap.crown_back_white)
                        } else {
                            var key = "backBitmap:\t" + topBar.backColor
                            var bitmap: Bitmap? = CacheUtils.getInstance().getAsBitmap(key)
                            //Log.e("test","缓存位图:\t"+bitmap)
                            if (bitmap == null) {
                                var drawable = resources.getDrawable(R.mipmap.crown_back_white)
                                var bitmapDrawable: BitmapDrawable = drawable as BitmapDrawable
                                bitmap = Bitmaps.getColorBitmap(topBar.backColor, bitmapDrawable.bitmap)
                                CacheUtils.getInstance().put(key, bitmap)//缓存该颜色值的位图，以便复用
                            }
                            backgroundDrawable = BitmapDrawable(bitmap)
                        }
                        onClick {
                            ui.finish()//返回键默认退出当前Activity
                        }
                    }
                }.lparams {
                    if (topBar.backColor != Color.TRANSPARENT) {//透明色,默认就是wrapContent
                        width = px.y(24)
                        height = px.y(41)
                    }
                    leftMargin = topBar.offset
                    centerVertically()
                }
                //标题栏
                textView {
                    id = px.id("txt_center")
                    textSize = topBar.textSize
                    setTextColor(topBar.textColor)
                    text = topBar.title
                }.lparams {
                    centerInParent()
                }
                //右边文字或图片
                textView {
                    id = px.id("txt_right")
                    textSize = topBar.textSize
                    setTextColor(topBar.textColor)
                }.lparams {
                    centerVertically()
                    alignParentRight()
                    rightMargin = topBar.offset
                }
                //底部阴影分割线
                view {
                    id = px.id("shadow_view_bottom")
                    background = resources.getDrawable(R.drawable.crown_shadow_line_up_to_down)//阴影线，方向从上往下
                    if (topBar.shadow) {
                        visibility = View.VISIBLE
                    } else {
                        visibility = View.INVISIBLE
                    }
                }.lparams {
                    width = matchParent
                    height = px.x(2)//高度为2,效果最好。
                    alignParentBottom()
                }

            }.lparams {
                width = matchParent
                height = topBar.height
                topMargin = px.statusHeight
            }
        }
    }
}