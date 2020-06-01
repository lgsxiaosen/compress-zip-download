package com.githut.lgsxiaosen.service;

import java.io.InputStream;
import java.util.zip.ZipOutputStream;

public interface DownloadFileService {

    void download(ZipOutputStream zos);

}
