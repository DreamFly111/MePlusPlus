package com.meplus.speech.api;


import com.meplus.speech.model.TransResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dandanba on 3/9/16.
 */
public interface ApiService {
    @GET("translate")
    Observable<TransResult> translate(@Query("appid") String appid, @Query("q") String q, @Query("from") String from,
                                      @Query("to") String to, @Query("salt") long salt, @Query("sign") String sign);
}
