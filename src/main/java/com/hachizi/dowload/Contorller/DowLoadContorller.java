package com.hachizi.dowload.Contorller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by HHYang on 2018/1/28.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("hachuizi")
public class DowLoadContorller {

    @PostMapping(value = "dowload", consumes = "application/json;charset=UTF-8")
    public String dowload(HttpServletResponse response, @RequestBody String strJson) throws InterruptedException, IOException {
        JSONObject pathJson = JSONObject.parseObject(strJson);
        String path = String.valueOf(pathJson.get("path"));
        String pass = String.valueOf(pathJson.get("pass"));
        String url = String.valueOf(pathJson.get("url"));
        if(!"http://dowload.hachuizi.com:1024/".equals(url)){
            return "网络异常";
        }
        if("".equals(path)){
            System.out.print("请求地址为空");
            return "请求地址为空";
        }
        if(!"fanqiangmima2333".equals(pass)){
            System.out.print("密码错误");
            return "密码不正确";
        }
        downloadNet(response,path);
        System.out.print("Dowload Success!!!");
        execsh("bash /root/qq/qq.sh");
        System.out.print("Qrsync Success!!!");
        execsh("bash /root/dd/dd.sh");
        System.out.print("Clean Success!!!");
        String[] Paths = path.split("/");
        String name = Paths[Paths.length-1];
        return "http://upload.hachuizi.com/"+name;
    }

    public boolean downloadNet(HttpServletResponse response, String Path) throws MalformedURLException {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        String[] Paths = Path.split("/");
        String name = Paths[Paths.length-1];
        URL url = new URL(Path);

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream("/root/dd/"+name);

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void execsh(String path) throws InterruptedException, IOException {
        String shpath= path;   //程序路径
        Process process =null;
        process = Runtime.getRuntime().exec(shpath);
        process.waitFor();
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }
        input.close();
    }
}
