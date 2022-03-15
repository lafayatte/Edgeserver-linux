package com.cug.utils.server;

import cn.xjfme.encrypt.utils.Util;
import cn.xjfme.encrypt.utils.sm2.SM2EncDecUtils;
import com.alibaba.fastjson.JSONObject;
import com.cug.utils.utils.Input;
import java.io.IOException;

/**
 * @create 2020-11-05 16:15
 *
 * 使用接收到的小车SM2公钥，对服务器公钥和SM4Key密钥加密，分别读入然后串接，到小车端再截取
 * 步骤5的前提
 *
 */
public class KeyEncrypt {
    public static Object main() throws Exception {

        //读入服务器SM2公钥
        String jsonkey = Input.getString("/tmp/Java/EdgeServer/pubkey.json");
        JSONObject jsonObject = JSONObject.parseObject(jsonkey);
        String src = jsonObject.getString("pubkey");

        //读入SM4密钥，和公钥串接
        String jsonkey1 = Input.getString("/tmp/Java/EdgeServer/sm4key.json");
        JSONObject jsonObject1 = JSONObject.parseObject(jsonkey1);
        String src1 = jsonObject1.getString("sm4key");
        String src12 = src + src1;

        //读入接收到的小车SM2公钥，用于加密
        String jsonkey2 = Input.getString("/tmp/Java/EdgeServer/broadcast_receive.json");
        JSONObject jsonObject2 = JSONObject.parseObject(jsonkey2);
        String pubkey_vehicle = jsonObject2.getString("pubkey");

        String SM2Enc = SM2Enc(pubkey_vehicle, src12);
        //(公钥，源文件)

        JSONObject keyEncrypt =new JSONObject();
        keyEncrypt.put("encrypt",SM2Enc);

        return keyEncrypt;
        //生成的加密文件，会被发送给车

    }

    //用SM2公钥加密
    public static String SM2Enc(String pubKey, String src) throws IOException {
        String encrypt = SM2EncDecUtils.encrypt(Util.hexStringToBytes(pubKey), src.getBytes());
        encrypt=encrypt.substring(2,encrypt.length());
        return encrypt;
    }


}
