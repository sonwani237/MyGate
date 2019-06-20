package com.troology.mygate.utils;

import com.google.gson.JsonObject;
import com.troology.mygate.add_flat.model.AddFlatResponse;
import com.troology.mygate.dashboard.model.MemberListResponse;
import com.troology.mygate.dashboard.model.ResidentsResponse;
import com.troology.mygate.login_reg.model.ApartmentsResponse;
import com.troology.mygate.login_reg.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface EndPointInterface {

    @POST("/demo/mygateapp/api/user_login")
    Call<ApiResponse> sendOTP(@Header("Content-Type") String content,
                              @Body JsonObject jsonObject);

    @POST("/demo/mygateapp/api/verify_otp")
    Call<ApiResponse> verifyOTP(@Header("Content-Type") String content,
                                @Body JsonObject jsonObject);

    @POST("/demo/mygateapp/api/register")
    Call<ApiResponse> register(@Header("Content-Type") String content,
                               @Body JsonObject jsonObject);

    @POST("/demo/mygateapp/api/view_member")
    Call<MemberListResponse> viewMember(@Header("Content-Type") String content,
                                        @Body JsonObject jsonObject);

    @POST("/demo/mygateapp/api/add_flat_member")
    Call<MemberListResponse> addMember(@Header("Content-Type") String content,
                                       @Body JsonObject jsonObject);

    @POST("/demo/mygateapp/api/get_city")
    Call<AddFlatResponse> getCity(@Header("Content-Type") String content,
                                  @Body JsonObject jsonObject);

    @POST("/demo/mygateapp/api/get_country")
    Call<AddFlatResponse> getCountry(@Header("Content-Type") String content,
                                     @Body JsonObject object);

    @POST("/demo/mygateapp/api/get_state")
    Call<AddFlatResponse> getState(@Header("Content-Type") String content,
                                   @Body JsonObject jsonObject);

    @POST("/demo/mygateapp/api/get_apartment")
    Call<AddFlatResponse> getApartment(@Header("Content-Type") String content,
                                       @Body JsonObject jsonObject);

    @POST("/demo/mygateapp/api/get_flats")
    Call<AddFlatResponse> getFlat(@Header("Content-Type") String content,
                                  @Body JsonObject jsonObject);

    @POST("/demo/mygateapp/api/view_residents_for_user")
    Call<ResidentsResponse> getResidents(@Header("Content-Type") String content,
                                         @Body JsonObject jsonObject);

    @POST("/demo/mygateapp/api/add_flat_details")
    Call<AddFlatResponse> AddFlat(@Header("Content-Type") String content,
                                  @Body JsonObject jsonObject);

    @POST("/demo/mygateapp/api/add_meeting_request")
    Call<ResidentsResponse> AddRequest(@Header("Content-Type") String content,
                                     @Body JsonObject jsonObject);

    @POST("/demo/mygateapp/api/flat_details")
    Call<ApartmentsResponse> ApartmentsDetail(@Header("Content-Type") String content,
                                              @Body JsonObject jsonObject);

}
