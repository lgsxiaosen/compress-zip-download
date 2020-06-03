package com.githut.lgsxiaosen.utils;

import com.githut.lgsxiaosen.dto.CompressFileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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
        try (ZipInputStream zis = new ZipInputStream(file.getInputStream())){
            File sourceFile = new File(path + File.separator + file.getOriginalFilename());
            file.transferTo(sourceFile.getAbsoluteFile());
            try(ZipFile zipFile = new ZipFile(sourceFile)){
                Enumeration<?> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    if (!(entry.isDirectory())){
                        InputStream is = new ZipInputStream(zipFile.getInputStream(entry));
                        String name = entry.getName();
                        String fileName = "";
                        if (Objects.nonNull(name)){
                            int i = name.lastIndexOf("/");
                            fileName = name.substring(i).split("/")[1];
                        }
                        CompressFileDto compress = new CompressFileDto();
                        compress.setFile(true);
                        compress.setFileName(fileName);
                        compress.setIn(is);
                        list.add(compress);
                    }
                }
            }
        }catch (Exception e){
            logger.error("解压异常：{}", e.toString());
        }
        return list;
    }

}
