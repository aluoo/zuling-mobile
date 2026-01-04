package com.zxtx.hummer.product.service;

import com.zxtx.hummer.HummerApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

@Slf4j
public class OrderTaskServiceTest extends HummerApplicationTest {
    @Autowired
    OrderTaskService service;

    @Test
    public void autoCloseOverdueOrder() {
        service.autoCloseOverdueOrder();
    }
}