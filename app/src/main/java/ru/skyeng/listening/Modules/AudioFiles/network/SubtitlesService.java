package ru.skyeng.listening.Modules.AudioFiles.network;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitleFile;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface SubtitlesService{

    @GET("/audios/{audioId}/subtitles")
    Observable<List<SubtitleFile>> getSubtitles(
            @Path("audioId") int audioId
    );
}
