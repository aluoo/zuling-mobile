package com.zxtx.hummer.cms.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.cms.dao.ArticleMapper;
import com.zxtx.hummer.cms.domain.Article;
import com.zxtx.hummer.cms.domain.dto.ArticleDTO;
import com.zxtx.hummer.cms.domain.response.ArticleVO;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.common.exception.BizError;
import com.zxtx.hummer.common.exception.BusinessException;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/9/12
 */
@Slf4j
@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> {

    public PageUtils<ArticleVO> listPage(ArticleDTO req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<Article> list = this.lambdaQuery()
                .eq(req.getCategoryId() != null, Article::getCategoryId, req.getCategoryId())
                .like(StrUtil.isNotBlank(req.getTitle()), Article::getTitle, req.getTitle())
                .eq(Article::getDeleted, false)
                .orderByDesc(Article::getActivated)
                .orderByDesc(Article::getSort)
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }
        List<ArticleVO> resp = BeanUtil.copyToList(list, ArticleVO.class);
        resp.forEach(vo -> {
            vo.setActivatedStr(vo.getActivated() ? "已发布" : "已下架");
        });
        return new PageUtils(resp, (int) page.getTotal());
    }

    public ArticleDTO getArticleInfoById(Long id) {
        ArticleDTO vo = new ArticleDTO();
        Article bean = this.lambdaQuery().eq(Article::getDeleted, false).eq(Article::getId, id).one();
        if (bean == null) {
            return vo;
        }
        BeanUtil.copyProperties(bean, vo);
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean save(ArticleDTO req) {
        Article bean = Article.builder()
                .id(SnowflakeIdWorker.nextID())
                .createBy(ShiroUtils.getUserId())
                .deleted(false)
                .activated(false)
                .sort(req.getSort())
                .categoryId(req.getCategoryId() != null ? req.getCategoryId() : 1L)
                .title(req.getTitle())
                .subTitle(req.getSubTitle())
                .description(req.getDescription())
                .cover(req.getCover())
                .content(req.getContent())
                .isPopular(req.getIsPopular() != null ? req.getIsPopular() : false)
                .isTop(req.getIsTop() != null ? req.getIsTop() : false)
                .views(0L)
                .build();
        return this.save(bean);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean edit(ArticleDTO req) {
        if (Objects.isNull(req.getId())) {
            throw new BusinessException(BizError.INVALID_PARAMETER);
        }
        return this.lambdaUpdate()
                .set(Article::getUpdateTime, new Date())
                .set(Article::getUpdateBy, ShiroUtils.getUserId())
                .set(req.getSort() != null, Article::getSort, req.getSort())
                .set(req.getCategoryId() != null , Article::getCategoryId, req.getCategoryId())
                .set(Article::getTitle, req.getTitle())
                .set(StrUtil.isNotBlank(req.getSubTitle()), Article::getSubTitle, req.getSubTitle())
                .set(StrUtil.isNotBlank(req.getDescription()), Article::getDescription, req.getDescription())
                .set(StrUtil.isNotBlank(req.getCover()), Article::getCover, req.getCover())
                .set(Article::getContent, req.getContent())
                .set(req.getIsTop() != null, Article::getIsTop, req.getIsTop())
                .set(req.getIsPopular() != null, Article::getIsPopular, req.getIsPopular())
                .eq(Article::getId, req.getId())
                .eq(Article::getDeleted, false)
                .update();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean switchActivated(ArticleDTO req) {
        if (Objects.isNull(req.getId())) {
            throw new BusinessException(BizError.INVALID_PARAMETER);
        }
        Date publishTime = req.getActivated() ? new Date() : null;

        return this.lambdaUpdate()
                .set(Article::getUpdateTime, new Date())
                .set(Article::getUpdateBy, ShiroUtils.getUserId())
                .set(Article::getActivated, req.getActivated())
                .set(publishTime != null, Article::getPublishTime, publishTime)
                .eq(Article::getId, req.getId())
                .eq(Article::getDeleted, false)
                .update();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        return this.lambdaUpdate()
                .set(Article::getUpdateTime, new Date())
                .set(Article::getUpdateBy, ShiroUtils.getUserId())
                .set(Article::getDeleted, true)
                .eq(Article::getId, id)
                .eq(Article::getDeleted, false)
                .update();
    }
}