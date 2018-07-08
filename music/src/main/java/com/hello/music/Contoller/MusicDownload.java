package com.hello.music.Contoller;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.*;
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

                // parse information of the song using json and downthe the song into fixed directory

                JSONObject obj2 = new JSONObject(response.toString());
                //System.out.println(obj.getJSONArray("data").get(0));
                JSONObject song = new JSONObject(obj2.getJSONArray("data").get(0).toString());
                String title = song.getString("title");
                String song_url = song.getString("url");
                System.out.println("title -> "+title);
                System.out.println("url -> "+ song_url);

                // clear all files in the directory
                clearFiles("/home/vcap/app/BOOT-INF/classes/static/music_store");
                // 利用FileUtils.copyURLToFile()实现文件下载
                URL httpurl = new URL(song_url);
                FileUtils.copyURLToFile(httpurl, new File("/home/vcap/app/BOOT-INF/classes/static/music_store/"+title+".mp3"));

                return response.toString();

            } else {
                return "There is somthing wrong!";
            }
        }
        catch(Exception e){
            return "There is somthing wrong!";
        }
    }

    // 删除指定目录下的所有文件
    public static void clearFiles(String filePath){
        File scFileDir = new File(filePath);
        File TrxFiles[] = scFileDir.listFiles();
        for(File curFile:TrxFiles ){
            curFile.delete();
        }
    }


}
