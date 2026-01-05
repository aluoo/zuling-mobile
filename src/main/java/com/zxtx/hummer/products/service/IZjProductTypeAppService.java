package com.zxtx.hummer.products.service;

import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.products.domain.ZjProductTypeApp;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zxtx.hummer.products.domain.request.ZjProductTypeAppOperatorReq;
import com.zxtx.hummer.products.domain.request.ZjProductTypeAppQueryReq;
import com.zxtx.hummer.products.domain.response.ZjProductTypeAppVO;

/**
 * <p>
 * 小程序产品种类 服务类
 * </p>
 *
 * @author L
 * @since 2026-01-05
 */
public interface IZjProductTypeAppService extends IService<ZjProductTypeApp> {

    PageUtils<ZjProductTypeAppVO> listWithPage(ZjProductTypeAppQueryReq req);
    void add(ZjProductTypeAppOperatorReq req);
    void edit(ZjProductTypeAppOperatorReq req);
    void delete(Integer id);

}
