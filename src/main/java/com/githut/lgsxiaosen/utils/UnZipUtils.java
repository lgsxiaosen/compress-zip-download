package com.githut.lgsxiaosen.utils;

import com.githut.lgsxiaosen.dto.CompressFileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @author liuguisen
 * @date 2020/6/3
 **/
public class UnZipUtils {
    private static final Logger logger = LoggerFactory.getLogger(UnZipUtils.class);

    public static List<CompressFileDto> unZip(MultipartFile file, String path){
        List<CompressFileDto> list = new ArrayList<>();
        // 解压
        try {
            File sourceFile = new File(path + File.separator + file.getOriginalFilename());
            file.transferTo(sourceFile.getAbsoluteFile());
            try(ZipFile zipFile = new ZipFile(sourceFile)){
                Enumeration<?> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    if (entry.isDirectory()){
                        continue;
                    }
                    int n;
                    byte[] bytes = new byte[1024];
                    CompressFileDto compress = new CompressFileDto();
                    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
                        try (InputStream is = zipFile.getInputStream(entry)){
                            while ((n = is.read(bytes)) != -1) {
                                baos.write(bytes, 0, n);
                            }
                        }
                        InputStream in = new ByteArrayInputStream(baos.toByteArray());
                        compress.setIn(in);
                    }
                    String name = entry.getName();
                    String fileName = "";
                    if (Objects.nonNull(name)){
                        int i = name.lastIndexOf("/");
                        fileName = name.substring(i).split("/")[1];
                    }
                    compress.setFile(true);
                    compress.setFileName(fileName);
                    list.add(compress);
                }
            }
        }catch (Exception e){
            logger.error("解压异常：{}", e.toString());
        }
        return list;
    }

    public static List<CompressFileDto> unZip2(MultipartFile file){
        List<CompressFileDto> list = new ArrayList<>();
        // 解压
        try (ZipInputStream zis = new ZipInputStream(file.getInputStream(), Charset.forName("GBK"))){
            ZipEntry ze = null;
            //循环遍历
            while ((ze = zis.getNextEntry()) != null) {
                if (ze.isDirectory()){
                    continue;
                }
                logger.info("文件名：" + ze.getName() + " 文件大小：" + ze.getSize() + " bytes");
                //读取
                int n;
                byte[] bytes = new byte[1024];
                CompressFileDto compress = new CompressFileDto();
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
                    while ((n = zis.read(bytes)) != -1) {
                        baos.write(bytes, 0, n);
                    }
                    InputStream in = new ByteArrayInputStream(baos.toByteArray());
                    compress.setIn(in);
                }
                String name = ze.getName();
                String fileName = name;
                if (Objects.nonNull(name)&&name.contains("/")){
                    int i = name.lastIndexOf("/");
                    fileName = name.substring(i).split("/")[1];
                }
                compress.setFile(true);
                compress.setFileName(fileName);
                list.add(compress);
                }
        }catch (Exception e){
            logger.error("解压异常：{}", e.toString());
        }
        return list;
    }

}
