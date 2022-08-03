package com.jos.dem.vetlog.client;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.POST;

public interface JmailerClient {

    @POST("message")
    Call<Response<String>> sendMessage();

}
