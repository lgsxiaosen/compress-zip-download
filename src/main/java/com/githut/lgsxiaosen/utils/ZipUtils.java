package com.githut.lgsxiaosen.utils;

import com.githut.lgsxiaosen.dto.CompressFileDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author liuguisen
 * @date 2020/6/1
 **/
public class ZipUtils {
    private static final Logger logger = LoggerFactory.getLogger(ZipUtils.class);

    public static String zipFiles(String zipFileName, String sourceZipDir){
        // 要生成的压缩包名字
        String zipFilePath = sourceZipDir + File.separator + zipFileName + ".zip";
        File file = new File(zipFilePath);
        try(OutputStream outputStream = new FileOutputStream(file)) {
            toZip(sourceZipDir, outputStream, true, zipFileName);
            return zipFilePath;
        } catch (Exception e) {
            logger.error("zipFiles error: {}", e.toString());
        }
        return "";
    }

    public static void zipInputStream(InputStream io, ZipOutputStream zos, String fileName, boolean isFile) throws Exception{
        byte[] buf = new byte[1024];
        if (Objects.nonNull(io)&&isFile){
            zos.putNextEntry(new ZipEntry(fileName));
            int len;
            while ((len = io.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
        }else if(!isFile){
            zos.putNextEntry(new ZipEntry(fileName + "/"));
        }
        // Complete the entry
        zos.closeEntry();
    }

    public static void getZipInputStream(List<CompressFileDto> list, ZipOutputStream zos){
        try {
            for (CompressFileDto compress: list){
                zipInputStream(compress.getIn(), zos, compress.getFileName(), compress.isFile());
            }
        } catch (Exception e) {
            logger.error("压缩异常：error={}", e.toString());
        }
    }

    public static ByteArrayInputStream parse(OutputStream out) throws Exception{
        ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * 压缩为zip包
     * @param srcDir 压缩包所在路径
     * @param out 输出流
     * @param keepDirStructure
     * @param filterFileName 压缩文件名字
     * @throws RuntimeException
     */
    public static void toZip(String srcDir, OutputStream out, boolean keepDirStructure, String filterFileName) throws RuntimeException{
        long start = System.currentTimeMillis();
        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos,null, keepDirStructure, filterFileName);
            long end = System.currentTimeMillis();
            logger.info("压缩完成，耗时：{}ms", (end - start));
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils",e);
        }
    }

    /**
     * 不压缩当前目录
     * @param sourceFile
     * @param zos
     * @param name
     * @param keepDirStructure
     * @param filterFileName
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean keepDirStructure,String filterFileName) throws Exception{
        byte[] buf = new byte[1024];
        if(sourceFile.isFile()&& StringUtils.isNotEmpty(name)){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            if(name.contains(filterFileName)){
                return;
            }
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            try(FileInputStream in = new FileInputStream(sourceFile)) {
                while ((len = in.read(buf)) != -1){
                    zos.write(buf, 0, len);
                }
                // Complete the entry
                zos.closeEntry();
            }
        } else {
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(keepDirStructure){
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (keepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        if(name==null) {
                            compress(file, zos, file.getName(), keepDirStructure, filterFileName);
                        }else{
                            compress(file, zos, name + "/" + file.getName(), keepDirStructure, filterFileName);
                        }
                    } else {
                        compress(file, zos, file.getName(), keepDirStructure, filterFileName);
                    }
                }
            }
        }
    }

}
