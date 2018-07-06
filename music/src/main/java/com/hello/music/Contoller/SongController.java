package com.hello.music.Contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller

public class SongController {

    @GetMapping("/index")
    public String getForm(Model model) {
        model.addAttribute("songInfo", new Song_Info());
        return "index";
    }

    @PostMapping("/index")
    public String submitForm(@ModelAttribute Song_Info songInfo, Map <String, Object> map) throws Exception {
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

}
