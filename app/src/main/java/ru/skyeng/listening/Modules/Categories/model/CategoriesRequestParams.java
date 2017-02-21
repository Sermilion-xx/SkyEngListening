package ru.skyeng.listening.Modules.Categories.model;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 17/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class CategoriesRequestParams {

    private int page;
    private int pageSize;

    public CategoriesRequestParams(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public CategoriesRequestParams() {
        page = 1;
        pageSize = 15;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
