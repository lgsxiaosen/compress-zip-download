package com.githut.lgsxiaosen.utils;

import com.githut.lgsxiaosen.dto.CompressFileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * @author liuguisen
 * @date 2020/6/3
 **/
public class UnZipUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(UnZipUtilsTest.class);

    public static void main(String[] args){
        String zipPath = "D:\\CODE\\personal-code\\zip-test.zip";
        String unZipPath = "D:\\测试\\解压";
        try (InputStream in = new FileInputStream(zipPath)){
            MultipartFile file = new MockMultipartFile("file", "zip-test.zip", "", in);
            List<CompressFileDto> list = UnZipUtils.unZip(file, unZipPath);
            for (CompressFileDto commpress: list){
                File file1 = new File(unZipPath + File.separator + commpress.getFileName());
//                if (!file1.exists()){
//                    file1.mkdirs();
//                }
                try (InputStream comIn = commpress.getIn()){
                    try (OutputStream os = new FileOutputStream(file1)){
                        int temp = 0 ;
                        while((temp = comIn.read())!=-1){
                            os.write(temp) ;
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("解压异常：{}", e.toString());
        }

    }

}
