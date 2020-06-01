package com.githut.lgsxiaosen.service.impl;

import com.githut.lgsxiaosen.dto.CompressFileDto;
import com.githut.lgsxiaosen.service.DownloadFileService;
import com.githut.lgsxiaosen.utils.ZipUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * @author liuguisen
 * @date 2020/6/1
 **/
@Service
public class DownloadFileServiceImpl implements DownloadFileService {
    private static final Logger logger = LoggerFactory.getLogger(DownloadFileServiceImpl.class);

    /**
     * 下周文件压缩包
     * @return
     */
    @Override
    public void download(ZipOutputStream zos) {
        String path = "D:\\CODE\\personal-code\\zip-test";
        String file1 = "新建文本文档.txt";
        String file2 = "data\\test1.txt";
        String file3 = "data\\新建文件夹\\test2.txt";
        try (InputStream io1 = new FileInputStream(new File(path+File.separator+file1));
             InputStream io2 = new FileInputStream(new File(path+File.separator+file2));
             InputStream io3 = new FileInputStream(new File(path+File.separator+file3))){
            // 压缩文件
            List<CompressFileDto> list = new ArrayList<>();
            CompressFileDto compress = new CompressFileDto();
            compress.setFileName("新建文本文档.txt");
            compress.setIn(io1);
            compress.setPath("");
            CompressFileDto compress1 = new CompressFileDto();
            compress1.setFileName("test1.txt");
            compress1.setIn(io2);
            compress1.setPath("data1");
            CompressFileDto compress2 = new CompressFileDto();
            compress2.setFileName("test2.txt");
            compress2.setIn(io3);
            compress2.setPath("data1\\te");
            list.add(compress);
            list.add(compress1);
            list.add(compress2);
            ZipUtils.getZipInputStream(list, zos);
        }catch (Exception e){
            logger.info("下载文件异常：{}", e.toString());
        }

    }


}
