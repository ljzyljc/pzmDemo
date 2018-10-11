package cn.android.support.v7.lib.sin.crown.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON数据转实体类工具，一般都是读取json数据。而转化为json数据的，比较少用。
 * 1,必须有空构造函数，2，必须有get，set方法【get,set后面的那个字母必须是大写】。
 * 3，必须实现Serializable接口(implements)。这个没有必要，如果要对这个实体类进行数据缓存的话，就必须实现序列化。单纯的json数据转换，不需要实现Serializable。只有对数据进行保存的时候才需要。
 * 4，只对当前类属性有效，父类属性访问不到。
 * <p>
 * 日期格式，也是String类型。一定要使用Integer,Boolean类型，而不是基本类型int,boolean。不要使用基本类型，也不要使用Object
 * 5，属性支持String，Double，Float，Integer，Boolean，Long类型【Integer能转int，Boolean能转boolean,基本类型能够转换成所对应的类型】,不支持Byte,char类型
 * <p>
 * 对Object也不支持，只是保存了Object的引用地址而已，无法真正保存一个Object对象。
 * 虽然支持基本类型(Object强制转换的)，不推荐使用基本类型。尽量使用Strign,Integer等类型，而不是int,long。尽量不要使用基本类型。
 * <p>
 * <p>
 * JSONObjiect对象，自带url解密。即"\u6731\u4e8c" 这个格式，谷歌自带的json对象是可以解析出来的。
 * <p>
 * Created by 彭治铭 on 2017/5/26.
 */

public class JSONObjectUtils<T> {
    private static JSONObjectUtils utilJSONObject;

    private JSONObjectUtils() {
    }

    public static JSONObjectUtils getInstance() {
        if (utilJSONObject == null) {
            utilJSONObject = new JSONObjectUtils();
        }
        return utilJSONObject;
    }

    /**
     * JSONObject对象转实体类【通过反射实现，亲测可行，实体类只要不混淆即可，签名打包都没问题。效果杠杠的】
     *
     * @param jsonObject org.json.JSONObject 安卓自带的json对象，非第三方
     * @param clazz      泛型，Class类型。实体类的所有属性都必须是String类型。且必须具体空构造函数和set方法。【get方法里面没有用到，所以不需要】
     * @return 返回实体类
     */
    public T getBean(JSONObject jsonObject, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();//泛型实例化,注意啦，这一步，必须具备空构造函数，不然无法实例化
            //遍历类 成员属性【只对当前类有效，父类无效，即只获取本类的属性】
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                // 获取属性的名字
                String name = fields[i].getName();
                String jName = name;
                // 将属性的首字符大写，方便构造get，set方法
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                // 获取属性的类型
                String type = fields[i].getGenericType().toString();
                //Method m = t.getClass().getMethod("get" + name);
                // 调用getter方法获取属性值
                //String value = (String) m.invoke(t);
                //调用set方法

                ///Log.e("test","name:\t"+name);
                if (!jsonObject.has(jName)) {//判斷json數據是否存在該字段
                    continue;
                }

                Class[] parameterTypes = new Class[1];
                parameterTypes[0] = fields[i].getType();
                Method m = t.getClass().getMethod("set" + name, parameterTypes);//set方法
                Object[] objects = new Object[1];

                Object obj = jsonObject.get(jName);
                //判断值是否为空
                if (obj == null || obj.toString().equals("null") || obj.toString().equals("")) {
                    continue;//不允许值为空
                }

                //Log.e("test","type:\t"+type);
                // 如果type是类类型，则前面包含"class "，后面跟类名【一般都是String,Double,Object，所以这三个写前面】
                if (type.equals("class java.lang.String")) {
                    objects[0] = jsonObject.getString(jName);//String类型
                } else if (type.equals("class java.lang.Double")) {
                    objects[0] = jsonObject.getDouble(jName);//Double类型
                } else if (type.equals("class java.lang.Object")) {
                    objects[0] = jsonObject.get(jName);//Object类型
                } else if (type.equals("class java.lang.Float")) {
                    objects[0] = Float.valueOf(jsonObject.get(jName).toString());//Float类型
                } else if (type.equals("class java.lang.Integer")) {
                    objects[0] = jsonObject.getInt(jName);//Integer类型
                } else if (type.equals("class java.lang.Boolean")) {
                    objects[0] = jsonObject.getBoolean(jName);//Boolean类型
                    //Log.e("test","波尔:\t"+objects[0]);
                } else if (type.equals("class java.lang.Long")) {
                    objects[0] = jsonObject.getLong(jName);//Long类型
                } else {
                    objects[0] = jsonObject.get(jName);//Object类型,兼容基本类型
                }
                //Log.e("test","objects[0]:\t"+objects[0]+"\tjName:\t"+jName+"\t等于空:\t"+(objects[0]!=null)+"\t"+(!objects[0].toString().equals("null"))+"\t"+(!objects[0].toString().equals("")));
                if (objects[0] != null && !objects[0].toString().equals("null") && !objects[0].toString().equals("")) {
                    try {
                        //Log.e("test","type:\t"+type+"\tjName:\t"+jName+"\t值:\t"+objects[0]);
                        m.invoke(t, objects);//方法调用
                    } catch (Exception e) {
                        Log.e("test", "UtilJSONObject赋值异常:\t" + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            Log.e("test", "UtilJSONObject反射异常:\t" + e.getMessage());
        }
        return t;
    }

    /**
     * JSONObject转ArrayList
     *
     * @param jsonArray org.json.JSONArray安卓自带json数组
     * @param clazz     泛型，Class类型。
     * @return 返回 ArrayList 数组
     */
    public List<T> getArrayList(JSONArray jsonArray, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(getBean(jsonArray.getJSONObject(i), clazz));
            }
        } catch (Exception e) {
            Log.e("test", "UtilJSONObject反射List异常:\t" + e.getMessage());
        }
        return list;
    }

