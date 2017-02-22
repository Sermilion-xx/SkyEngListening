package ru.skyeng.listening.Modules.Categories.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.skyeng.listening.Modules.Categories.model.TagsData;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface CategoriesService {

    @GET("/tags")
    Observable<TagsData> getTags(
            @Query("page") Integer page,
            @Query("pageSize") Integer pageSize
    );
}
