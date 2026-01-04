package com.zxtx.hummer.insurance.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.insurance.dao.mapper.DiInsuranceUserAccountMapper;
import com.zxtx.hummer.insurance.domain.DiInsuranceUserAccount;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 险种类型表 服务实现类
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Service
public class DiInsuranceUserAccountService extends ServiceImpl<DiInsuranceUserAccountMapper, DiInsuranceUserAccount> {

    public void createUser(String name,String mobile,String idCard){

        List<DiInsuranceUserAccount> userList =  this.lambdaQuery()
                .eq(DiInsuranceUserAccount::getMobile,mobile).list();

        if(CollUtil.isNotEmpty(userList)) return;

        DiInsuranceUserAccount userAccount = new DiInsuranceUserAccount();
        userAccount.setMobile(mobile);
        userAccount.setName(name);
        userAccount.setIdCard(idCard);
        userAccount.setPassWord(SecureUtil.md5(idCard.substring(idCard.length()-6)));
        userAccount.setStatus(1);
        this.save(userAccount);
    }

    public Map<Long, DiInsuranceUserAccount> getUserMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<DiInsuranceUserAccount> list = this.lambdaQuery()
                .in(DiInsuranceUserAccount::getId, ids)
                .list();
        return CollUtil.isEmpty(list)
                ? Collections.emptyMap()
                : list.stream().collect(Collectors.toMap(DiInsuranceUserAccount::getId, Function.identity()));
    }
}