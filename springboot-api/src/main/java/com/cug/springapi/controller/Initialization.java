package com.cug.springapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.cug.utils.server.HashCompute;
import com.cug.utils.utils.GenerateSM2Key;
import com.cug.utils.utils.GenerateSM4Key;
import com.cug.utils.utils.Input;
import com.cug.utils.utils.Url;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author gg
 * @create 2020-11-19 9:15
 *
 * 初始化主要是两部分，一个是生成服务器自己的密钥，一个是上传SID和公钥hash，这是在部署时进行的
 *
 */
@Component
@Order(0)
public class Initialization implements CommandLineRunner {
    public static final String ADD_URL = "http://mgds.mingbyte.com/carbaas/uploadServerKey";

    public static void init() throws Exception {
        //1.GenerateSM2Key，产生SM2密钥，分为公钥和私钥
        GenerateSM2Key.main("/tmp/Java/EdgeServer/pubkey.json",
                "/tmp/Java/EdgeServer/prikey.json");
        //2.GenerateSM4Key，产生SM4密钥
        GenerateSM4Key.main("/tmp/Java/EdgeServer/sm4key.json");
        System.out.println("已生成SM2密钥对和SM4密钥！");
        JSONObject obj = new JSONObject();
        //读入并添加SID
        String jsonSID = Input.getString("/tmp/Java/EdgeServer/SID.json");
        JSONObject jsonObject1 = JSONObject.parseObject(jsonSID);
        String SID = jsonObject1.getString("SID");
        obj.put("serverId",SID);
        //计算并上传服务器公钥哈希
        obj.put("pubKeyHash", HashCompute.hashCompute("/tmp/Java/EdgeServer/pubkey.json","pubkey"));
        Url.send(ADD_URL,obj);

    }

    @Override
    public void run(String... args) throws Exception {
        init();
        System.out.println("服务器部署完成！");
    }
}
