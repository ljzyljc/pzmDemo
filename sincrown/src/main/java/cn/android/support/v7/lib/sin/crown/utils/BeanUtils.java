package cn.android.support.v7.lib.sin.crown.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * String转实体类
 * Created by 彭治铭 on 2018/3/2.
 */

public class BeanUtils {
    private static BeanUtils beanUtils;

    public static BeanUtils getInstance() {
        if (beanUtils == null) {
            beanUtils = new BeanUtils();
        }
        return beanUtils;
    }

    /**
     * String转换为实体类
     *
     * @param bean 泛型类型，不能为空(用于指明泛型),泛型方法，参数必须指定一个泛型。
     * @param data Json数据字符串
     * @param <M>
     * @return
     */
    public <M> M getBean(M bean, String data) {
        return getBean(bean, data, null);
    }

    /**
     * String转换为实体类
     *
     * @param bean  泛型类型，不能为空(用于指明泛型),泛型方法，参数必须指定一个泛型。
     * @param data  Json数据字符串
     * @param field json数据里面的字段。拿该字段里的数据。如果字段为空，就使用data原数据。
     * @param <M>
     * @return
     */
    public <M> M getBean(M bean, String data, String field) {
        try {
            if (field != null && !field.trim().equals("")) {
                data = getData(data, field);//字段不为空，就获取字段里面的数据。
            }
            if (data != null && !data.trim().equals("") && !data.trim().equals("null")) {
                if (bean.getClass().getName().trim().equals(String.class.getName().trim())) {
                    bean = (M) data;//字符串类型，防止为JSONArray时报错。
                } else {
                    bean = (M) JSONObjectUtils.getInstance().getBean(new JSONObject(data), bean.getClass());//这一步开始，bean就已经改变，不在是以前的bean，是新new出来的。和以前没有关系了。是两个对象了。。java传对象，传的是引用的拷贝。
                }
            }
        } catch (Exception e) {
            Log.e("test", "UtilHttp getBean异常2:\t" + e.getMessage());
        }
        return bean;
    }

    /**
     * String 转实体类数组
     *
     * @param bean  泛型类型，不能为空(用于指明泛型),泛型方法，参数必须指定一个泛型。
     * @param data  Json数据字符串
     * @param field json数据里面的字段。拿该字段里的数据
     * @param <M>
     * @return
     */
    public <M> List<M> getBeans(M bean, String data, String field) {
        List<M> beans = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                data = jsonArray.getString(i);
                if (data != null) {
                    bean = getBean(bean, data, field);
                    if (bean != null) {
                        beans.add(bean);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("test", "UtilBean getDatas()异常:\t" + e.getMessage());
        }
        return beans;
    }

    /**
     * 获取json数据莫个字段的数据
     *
     * @param result JSON数据字符串
     * @param field  字段名
     * @return
     */
    public String getData(String result, String field) {
        String data = null;
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.has(field)){
                data = jsonObject.getString(field);
            }
        } catch (Exception e) {
            Log.e("test", "UtilBean getData()异常:\t" + e.getMessage());
        }
        return data;
    }

}
