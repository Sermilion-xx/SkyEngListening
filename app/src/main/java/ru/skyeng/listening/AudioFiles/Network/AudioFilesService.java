package ru.skyeng.listening.AudioFiles.network;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.skyeng.listening.AudioFiles.model.AudioData;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface AudioFilesService {

    @GET("/audios")
    Observable<AudioData> getAudioFiles(
            @Query("page") Integer  page,
            @Query("pageSize") Integer  pageSize,
            @Query("title") String title,
            @Query("accentId") Integer  accentId,
            @Query("levelId") Integer  levelId,
            @Query("tagIds[]") List<Integer> tagIds,
            @Query("durationGT") Integer  durationGT,
            @Query("durationLT") Integer  durationLT
    );
}
