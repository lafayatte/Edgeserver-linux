package com.cug.utils.chain;

import com.alibaba.fastjson.JSONObject;
import com.cug.utils.server.HashCompute;
import com.cug.utils.utils.Input;
import com.cug.utils.utils.Url;

/**
 * @create 2020-10-27 21:41
 * 服务器验证小车公钥和VID是否已在区块链上
 * 步骤3
 * 根据返回内容，若为1，继续下一步，若为0，直接返回拒绝服务
 */
public class ChainCheck {

    private static final String ADD_URL = "http://mgds.mingbyte.com/carbaas/verifyVehicleKey";
    private static final int timeDif = 9999;
    //时间戳差的判断，可以根据网络状况调整

    public static int send(String keypath) throws Exception {
        //边缘服务器端验证的是车的VID和公钥是否匹配
        String jsonVID = Input.getString(keypath);
        JSONObject jsonObject = JSONObject.parseObject(jsonVID);
        String VID_Time = jsonObject.getString("VID_Time");
        //取前4位为VID
        String vid = VID_Time.substring(0,4);
        String timeStamp = VID_Time.substring(4);
        long time = Long.parseLong(timeStamp);
        long timeNow=System.currentTimeMillis();

        if (timeNow - time > timeDif){
            //时间戳超时！
            return 0;
        }else{
            JSONObject obj = new JSONObject();
            obj.put("vehicleId", vid);
            // VID，后面再调用VID返回函数
            obj.put("pubKeyHash", HashCompute.hashCompute(keypath,"pubkey"));
            String payLoad = Url.send(ADD_URL,obj);
            int a = Integer.parseInt(payLoad);
            if(a==0){
                System.out.println("区块链核验不通过!!!");
            }
            return a;
        }
    }

}
