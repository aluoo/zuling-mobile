package com.zxtx.hummer.products.service;

import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.products.domain.ZjProductType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zxtx.hummer.products.domain.request.ZjProductTypeOperatorReq;
import com.zxtx.hummer.products.domain.request.ZjProductTypeQueryReq;
import com.zxtx.hummer.products.domain.response.ZjProductTypeVO;

import java.util.List;

/**
 * <p>
 * 商品类目 服务类
 * </p>
 *
 * @author L
 * @since 2026-01-05
 */
public interface IZjProductTypeService extends IService<ZjProductType> {


    void add(ZjProductTypeOperatorReq req);
    void edit(ZjProductTypeOperatorReq req);
    void delete(Integer id);

    /**
     * 分页查询商品类型列表（支持列表和树形结构）
     */
    PageUtils<ZjProductTypeVO> listWithPage(ZjProductTypeQueryReq req);


    /**
     * 获取选择树
     * @param excludeId 排除的节点ID（修改时使用，防止选择自己作为父类）
     * @return 树形结构列表
     */
    List<ZjProductTypeVO> getSelectTree(Integer excludeId);
}
