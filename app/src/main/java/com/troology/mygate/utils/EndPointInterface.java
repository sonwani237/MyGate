package com.troology.mygate.utils;

import com.google.gson.JsonObject;
import com.troology.mygate.add_flat.model.AddFlatResponse;
import com.troology.mygate.login_reg.model.ApartmentsResponse;
import com.troology.mygate.login_reg.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface EndPointInterface {

    @POST("demo/mygetapp/api/send_otp")
    Call<ApiResponse> sendOTP(@Header("Content-Type") String content,
                              @Body JsonObject jsonObject);

    @POST("demo/mygetapp/api/varify_otp")
    Call<ApiResponse> verifyOTP(@Header("Content-Type") String content,
                                @Body JsonObject jsonObject);

    @POST("demo/mygetapp/api/register")
    Call<ApiResponse> register(@Header("Content-Type") String content,
                               @Body JsonObject jsonObject);

    @POST("demo/mygetapp/api/get_location")
    Call<AddFlatResponse> getLocation(@Header("Content-Type") String content,
                                      @Body JsonObject jsonObject);

    @POST("demo/mygetapp/api/get_building")
    Call<AddFlatResponse> getBuilding(@Header("Content-Type") String content,
                                      @Body JsonObject jsonObject);

    @POST("demo/mygetapp/api/get_apartment_no")
    Call<AddFlatResponse> getApartment(@Header("Content-Type") String content,
                                       @Body JsonObject jsonObject);

    @POST("demo/mygetapp/api/add_appartments")
    Call<AddFlatResponse> AddFlat(@Header("Content-Type") String content,
                                  @Body JsonObject jsonObject);

    @POST("demo/mygetapp/api/apartments_detail")
    Call<ApartmentsResponse> ApartmentsDetail(@Header("Content-Type") String content,
                                              @Body JsonObject jsonObject);

    @GET("demo/mygetapp/api/get_city")
    Call<AddFlatResponse> getCity();

}
