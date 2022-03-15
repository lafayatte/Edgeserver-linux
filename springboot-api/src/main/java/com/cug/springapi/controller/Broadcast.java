package com.cug.springapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.cug.springapi.utils.JsonObject;
import com.cug.utils.chain.ChainCheck;
import com.cug.utils.server.KeyEncrypt;
import com.cug.utils.server.SignVerify;
import com.cug.utils.server.Signature;
import com.cug.utils.utils.Output;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * @author gg
 * @create 2020-11-17 21:20
 *
 *包含步骤2-5
 *广播接收内容（签名S，原文，小车公钥）
 *经过提取小车VID，计算hash(PKv)，到区块链核验(VID：hash(PKv))，用PKv和原文验证签名S；
 *之间有一个验证不通过，随即返回拒绝给车
 *验证通过后，服务器进行加密、签名，给小车返回（加密的服务器公钥和SM4密钥，签名，原文）
 */

@RestController
@RequestMapping("server")
public class Broadcast {

    @PostMapping("/broadcast")
    public static Object Receive(@RequestBody JSONObject jsonObject ) throws Exception {

        String pubkey = null;
        try {
            pubkey = JsonObject.jsonToString(jsonObject,"pubkey");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sm2_sign = null;
        try {
            sm2_sign = JsonObject.jsonToString(jsonObject,"sm2_sign");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String VID_Time = null;
        try {
            VID_Time = JsonObject.jsonToString(jsonObject,"VID_Time");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //接收车发来的三个信息，签名，原文VID_Time,公钥
        JSONObject getObject = new  JSONObject();
        getObject.put("pubkey",pubkey);
        getObject.put("sm2_sign",sm2_sign);
        getObject.put("VID_Time",VID_Time);

        //1.BroadcastReceive，接收并存储
        Output.wirteText(String.valueOf(getObject),"/tmp/Java/EdgeServer/broadcast_receive.json");

        //2.ChainCheck,提出VID，计算hash(PKv),到区块链核验(VID：hash(PKv))
        int a = ChainCheck.send("/tmp/Java/EdgeServer/broadcast_receive.json");

        //3.SignVerify,用PKv和原文验证签名S
        boolean b = false;
        try {
            b = SignVerify.main();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (a == 1 && b){
            //表示核验通过和签名验证通过
            System.out.println("身份核验和签名验证通过");
            //4.KeyEncrypt，使用接收到的小车SM2公钥，对服务器公钥和SM4Key密钥加密
            Object keyEncrypt = null;
            try {
                keyEncrypt = KeyEncrypt.main();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //5.Signature，用服务器私钥，对SID和时间Time进行签名，返回结果为签名内容和原文
            Object signature = null;
            try {
                signature = Signature.main();
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONObject object = new  JSONObject();
            object.put("serverID","2000");
            object.put("code","200");
            object.put("Message","验证通过");
            object.putAll((Map<? extends String, ? extends Object>) keyEncrypt);
            object.putAll((Map<? extends String, ? extends Object>) signature);
            return object;
        }
        else {
            JSONObject object = new JSONObject();
            object.put("serverID","2000");
            object.put("code", "200");
            object.put("Message", "验证失败");
            return object;
        }

    }

}
