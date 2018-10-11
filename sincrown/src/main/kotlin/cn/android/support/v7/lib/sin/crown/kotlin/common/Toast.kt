package cn.android.support.v7.lib.sin.crown.kotlin.common

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.Toast
import cn.android.support.v7.lib.sin.crown.kotlin.base.BaseApplication
import cn.android.support.v7.lib.sin.crown.kotlin.widget.RoundTextView
import org.jetbrains.anko.UI
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.padding
import org.jetbrains.anko.verticalLayout

/**
 * Created by 彭治铭 on 2018/6/24.
 */
object Toast {
    var toast: Toast? = null
    var textView: RoundTextView? = null
    fun view(): View? = with(px.context()?.baseContext) {
        this?.let {
            with(it) {
                UI {
                    verticalLayout {
                        textView = RoundTextView(this.context).apply {}.lparams {}
                        addView(textView)
                    }
                }?.view
            }
        }
    }

    var yOffset = px.y(160)//提示框的与屏幕底部的距离。

    //以下默认属性，可以全局修改。根据需求来改。
    //var defaultColor = Color.parseColor("#bb313131")//默认背景颜色（浅黑色）
    var defaultColor = Color.parseColor("#61A465")//浅绿色，效果不错。
    var defaultPdding = px.x(24)//默认内补丁
    var defaultRadius = px.x(480f)//默认圆角半径（尽可能的大，确保圆形效果）
    var defaultTextSize = px.textSizeX(32)//默认字体大小
    var defaultTextColor = Color.WHITE//默认字体颜色
    private fun default() {
        textView?.let {
            //默认样式
            it.backgroundColor = defaultColor
            it.padding = defaultPdding
            it.all_radius = defaultRadius
            it.setTextSize(defaultTextSize)
            it.setTextColor(defaultTextColor)
        }
    }

//    调用案例
//    Toast.show("提示信息")
//    Toast.show(exitInfo){
//        it.apply {
//            backgroundColor= Color.parseColor("#61A465")//根据需求自定义文本框样式,这里是浅绿色，效果不错。
//        }
//    }

    //显示文本
    fun show(text: String?, init: ((textView: RoundTextView) -> Unit)? = null) {
        text?.let {
            if(it.trim().equals("")){
                return
            }
            default()
            if (toast == null) {
                toast = Toast(BaseApplication.getInstance())
                toast?.setDuration(Toast.LENGTH_SHORT)// 显示时长，1000为1秒
                val view = view()
                toast?.setView(view)// 自定义view
                default()
            }
            init?.let {
                textView?.let {
                    init(it)//可根据需求自定义样式
                }
            }
            toast?.setGravity(Gravity.CENTER or Gravity.BOTTOM, 0, yOffset)// 显示位置
            textView?.setText(text)
            toast?.show()
        }
    }
}