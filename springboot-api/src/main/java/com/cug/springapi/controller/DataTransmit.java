package com.cug.springapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.cug.utils.server.ImageDecrypt;
import com.cug.utils.server.TokenCreate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.Map;

/**
 * @author gg
 * @create 2020-11-18 9:13
 *
 * 步骤8-11
 * 服务器接收小车发送的加密数据，在服务器端进行解密(ImageDecrypt)
 * 转发解密后的视频到中心服务器(ImageTransmit)
 * 中心服务器返回消息(TransmitResponse)
 * 生成Token并返回给小车(TokenCreate,TokenTransmit)
 */
@RestController
@ResponseBody
@RequestMapping("server")

public class DataTransmit {

    static String videoPath = null;
    static String msg = null;

    @RequestMapping(value = "/datatransmit",method = RequestMethod.POST)
    public static Object upload(MultipartFile file) {
        if (file == null) {
            msg = "上传失败，未选择文件";
        }
        String fileName = file.getOriginalFilename();
        String filePath = "/tmp/Java/EdgeServer/EncryptData/";
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            //上传成功！
            msg = "上传到边缘服务器成功";
        } catch (IOException e) {
            //上传异常！
            msg = "上传到边缘服务器失败";
        }
        switch (msg){
            case "上传到边缘服务器成功":
                //2.ImageDecrypt,解密视频,videoPath是解密后的视频地址
                try {
                    videoPath = ImageDecrypt.main();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //3.ImageTransmit,传入解密视频路径和中心服务器url,发送至中心服务器
                String str = com.cug.utils.centerserver.DataTransmit.send(videoPath);
                //接收中心服务器的响应，是否成功
                boolean status = str.contains("成功");
                if (status){
                    //4.TokenCreate，生成Token
                    Object jsonToken = TokenCreate.main();
                    //5.object返回Token及消息
                    JSONObject object = new  JSONObject();
                    object.putAll((Map<? extends String, ? extends Object>) jsonToken);
                    object.put("serverID","2000");
                    object.put("code","200");
                    object.put("message",msg +"，上传到中心服务器成功！");
                    return object;
                }else{
                    //上传到中心服务器失败
                    JSONObject object = new  JSONObject();
                    object.put("serverID","2000");
                    object.put("code","434");
                    object.put("status",msg +"，上传到中心服务器失败！");
                    return object;
                }

            case "上传到边缘服务器失败":
                JSONObject object1 = new  JSONObject();
                object1.put("serverID","2000");
                object1.put("code","424");
                object1.put("message",msg);
                return object1;
            default:
                JSONObject object2 = new  JSONObject();
                object2.put("serverID","2000");
                object2.put("code","424");
                object2.put("message",msg);
                return object2;
        }
    }
}
