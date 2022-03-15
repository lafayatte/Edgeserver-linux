package com.cug.utils.centerserver;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**

 * 边缘服务器将视频图片数据传送到中心服务器,角色为Client
 * 步骤9
 */
public class DataTransmit  {

    private static final String URL = "http://phfz.mingbyte.com/html/upload.php";
    private static String result;
    private static String BOUNDARY = "20201228";
    private static String PREFIX = "--", LINE_END = "\r\n";
    private static String CONTENT_TYPE = "multipart/form-data";
    // 内容类型

    public static String send(String path) {

        try {
            java.net.URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            File file = new File(path);
            if (file != null) {
                //当文件不为空，把文件包装并且上传
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /*
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */

                sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        +  "UTF-8" + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();

                dos.write(end_data);
                dos.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String lines;
                StringBuffer sb1 = new StringBuffer("");
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sb1.append(lines);
                }
                result = sb1.toString();
                System.out.println(result);

            }
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        send("D:\\TestData\\Vehicle\\ImageData\\car.png");
    }

}
