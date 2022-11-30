package com.example.examenfinal.services;

import com.example.examenfinal.entities.Image;
import com.example.examenfinal.entities.ImageResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ImagenService {

    @Headers("Authorization: Client-ID 8bcc638875f89d9")
    @POST("3/image")
    Call<ImageResponse> create(@Body Image imagen);
}
