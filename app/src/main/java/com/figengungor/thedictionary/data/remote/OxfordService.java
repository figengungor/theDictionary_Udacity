package com.figengungor.thedictionary.data.remote;

import com.figengungor.thedictionary.data.model.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by figengungor on 5/20/2018.
 */

public interface OxfordService {

    @GET("entries/{source_lang}/{word_id}")
    Call<SearchResponse> search(@Path("source_lang") String sourceLanguage,
                                @Path("word_id") String word,
                                @Header("app_id") String appId,
                                @Header("app_key") String appKey);

}
