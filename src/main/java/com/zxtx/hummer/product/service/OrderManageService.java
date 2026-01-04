package com.zxtx.hummer.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.service.CompanyService;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.product.domain.Order;
import com.zxtx.hummer.product.domain.enums.OrderStatusEnum;
import com.zxtx.hummer.product.domain.request.ProductOrderQueryReq;
import com.zxtx.hummer.product.domain.response.ProductOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/5/21
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderManageService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmployeeMapper employeeMapper;

    public PageUtils<ProductOrderVO> listOrder(ProductOrderQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        LambdaQueryWrapper<Order> qw = new LambdaQueryWrapper<Order>()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(req.getId() != null, Order::getId, req.getId())
                .eq(StrUtil.isNotBlank(req.getOrderNo()), Order::getOrderNo, req.getOrderNo())
                .eq(StrUtil.isNotBlank(req.getImeiNo()), Order::getImeiNo, req.getImeiNo())
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime);
        List<Order> list = orderService.list(qw);
        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }
        List<ProductOrderVO> resp = BeanUtil.copyToList(list, ProductOrderVO.class);

        Set<Long> employeeIds = ProductOrderVO.extractIds(resp, ProductOrderVO::getStoreEmployeeId, ProductOrderVO::getRecyclerEmployeeId);
        Set<Long> companyIds = ProductOrderVO.extractIds(resp, ProductOrderVO::getStoreCompanyId, ProductOrderVO::getRecyclerCompanyId);
        List<Company> companyList = companyService.lambdaQuery().in(Company::getId, companyIds).list();
        Map<Long, Company> companyMap = companyList.stream().collect(Collectors.toMap(Company::getId, Function.identity()));
        List<Employee> employees = employeeMapper.selectList(new LambdaQueryWrapper<Employee>().in(Employee::getId, employeeIds));
        Map<Long, Employee> employeeMap = employees.stream().collect(Collectors.toMap(Employee::getId, Function.identity()));

        resp.forEach(vo -> {
            vo.setFinalPriceStr();
            String storeCompanyName = Optional.ofNullable(companyMap.get(vo.getStoreCompanyId())).map(Company::getName).orElse(null);
            String recyclerCompanyName = Optional.ofNullable(companyMap.get(vo.getRecyclerCompanyId())).map(Company::getName).orElse(null);
            vo.setStoreCompanyName(storeCompanyName);
            vo.setRecyclerCompanyName(recyclerCompanyName);
            vo.setStoreEmployeeName(Optional.ofNullable(employeeMap.get(vo.getStoreEmployeeId())).map(Employee::getName).orElse(null));
            vo.setStoreEmployeeMobile(Optional.ofNullable(employeeMap.get(vo.getStoreEmployeeId())).map(Employee::getMobileNumber).orElse(null));
            vo.setRecyclerEmployeeName(Optional.ofNullable(employeeMap.get(vo.getRecyclerEmployeeId())).map(Employee::getName).orElse(null));
            vo.setRecyclerEmployeeMobile(Optional.ofNullable(employeeMap.get(vo.getRecyclerEmployeeId())).map(Employee::getMobileNumber).orElse(null));
            vo.setStatusStr(EnumUtil.getBy(OrderStatusEnum::getCode, vo.getStatus()).getDesc());
        });

        return new PageUtils<>(resp, page.getTotal());
    }
}