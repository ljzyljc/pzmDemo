package cn.android.support.v7.lib.sin.crown.kotlin.bean

import cn.android.support.v7.lib.sin.crown.utils.TimeUtils

/**
 * 日期选择数据（保存了，年，月，日，及其下标）
 * Created by 彭治铭 on 2018/6/3.
 */
class DateChoose {

    var yyyy: String//年
    var MM: String//月

    var dd: String//日

    init {
        //初始化，年月日，以当前时间为标准
        yyyy = TimeUtils.getInstance().getYear(null).toString()
        MM = TimeUtils.getInstance().getMonth(null).toString()
        dd = TimeUtils.getInstance().getDay(null).toString()
    }

    //默认格式 yyyy-MM-dd
    override fun toString(): String {
        var M=MM
        if(M.length<2){
            M="0"+M
        }
        var d=dd
        if(d.length<2){
            d="0"+d
        }
        return yyyy + "-" + M + "-" + d
    }

}