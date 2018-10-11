package cn.android.support.v7.lib.sin.crown.kotlin.dialog

import android.app.Activity
import android.graphics.Color
import android.view.View
import cn.android.support.v7.lib.sin.crown.kotlin.R
import cn.android.support.v7.lib.sin.crown.kotlin.base.BaseDialog
import cn.android.support.v7.lib.sin.crown.kotlin.bean.DateChoose
import cn.android.support.v7.lib.sin.crown.kotlin.common.px
import cn.android.support.v7.lib.sin.crown.utils.ProportionUtils
import cn.android.support.v7.lib.sin.crown.utils.TimeUtils
import cn.android.support.v7.lib.sin.crown.view.RollerView

/**
 * 日期选择器
 * Created by 彭治铭 on 2018/6/3.
 */
//使用说明
//var dateChoose = DateChoose()
//val dateChooseDialog:DateChooseDialog by lazy { DateChooseDialog(this, dateChoose).setCallBack { dateChoose = it }}
//dateChooseDialog.show()
class DateChooseDialog(context: Activity, var dateChoose: DateChoose=DateChoose(), isStatus: Boolean = true, isTransparent: Boolean = true) : BaseDialog(context, R.layout.crown_dialog_date_choose,isStatus,isTransparent) {
    val yyyy: RollerView by lazy { findViewById<RollerView>(R.id.crown_roller_yyyy) }
    val MM: RollerView by lazy { findViewById<RollerView>(R.id.crown_roller_MM) }
    val dd: RollerView by lazy { findViewById<RollerView>(R.id.crown_roller_dd) }

    init {
        ProportionUtils.getInstance().adapterWindow(context,dialog?.window)//适配
        dialog?.window?.setWindowAnimations(R.style.crown_window_bottom)//动画
        //取消
        findViewById<View>(R.id.crown_txt_cancel).setOnClickListener {
            dismiss()
        }
        //完成
        findViewById<View>(R.id.crown_txt_ok).setOnClickListener {
            dismiss()
        }
        //年
        var list_yyyy = ArrayList<String>()
        for (i in 2010..2030) {
            list_yyyy.add(i.toString())
        }
        yyyy.setLineColor(Color.TRANSPARENT).setItems(list_yyyy).setTextSize(px.x(40f)).setCount(5)
                .setDefaultTextColor(Color.parseColor("#444444")).setSelectTextColor(Color.parseColor("#444444"))
        //月
        var list_MM = ArrayList<String>()
        for (i in 1..12) {
            list_MM.add(i.toString())
        }
        MM.setLineColor(Color.TRANSPARENT).setItems(list_MM).setTextSize(px.x(40f)).setCount(5)
                .setDefaultTextColor(Color.parseColor("#444444")).setSelectTextColor(Color.parseColor("#444444"))
        MM.setItemSelectListener(object : RollerView.ItemSelectListener {
            override fun onItemSelect(item: String?, position: Int) {
                //月份监听
                updateDays()
            }
        })
        //日
        dd.setLineColor(Color.TRANSPARENT).setTextSize(px.x(40f)).setCount(5)
                .setDefaultTextColor(Color.parseColor("#444444")).setSelectTextColor(Color.parseColor("#444444"))

        //fixme 设置数据滚轮循环效果
        yyyy.isCyclic = true
        MM.isCyclic = true
        dd.isCyclic = true

        isDismiss(true)
    }

    fun updateDays() {
        //日，联动，更加月份而改变
        var list_dd = ArrayList<String>()
        val mDay = TimeUtils.getInstance().getMonthDay(yyyy.currentItemValue + "-" + MM.currentItemValue)//天数
        for (i in 1..mDay) {
            list_dd.add(i.toString())
        }
        dd.setItems(list_dd)
    }

    override fun listener() {
        updateDays()
        //选中
        yyyy.setCurrentPostion(yyyy.getItemPostion(dateChoose.yyyy))
        MM.setCurrentPostion(MM.getItemPostion(dateChoose.MM))
        dd.setCurrentPostion(dd.getItemPostion(dateChoose.dd))
    }

    override fun recycleView() {
    }

    //日期返回回调
    fun setCallBack(callbak: (dateChoose: DateChoose) -> Unit): DateChooseDialog {
        //完成
        findViewById<View>(R.id.crown_txt_ok).setOnClickListener {
            dateChoose.yyyy = yyyy.currentItemValue
            dateChoose.MM = MM.currentItemValue
            dateChoose.dd = dd.currentItemValue
            callbak(dateChoose)
            dismiss()
        }
        return this
    }

}