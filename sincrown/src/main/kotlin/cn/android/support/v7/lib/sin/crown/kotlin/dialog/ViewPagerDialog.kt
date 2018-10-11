package cn.android.support.v7.lib.sin.crown.kotlin.dialog

import android.app.Activity
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.*
import cn.android.support.v7.lib.sin.crown.kotlin.R
import cn.android.support.v7.lib.sin.crown.kotlin.base.BaseDialog
import cn.android.support.v7.lib.sin.crown.kotlin.https.Bitmaps
import cn.android.support.v7.lib.sin.crown.kotlin.widget.DotsView
import cn.android.support.v7.lib.sin.crown.utils.AssetsUtils
import cn.android.support.v7.lib.sin.crown.utils.ProportionUtils

/**
 * 网络位图放大显示
 */
class ViewPagerDialog(var activity: Activity?, isStatus: Boolean = false, isTransparent: Boolean = true) : BaseDialog(activity, R.layout.crown_dialog_viewpager,isStatus,isTransparent) {

    val viewPager: ViewPager by lazy { findViewById<ViewPager>(R.id.crown_viewpager) }
    val adapter: MyPagerAdapter by lazy { MyPagerAdapter(this) }

    val dots: DotsView by lazy { findViewById<DotsView>(R.id.dots) }

    init {
        ProportionUtils.getInstance().adapterWindow(activity,dialog?.window)//适配
        viewPager.adapter = adapter
        //动画，大小渐变
        setWindowAnimations(R.style.crown_window_samll_large)
        //滑动监听
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                dots.selectPosition(position).invalidate()
            }
        })
        isDismiss(false)
    }

    override fun listener() {
        adapter.notifyDataSetChanged()
        viewPager.currentItem = currentItem
        dots.count(adapter.uris.size).selectPosition(currentItem).invalidate()
    }

    override fun recycleView() {
        //释放掉位图
        for (uri in adapter.uris) {
            AssetsUtils.getInstance().recycleBitmap(uri)
        }
        adapter.uris.clear()
    }

    //设置网络图片数组
    fun uris(vararg uris: String): ViewPagerDialog {
        adapter.uris.clear()
        adapter.uris.addAll(uris.toMutableList().filter {
            //对url进行过滤
            if (it != null && !it.equals("")) {
                true
            } else {
                false
            }
        })
        return this
    }

    //当前选中下标
    var currentItem = 0

    fun currentItem(currentItem: Int): ViewPagerDialog {
        this.currentItem = currentItem
        dots.selectPosition(currentItem).invalidate()
        return this
    }

    class MyPagerAdapter(var dialog: ViewPagerDialog?, var uris: MutableList<String> = mutableListOf()) : PagerAdapter() {

        //fixme 默认时POSITION_UNCHANGED的，在该模式下adapter.notifyDataSetChangegd()；是无效的
        //fixme 解决方案就是在getItemPostion的方法，返回POSTION_NONE就可以了
        override fun getItemPosition(`object`: Any): Int {
            //return super.getItemPosition(`object`)
            return POSITION_NONE
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj//只有返回true时。才会显示视图
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            //super.destroyItem(container, position, obj)
            container?.removeView(obj as View)
            //释放掉位图
            AssetsUtils.getInstance().recycleBitmap(uris[position])
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val itemView = LayoutInflater.from(container?.getContext()).inflate(R.layout.crown_dialog_viewpager_item, container, false)
            var view = itemView.findViewById<View>(R.id.crown_viewpager_item)
            Bitmaps(uris[position], dialog?.activity).view(view).showLoad(true).onFailure {
                Log.e("test", "失败:\t" + it)
            }.Get()
            container.addView(itemView)//fixme 注意，必不可少。不然显示不出来。这里是itemView，不是view哦。之前就写错了，死活不出来
            //手势
            var simpleOnGestureListener = object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    dialog?.dismiss()//单击关闭
                    return super.onSingleTapConfirmed(e)
                }
            }
            var gestureDetectorCompat = GestureDetectorCompat(dialog?.activity, simpleOnGestureListener)
            view.setOnTouchListener { view, motionEvent ->
                gestureDetectorCompat.onTouchEvent(motionEvent)
                true
            }
            return itemView
        }

        override fun getCount(): Int {
            return uris.size
        }
    }

}