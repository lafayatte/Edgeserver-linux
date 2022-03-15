package com.cug.springapi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author gg
 * @create 2020-11-19 15:35
 */
public class Transmit {

    public static void start(String src,String iP) throws IOException {
        //创建Socket对象，指明服务器端的ip和端口号
        Socket socket = new Socket(InetAddress.getByName(iP),9090);
        OutputStream os = socket.getOutputStream();
        FileInputStream fis = new FileInputStream(new File(String.valueOf(src)));
        byte[] buffer = new byte[1024];
        int len;
        while((len = fis.read(buffer)) != -1){
            os.write(buffer,0,len);
        }
        fis.close();
        os.close();
        socket.close();
    }


}
