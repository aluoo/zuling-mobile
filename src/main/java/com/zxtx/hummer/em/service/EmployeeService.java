package com.zxtx.hummer.em.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/10/28
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class EmployeeService extends ServiceImpl<EmployeeMapper, Employee> {
}