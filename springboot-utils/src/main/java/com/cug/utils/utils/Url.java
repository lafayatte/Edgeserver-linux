package com.cug.utils.utils;

import com.alibaba.fastjson.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author gg
 * @create 2020-12-11 20:32
 */
public class Url {
    public static String payLoad ;

    public static String send(String url, JSONObject objInfo) {
        try{
            //创建连接
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            connection.connect();

            //POST请求
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(objInfo.toString().getBytes("UTF-8"));
            out.flush();
            out.close();

            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer();
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "UTF-8");
                sb.append(lines);
            }

            String result = sb.toString();
            System.out.println("区块链返回信息："+result);
            JsonParser jp = new JsonParser();
            JsonObject jo = jp.parse(result).getAsJsonObject();
            payLoad = jo.get("PayLoad").getAsString();

            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payLoad;
    }

}
