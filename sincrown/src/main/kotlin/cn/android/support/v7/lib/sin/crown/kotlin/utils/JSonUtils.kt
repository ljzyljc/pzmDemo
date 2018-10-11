package cn.android.support.v7.lib.sin.crown.kotlin.utils

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import cn.android.support.v7.lib.sin.crown.kotlin.type.TypeReference


/**
 * Kotlin json解析类。data class类型，参数必须有默认值才行。即data类型有默认参数，就等价空构造函数。
 * 单例模式。直接通过类名即可调用里面的方法。都是静态的。
 * Created by 彭治铭 on 2018/4/24.
 */
object JSonUtils {

    //fixme java实体类必须有setter()方法。
    //fixme kotlin 属性，不能被internal修饰。

    //fixme 内联函数不能再调用内联函数，即内联函数不能闭合[即不能自己调用自己]

    //fixme 直接传入泛型即可。基本支持所有类型。随便传。只要json数据格式正确，以及字段对应上即可。如果转换失败，一般都是json格式的问题。测试没问题，能够正确解析。
    //fixme 支持：Mode1；Model<Mode2>；Model<Model<Mode2>>；Model<ArrayList<Mode2>>；ArrayList<Mode2>；ArrayList<Model<Mode2>>,目前只支持三层。多了解析不出来
    //fixme 解析数据大约28毫秒。
    inline fun <reified T> parseAny(json: String?, vararg field: String): T {
        var typeReference = object : TypeReference<T>() {}
        return parseObject(parseJson(json, *field), typeReference.GenericClass, typeReference.GenericClass2, typeReference.GenericClass3) as T
    }

    //根据传入类型，自动分辨json和json数组。
    inline fun <reified T, reified T2, reified T3> parseObject(json: String?, clazz: Class<T>, clazzT2: Class<T2>? = null, clazzT3: Class<T3>? = null): Any? {
        if (clazz.name.trim().equals("java.util.ArrayList")) {
            //JSON数组
            try {
                clazzT2?.let {
                    var jsonArray = JSONArray(json.toString())
                    var last = jsonArray.length()
                    last -= 1//最后一个下标
                    if (last < 0) {
                        last = 0
                    }
                    var list = ArrayList<Any>()
                    for (i in 0..last) {
                        var m = parseObject(jsonArray.getJSONObject(i), clazzT2, clazzT3)
                        m?.let {
                            list.add(it as Any)
                        }
                    }
                    return list
                }

            } catch (e: Exception) {
                Log.e("test", "json数组异常:\t" + e.message)
            }
        } else {
            //JSON
            try {
                var jsonObject = JSONObject(json.toString())
                return parseObject(jsonObject, clazz, clazzT2, clazzT3)
            } catch (e: Exception) {
                Log.e("test", "json异常:\t" + e.message)
            }
        }
        return null
    }

