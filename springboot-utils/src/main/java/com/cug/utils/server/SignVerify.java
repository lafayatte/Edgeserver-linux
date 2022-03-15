package com.cug.utils.server;

import cn.xjfme.encrypt.utils.Util;
import cn.xjfme.encrypt.utils.sm2.SM2SignVO;
import cn.xjfme.encrypt.utils.sm2.SM2SignVerUtils;
import com.cug.utils.utils.Input;

/**
 * @create 2020-10-27 21:41
 * 服务器端用小车的公钥验证和发生的签名是否匹配，通过后才会进行第5步
 * 步骤4、5之间
 */
public class SignVerify {

    public static boolean verifySM2Signature(String pubKey, String sourceData, String hardSign) {
        SM2SignVO verify = SM2SignVerUtils.VerifySignSM2(Util.hexStringToBytes(pubKey), Util.hexToByte(sourceData), Util.hexToByte(hardSign));
        return verify.isVerify();
    }

    public static String jsonToString(String path , String key) throws Exception {
        String fi = Input.getString(path);
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(fi);
        String src = jsonObject.getString(key);
        return src;
        //返回传入路径和Key值对应的value值
    }

    public static boolean main() throws Exception {

        //签名
        String src = jsonToString("/tmp/Java/EdgeServer/broadcast_receive.json","VID_Time");
        String srcHex = Util.byteToHex(src.getBytes());
        //读入签名的原内容（VID_Time）,将其转成Hex字符串

        String pubkey = jsonToString("/tmp/Java/EdgeServer/broadcast_receive.json","pubkey");
        //读入小车公钥
        String sm2_sign = jsonToString("/tmp/Java/EdgeServer/broadcast_receive.json","sm2_sign");
        //读入签名内容，只取其中sm2_sign部分

        //验签，用车的公钥验签；（公钥，原文，签名内容）
        boolean b = verifySM2Signature(pubkey.trim(), srcHex, sm2_sign);
        return b;
    }

}
