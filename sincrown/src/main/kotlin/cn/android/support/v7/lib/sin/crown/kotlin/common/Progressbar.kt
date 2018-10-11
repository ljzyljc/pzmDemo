package cn.android.support.v7.lib.sin.crown.kotlin.common

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import cn.android.support.v7.lib.sin.crown.kotlin.base.BaseDialog
import cn.android.support.v7.lib.sin.crown.view.ProgressCircleView
import org.jetbrains.anko.UI
import org.jetbrains.anko.verticalLayout

/**
 * 进度条
 * Created by 彭治铭 on 2018/6/24.
 */
class Progressbar(activity: Activity, isStatus: Boolean = true, isTransparent: Boolean = false) : BaseDialog(activity, isStatus = isStatus, isTransparent = isTransparent) {

    override fun onCreateView(context: Context): View? {
        return context.UI {
            verticalLayout {
                gravity = Gravity.CENTER
                var progressView = ProgressCircleView(this.context)
                addView(progressView)
            }
        }.view
    }

    init {
        isDismiss(false)//触摸不消失
        isLocked(true)//屏蔽返回键
    }

    override fun listener() {}

    override fun recycleView() {}
}