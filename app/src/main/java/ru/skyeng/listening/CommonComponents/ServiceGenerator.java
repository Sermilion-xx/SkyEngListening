package ru.skyeng.listening.CommonComponents;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

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

    private HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC));


    private Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create()
                    );

    private Retrofit retrofit = builder.client(httpClient.build()) .build();

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
