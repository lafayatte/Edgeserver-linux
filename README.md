# Authentication

##EdgeServer Module
边缘服务器模块

1.GenerateSM2Key类
：只在初始化时执行一次，产生服务器的公钥和私钥，用于对发送信息的加密和签名，步骤0。

2.GenerateSM4Key类
：只在初始化时执行一次，产生对称密钥，用于对视频图像的加密，步骤0。

3.HashCompute类
：服务器端计算哈希有两次，一次是初始计算自己公钥的hash，步骤1‘之前；
一次是核验小车身份时计算小车公钥的hash，步骤3之前。

4.UpChain类
：上传SID和公钥hash到区块链，步骤1’。

5.BroadcastReceive
：接收小车发送的广播信息，初步以点对点连接，后面使用广播，步骤2。

6.ChainCheck类
：到区块链核验服务器身份，步骤3、4。

7.SignVerify类
：签名验证，对小车发来的签名使用小车公钥和原文信息进行验证，确保小车身份
，步骤4之后。

8.KeyEncrypt类
：密钥加密，将服务器公钥和SM4Key加密，步骤5之前。

9.Signature类
：签名，使用服务器私钥对SID和Time签名，步骤5之前。

10.BroadcastResponse
：回复小车的广播，附带签名和加密信息返回，步骤5。

11.EncryptImageReceive
：接收小车发来的加密视频图像数据，并存储到边缘服务器上，步骤8。

12.ImageDecrypt类
：使用SM4Key对称密钥，解密视频图片数据的，步骤8，9之间。

13.DataTransmit类
：将解密后的视频图像数据上传到中心服务器上，步骤9。

14.TransmitResponse类
：接收中心服务器上传成功后的回复信息，步骤10。

15.TokenCreate类
：产生令牌，步骤11之前。

16.TokenTransmit
：转发令牌信息给小车，步骤11。
