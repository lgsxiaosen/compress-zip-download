package com.githut.lgsxiaosen.dto;

import java.io.InputStream;

/**
 * @author liuguisen
 * @date 2020/6/1
 **/
public class CompressFileDto {

    private String fileName;

    private String path;

    private InputStream in;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public InputStream getIn() {
        return in;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }
}
