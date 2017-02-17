package ru.skyeng.listening.CommonComponents;

import ru.skyeng.listening.CommonComponents.Interfaces.RequestParams;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 17/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class BaseRequestParams implements RequestParams {
    private int page;
    private int pageSize;

    public BaseRequestParams(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public BaseRequestParams() {
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
