package com.hello.music.Contoller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MusicDownload {

    // data field
    private String name;
    private String platform;

    // construct function
    public MusicDownload(String name, String platform){
        this.name = name;
        this.platform = platform;

    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    // HTTP POST request
    public String doPost(){

        String url = "http://www.qmdai.cn/yinyuesou/";
        try {
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Host", "http://www.qmdai.cn");
            final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36";
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Origin", "http://www.qmdai.cn");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            //conn.setRequestProperty("Referer", "https://accounts.google.com/ServiceLoginAuth");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            String postParams = String.format("input=%s&filter=name&type=%s&page=1", name, platform);
            System.out.println(postParams);
            conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

            // For POST only - START
            conn.setDoOutput(true);
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
            os.write(postParams);
            os.flush();
            os.close();
            // For POST only - END

            int responseCode = conn.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                return response.toString();
            } else {
                return "There is somthing wrong!";
            }
        }
        catch(Exception e){
            return "There is somthing wrong!";
        }
    }


}
