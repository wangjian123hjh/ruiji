package com.example.springboot.controller;

import com.example.springboot.common.R;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;
    @RequestMapping("/upload")
    public R<String> upload(MultipartFile file){
        //使用uuid重新生成文件名
        String uuid = UUID.randomUUID().toString();
        String substring = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String suffix=uuid+substring;
        try {
            file.transferTo(new File(basePath+suffix));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(suffix);
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len=0;
            byte[] bytes= new byte[1024];
            while ((len=fileInputStream.read(bytes))!=-1)
            {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
