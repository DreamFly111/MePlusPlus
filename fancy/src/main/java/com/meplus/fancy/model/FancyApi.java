package com.meplus.fancy.model;

import com.meplus.fancy.model.entity.Book;
import com.meplus.fancy.model.entity.User;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.Header;
import rx.Observable;

/**
 * Created by dandanba on 3/9/16.
 */
public class FancyApi implements ApiService {

    @Override
    public Observable<Response<User>> getborrowlistbyrobot(@FieldMap Map<String, String> map, @Header("X-FANCY-TIMESTAMP") String timestamp, @Header("X-FANCY-SIGN") String sign) {
        return null;
    }

    @Override
    public Observable<Response<Book>> borrowbyrobot(@FieldMap Map<String, String> map, @Header("X-FANCY-TIMESTAMP") String timestamp, @Header("X-FANCY-SIGN") String sign) {
        return null;
    }

    @Override
    public Observable<Response<Book>> returnbyrobot(@FieldMap Map<String, String> map, @Header("X-FANCY-TIMESTAMP") String timestamp, @Header("X-FANCY-SIGN") String sign) {
        return null;
    }

    @Override
    public Observable<Response<List<Book>>> getborrowedlistbyrobot(@FieldMap Map<String, String> map, @Header("X-FANCY-TIMESTAMP") String timestamp, @Header("X-FANCY-SIGN") String sign) {
        return null;
    }
}
