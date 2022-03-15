package com.cug.utils.utils;

import java.io.*;

/**
 * @create 2020-10-28 19:42
 */
 public class IOUtil {
/*    public static void main(String[] args) {
        //指定文件源，获得该文件的字节数组
        byte[] datas = fileToByteArray("D:\\Test\\001.jpg");//图片转为字节数组
        byteArrayToFile(datas,"D:\\Test\\out.jpg");//字节数组转为图片
    }*/

    public static byte[] fileToByteArray(String filePath) {
        //创建源与目的地
        File src = new File(filePath);//获得文件的源头，从哪开始传入(源)
        byte[] dest = null;//最后在内存中形成的字节数组(目的地)
        //选择流
        InputStream is = null;//此流是文件到程序的输入流
        ByteArrayOutputStream baos = null;//此流是程序到新文件的输出流
        //操作(输入操作)
        try {
            is = new FileInputStream(src);//文件输入流
            baos = new ByteArrayOutputStream();//字节输出流，不需要指定文件，内存中存在
            byte[] flush = new byte[1024 * 10];//设置缓冲，这样便于传输，大大提高传输效率
            int len = -1;//设置每次传输的个数,若没有缓冲的数据大，则返回剩下的数据，没有数据返回-1
            while ((len = is.read(flush)) != -1) {
                baos.write(flush, 0, len);//每次读取len长度数据后，将其写出
            }
            baos.flush();//刷新管道数据
            dest = baos.toByteArray();//最终获得的字节数组
            return dest;//返回baos在内存中所形成的字节数组
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //释放资源,文件需要关闭,字节数组流无需关闭
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }
    public static void byteArrayToFile(byte[] src, String filePath) {
        //创建源
        File dest = new File(filePath);//目的地，新文件
        //src字节数组已经存在
        //选择流
        InputStream is = null;//ByteArrayInputStream的父类
        OutputStream os = null;
        //操作
        try {
            is = new ByteArrayInputStream(src);//字节数组与程序之间的管道
            os = new FileOutputStream(dest);//程序与新文件之间的管道
            //一样的字节数组缓冲操作
            byte[] flush = new byte[1024 * 10];
            int len = -1;
            while ((len = is.read(flush)) != -1) {
                os.write(flush, 0, len);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != os) {//关闭文件流
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
