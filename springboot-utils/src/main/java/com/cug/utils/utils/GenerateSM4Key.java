package com.cug.utils.utils;

import cn.hutool.json.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import javax.crypto.KeyGenerator;
import java.security.SecureRandom;
import java.security.Security;
import static cn.hutool.crypto.symmetric.SM4.ALGORITHM_NAME;

/**
 * @create 2020-11-02 15:24
 * 生成SM4密钥，只在初始化时执行一次，SM4Key会以加密状态发送给车辆端，用于加密视频和图片
 * 预备步骤
 */
public class GenerateSM4Key {
    public static final int KEY_SIZE = 128;

    public static void main(String keypath) throws Exception {

        //生成Key并输出到指定位置存储
        byte[] bytes = generateKey(KEY_SIZE);
        JSONObject jsonkey =new  JSONObject();
        jsonkey.accumulate("sm4key",ByteUtils.toHexString(bytes));
        Output.wirteText(String.valueOf(jsonkey),keypath);
        //输出到服务器存储，之后还要用于对视频图片的解密

    }
    public static byte[] generateKey(int keySize) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }
    static{
        try{
            Security.addProvider(new BouncyCastleProvider());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
