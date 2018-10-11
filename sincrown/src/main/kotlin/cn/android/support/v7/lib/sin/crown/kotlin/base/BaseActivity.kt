package cn.android.support.v7.lib.sin.crown.kotlin.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import cn.android.support.v7.lib.sin.crown.kotlin.R
import cn.android.support.v7.lib.sin.crown.kotlin.common.Toast

/**
 * Created by 彭治铭 on 2018/6/24.
 */
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 将当前Activity添加到栈中
        BaseActivityManager.getInstance().pushActivity(this)
        requestWindowFeature(Window.FEATURE_NO_TITLE)//无标题栏(setContentView()之前才有效)
        //设置状态栏透明
        BaseApplication.getInstance().setStatusBarTransparent(window)
    }

    //是否开启动画。true开启(默认开启)，false不开启。[子类可以重写]
    protected open fun isopenAnim(): Boolean {
        return true
    }

    open var enterAnim = R.anim.crown_right_in_without_alpha//进入动画[子类可重写]
    open var exitAnim = R.anim.crown_right_out_without_alpha//退出动画[子类可重写]

    //true 状态栏字体颜色为 黑色，false 状态栏字体颜色为白色。子类可以重写
    protected open fun isDarkMode(): Boolean {
        return BaseApplication.getInstance().darkmode
    }

    override fun onResume() {
        super.onResume()
        //设置状态栏字体颜色
        BaseApplication.getInstance().setStatusBarDrak(window, isDarkMode())
        if (isopenAnim()) {
            //启动动画(app首次启动也有效果。)
            overridePendingTransition(enterAnim, exitAnim)
            //overridePendingTransition(0, 0);这个可以关闭动画，系统默认动画也会关闭
        } else {
            overridePendingTransition(0, 0)//关闭动画。防止动画没有关闭。
        }
    }

    override fun onPause() {
        super.onPause()
        if (isopenAnim()) {
            //退出动画(app最后一个activity,关闭应用时，无效。即退出应用时无效)
            overridePendingTransition(enterAnim, exitAnim)
        } else {
            overridePendingTransition(0, 0)
        }
    }

    //true 程序按两次退出。false正常按键操作。[子类可以重写]
    protected open fun isExit(): Boolean {
        return false//默认不监听返回键，不退出
    }

    private var exitTime: Long = 0
    //open var exitInfo = "再按一次退出"//退出提示信息[子类可以重写]
    open var exitInfo = "别点了，再点我就要走了"
    //监听返回键
    override fun onBackPressed() {
        if (isExit()) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.show(exitInfo)
                exitTime = System.currentTimeMillis()
            } else {
                finish()
                BaseActivityManager.getInstance().finishAllActivity()
                BaseApplication.getInstance().exit()//退出应用（杀进程）
            }
        } else {
            super.onBackPressed()
        }
    }

}