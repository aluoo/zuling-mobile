package com.zxtx.hummer.common.oss;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@FunctionalInterface
public interface OssFileNameStrategy {

    String getFileName(MultipartFile file);


}
