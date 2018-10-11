package cn.android.support.v7.lib.sin.crown.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * 数值计算
 */
public class NumberUtils {
    static NumberUtils number;

    public static NumberUtils getInstance() {
        if (number == null) {
            number = new NumberUtils();
        }
        return number;
    }

    /**
     * 如果数字小于10，则前面自动补上一个0
     *
     * @param s
     * @return
     */
    public String add0(String s) {
        int n = Integer.valueOf(s);
        if (n < 10) {
            return "0" + s;
        }
        return s;
    }

    /**
     * @param num 随机数的个数
     * @return 返回随机数
     */
    public String getRandom(int num) {
        //产生一个100以内的整数：int x=(int)(Math.random()*100);
        return getRandom(num,(int)(Math.random()*100));
    }

    /**
     * @param num   随机数的个数
     * @param seeds 随机种子
     * @return
     */
    public String getRandom(int num, int seeds) {
        String code = "";
        // 以时间为种子
        Random rand = new Random(System.currentTimeMillis() + seeds+(int)(Math.random()*1000));
        // 生成正随机数
        for (int i = 0; i < num; i++) {
            code = code + Math.abs(rand.nextInt() % 10);
        }
        return code;
    }

    /**
     * 返回小数点后指定个数的小数。【double和float数字个数过大时，就会显示16进制】，这个方法是以十进制为标准显示的。
     * 保留浮点型，小数点后指定个数。返回字符串类型。
     *
     * @param tempresult
     * @param keep 小数点后的个数。随便几个都可以。
     * @return
     */
    public String getReal(Double tempresult, int keep) {
        DecimalFormat df1 = null;
        if (keep <= 0) {
            keep=0;
            df1 = new DecimalFormat("0");
        }else {
            String n="";
            for(int i=1;i<=keep;i++){
                n=n+"0";
            }
            df1 = new DecimalFormat("0."+n.trim());
        }
        return df1.format(tempresult);
    }


    /**
     * 将字符串转换为整形。精确到小数后两位（其后全部割舍，不采用四舍五入），
     * 转换后的整数是原有的100倍（因为 微信的1对应0.01）
     *
     * @param s
     * @return
     */
    public String getStrToInt(String s) {
        Double b = Double.parseDouble(s) * 100;
        DecimalFormat format = new DecimalFormat("0.00 ");
        b = Double.parseDouble(format.format(b));
        int i = b.intValue();
        return i + "";
    }

    private DecimalFormat format = new DecimalFormat("0.00");// 格式

    /**
     * 计算大小,单位根据文件大小情况返回。返回结果带有单位。
     *
     * @param data 数据大小
     * @return
     */
    public String getDataSize(double data) {
        if (data < 1024 && data >= 0) {
            data = Double.parseDouble(format.format(data));
            return data + "B";
        } else if (data >= 1024 && data < 1024 * 1024) {
            data = data / 1024;
            data = Double.parseDouble(format.format(data));
            return data + "KB";
        } else if (data >= 1024 * 1024) {
            data = data / 1024 / 1024;
            data = Double.parseDouble(format.format(data));
            return data + "MB";
        }
        return null;
    }

    /**
     * @param data 单位MB。返回的结果不会带有MB两个字。返回格式 "0.00"
     * @return
     */
    public Double getDataSizeMB(double data) {
        data = data / 1024 / 1024;
        data = Double.parseDouble(format.format(data));
        return data;
    }

    /**
     * 计算百分比
     *
     * @param x     当前数值
     * @param total 总数值
     * @param keep  保留小数个数。0不保留小数，1小数一位，2小数2位。最大支持小数后四位
     * @return 返回百分比字符串。自带%百分比符合。
     */
    public String getPercent(long x, long total, int keep) {
        String result = "";// 接受百分比的值
        double x_double = x * 1.0;
        double tempresult = x / (double) total;
        // NumberFormat nf = NumberFormat.getPercentInstance(); 注释掉的也是一种方法
        // nf.setMinimumFractionDigits( 2 ); 保留到小数点后几位
        DecimalFormat df1 = null;
        if (keep <= 0) {
            df1 = new DecimalFormat("0%");
        } else if (keep == 1) {
            df1 = new DecimalFormat("0.0%");
        } else if (keep == 2) {
            df1 = new DecimalFormat("0.00%"); // ##.00%
        } else if (keep == 3) {
            df1 = new DecimalFormat("0.000%"); // ##.00%
        } else if (keep >= 4) {
            df1 = new DecimalFormat("0.0000%"); // ##.00%
        }
        // 百分比格式，后面不足2位的用0补齐
        // result=nf.format(tempresult);
        result = df1.format(tempresult);
        return result;
    }

    /**
     * 获取Bitmap图像的大小
     *
     * @param bitmap
     * @return
     */
    @SuppressLint("NewApi")
    public long getBitmapsize(Bitmap bitmap) {

        //在Android API（12）之前的版本和后来的版本是不一样：
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();

    }

}
