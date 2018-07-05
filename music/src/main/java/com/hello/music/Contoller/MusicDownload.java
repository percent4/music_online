package com.hello.music.Contoller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
    public String sendPost(){

        String url = "http://www.qmdai.cn/yinyuesou/";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // HTTP headers
        final String USER_AGENT = "Mozilla/5.0 (X11; Linux i686; U;) Gecko/20070322 Kazehakase/0.4.5";
        //final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36";
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        post.setHeader("Origin", "http://www.qmdai.cn");
        post.setHeader("X-Requested-With", "XMLHttpRequest");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        post.setHeader("Accept-Encoding", "gzip, deflate");
        post.setHeader("Accept-Language", "zh-CN,zh;q=0.9");

        // HTTP post data
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("input", name));
        urlParameters.add(new BasicNameValuePair("filter", "name"));
        urlParameters.add(new BasicNameValuePair("type", platform));
        urlParameters.add(new BasicNameValuePair("page", "1"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters, Charset.forName("UTF-8")));

        try {
            HttpResponse response = client.execute(post);

            // HTTP response
            // get HTTP response content in String format
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), Charset.forName("UTF-8")));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        }
        catch (ConnectException e){
            return "连接超时，请重试！";
        }
        catch (ClientProtocolException e) {
            return e.toString();
        }
        catch (IOException e) {
            return e.toString();
        }

    }


}