    //实体类转化成json数据,参数直接传实体类对象即可
    public JSONObject BeanToJSON(T t) {
        JSONObject jsonObject = new JSONObject();
        try {
            //遍历类 成员属性【只对当前类有效，父类无效，即只获取本类的属性】
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                // 获取属性的名字
                String name = fields[i].getName();
                if (name.trim().equals("$change") || name.trim().equals("serialVersionUID")) {
                    continue;
                }
                String jName = name;
                // 将属性的首字符大写，方便构造get，set方法
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                // 获取属性的类型
                String type = fields[i].getGenericType().toString();
                //Log.e("test","type:\t"+type+"\tjName:\t"+jName);
                Method m = null;
                try {
                    // 调用getter方法获取属性值
                    m = t.getClass().getMethod("get" + name);
                    Object obj = m.invoke(t);
                    //Log.e("test","obj:\t"+obj);
                    if (obj == null) {
                        continue;
                    }
                } catch (Exception e) {
                    Log.e("test", "get()异常:\t" + e.getMessage());
                }
                // 如果type是类类型，则前面包含"class "，后面跟类名
                if (type.equals("class java.lang.String")) {
                    String value = (String) m.invoke(t);
                    jsonObject.put(jName, value);
                } else if (type.equals("class java.lang.Double")) {
                    double value = (Double) m.invoke(t);
                    jsonObject.put(jName, value);
                } else if (type.equals("class java.lang.Object")) {
                    Object value = m.invoke(t);
                    jsonObject.put(jName, value);
                } else if (type.equals("class java.lang.Float")) {
                    float value = (float) m.invoke(t);
                    jsonObject.put(jName, value);
                } else if (type.equals("class java.lang.Integer")) {
                    int value = (int) m.invoke(t);
                    jsonObject.put(jName, value);
                } else if (type.equals("class java.lang.Boolean")) {
                    boolean value = (boolean) m.invoke(t);
                    jsonObject.put(jName, value);
                } else if (type.equals("class java.lang.Long")) {
                    long value = (long) m.invoke(t);
                    jsonObject.put(jName, value);
                } else {
                    Object value = m.invoke(t);
                    if (value != null && !value.equals("null") && !value.equals("")) {
                        jsonObject.put(jName, value);//Object类型,兼容基本类型
                    }
                }
            }
        } catch (Exception e) {
            Log.e("test", "UtilJSONObject实体类转JSON数据异常:\t" + e.getMessage());
        }
        return jsonObject;
    }

    //ArrayList转化为JSON
    public JSONArray ArrayListToJSONArray(List<T> list) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < list.size(); i++) {
                JSONObject jsonObject = BeanToJSON(list.get(i));
                jsonArray.put(i, jsonObject);
            }
        } catch (Exception e) {
            Log.e("test", "UtilJSONObject List转换JSON异常:\t" + e.getMessage());
        }
        return jsonArray;
    }

    //兼容之前的。
    public JSONArray ArrayListToJSON(List<T> list) {
        return ArrayListToJSONArray(list);
    }
}
