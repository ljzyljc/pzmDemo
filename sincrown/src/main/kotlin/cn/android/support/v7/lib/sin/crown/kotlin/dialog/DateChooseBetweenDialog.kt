package cn.android.support.v7.lib.sin.crown.kotlin.dialog

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.TextView
import cn.android.support.v7.lib.sin.crown.kotlin.R
import cn.android.support.v7.lib.sin.crown.kotlin.base.BaseDialog
import cn.android.support.v7.lib.sin.crown.kotlin.bean.DateChoose
import cn.android.support.v7.lib.sin.crown.kotlin.common.px
import cn.android.support.v7.lib.sin.crown.utils.ProportionUtils
import cn.android.support.v7.lib.sin.crown.utils.TimeUtils
import cn.android.support.v7.lib.sin.crown.view.RollerView

/**
 * 日期选择器(开始日期 至 结束日期)
 * Created by 彭治铭 on 2018/6/3.
 */
//使用说明
//            DateChooseBetweenDialog(this, DateChoose(), DateChoose()).setCallBack { dateChooseStart, dateChooseEnd ->
//                Log.e("test", "回调\t开始日期:\t" + dateChooseStart.toString() + "\t结束日期:\t" + dateChooseEnd.toString())
//            }.end()//选择结束日期
class DateChooseBetweenDialog(context: Activity, var dateChooseStart: DateChoose = DateChoose(), var dateChooseEnd: DateChoose = DateChoose(), isStatus: Boolean = true, isTransparent: Boolean = true) : BaseDialog(context, R.layout.crown_dialog_date_choose_between,isStatus,isTransparent) {
    val yyyy: RollerView by lazy { findViewById<RollerView>(R.id.crown_roller_yyyy) }
    val MM: RollerView by lazy { findViewById<RollerView>(R.id.crown_roller_MM) }
    val dd: RollerView by lazy { findViewById<RollerView>(R.id.crown_roller_dd) }

    val txtStart: TextView by lazy { findViewById<TextView>(R.id.crown_txt_start) }//开始日期文本
    val viewStar: View by lazy { findViewById<View>(R.id.crown_view_start) }//开始日期横线
    val linearStart: View by lazy { findViewById<View>(R.id.linearStart) }//开始日期
    val date_start: View by lazy { findViewById<View>(R.id.date_start) }//开始日期

    val txtEnd: TextView by lazy { findViewById<TextView>(R.id.crown_txt_end) }//结束日期文本
    val viewEnd: View by lazy { findViewById<View>(R.id.crown_view_end) }//结束日期横线
    val linearEnd: View by lazy { findViewById<View>(R.id.linearEend) }//结束日期
    val date_end: View by lazy { findViewById<View>(R.id.date_end) }//结束日期


    val yyyy2: RollerView by lazy { findViewById<RollerView>(R.id.crown_roller_yyyy2) }
    val MM2: RollerView by lazy { findViewById<RollerView>(R.id.crown_roller_MM2) }
    val dd2: RollerView by lazy { findViewById<RollerView>(R.id.crown_roller_dd2) }

    //开始日期选择
    fun star(): DateChooseBetweenDialog {
        txtStart.setTextColor(Color.parseColor("#3886C6"))
        viewStar.setBackgroundColor(Color.parseColor("#418fde"))

        txtEnd.setTextColor(Color.parseColor("#000000"))
        viewEnd.setBackgroundColor(Color.parseColor("#DADADA"))

        date_end.visibility = View.INVISIBLE
        date_start.visibility = View.VISIBLE
        return this
    }

    //结束日期选择
    fun end(): DateChooseBetweenDialog {
        txtEnd.setTextColor(Color.parseColor("#3886C6"))
        viewEnd.setBackgroundColor(Color.parseColor("#418fde"))

        txtStart.setTextColor(Color.parseColor("#000000"))
        viewStar.setBackgroundColor(Color.parseColor("#DADADA"))

        date_start.visibility = View.INVISIBLE
        date_end.visibility = View.VISIBLE
        return this
    }

