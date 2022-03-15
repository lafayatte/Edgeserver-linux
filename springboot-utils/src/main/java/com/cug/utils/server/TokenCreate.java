package com.cug.utils.server;

import com.alibaba.fastjson.JSONObject;
import com.cug.utils.utils.Output;

/**
 * @create 2020-10-27 22:14
 *
 * Token ,内容暂时没确定
 */
public class TokenCreate {

    public static Object main() {
        JSONObject jsonToken =new  JSONObject();
        jsonToken.put("Token", "TokenExample");
        //Output.wirteText(String.valueOf(jsonToken),"/tmp/Java/TestData/EdgeServer/Token.json");
        return jsonToken;
    }

}
