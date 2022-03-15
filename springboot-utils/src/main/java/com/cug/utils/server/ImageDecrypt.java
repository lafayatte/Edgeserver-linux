package com.cug.utils.server;

import cn.hutool.core.io.IoUtil;
import com.cug.utils.utils.Input;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.management.openmbean.InvalidKeyException;
import java.io.*;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

/**
 * @create 2020-10-27 21:42
 * 视频图片数据的解密功能，使用SM4对称密钥解密
 * 步骤8、9之间
 */
public class ImageDecrypt {

    public static String main() throws Exception {

        String dp = "/tmp/Java/EdgeServer/EncryptData/EncryptFile";
        //加密后文件
        String dp2 = "/tmp/Java/EdgeServer/DecryptData/DecryptVideo.mp4";
        //解密后文件，指定文件格式，如.mp4可直接显示播放

        String key = jsonToString("/tmp/Java/EdgeServer/sm4key.json","sm4key");
        byte[] keyData = ByteUtils.fromHexString(key);
        //解密文件并输出存储
        decryptFile(keyData, dp, dp2);
        return dp2;
    }

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    //生成 Cipher
    public static Cipher generateCipher(int mode, byte[] keyData) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, java.security.InvalidKeyException {
        Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS5Padding", BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(keyData, "SM4");
        cipher.init(mode, sm4Key);
        return cipher;
    }

    /**
     * 解密文件
     *
     * @param sourcePath 待解密的文件路径
     * @param targetPath 解密后的文件路径
     */
    public static void decryptFile(byte[] keyData, String sourcePath, String targetPath) {
        FileInputStream in = null;
        ByteArrayInputStream byteArrayInputStream = null;
        OutputStream out = null;
        CipherOutputStream cipherOutputStream = null;
        try {
            in = new FileInputStream(sourcePath);
            byte[] bytes = IoUtil.readBytes(in);
            byteArrayInputStream = IoUtil.toStream(bytes);
            Cipher cipher = generateCipher(Cipher.DECRYPT_MODE, keyData);
            out = new FileOutputStream(targetPath);
            cipherOutputStream = new CipherOutputStream(out, cipher);
            IoUtil.copy(byteArrayInputStream, cipherOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (java.security.InvalidKeyException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(cipherOutputStream);
            IoUtil.close(out);
            IoUtil.close(byteArrayInputStream);
            IoUtil.close(in);
        }
    }

    public static String jsonToString(String path , String key) throws Exception {
        String fi = Input.getString(path);
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(fi);
        String src = jsonObject.getString(key);
        return src;
        //返回传入路径和Key值对应的value值
    }
}

