package ru.skyeng.listening.AudioFiles.Network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.skyeng.listening.AudioFiles.domain.AudioData;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface AudioFilesService {

    @GET("/audios")
    Call<AudioData> getAudioFiles(
            @Query("page") Integer  page,
            @Query("pageSize") Integer  pageSize,
            @Query("title") String title,
            @Query("accentId") Integer  accentId,
            @Query("levelId") Integer  levelId,
            @Query("tagIds[]") List<String> tagIds,
            @Query("durationGT") Integer  durationGT,
            @Query("durationLT") Integer  durationLT
    );
}
