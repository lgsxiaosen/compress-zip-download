package com.githut.lgsxiaosen.service.impl;

import com.githut.lgsxiaosen.dto.CompressFileDto;
import com.githut.lgsxiaosen.service.DownloadFileService;
import com.githut.lgsxiaosen.utils.ZipUtils;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
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
            compress.setFile(true);
            CompressFileDto compress1 = new CompressFileDto();
            compress1.setFileName("data1\\test1.txt");
            compress1.setIn(io2);
            compress1.setFile(true);
            CompressFileDto compress2 = new CompressFileDto();
            compress2.setFileName("data1\\te\\test2.txt");
            compress2.setIn(io3);
            compress2.setFile(true);
            CompressFileDto compress3 = new CompressFileDto();
            compress3.setFileName("data1\\tet");
            CompressFileDto compress4 = new CompressFileDto();
            compress4.setFileName("data1\\test.xml");
            compress4.setIn(getXmlStream());
            compress4.setFile(true);
            list.add(compress);
            list.add(compress1);
            list.add(compress2);
            list.add(compress3);
            list.add(compress4);
            ZipUtils.getZipInputStream(list, zos);
        }catch (Exception e){
            logger.info("下载文件异常：{}", e.toString());
        }
    }

    private InputStream getXmlStream() throws Exception{
        String path = "D:\\CODE\\personal-code\\zip-test\\modulFile.xml";
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new File(path));
        } catch (Exception e) {
            logger.error("加载xml异常：error={}", e.toString());
        }
        // 自定义xml样式
        OutputFormat format = new OutputFormat();
        // 行缩进
        format.setIndentSize(2);
        // 一个结点为一行
        format.setNewlines(true);
        // 去重空格
        format.setTrimText(true);
        format.setPadText(true);
        format.setNewLineAfterDeclaration(false);
        XMLWriter writer = null;
        try (ByteArrayOutputStream bao = new ByteArrayOutputStream()){
            writer = new XMLWriter(bao, format);
            writer.write(document);
            ByteArrayInputStream bai = new ByteArrayInputStream(bao.toByteArray());
            return bai;
        }finally {
            writer.close();
        }
    }


}
