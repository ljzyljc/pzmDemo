package cn.android.support.v7.lib.sin.crown.kotlin.utils

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import cn.android.support.v7.lib.sin.crown.kotlin.common.px

/**
 * fixme 用代码来实现selector 选择器 需要手动设置View的isSelected才会有选中效果。
 * fixme Chekbox和RadioButton 选中按钮自动设置了isSelected,所以不需要手动设置。
 *
 * fixme isSelected=true true 选中，false 未选中。可以通过代码设置
 * fixme isSelectable 所有的View都具备select选中能力
 *
 */
object SelectorUtils {
    /**
     * NormalID 默认背景图片id
     * PressID 按下背景图片id
     * SelectID 选中(默认和按下相同)时背景图片id
     */
    fun selectorDrawable(view: View, NormalID: Int, PressID: Int, SelectID: Int = PressID) {
        val drawable = StateListDrawable()
        var drawableNormal = px.context()?.resources?.getDrawable(NormalID)
        var drawablePress = px.context()?.resources?.getDrawable(PressID)
        var drawableSelect = px.context()?.resources?.getDrawable(SelectID)
        //fixme - 表示fasle
        view.isClickable = true//具体点击能力
        //按下
        drawable.addState(intArrayOf(android.R.attr.state_pressed), drawablePress)
        //选中
        drawable.addState(intArrayOf(android.R.attr.state_checked), drawableSelect)
        drawable.addState(intArrayOf(android.R.attr.state_selected), drawableSelect)
        //未选中 + 未按下 (也就是一般状态)
        drawable.addState(intArrayOf(-android.R.attr.state_checked), drawableNormal)
        if (view is CheckBox) {//多选框
            view.buttonDrawable = drawable
        } else if (view is RadioButton) {//单选框
            view.buttonDrawable = drawable
        } else {//一般View
            view.setBackgroundDrawable(drawable)
        }
    }

    /**
     * NormalBtmap 默认背景位图
     * PressBitmap 按下时背景位图
     * SelectBitmap 选中(默认和按下相同)时背景位图
     */
    fun selectorDrawable(view: View, NormalBtmap: Bitmap, PressBitmap: Bitmap, SelectBitmap: Bitmap = PressBitmap) {
        val drawable = StateListDrawable()
        var drawableNormal = BitmapDrawable(NormalBtmap)
        var drawablePress = BitmapDrawable(PressBitmap)
        var drawableSelect = BitmapDrawable(SelectBitmap)
        //fixme - 表示fasle
        view.isClickable = true//具体点击能力
        //按下
        drawable.addState(intArrayOf(android.R.attr.state_pressed), drawablePress)
        //选中
        drawable.addState(intArrayOf(android.R.attr.state_checked), drawableSelect)
        drawable.addState(intArrayOf(android.R.attr.state_selected), drawableSelect)
        //未选中 + 未按下 (也就是一般状态)
        drawable.addState(intArrayOf(-android.R.attr.state_checked), drawableNormal)

        if (view is CheckBox) {//多选框
            view.buttonDrawable = drawable
        } else if (view is RadioButton) {//单选框
            view.buttonDrawable = drawable
        } else {//一般View
            view.setBackgroundDrawable(drawable)
        }
    }

    /**
     * NormalColor 正常背景颜色值
     * PressColor  按下正常背景颜色值
     * SelectColor 选中(默认和按下相同)背景颜色值
     */
    fun selectorColor(view: View, NormalColor: Int, PressColor: Int, SelectColor: Int = PressColor) {
        val drawable = StateListDrawable()
        var drawableNormal = ColorDrawable(NormalColor)
        var drawablePress = ColorDrawable(PressColor)
        var drawableSelect = ColorDrawable(SelectColor)
        //fixme - 表示fasle
        view.isClickable = true//具体点击能力
        //按下
        drawable.addState(intArrayOf(android.R.attr.state_pressed), drawablePress)
        //选中
        drawable.addState(intArrayOf(android.R.attr.state_checked), drawableSelect)
        drawable.addState(intArrayOf(android.R.attr.state_selected), drawableSelect)
        //未选中 + 未按下 (也就是一般状态)
        drawable.addState(intArrayOf(-android.R.attr.state_checked), drawableNormal)
        if (view is CheckBox) {//多选框
            view.buttonDrawable = drawable
        } else if (view is RadioButton) {//单选框
            view.buttonDrawable = drawable
        } else {//一般View
            view.setBackgroundDrawable(drawable)
        }
    }

