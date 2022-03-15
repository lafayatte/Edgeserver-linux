package com.cug.utils.chain;

import com.alibaba.fastjson.JSONObject;
import com.cug.utils.server.HashCompute;
import com.cug.utils.utils.Input;
import com.cug.utils.utils.Url;

/**
 * @create 2020-10-27 21:40
 * 步骤1‘，上传服务器公钥hash和SID到区块链
 * 只在Initialization初始化时执行一次
 */
public class UpChain {
    private static final String ADD_URL = "http://mgds.mingbyte.com/carbaas/uploadServerKey";

    public static void main(String[] args) throws Exception {
        JSONObject obj = new JSONObject();
        //读入并添加SID
        String jsonSID = Input.getString("/tmp/Java/EdgeServer/SID.json");
        JSONObject jsonObject = JSONObject.parseObject(jsonSID);
        String SID = jsonObject.getString("SID");
        obj.put("serverId",SID);
        //计算并上传服务器公钥哈希
        obj.put("pubKeyHash", HashCompute.hashCompute("/tmp/Java/EdgeServer/pubkey.json","pubkey"));
        Url.send(ADD_URL,obj);
    }
}
