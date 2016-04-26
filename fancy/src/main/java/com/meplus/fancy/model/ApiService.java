package com.meplus.fancy.model;


import com.meplus.fancy.model.entity.Book;
import com.meplus.fancy.model.entity.User;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by dandanba on 3/9/16.
 */
public interface ApiService {
    @FormUrlEncoded
    @POST("api/borrow/getborrowlistbyrobot")
    Observable<Response<User>> getborrowlistbyrobot(@FieldMap Map<String, String> map, @Header("X-FANCY-TIMESTAMP") String timestamp, @Header("X-FANCY-SIGN") String sign);

    @FormUrlEncoded
    @POST("api/borrow/borrowbyrobot")
    Observable<Response<Book>> borrowbyrobot(@FieldMap Map<String, String> map, @Header("X-FANCY-TIMESTAMP") String timestamp, @Header("X-FANCY-SIGN") String sign);

    @FormUrlEncoded
    @POST("api/borrow/returnbyrobot")
    Observable<Response<Book>> returnbyrobot(@FieldMap Map<String, String> map, @Header("X-FANCY-TIMESTAMP") String timestamp, @Header("X-FANCY-SIGN") String sign);

    @FormUrlEncoded
    @POST("api/borrow/getborrowedlistbyrobot")
    Observable<Response<List<Book>>> getborrowedlistbyrobot(@FieldMap Map<String, String> map, @Header("X-FANCY-TIMESTAMP") String timestamp, @Header("X-FANCY-SIGN") String sign);
}
