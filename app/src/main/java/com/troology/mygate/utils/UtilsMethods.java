package com.troology.mygate.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.add_flat.model.AddFlatResponse;
import com.troology.mygate.dashboard.ui.Dashboard;
import com.troology.mygate.login_reg.model.ApiResponse;
import com.troology.mygate.login_reg.ui.OTPVerification;
import com.troology.mygate.login_reg.ui.RegisterScreen;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public enum UtilsMethods {

    INSTANCE;

    public void snackBar(String message, View view) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        snackbar.show();
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void sendOTP(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ApiResponse> call = pointInterface.sendOTP( ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("SignUpRequest", "Signup response : " + response.body() + "   " + new Gson().toJson(response.body()));
                if (response.body()!=null && response.body().getStatus()){
                    context.startActivity(new Intent(context, OTPVerification.class)
                            .putExtra("mobile", response.body().getMobile()));
                }else if (response.body() != null){
                    snackBar(response.body().getMsg(), view);
                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("Signresponse", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void verifyOTP(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ApiResponse> call = pointInterface.verifyOTP( ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("SignUpRequest", "Signup response : " + response.body() + "   " + new Gson().toJson(response.body()));

                if (response.body()!=null && response.body().getStatus() && response.body().getUserDetails()!=null){
                    save(context, ApplicationConstant.INSTANCE.loginPerf, response.body().getUserDetails());
                    context.startActivity(new Intent(context, Dashboard.class));
                }else if (response.body()!=null && !response.body().getMsg().equalsIgnoreCase("OTP does not Matched")){
                    context.startActivity(new Intent(context, RegisterScreen.class)
                    .putExtra("mobile", response.body().getMobile()));
                }else if (response.body()!=null){
                    snackBar(response.body().getMsg(), view);
                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("Signresponse", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void register(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ApiResponse> call = pointInterface.register( ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("SignUpRequest", "Signup response : " + response.body() + "   " + new Gson().toJson(response.body()));

                if (response.body()!=null && response.body().getStatus() && response.body().getUserDetails()!=null){
                    save(context, ApplicationConstant.INSTANCE.loginPerf, response.body().getUserDetails());
                    context.startActivity(new Intent(context, Dashboard.class));
                }else if (response.body()!=null){
                    snackBar(response.body().getMsg(), view);
                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("Signresponse", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void getCity(final Context context, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<AddFlatResponse> call = pointInterface.getCity();
        call.enqueue(new Callback<AddFlatResponse>() {
            @Override
            public void onResponse(Call<AddFlatResponse> call, Response<AddFlatResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("getCity", "response : " + response.body() + "   " + new Gson().toJson(response.body()));

                if (response.body()!=null && response.body().getStatus() && response.body().getCities().size()>0){
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("CityList",new Gson().toJson(response.body().getCities()));
                    GlobalBus.getBus().post(activityMessage);
                }else if (response.body()!=null && !response.body().getStatus()){
                    snackBar(response.body().getMsg(), view);
                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }

            }

            @Override
            public void onFailure(Call<AddFlatResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("Signresponse", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void getLocation(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<AddFlatResponse> call = pointInterface.getLocation(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<AddFlatResponse>() {
            @Override
            public void onResponse(Call<AddFlatResponse> call, Response<AddFlatResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("getCity", "response : " + response.body() + "   " + new Gson().toJson(response.body()));

                if (response.body()!=null && response.body().getStatus() && response.body().getLocations().size()>0){
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("LocationList",new Gson().toJson(response.body().getLocations()));
                    GlobalBus.getBus().post(activityMessage);
                }else if (response.body()!=null && !response.body().getStatus()){
                    snackBar(response.body().getMsg(), view);
                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }

            }

            @Override
            public void onFailure(Call<AddFlatResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("Signresponse", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void getBuilding(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<AddFlatResponse> call = pointInterface.getBuilding(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<AddFlatResponse>() {
            @Override
            public void onResponse(Call<AddFlatResponse> call, Response<AddFlatResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("BuildingList", "response : " + new Gson().toJson(response.body()));

                if (response.body()!=null && response.body().getStatus() && response.body().getBuildings().size()>0){
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("BuildingList",new Gson().toJson(response.body().getBuildings()));
                    GlobalBus.getBus().post(activityMessage);
                }else if (response.body()!=null && !response.body().getStatus()){
                    snackBar(response.body().getMsg(), view);
                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }

            }

            @Override
            public void onFailure(Call<AddFlatResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("BuildingList", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void getApartment(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<AddFlatResponse> call = pointInterface.getApartment(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<AddFlatResponse>() {
            @Override
            public void onResponse(Call<AddFlatResponse> call, Response<AddFlatResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("BuildingList", "response : " + new Gson().toJson(response.body()));

                if (response.body()!=null && response.body().getStatus() && response.body().getApartment_no().size()>0){
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("ApartmentList",new Gson().toJson(response.body().getApartment_no()));
                    GlobalBus.getBus().post(activityMessage);
                }else if (response.body()!=null && !response.body().getStatus()){
                    snackBar(response.body().getMsg(), view);
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("NoApartment",new Gson().toJson(response.body().getApartment_no()));
                    GlobalBus.getBus().post(activityMessage);
                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }

            }

            @Override
            public void onFailure(Call<AddFlatResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("Signresponse", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void AddFlat(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<AddFlatResponse> call = pointInterface.AddFlat(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<AddFlatResponse>() {
            @Override
            public void onResponse(Call<AddFlatResponse> call, Response<AddFlatResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("AddFlat", "response : " + new Gson().toJson(response.body()));

              /*  if (response.body()!=null && response.body().getStatus() && response.body().getApartment_no().size()>0){

                }else if (response.body()!=null && !response.body().getStatus()){

                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
*/
            }

            @Override
            public void onFailure(Call<AddFlatResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("Signresponse", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public SharedPreferences getPerf(final Context context){
        return context.getSharedPreferences(ApplicationConstant.INSTANCE.appPref, context.MODE_PRIVATE);
    }

    public void save(final Context context, String key, Object o) {
        getPerf(context).edit()
                .putString(key, new Gson().toJson(o))
                .apply();
    }

    public <T> T get(final Context context, String key, Class<T> returnType) {
        String json = getPerf(context).getString(key, null);
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, returnType);
    }

}
