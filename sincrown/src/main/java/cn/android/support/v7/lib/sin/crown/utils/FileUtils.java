package cn.android.support.v7.lib.sin.crown.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 彭治铭 on 2018/1/21.
 */

public class FileUtils {
    private static FileUtils fileUtils;

    public static FileUtils getInstance() {
        if (fileUtils == null) {
            fileUtils = new FileUtils();
        }
        return fileUtils;
    }

    private FileUtils() {
    }

    /**
     * 创建文件
     *
     * @param path     路径【目录，不包含文件名】。
     * @param fileName 文件名。包含文件名后缀。
     * @return
     */
    public File createFile(String path, String fileName) {
        File fileParent = new File(path);
        if (fileParent.exists() == false) {
            fileParent.mkdirs();//判断该目录是否存在，不存在，就创建目录。
        }
        File file = new File(path + "/" + fileName);
        if (file.exists() == false) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e("test", "文件创建失败:\t" + e.getMessage());
            }
        }
        return file;
    }

    /**
     * 读取某个文件夹下的所有文件【包括子文件夹】
     *
     * @param filepath 文件夹路径
     */
    public List<File> readfiles(String filepath) {
        return readfiles(filepath, null);
    }

    /**
     * 读取某个文件夹下的所有文件【包括子文件夹】
     *
     * @param filepath 文件夹路径
     * @param files    文件集合，可以为null.为null时，会自行创建集合。
     */
    private List<File> readfiles(String filepath, List<File> files) {
        if (files == null) {
            files = new ArrayList<File>();
        }
        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                files.add(file);
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "/" + filelist[i]);//不要使用双反斜杠"\\"【可能不识别】,最好使用斜杠"/"
                    if (!readfile.isDirectory()) {
                        files.add(readfile);
                        //Log.e("test","文件名:\t"+readfile.getName()+"\t路径：\t"+readfile.getPath()+"\t是否为文件"+readfile.isFile()+"\t大小:\t"+readfile.length()+"\tfilepath:\t"+filepath);
                    } else if (readfile.isDirectory()) {
                        readfiles(filepath + "\\" + filelist[i], files);//递归，遍历文件夹下的子文件夹。
                    }
                }
            }
        } catch (Exception e) {
            Log.e("获取所有文件失败", "原因" + e.getMessage());
        }
        return files;
    }

    /**
     * 删除文件
     *
     * @param path 文件完整路径，包括文件后缀名 （以path为主，当path为null时，dir和name才有效）
     * @param dir  文件目录
     * @param name 文件名（包括后缀）
     */
    public void delFile(String path, String dir, String name) {
        try {
            if (path == null) {
                File file = new File(dir, name);
                if (file.exists()) {
                    file.delete();
                }
            } else {
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("文件删除异常", "异常信息" + e.getMessage());
        }

    }

    /**
     * 删除某个文件夹下的所有文件夹和文件
     *
     * @param delpath 文件夹路径
     */
    public boolean delAllFiles(String delpath) {
        try {
            File file = new File(delpath);
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + "/" + filelist[i]);//不要使用双反斜杠"\\"【可能不识别】,最好使用斜杠"/"
                    //Log.e("test","文件路径:\t"+delfile.getPath()+"\t名称:\t"+delfile.getName()+"\t"+filelist[i]);
                    if (!delfile.isDirectory()) {
                        //Log.e("test","删除:\t"+delfile.getName());
                        delfile.delete();
                    } else if (delfile.isDirectory()) {
                        delAllFiles(delpath + "/" + filelist[i]);
                    }
                }
                file.delete();
            }
        } catch (Exception e) {
            Log.e("删除所有文件异常", "原因" + e.getMessage());
        }
        return true;
    }

    /**
     * 復制文件到指定目录
     *
     * @param target 要复制的文件
     * @param dest   指定目录。
     */
    public File copyFile(File target, String dest) {
        String filename = target.getName();//文件名就使用原本的文件名。【获取的文件名是带后缀的。】
        File destFile = new File(dest + "/" + filename);////不要使用双反斜杠"\\"【可能不识别】,最好使用斜杠"/"
        try {
            //先进行输入才能进行输出，代码书序不能变
            InputStream in = new FileInputStream(target);
            OutputStream out = new FileOutputStream(destFile);
            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            Log.e("test", "文件复制异常:\t" + e.getMessage());
        }
        return destFile;
    }

    /**
     * 流转换成文件
     *
     * @param inputStream 流
     * @param path        路径
     * @param fileName    文件名
     * @return
     */
    public File inputSteamToFile(InputStream inputStream, String path, String fileName) {
        File destFile = new File(path + "/" + fileName);////不要使用双反斜杠"\\"【可能不识别】,最好使用斜杠"/"
        try {
            //先进行输入才能进行输出，代码书序不能变
            OutputStream out = new FileOutputStream(destFile);
            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.close();
            inputStream.close();
        } catch (Exception e) {
            Log.e("test", "流转换文件异常:\t" + e.getMessage());
        }
        return destFile;
    }

}
