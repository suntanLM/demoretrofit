package com.example.phuon.demojson;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by phuon on 11/25/2017.
 */

public interface APiService {
    @GET("/json")
    Call<List<Product>> getbookdetail();
}
