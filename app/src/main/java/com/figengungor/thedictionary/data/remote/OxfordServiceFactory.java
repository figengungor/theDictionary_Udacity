package com.figengungor.thedictionary.data.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by figengungor on 5/20/2018.
 */

public class OxfordServiceFactory {

    private static final String BASE_URL = "https://od-api.oxforddictionaries.com/api/v1/";
    private static final String APP_ID= "app_id";
    private static final String APP_KEY= "app_key";
    private static final int HTTP_READ_TIMEOUT = 10;
    private static final int HTTP_CONNECT_TIMEOUT = 6;

    public static OxfordService createService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();

        return retrofit.create(OxfordService.class);
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder();
        httpClientBuilder.connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(createLoggingInterceptor());
        return httpClientBuilder.build();
    }

    private static HttpLoggingInterceptor createLoggingInterceptor(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return logging;
    }


}
