package com.cug.utils.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author gg
 * @create 2020-11-02 16:18
 */
public class Output {
    public static void wirteText(String dataStr,String filePath) {
        FileWriter fw = null;
        PrintWriter pw = null;
        //如果文件存在，则追加内容；如果文件不存在，则创建文件
        File f=new File(filePath);
        try {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);
            pw.println(dataStr);
        } catch (IOException e) {
            System.out.println("【在写入TXT文件出现问题】");
        } finally {
            try {
                pw.flush();
                fw.flush();
                pw.close();
                fw.close();
            } catch (IOException e) {
                System.out.println("【在进行刷新/关闭文件的时候出现异常】");
            }
        }

    }
}
