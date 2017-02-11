package ru.skyeng.listening.CommonCoponents;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class ServiceGenerator {

    private static final String SCHEMA = "https://";
    private static final String BASE_URL = SCHEMA + "api.listening.skyeng.ru/";

    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(
                            GsonConverterFactory.create()
                    );

    private Retrofit retrofit = builder.client(httpClient.build()) .build();

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
