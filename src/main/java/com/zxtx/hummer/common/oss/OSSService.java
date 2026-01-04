package com.zxtx.hummer.common.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.zxtx.hummer.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class OSSService {

    @Autowired
    private OSSConfig ossConfig;

    public String put(String bucketName, MultipartFile file, OssFileNameStrategy ossFileNameStrategy) throws IOException {
        OSS client = new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        InputStream is = file.getInputStream();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(is.available());
        String fileName = null;
        if (ossFileNameStrategy == null) {
            fileName = OssFilenameStrategyEnum.defaultStrategy.getFileName(file);
        } else {
            fileName = ossFileNameStrategy.getFileName(file);
        }

        client.putObject(bucketName, fileName, is, metadata);
        client.shutdown();
        return Constants.HTTPS + bucketName + "." + ossConfig.getEndpoint() + "/" + fileName;
    }


}
