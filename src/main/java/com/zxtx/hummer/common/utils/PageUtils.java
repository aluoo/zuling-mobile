package com.zxtx.hummer.common.utils;

import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author bootdo 1992lcg@163.com
 */
public class PageUtils<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int total;
    private List<T> rows;

    public PageUtils(List<T> list, int total) {
        this.rows = list;
        this.total = total;
    }

    public PageUtils(List<T> list, long total) {
        this.rows = list;
        this.total = (int) total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public static PageUtils create(Object o) {
        if (o instanceof Page) {
            Page page = (Page) o;
            return new PageUtils(page, (int) page.getTotal());
        }
        return null;
    }

    public static PageUtils ofPage(com.baomidou.mybatisplus.extension.plugins.pagination.Page<?> page) {

        return new PageUtils(page.getRecords(), (int) page.getTotal());
    }

    public static <T> PageUtils<T> emptyPage() {
        return new PageUtils<>(new ArrayList<>(), 0);
    }
}