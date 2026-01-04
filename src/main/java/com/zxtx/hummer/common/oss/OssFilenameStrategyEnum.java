package com.zxtx.hummer.common.oss;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public enum OssFilenameStrategyEnum implements OssFileNameStrategy {

    defaultStrategy() {
        @Override
        public String getFileName(MultipartFile file) {
            return file.getName();
        }
    },


    ;

    private OssFilenameStrategyEnum() {

    }


}