    init {
        ProportionUtils.getInstance().adapterWindow(context, dialog?.window)//适配
        dialog?.window?.setWindowAnimations(R.style.crown_window_bottom)//动画
        //取消
        findViewById<View>(R.id.crown_txt_cancel).setOnClickListener {
            dismiss()
        }
        //完成
        findViewById<View>(R.id.crown_txt_ok).setOnClickListener {
            dismiss()
        }

        linearStart.setOnClickListener {
            //开始日期
            star()
        }

        linearEnd.setOnClickListener {
            //结束日期
            end()
        }
        star()//默认选择开始日期
        txtStart.setText(dateChooseStart.toString())
        txtEnd.setText(dateChooseEnd.toString())
        //年
        var list_yyyy = ArrayList<String>()
        for (i in 2010..2030) {
            list_yyyy.add(i.toString())
        }
        yyyy.setLineColor(Color.TRANSPARENT).setItems(list_yyyy).setTextSize(px.x(40f)).setCount(5)
                .setDefaultTextColor(Color.parseColor("#888888")).setSelectTextColor(Color.parseColor("#444444"))
        yyyy2.setLineColor(Color.TRANSPARENT).setItems(list_yyyy).setTextSize(px.x(40f)).setCount(5)
                .setDefaultTextColor(Color.parseColor("#888888")).setSelectTextColor(Color.parseColor("#444444"))
        //监听
        yyyy.setItemSelectListener(object : RollerView.ItemSelectListener {
            override fun onItemSelect(item: String?, position: Int) {
                item?.let {
                    dateChooseStart.yyyy = item
                }
                txtStart.setText(dateChooseStart.toString())
            }
        })
        yyyy2.setItemSelectListener(object : RollerView.ItemSelectListener {
            override fun onItemSelect(item: String?, position: Int) {
                item?.let {
                    dateChooseEnd.yyyy = item
                }
                txtEnd.setText(dateChooseEnd.toString())
            }
        })
        //月
        var list_MM = ArrayList<String>()
        for (i in 1..12) {
            list_MM.add(i.toString())
        }
        MM.setLineColor(Color.TRANSPARENT).setItems(list_MM).setTextSize(px.x(40f)).setCount(5)
                .setDefaultTextColor(Color.parseColor("#888888")).setSelectTextColor(Color.parseColor("#444444"))
        MM.setItemSelectListener(object : RollerView.ItemSelectListener {
            override fun onItemSelect(item: String?, position: Int) {
                item?.let {
                    dateChooseStart.MM = it
                }
                txtStart.setText(dateChooseStart.toString())
                //月份监听
                updateDays()
            }
        })
        MM2.setLineColor(Color.TRANSPARENT).setItems(list_MM).setTextSize(px.x(40f)).setCount(5)
                .setDefaultTextColor(Color.parseColor("#888888")).setSelectTextColor(Color.parseColor("#444444"))
        MM2.setItemSelectListener(object : RollerView.ItemSelectListener {
            override fun onItemSelect(item: String?, position: Int) {
                //月份监听
                item?.let {
                    dateChooseEnd.MM = it
                }
                txtEnd.setText(dateChooseEnd.toString())
                updateDays2()
            }
        })
        //日
        dd.setLineColor(Color.TRANSPARENT).setTextSize(px.x(40f)).setCount(5)
                .setDefaultTextColor(Color.parseColor("#888888")).setSelectTextColor(Color.parseColor("#444444"))
        dd2.setLineColor(Color.TRANSPARENT).setTextSize(px.x(40f)).setCount(5)
                .setDefaultTextColor(Color.parseColor("#888888")).setSelectTextColor(Color.parseColor("#444444"))
        dd.setItemSelectListener(object : RollerView.ItemSelectListener {
            override fun onItemSelect(item: String?, position: Int) {
                //日期监听
                item?.let {
                    dateChooseStart.dd = it
                }
                txtStart.setText(dateChooseStart.toString())
            }
        })
        dd2.setItemSelectListener(object : RollerView.ItemSelectListener {
            override fun onItemSelect(item: String?, position: Int) {
                //日期监听
                item?.let {
                    dateChooseEnd.dd = it
                }
                txtEnd.setText(dateChooseEnd.toString())
            }
        })

        //fixme 设置数据滚轮循环效果
        yyyy.isCyclic = true
        MM.isCyclic = true
        dd.isCyclic = true
        yyyy2.isCyclic = true
        MM2.isCyclic = true
        dd2.isCyclic = true

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

    fun updateDays2() {
        //日，联动，更加月份而改变
        var list_dd = ArrayList<String>()
        val mDay = TimeUtils.getInstance().getMonthDay(yyyy2.currentItemValue + "-" + MM2.currentItemValue)//天数
        for (i in 1..mDay) {
            list_dd.add(i.toString())
        }
        dd2.setItems(list_dd)
    }

    override fun listener() {
        updateDays()
        updateDays2()
        //选中
        yyyy.setCurrentPostion(yyyy.getItemPostion(dateChooseStart.yyyy))
        MM.setCurrentPostion(MM.getItemPostion(dateChooseStart.MM))
        dd.setCurrentPostion(dd.getItemPostion(dateChooseStart.dd))

        yyyy2.setCurrentPostion(yyyy2.getItemPostion(dateChooseEnd.yyyy))
        MM2.setCurrentPostion(MM2.getItemPostion(dateChooseEnd.MM))
        dd2.setCurrentPostion(dd2.getItemPostion(dateChooseEnd.dd))
    }

    override fun recycleView() {
    }

    //日期返回回调
    fun setCallBack(callbak: (dateChooseStart: DateChoose, dateChooseEnd: DateChoose) -> Unit): DateChooseBetweenDialog {
        //完成
        findViewById<View>(R.id.crown_txt_ok).setOnClickListener {
            dateChooseStart.yyyy = yyyy.currentItemValue
            dateChooseStart.MM = MM.currentItemValue
            dateChooseStart.dd = dd.currentItemValue

            dateChooseEnd.yyyy = yyyy2.currentItemValue
            dateChooseEnd.MM = MM2.currentItemValue
            dateChooseEnd.dd = dd2.currentItemValue

            callbak(dateChooseStart, dateChooseEnd)
            dismiss()
        }
        return this
    }

}