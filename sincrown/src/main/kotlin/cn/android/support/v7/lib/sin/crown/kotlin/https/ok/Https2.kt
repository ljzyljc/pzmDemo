package cn.android.support.v7.lib.sin.crown.kotlin.https.ok

import android.app.Activity
import android.util.Log
import cn.android.support.v7.lib.sin.crown.kotlin.common.Progressbar
import cn.android.support.v7.lib.sin.crown.kotlin.https.Http
import cn.android.support.v7.lib.sin.crown.kotlin.utils.KGsonUtils
import java.io.File

open class Https2() {
    open var url: String? = null
    fun url(url: String): Https2 {
        this.url = url
        return this
    }

    open var activity: Activity? = null
    //fixme activity不为空时，回调到主线程。且进度条也是在Activity不为空时才有效。
    fun activity(activity: Activity): Https2 {
        this.activity = activity
        return this
    }

    protected open var timeOut = 5000//超时链接时间，单位毫秒,一般500毫秒足已。亲测100%有效。极少数设备可能脑抽无效。不用管它。
    fun timeOut(timeOut: Int = this.timeOut): Https2 {
        this.timeOut = timeOut
        return this
    }

    open var load: Boolean = false//fixme 是否显示进度条，默认不显示， (Activity不能为空，Dialog需要Activity的支持)
    //弹框默认参数，就是true,开启。
    fun showLoad(activity: Activity? = null, isLoad: Boolean = true): Https2 {
        activity?.let {
            this.activity = activity
        }
        this.load = isLoad
        return this
    }

    //进度条变量名，子类虽然可以重写，但是类型改不了。所以。进度条就不允许继承了。子类自己去定义自己的进度条。
    protected open var progressbar: Progressbar? = null//进度条(Activity不能为空，Dialog需要Activity的支持)

    //fixme 显示进度条[子类要更改进度条，可以重写这个]
    //重写的时候，注意屏蔽父类的方法，屏蔽 super.showProgress()
    open fun showProgressbar() {
        if (load) {
            if (progressbar == null && activity != null) {
                progressbar = Progressbar(activity!!)
            }
            progressbar?.show()
        }
    }

    //fixme 关闭进度条[子类可以重写,重写的时候，记得对自己的进度条进行内存释放。]
    //重写的时候，注意屏蔽父类的方法，屏蔽 super.showProgress()
    open fun dismissProgressbar() {
        if (load) {
            progressbar?.let {
                progressbar?.dismiss()
                progressbar = null
            }
        }
    }

    open var cacle: Boolean = false//是否缓存，默认不缓存
    fun cacle(isCache: Boolean = true): Https2 {
        cacle = isCache
        return this
    }

    open var repeat: Boolean = false//是否允许网络重复请求。默认不允许重复请求。
    fun repeat(isRepeat: Boolean = true): Https2 {
        this.repeat = isRepeat
        return this
    }

    open var showParams: Boolean = false//是否显示打印参数，默认不打印
    fun showParams(isShowParam: Boolean = true): Https2 {
        this.showParams = isShowParam
        return this
    }

    //开始回调
    open var start: (() -> Unit)? = null

    fun onStart(start: (() -> Unit)? = null): Https2 {
        this.start = start
        return this
    }

    //成功回调
    open var success: ((result: String) -> Unit)? = null

    fun onSuccess(success: ((result: String) -> Unit)? = null): Https2 {
        this.success = success
        return this
    }

    //失败回调
    open var failure: ((errStr: String?) -> Unit)? = null

    fun onFailure(failure: ((errStr: String?) -> Unit)? = null): Https2 {
        this.failure = failure
        return this
    }

    //结束回调，无论是成功还是失败都会调用(最后执行)
    open var finish: (() -> Unit)? = null

    fun onFinish(finish: (() -> Unit)? = null): Https2 {
        this.finish = finish
        return this
    }

    //参数
    //header头部参数。Get，Post都行
    open val headers: MutableMap<String, String> by lazy { mutableMapOf<String, String>() }
    //params属于 body子集。Get，Post都行
    open val params: MutableMap<String, String?> by lazy { mutableMapOf<String, String?>() }
    //files也属于params，文件上传。Pst请求
    open val files: MutableMap<String, File> by lazy { mutableMapOf<String, File>() }
    //params,files,body都可以同时使用。Post请求
    open var body: String? = null

    //fixme 方法必须放在变量声明之后
    //设置默认参数，以及对参数做一些特殊处理（如加密）。子类可以重写
    //在请求之前会调用。
    protected open fun onPreParameter() {
        //header默认参数
        //headers.put("1", "1")
        //params默认参数
        //params.put("2", "2")
    }

    //fixme 对服务器返回数据最先处理，做一些特殊处理。
    //fixme 如数据解密等（先解密，然后才进行json解析）。子类可以重写
    open fun onPostResponse(response: String): String {
        return response
    }

    fun body(body: String? = null): Https2 {
        body?.let {
            this.body = body
        }
        return this
    }

    //添加头部参数
    fun addHeader(key: String, value: String): Https2 {
        headers.put(key, value)
        return this
    }

    //添加头部参数(融合两个Map)
    fun addHeader(header: MutableMap<String, String>? = null): Https2 {
        header?.let {
            for ((key, value) in header.entries) {
                headers.put(key, value)
            }
        }
        return this
    }

    fun addParam(key: String, value: String?): Https2 {
        value?.let {
            if (!it.trim().equals("") && !it.trim().equals("null") && !it.trim().equals("NULL")) {
                params.put(key, value)
            }
        }
        return this
    }

    fun addParam(param: MutableMap<String, String>? = null): Https2 {
        param?.let {
            for ((key, value) in param.entries) {
                params.put(key, value)
            }
        }
        return this
    }

    fun addFile(key: String, value: File): Https2 {
        files.put(key, value)
        return this
    }

    fun addFile(file: MutableMap<String, File>? = null): Https2 {
        file?.let {
            for ((key, value) in file.entries) {
                files.put(key, value)
            }
        }
        return this
    }

    //fixme Get请求,所有参数设置完成之后再调用
    inline fun <reified T> get(vararg field: String, noinline callback: ((t: T) -> Unit)? = null) {
        onPreParameter()
        Http.LogParams(this)
        Http.Get2(url, activity, this, requestCallBack = object : GenericsCallback2(this) {
            override fun onResponse(response: String?) {
                callback?.let {
                    response?.let {
                        //fixme 默认返回原始数据String(包括缓存数据)，数据不为空的时候调用
                        callback(KGsonUtils.parseAny<T>(it, *field))
                    }
                }
                super.onResponse(response)
            }
        }, timeOut = timeOut)
    }

    //fixme Post请求,所有参数设置完成之后再调用
    inline fun <reified T> post(vararg field: String, noinline callback: ((t: T) -> Unit)? = null) {
        onPreParameter()
        Http.LogParams(this)
        Http.Post2(url, activity, this, requestCallBack = object : GenericsCallback2(this) {
            override fun onResponse(response: String?) {
                callback?.let {
                    response?.let {
                        //fixme 默认返回原始数据String(包括缓存数据)，数据不为空的时候调用
                        callback(KGsonUtils.parseAny<T>(it, *field))
                    }
                }
                super.onResponse(response)
            }

        }, timeOut = timeOut)
    }
}