    /**
     * NormalColor 正常背景颜色值
     * PressColor  按下背景颜色值
     * SelectColor 选中(默认和按下时相同)背景颜色值
     */
    fun selectorColor(view: View, NormalColor: String, PressColor: String, SelectColor: String = PressColor) {
        val drawable = StateListDrawable()
        var drawableNormal = ColorDrawable(Color.parseColor(NormalColor))
        var drawablePress = ColorDrawable(Color.parseColor(PressColor))
        var drawableSelect = ColorDrawable(Color.parseColor(SelectColor))
        //fixme - 表示fasle
        view.isClickable = true//具体点击能力
        //按下
        drawable.addState(intArrayOf(android.R.attr.state_pressed), drawablePress)
        //选中
        drawable.addState(intArrayOf(android.R.attr.state_checked), drawableSelect)
        drawable.addState(intArrayOf(android.R.attr.state_selected), drawableSelect)
        //未选中 + 未按下 (也就是一般状态)
        drawable.addState(intArrayOf(-android.R.attr.state_checked), drawableNormal)
        if (view is CheckBox) {//多选框
            view.buttonDrawable = drawable
        } else if (view is RadioButton) {//单选框
            view.buttonDrawable = drawable
        } else {//一般View
            view.setBackgroundDrawable(drawable)
        }
    }

    /**
     * NormalColor 正常字体颜色值
     * PressColor  按下时字体颜色值
     * SelectColor 选中(默认和按下相同)字体颜色值
     */
    fun selectorTextColor(view: View, NormalColor: Int, PressColor: Int, SelectColor: Int = PressColor) {
        val colors = intArrayOf(PressColor, SelectColor, SelectColor, NormalColor)
        val states = arrayOfNulls<IntArray>(4)
        //fixme 以下顺序很重要。特别是最后一个，普通效果。必须放在最后一个，不然可能没有效果。
        states[0] = intArrayOf(android.R.attr.state_pressed)//按下
        states[1] = intArrayOf(android.R.attr.state_checked)//选中
        states[2] = intArrayOf(android.R.attr.state_selected)//选中
        states[3] = intArrayOf(-android.R.attr.state_checked)//未选中，未按下，普通一般效果
        val colorStateList = ColorStateList(states, colors)
        view.isClickable = true//具体点击能力
        if (view is TextView) {
            view.setTextColor(colorStateList)
        }
    }

    /**
     * NormalColor 正常字体颜色值
     * PressColor  按下时颜色值
     * SelectColor 选中(默认和按下时相同)  字体颜色值
     */
    fun selectorTextColor(view: View, NormalColor: String, PressColor: String, SelectColor: String = PressColor) {
        val colors = intArrayOf(Color.parseColor(PressColor), Color.parseColor(SelectColor), Color.parseColor(SelectColor), Color.parseColor(NormalColor))
        val states = arrayOfNulls<IntArray>(4)
        //fixme 以下顺序很重要。特别是最后一个，普通效果。必须放在最后一个，不然可能没有效果。
        states[0] = intArrayOf(android.R.attr.state_pressed)//按下
        states[1] = intArrayOf(android.R.attr.state_checked)//选中
        states[2] = intArrayOf(android.R.attr.state_selected)//选中
        states[3] = intArrayOf(-android.R.attr.state_checked)//未选中，未按下，普通一般效果
        val colorStateList = ColorStateList(states, colors)
        view.isClickable = true//具体点击能力
        if (view is TextView) {
            view.setTextColor(colorStateList)
        }
    }

}