package com.githut.lgsxiaosen.controller;

import com.githut.lgsxiaosen.service.DownloadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipOutputStream;

/**
 * @author liuguisen
 * @date 2020/6/1
 **/
@RestController
public class DownloadController {

    @Autowired
    private DownloadFileService downloadFileService;

    @GetMapping("/download")
    public void downloadFile(HttpServletResponse response) throws Exception{
            response.reset();
            response.setContentType("bin");
            DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String time = LocalDateTime.now().format(ftf);
            String fileName = "downloadFile-"+ time + ".zip";
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            // 循环取出流中的数据
            try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())){
            downloadFileService.download(zos);
        }
    }
}
