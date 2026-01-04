package com.zxtx.hummer.company.service;

import cn.hutool.core.io.FileUtil;
import com.zxtx.hummer.HummerApplicationTest;
import com.zxtx.hummer.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/10/28
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@ActiveProfiles("test")
public class CompanyBatchCreateServiceTest extends HummerApplicationTest {
    @Autowired
    private CompanyBatchCreateService service;

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void test() {
        String filePath = "C:\\Users\\xmayc\\Desktop\\批量导入创建门店模板.xlsx";
        try {
            File file = FileUtil.file(filePath);
            MultipartFile multipartFile = convert(file);
            service.batchCreateCompanyFromImport(multipartFile);
        } catch (BaseException | IOException e) {
            if (e instanceof BaseException) {
                log.error(((BaseException) e).getError().getMessage());
            }
            throw new RuntimeException(e);
        }
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void test1() {
        String filePath = "C:\\Users\\xmayc\\Desktop\\批量导入创建员工模板.xlsx";
        try {
            File file = FileUtil.file(filePath);
            MultipartFile multipartFile = convert(file);
            service.batchCreateEmployeeFromImport(multipartFile);
        } catch (BaseException | IOException e) {
            if (e instanceof BaseException) {
                log.error(((BaseException) e).getError().getMessage());
            }
            throw new RuntimeException(e);
        }
    }

    public static MultipartFile convert(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File must not be null and must exist");
        }

        String fileName = file.getName();
        InputStream inputStream = Files.newInputStream(file.toPath());
        return new MockMultipartFile(fileName, fileName, "application/octet-stream", inputStream);
    }
}