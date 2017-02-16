package ru.skyeng.listening.Modules.Categories.model;

import ru.skyeng.listening.CommonComponents.Interfaces.RequestParams;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 15/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class TagsRequestParams implements RequestParams {
    private int page;
    private int pageSize;

    public TagsRequestParams(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public TagsRequestParams() {
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
