package com.hello.music.Contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

@Controller

public class SongController {

    @GetMapping("/index")
    public String getForm(Model model, Map <String, Object> map){

        model.addAttribute("songInfo", new Song_Info());
        // 获取访问量信息
        String txtFilePath = "/home/vcap/app/BOOT-INF/classes/static/others/count.txt";
        Long count = Get_Visit_Count(txtFilePath);
        System.out.println(count);
        map.put("count", count);

        return "index";
    }

    @PostMapping("/index")
    public String submitForm(@ModelAttribute Song_Info songInfo, Map <String, Object> map) throws Exception {

        // 获取歌曲信息
        String song_name = songInfo.getSong();
        String platform = songInfo.getPlatform();
        System.out.println(song_name+':'+platform);
        MusicDownload music_download = new MusicDownload(song_name, platform);
        byte[] post_data = music_download.doPost().getBytes("utf-8");
        String info = new String(post_data,"utf-8");
        System.out.println(info);
        map.put("music", info);

        return "play";
    }

    @GetMapping("/dev")
    public String devEnviroment(){
        return "dev";
    }

    //文件下载相关代码
    @RequestMapping("/download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //String fileName = "aim_test.txt";  //下载的文件名
        File scFileDir = new File("/home/vcap/app/BOOT-INF/classes/static/music_store");
        File TrxFiles[] = scFileDir.listFiles();
        System.out.println(TrxFiles[0]);
        String fileName = TrxFiles[0].getName(); //下载的文件名
        if (fileName != null) {
            //设置文件路径
            String realPath = "/home/vcap/app/BOOT-INF/classes/static/music_store/";
            File file = new File(realPath, fileName);
            if (file.exists()) {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("Download the song successfully!");
                } catch (Exception e) {
                    System.out.println("Download the song failed!");
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Long Get_Visit_Count(String txtFilePath) {

        try {
            //读取文件(字符流)
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(txtFilePath),"UTF-8"));
            //循环读取数据
            String str = null;
            StringBuffer content = new StringBuffer();
            while ((str = in.readLine()) != null) {
                content.append(str);
            }
            //关闭流
            in.close();

            //System.out.println(content);
            // 解析获取的数据
            Long count = Long.valueOf(content.toString());
            count ++;
            //写入相应的文件
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(txtFilePath),"UTF-8"));
            out.write(String.valueOf(count));

            //清楚缓存
            out.flush();
            //关闭流
            out.close();

            return count;
        }
        catch (Exception e){
            e.printStackTrace();
            return 0L;
        }


    }
}
