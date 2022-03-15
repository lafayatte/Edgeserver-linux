package com.cug.springapi.utils;

import com.alibaba.fastjson.JSONObject;
import com.cug.utils.utils.Input;

/**
 * @author gg
 * @create 2020-11-18 19:08
 */
public class JsonObject {

    private String pubkey;
    private String sm2_sign;
    private String VID_Time;

    public static String jsonToString(JSONObject object, String key) throws Exception {

        String src = object.getString(key);
        return src;
        //返回Key值对应的value值
    }


}

