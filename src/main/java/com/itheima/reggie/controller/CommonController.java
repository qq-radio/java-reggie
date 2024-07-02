package com.itheima.reggie.controller;

import com.itheima.reggie.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RequestMapping("/common")
@RestController
public class CommonController {

  @Value("${reggie.path}")
  public String basePath;

  @PostMapping("/upload")
  public Result<String> upload(MultipartFile file) {
    /**
     * qq:
     * 当服务器接受到file时会暂存到计算机的C:\Users\24333\AppData\Local\Temp\tomcat.8080.xxxxxxxx\work\Tomcat\localhost\ROOT下面
     * 此时它是个.tmp格式的文件，如果把.tmp改为.jpg那这张图就可以打开了
     *
     * <p>qq: file是一个临时文件，因此这个时候必须转存到指定位置，否则本次请求完成后临时文件会删除
     */
    String name = file.getOriginalFilename();
    String ext = name.substring(name.lastIndexOf('.'));
    String newName = UUID.randomUUID().toString() + ext;

    File dir = new File(basePath);
    if (!dir.exists()) {
      dir.mkdir();
    }

    try {
      /** qq: file是一个临时文件，必须转存到指定位置，否则本次请求完成后临时文件会删除 */
      file.transferTo(new File(basePath + newName));
    } catch (IOException e) {

    }
    return Result.success(newName);
  }

  @GetMapping("/download")
  public void download(String name, HttpServletResponse response) {
    try {
      /** 输入流，通过输入流读取文件内容 */
      FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

      /** 输出流，通过输出流将文件写回浏览器 */
      ServletOutputStream outputStream = response.getOutputStream();

      response.setContentType("image/jepg");

      int len = 0;
      byte[] bytes = new byte[1024];
      while ((len = fileInputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, len);
        outputStream.flush();
      }

      outputStream.close();
      fileInputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