    //var clazz = 对象.javaClass
    //调用 getBean(jsonObject, clazz)!! 后面的感叹号是发生异常时会抛出异常。kotlin不强制捕捉异常。
    //parseObject(jsonObject, String.javaClass)
    //JSonUtils.parseObject(json,BaseBean::class.java)
    //JSonUtils.parseObject(json,any.javaClass)
    //fixme clazzT泛型的class类型，可以为空
    fun <T> parseObject(jsonObject: JSONObject?, clazz: Class<T>, clazzT2: Class<*>? = null, clazzT3: Class<*>? = null, index: Int = 1): T? {
        try {
            //泛型实例化,注意啦，这一步，必须具备空构造函数，不然无法实例化。或者有默认参数也行
            //必须有空构造函数，或者所有参数都有默认参数。说的是所有参数。不然无法实例化。
            var t: T = clazz.newInstance()

            //判断json数据是否为空
            if (jsonObject == null || jsonObject.toString().trim().equals("") || jsonObject.toString().trim().equals("{}") || jsonObject.toString().trim().equals("[]")) {
                return t
            }
            //Log.e("test","json数据：\t"+jsonObject.toString())
            //Log.e("test","执行中。。。")
            clazz?.declaredFields?.forEach {
                //Log.e("test","执行循环中。。。")
                var value: String? = null
                if (jsonObject.has(it.name)) {//判斷json數據是否存在該字段
                    value = jsonObject.getString(it.name)//获取json数据
                }
                //Log.e("test","属性：\t"+it.name+"\t数据:\t"+value)
                if (value != null && !value.trim().equals("") && !value.trim().equals("null")) {
                    //if (!it.name.equals("serialVersionUID") && !it.name.equals("\$change")) {
                    var type = it.genericType.toString().trim()//属性类型
                    var name = it.name.substring(0, 1).toUpperCase() + it.name.substring(1)//属性名称【首字目进行大写】。
                    val m = clazz.getMethod("set" + name, it.type)
                    //Log.e("test", "属性:\t" + it.name + "\t类型:\t" + it.genericType.toString() + "\ttype:\t" + type+"\t数据：\t"+value)
                    //fixme 以下兼容了八了基本类型和 Stirng及Any。几乎兼容所有类型。兼容了java 和 kotlin
                    //kotlin基本类型虽然都对象，但是class文件都是基本类型。不是class类型哦。
                    // 即kotlin基本类型的字节码都是基本类型。
                    if (type == "class java.lang.String" || type == "class java.lang.Object") {//Object 就是Any,class类型是相同的。
                        m.invoke(t, value)//String类型 Object类型
                    } else if (type == "int" || type.equals("class java.lang.Integer")) {
                        m.invoke(t, value.toInt())//Int类型
                    } else if (type == "float" || type.equals("class java.lang.Float")) {
                        m.invoke(t, value.toFloat())//Float类型
                    } else if (type == "double" || type.equals("class java.lang.Double")) {
                        m.invoke(t, value.toDouble())//Double类型
                    } else if (type == "long" || type.equals("class java.lang.Long")) {
                        m.invoke(t, value.toLong())//Long类型
                    } else if (type == "boolean" || type.equals("class java.lang.Boolean")) {
                        m.invoke(t, value.toBoolean())//布尔类型。 "true".toBoolean() 只有true能够转换为true，其他所有值都只能转换为false
                    } else if (type == "short" || type.equals("class java.lang.Short")) {
                        m.invoke(t, value.toShort())//Short类型
                    } else if (type == "byte" || type.equals("class java.lang.Byte")) {
                        var byte = value.toInt()//不能有小数点，不然转换异常。小数点无法正常转换成Int类型。可以有负号。负数能够正常转换。
                        if (byte > 127) {
                            byte = 127
                        } else if (byte < -128) {
                            byte = -128
                        }
                        m.invoke(t, byte.toByte())//Byte类型 ,范围是：-128~127
                    } else if (type == "char" || type.equals("class java.lang.Character")) {
                        m.invoke(t, value.toCharArray()[0])//Char类型。字符只有一个字符。即单个字符。
                    } else if (!type.equals("class java.util.HashMap")) {
                        try {
                            if ((type.toString().trim().equals("T") || type.toString().trim().equals("T2")) && clazzT2 != null) {
                                //Log.e("test", "嵌套泛型：\t" + clazzT2.name + "\t" + clazzT2)
                                //fixme 嵌套泛型。只支持一级嵌套泛型。不支持多层。泛型标志固定一下。就用T或者T2。不要用其他的。
                                if (clazzT2.name.equals("java.util.ArrayList") || clazzT2.name.equals("class java.util.ArrayList") || clazzT2.equals("class java.util.LinkedHashMap")) {
                                    //嵌套泛型数组,格式：Model<ArrayList<Mode2>>
                                    var jsonArray = JSONArray(value)
                                    var last = jsonArray.length()
                                    last -= 1//最后一个下标
                                    if (last < 0) {
                                        last = 0
                                    }
                                    var list = ArrayList<Any>()
                                    clazzT3?.let {
                                        for (i in 0..last) {
                                            var m = parseObject(jsonArray.getJSONObject(i), clazzT3)
                                            m?.let {
                                                list.add(it as Any)
                                            }
                                        }
                                    }
                                    m.invoke(t, list)
                                } else {
                                    //嵌套泛型实体类
                                    if (index == 1) {
                                        clazzT2?.let {
                                            m.invoke(t, parseObject(JSONObject(value), clazzT2, clazzT3, null, 2))
                                        }
                                    } else if (index >= 2) {
                                        clazzT2?.let {
                                            m.invoke(t, parseObject(JSONObject(value), clazzT2, null, null, 3))
                                        }
                                    }
                                }
                            } else {
                                //fixme 实体类里面嵌套普通具体的实体类[必须是具体的实体类型,可以无限循环下去，但是不能是ArrayList数组，因为无法获取数组内部的具体类型。]
                                if (!type.equals("class java.util.ArrayList") && !type.equals("class java.util.LinkedHashMap")) {
                                    var clazz = Class.forName(type.substring(5).trim())//具体类名，去除class前缀
                                    m.invoke(t, parseObject(JSONObject(value), clazz, null))
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("test", "嵌套json解析异常:\t" + e.message)
                        }

                    }
                }
            }
            return t
        } catch (e: Exception) {
            Log.e("test", "转化实体类解析异常:\t" + e.message)
        }
        return null
    }

    //必须传一个对象的实例。空的也行。
    // JSonUtils.parseObject(response, String())
    inline fun <T : Any> parseObject(result: String?, t: T): T? {
        //Log.e("test", "执行了T")
        var jsonObjec: JSONObject? = null
        try {
            jsonObjec = JSONObject(result)
        } catch (e: Exception) {
            Log.e("test", "json解析异常:\t" + result)
        }
        var typeReference = object : TypeReference<T>() {}
        //return parseObject(jsonObjec, t::class.java)
        //Log.e("test","类型:\t"+t::class.java+"\t"+typeReference.genericTClass)
        return parseObject(jsonObjec, t::class.java, typeReference.GenericClass2)//支持一级嵌套泛型。
    }

    inline fun <T : Any> parseObject(result: String?, t: T, typeReference: TypeReference<T>): T? {
        //Log.e("test", "执行了T")
        var jsonObjec: JSONObject? = null
        try {
            jsonObjec = JSONObject(result)
        } catch (e: Exception) {
            Log.e("test", "json解析异常:\t" + result)
        }
        //return parseObject(jsonObjec, t::class.java)
        //Log.e("test","类型:\t"+t::class.java+"\t"+typeReference.genericTClass)
        return parseObject(jsonObjec, t::class.java, typeReference.GenericClass2)//支持一级嵌套泛型。
    }

    //JSonUtils.parseArray(response, ArrayList<String>())
    //不知道是抽什么风，用T无法正常解析。只能用T2
    inline fun <reified T2 : Any> parseArray(result: String?, list: ArrayList<T2>): ArrayList<T2>? {
        //Log.e("test", "执行了 ArrayList<T>")
        var jsonArray = JSONArray(result)
        var length = jsonArray.length()
        if (length > 0) {
            length -= 1
            if (length < 0) {
                length = 0
            }
            var typeReference = object : TypeReference<T2>() {}
            for (i in 0..length) {
                var t = parseObject(jsonArray.getJSONObject(i), T2::class.java, typeReference.GenericClass2)//支持一级嵌套泛型。
                t?.let {
                    if (it is T2) {
                        list.add(it)
                    }
                }
            }
        }
        return list
    }

    //数据解析(解析之后，可以显示中文。)
    //根据字段解析数据(如果该字段不存在，就返回原有数据)
    fun parseJson(result: String?, vararg field: String): String? {
        var response = result
        //解析字段里的json数据
        for (i in field) {
            i?.let {
                var json = JSONObject(response)
                if (json.has(it)) {
                    response = json.getString(it)
                }
            }
        }
        return response
    }

}