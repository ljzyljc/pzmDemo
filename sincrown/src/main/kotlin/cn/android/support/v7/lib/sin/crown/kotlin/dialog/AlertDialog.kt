package cn.android.support.v7.lib.sin.crown.kotlin.dialog

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import cn.android.support.v7.lib.sin.crown.kotlin.R
import cn.android.support.v7.lib.sin.crown.kotlin.base.BaseDialog
import cn.android.support.v7.lib.sin.crown.kotlin.common.px
import org.jetbrains.anko.*

//            val alert: AlertDialog by lazy { AlertDialog(this) }
//            alert.little(false).title("温馨").mession("是否确认退出？").positive("确定"){
//                ToastUtils.showToastView("点击确定")
//            }.negative("NO"){
//                ToastUtils.showToastView("NO!!!")
//            }.isDismiss(false).show()
class AlertDialog(activity: Activity, isStatus: Boolean = true, isTransparent: Boolean = true) : BaseDialog(activity,isStatus = isStatus,isTransparent = true) {

    override fun onCreateView(context: Context): View? {
        return context.UI {
            verticalLayout {
                gravity = Gravity.CENTER
                relativeLayout {
                    id = px.id("crown_alert_parent")
                    isClickable = true
                    background=resources.getDrawable(R.drawable.crown_drawable_alert)
                    //标题
                    textView {
                        id = px.id("crown_txt_title")
                        textColor = Color.parseColor("#242424")
                        textSize = px.textSizeX(32)
                    }.lparams {
                        leftMargin = px.x(24)
                        topMargin = px.x(24)
                    }

                    //内容
                    textView {
                        id = px.id("crown_txt_mession")
                        textColor = Color.parseColor("#242424")
                        textSize = px.textSizeX(26)
                    }.lparams {
                        leftMargin = px.x(26)
                        centerVertically()
                    }

                    //取消
                    textView {
                        id = px.id("crown_txt_Negative")
                        textColor = Color.parseColor("#239F93")
                        textSize = px.textSizeX(26)
                        padding = px.x(24)
                    }.lparams {
                        alignParentBottom()
                        leftOf(px.id("crown_txt_Positive"))
                    }

                    //确定
                    textView {
                        id = px.id("crown_txt_Positive")
                        textColor = Color.parseColor("#239F93")
                        textSize = px.textSizeX(26)
                        padding = px.x(24)
                    }.lparams {
                        alignParentBottom()
                        alignParentRight()
                        leftOf(px.id("crown_txt_Positive"))
                    }

                }.lparams {
                    width = px.x(500)
                    height = px.y(300)
                }
            }
        }.view
    }

    var little = false//是否为小窗口，默认不是。
    fun little(little: Boolean = true): AlertDialog {
        this.little = little
        return this
    }

    val container: View by lazy { findViewById<View>(px.id("crown_alert_parent")) }
    //标题栏文本
    var txt_title: String? = ""
    val title: TextView by lazy { findViewById<TextView>(px.id("crown_txt_title")) }
    fun title(title: String? = null): AlertDialog {
        txt_title = title
        return this
    }

    //信息文本
    var txt_mession: String? = ""
    val mession: TextView by lazy { findViewById<TextView>(px.id("crown_txt_mession")) }
    fun mession(mession: String? = null): AlertDialog {
        txt_mession = mession
        return this
    }

    val negative: TextView by lazy { findViewById<TextView>(px.id("crown_txt_Negative")) }
    //左边，取消按钮
    fun negative(negative: String? = "取消", callback: (() -> Unit)? = null): AlertDialog {
        this.negative.setText(negative)
        this.negative.setOnClickListener {
            callback?.run {
                this()
            }
            dismiss()
        }
        return this
    }

    val positive: TextView by lazy { findViewById<TextView>(px.id("crown_txt_Positive")) }
    //右边，确定按钮
    fun positive(postive: String? = "确定", callback: (() -> Unit)? = null): AlertDialog {
        this.positive.setText(postive)
        this.positive.setOnClickListener {
            callback?.run {
                this()
            }
            dismiss()
        }
        return this
    }

    init {
        //取消
        negative.setOnClickListener {
            dismiss()
        }
        //确定
        positive.setOnClickListener {
            dismiss()
        }
        isDismiss(false)//默认不消失
    }


    override fun listener() {
        container.layoutParams.width = px.x(500)
        if (little) {
            container.layoutParams.height = px.x(200)
        } else {
            container.layoutParams.height = px.x(300)
        }
        container.requestLayout()
        title.setText(txt_title)
        mession.setText(txt_mession)
    }

    override fun recycleView() {
    }


}