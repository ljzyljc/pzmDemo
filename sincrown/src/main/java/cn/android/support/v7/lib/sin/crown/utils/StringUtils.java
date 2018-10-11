package cn.android.support.v7.lib.sin.crown.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;

public class StringUtils {
    public static StringUtils stringUtils;

    public static StringUtils getInstance() {
        if (stringUtils == null) {
            stringUtils = new StringUtils();
        }
        return stringUtils;
    }

    private StringUtils() {
    }

    /**
     * 去除字符串双引号
     *
     * @return
     */
    public String removeMarks(String result) {
        result = result.replace("\"", "");
        return result;
    }

    /**
     * 流转化为 String
     *
     * @param inputstream 流
     * @param character   字符编码，可以是"UTF-8"(空值默认),"GB2312"( 文件的编码格式),"ISO8859-1"
     * @return
     */
    public String readInput(InputStream inputstream, String character) {
        String detials = null;
        try {
            if (character == null || character.equals("")) {
                character = "UTF-8";
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    inputstream, character));
            String line = "";
            StringBuffer temp = null;
            temp = new StringBuffer("");
            while ((line = br.readLine()) != null) {
                temp.append(line);
            }
            detials = new String(temp);// String文本内容
            br.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return detials;
    }

    /**
     * Byte转字符串的时候，会有损失的。Byte如果要转字符串，最好是进行Base64加密成字符串。这样就不会有损失。
     * <p>
     * Stirng 转化为流
     *
     * @param detials   字符串
     * @param character 字符编码，可以是"UTF-8"(空值默认),"GB2312"( 文件的编码格式),"ISO8859-1"
     * @return
     */
    public InputStream getInputStream(String detials, String character) {
        InputStream inputstream = null;
        try {
            if (character == null || character.equals("")) {
                character = "UTF-8";
            }
            inputstream = new ByteArrayInputStream(detials.getBytes(character));
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("test", "流转化异常" + e.getMessage());
        }
        return inputstream;
    }

    /**
     * 将double转为数值，并最多保留num位小数。例如当num为2时，1.268为1.27，1.2仍为1.2；1仍为1，而非1.00;100.00则返回100。【会自动去除后面的0】
     *
     * @param d
     * @param num 小数位数
     * @return
     */
    public  String double2String(double d, int num) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(num);//保留两位小数
        nf.setGroupingUsed(false);//去掉数值中的千位分隔符

        String temp = nf.format(d);
        if (temp.contains(".")) {
            String s1 = temp.split("\\.")[0];
            String s2 = temp.split("\\.")[1];
            for (int i = s2.length(); i > 0; --i) {
                if (!s2.substring(i - 1, i).equals("0")) {
                    return s1 + "." + s2.substring(0, i);
                }
            }
            return s1;
        }
        return temp;
    }

    /**
     * 将double转为数值，并最多保留num位小数。
     *
     * @param d
     * @param num 小数个数
     * @param defValue 默认值。当d为null时，返回该值。
     * @return
     */
    public  String double2String(Double d, int num, String defValue){
        if(d==null){
            return defValue;
        }

        return double2String(d,num);
    }

}
