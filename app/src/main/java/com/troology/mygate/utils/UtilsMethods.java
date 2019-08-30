package com.troology.mygate.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.add_flat.model.AddFlatResponse;
import com.troology.mygate.add_flat.ui.AddFlat;
import com.troology.mygate.dashboard.model.MemberData;
import com.troology.mygate.dashboard.model.MemberListResponse;
import com.troology.mygate.dashboard.model.ResidentsResponse;
import com.troology.mygate.dashboard.ui.CreateRequest;
import com.troology.mygate.dashboard.ui.Dashboard;
import com.troology.mygate.dashboard.ui.LocalServices;
import com.troology.mygate.dashboard.ui.NotificationActivity;
import com.troology.mygate.dashboard.ui.PopupActivity;
import com.troology.mygate.dashboard.ui.RequestAdapter;
import com.troology.mygate.login_reg.model.ApartmentDetails;
import com.troology.mygate.login_reg.model.ApartmentsResponse;
import com.troology.mygate.login_reg.model.ApiResponse;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.login_reg.ui.OTPVerification;
import com.troology.mygate.login_reg.ui.RegisterScreen;
import com.troology.mygate.splash.model.NotificationModel;
import com.troology.mygate.splash.ui.SplashActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public String DateTime(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
            Date parsedDate = dateFormat.parse(time);
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return date.format(parsedDate);
        } catch (Exception e) {
            return time;
        }
    }

    public String ScheduleTime(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
            Date parsedDate = dateFormat.parse(time);
            DateFormat date = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            return date.format(parsedDate);
        } catch (Exception e) {
            return time;
        }
    }

    public String ShortTime(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
            Date parsedDate = dateFormat.parse(time);
            DateFormat date = new SimpleDateFormat("hh:mm a");
            return date.format(parsedDate);
        } catch (Exception e) {
            return time;
        }
    }

    public String ShortDate(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
            Date parsedDate = dateFormat.parse(time);
            DateFormat date = new SimpleDateFormat("MMM dd");
            return date.format(parsedDate);
        } catch (Exception e) {
            return time;
        }
    }

    public String Date(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
            Date parsedDate = dateFormat.parse(time);
            DateFormat date = new SimpleDateFormat("dd MMM yyyy");
            return date.format(parsedDate);
        } catch (Exception e) {
            return time;
        }
    }

    public String mDate(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
            Date parsedDate = dateFormat.parse(time);
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            return date.format(parsedDate);
        } catch (Exception e) {
            return time;
        }
    }

    public String ActiveTime(String time_stamp){
        Date dt = new Timestamp(Long.parseLong(time_stamp));
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(dt);
    }

    public String endDate(String time_stamp){
        Date dt = new Timestamp(Long.parseLong(time_stamp));
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dt);
    }

    public void snackBarLong(final Context context, View view) {
        final Snackbar snackbar = Snackbar.make(view, context.getResources().getString(R.string.invalid_session), Snackbar.LENGTH_INDEFINITE);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(context, ApplicationConstant.INSTANCE.userToken,"");
                snackbar.dismiss();
                Intent intent = new Intent(context, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                context.startActivity(intent);
            }
        });
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
        Call<ApiResponse> call = pointInterface.sendOTP(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("SignUpRequest", "Sign_up response : " + new Gson().toJson(response.body()));
                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200")) {
                    try {
                        ((RegisterScreen)context).finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, ""+response.body().getOtp().getPhone_otp(), Toast.LENGTH_LONG).show();

                    Intent i = new Intent(context, OTPVerification.class);
                    i.putExtra("mobile", jsonObject.get("mobile").getAsString());
                    context.startActivity(i);

                } else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("404")) {
                    Intent i = new Intent(context, RegisterScreen.class);
                    i.putExtra("mobile", jsonObject.get("mobile").getAsString());
                    context.startActivity(i);

                } else {
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
        Call<ApiResponse> call = pointInterface.verifyOTP(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("SignUpRequest", "Signup response : " + response.body().getUserDetails().get(0).getToken()+ "   " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200") && response.body().getUserDetails().get(0) != null) {
                    save(context, ApplicationConstant.INSTANCE.loginPerf, response.body().getUserDetails().get(0));
                    save(context, ApplicationConstant.INSTANCE.userToken, response.body().getUserDetails().get(0).getToken());
                    if ( response.body().getUserDetails().get(0).getImage()!=null) {
                        save(context, ApplicationConstant.INSTANCE.profilePic, response.body().getUserDetails().get(0).getImage());
                    }

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("uid", UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getUid());
                    jsonObject.addProperty("token", UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());

                    ApartmentsDetail(context, jsonObject, view, 0,null);
                } else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("500")) {
                    snackBarLong(context, view);
                }else if (response.body() != null) {
                    snackBar(response.body().getMsg(), view);
                } else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }
//            risika interprises raj motors

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("SignUpRequest", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void register(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ApiResponse> call = pointInterface.register(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("register", "Signup response : " + response.body() + "   " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200")) {

                    JsonObject object = new JsonObject();
                    object.addProperty("mobile", jsonObject.get("mobile").getAsString());

                    sendOTP(context, object, view, null);
                } else if (response.body() != null) {
                    snackBar(response.body().getMsg(), view);
                } else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("register", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void getCountry(final Context context, JsonObject object, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<AddFlatResponse> call = pointInterface.getCountry(ApplicationConstant.INSTANCE.contentType, object);
        call.enqueue(new Callback<AddFlatResponse>() {
            @Override
            public void onResponse(Call<AddFlatResponse> call, Response<AddFlatResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("getCity", "response : " + response.body() + "   " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus() && response.body().getCountries().size() > 0) {
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("CountryList", new Gson().toJson(response.body().getCountries()));
                    GlobalBus.getBus().post(activityMessage);
                } else if (response.body() != null && !response.body().getStatus()) {
                    snackBar(response.body().getMsg(), view);
                } else {
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

    public void getCity(final Context context, final JsonObject object, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<AddFlatResponse> call = pointInterface.getCity(ApplicationConstant.INSTANCE.contentType, object);
        call.enqueue(new Callback<AddFlatResponse>() {
            @Override
            public void onResponse(Call<AddFlatResponse> call, Response<AddFlatResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("getCity", "response : " + response.body() + "   " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus() && response.body().getCities().size() > 0) {
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("CityList", new Gson().toJson(response.body().getCities()));
                    GlobalBus.getBus().post(activityMessage);
                } else if (response.body() != null && !response.body().getStatus()) {
                    snackBar(response.body().getMsg(), view);
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("CityList", "");
                    GlobalBus.getBus().post(activityMessage);
                } else {
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

    public void getState(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<AddFlatResponse> call = pointInterface.getState(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<AddFlatResponse>() {
            @Override
            public void onResponse(Call<AddFlatResponse> call, Response<AddFlatResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("getCity", "response :  " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus() && response.body().getLocations().size() > 0) {
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("LocationList", new Gson().toJson(response.body().getLocations()));
                    GlobalBus.getBus().post(activityMessage);
                } else if (response.body() != null && !response.body().getStatus()) {
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("LocationList", "");
                    GlobalBus.getBus().post(activityMessage);
                    snackBar(response.body().getMsg(), view);
                } else {
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

    public void getApartment(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<AddFlatResponse> call = pointInterface.getApartment(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<AddFlatResponse>() {
            @Override
            public void onResponse(Call<AddFlatResponse> call, Response<AddFlatResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("getApartment", "response : " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus() && response.body().getApartment_no().size() > 0) {
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("ApartmentList", new Gson().toJson(response.body().getApartment_no()));
                    GlobalBus.getBus().post(activityMessage);
                } else if (response.body() != null && !response.body().getStatus()) {
                    snackBar(response.body().getMsg(), view);
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("ApartmentList", "");
                    GlobalBus.getBus().post(activityMessage);
                } else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<AddFlatResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("getApartment", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void getFlat(final Context context, final JsonObject jsonObject, final View view, final int type, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<AddFlatResponse> call = pointInterface.getFlat(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<AddFlatResponse>() {
            @Override
            public void onResponse(Call<AddFlatResponse> call, Response<AddFlatResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("getApartment", "response : " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus() && response.body().getFlats().size() > 0) {
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("FlatList", new Gson().toJson(response.body().getFlats()));
                    GlobalBus.getBus().post(activityMessage);
                } else if (response.body() != null && !response.body().getStatus()) {
                    if (type==1){
                        snackBar(response.body().getMsg(), view);
                    }
                    else
                    {
                        snackBar("No Branch Available", view);
                    }

                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("FlatList", "");
                    GlobalBus.getBus().post(activityMessage);
                } else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<AddFlatResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("getApartment", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void getRequest(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ResidentsResponse> call = pointInterface.getRequest(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ResidentsResponse>() {
            @Override
            public void onResponse(Call<ResidentsResponse> call, Response<ResidentsResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("getMeetingData", "response : " + new Gson().toJson(response.body().getMeetingData().getUser_meetings()));

                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200")) {
                    if (response.body().getData()!=null && response.body().getData().size() > 0){

                        ArrayList<NotificationModel> models = new ArrayList<>();

                        for (NotificationModel obj : response.body().getData()){
                            if (!obj.getTimeTo().equalsIgnoreCase("0")){
                                models.add(obj);
                            }
                            if (obj.getMobile().equalsIgnoreCase(get(context, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getMobile())){
                                save(context, ApplicationConstant.INSTANCE.userPassPerf, obj.getPasscode());
                            }
                        }

                        FragmentActivityMessage activityMessage =
                                new FragmentActivityMessage("RequestList", new Gson().toJson(models));
                        GlobalBus.getBus().post(activityMessage);
                    }else {
                        FragmentActivityMessage activityMessage =
                                new FragmentActivityMessage("RequestList", "");
                        GlobalBus.getBus().post(activityMessage);
                    }
                } else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("500")) {
                    snackBarLong(context, view);
                }
            }

            @Override
            public void onFailure(Call<ResidentsResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("getApartment", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void getResidents(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ResidentsResponse> call = pointInterface.getResidents(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ResidentsResponse>() {
            @Override
            public void onResponse(Call<ResidentsResponse> call, Response<ResidentsResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("getResidents", "response : " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200") && response.body().getResidentsData().size() > 0) {
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("ResidentsList", new Gson().toJson(response.body().getResidentsData()));
                    GlobalBus.getBus().post(activityMessage);
                } else if (response.body() != null && !response.body().getStatus().equalsIgnoreCase("200")) {
                    snackBar(response.body().getMsg(), view);
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("ResidentsList", "");
                    GlobalBus.getBus().post(activityMessage);
                }else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("500")) {
                    snackBarLong(context, view);
                } else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<ResidentsResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("getApartment", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void ApartmentsDetail(final Context context, final JsonObject jsonObject, final View view, final int key, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ApartmentsResponse> call = pointInterface.ApartmentsDetail(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ApartmentsResponse>() {
            @Override
            public void onResponse(Call<ApartmentsResponse> call, Response<ApartmentsResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("ApartmentsDetail", "response : " + new Gson().toJson(response.body()));
//                {"status":500,"msg":"token not matched. Unauthorized access"}
                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200") && response.body().getApartment_details().size() > 0) {

                    if (key == 0){
                        save(context, ApplicationConstant.INSTANCE.flatPerf, response.body().getApartment_details().get(0));
                        Intent intent = new Intent(context, Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("EXIT", true);
                        intent.putExtra("approval_status", response.body().getApartment_details().get(0).getApproval_status());
                        context.startActivity(intent);
                    }else {
                        ActivityActivityMessage activityMessage =
                                new ActivityActivityMessage("approval_status", response.body().getApartment_details().get(0).getApproval_status());
                        GlobalBus.getBus().post(activityMessage);
                    }
                } else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("404")) {
                    Intent intent = new Intent(context, AddFlat.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("EXIT", true);
                    context.startActivity(intent);
                } else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("500")) {
                    snackBarLong(context, view);
                } else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<ApartmentsResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("ApartmentsDetail", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void serviceMenList(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ApartmentsResponse> call = pointInterface.serviceMenList(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ApartmentsResponse>() {
            @Override
            public void onResponse(Call<ApartmentsResponse> call, Response<ApartmentsResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("ApartmentsDetail", "response : " + new Gson().toJson(response.body()));
                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200") && response.body().getServicemenData().size() > 0) {
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("ServicemenList", new Gson().toJson(response.body().getServicemenData()));
                    GlobalBus.getBus().post(activityMessage);
                } else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("404")) {
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("ServicemenList", "");
                    GlobalBus.getBus().post(activityMessage);
                } else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("500")) {
                    snackBarLong(context, view);
                } else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<ApartmentsResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("ApartmentsDetail", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void AddActivity(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ApartmentsResponse> call = pointInterface.AddActivity(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ApartmentsResponse>() {
            @Override
            public void onResponse(Call<ApartmentsResponse> call, Response<ApartmentsResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("ApartmentsDetail", "response : " + new Gson().toJson(response.body()));
//                {"status":500,"msg":"token not matched. Unauthorized access"}
                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200") ) {
                    snackBar(response.body().getMsg(), view);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((PopupActivity)context).finish();
                            } catch (Exception ignored) {
                            }
                        }
                    }, 2000);
                } else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("500")) {
                    snackBarLong(context, view);
                } else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<ApartmentsResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void viewMember(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<MemberListResponse> call = pointInterface.viewMember(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<MemberListResponse>() {
            @Override
            public void onResponse(Call<MemberListResponse> call, Response<MemberListResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("ApartmentsDetail", "response : " + new Gson().toJson(response.body()));
                if (response.body()!=null && response.body().getMemberData()!=null){
                    ArrayList<MemberData> memberData = new ArrayList<>();
                    for (MemberData obj : response.body().getMemberData()){
                        if (!obj.getmMobile().equalsIgnoreCase(get(context, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getMobile())){
                            memberData.add(obj);
                        }else {
                            save(context, ApplicationConstant.INSTANCE.userPassPerf, obj.getPasscode());
                        }
                    }
                    FragmentActivityMessage fragmentActivityMessage =
                            new FragmentActivityMessage("MemberList", new Gson().toJson(memberData));
                    GlobalBus.getBus().post(fragmentActivityMessage);
                }else if (response.body() != null && response.body().getStatus() == 404) {
                    FragmentActivityMessage fragmentActivityMessage =
                            new FragmentActivityMessage("MemberList", "");
                    GlobalBus.getBus().post(fragmentActivityMessage);
                }else if (response.body() != null && response.body().getStatus() == 500) {
                    snackBarLong(context, view);
                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<MemberListResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("ApartmentsDetail", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void viewServiceMember(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<MemberListResponse> call = pointInterface.viewServiceMember(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<MemberListResponse>() {
            @Override
            public void onResponse(Call<MemberListResponse> call, Response<MemberListResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("ServiceRequest", "response : " + new Gson().toJson(response.body()));
                if (response.body()!=null && response.body().getStatus() == 200 ){
                    if (response.body().getServiceRequestDetails()!=null && response.body().getServiceRequestDetails().size()>0){
                        FragmentActivityMessage fragmentActivityMessage =
                                new FragmentActivityMessage("ServiceList", new Gson().toJson(response.body().getServiceRequestDetails()));
                        GlobalBus.getBus().post(fragmentActivityMessage);
                    }else {
                        FragmentActivityMessage fragmentActivityMessage =
                                new FragmentActivityMessage("ServiceList","");
                        GlobalBus.getBus().post(fragmentActivityMessage);
                    }

                }else if (response.body() != null && response.body().getStatus() == 404) {

                }else if (response.body() != null && response.body().getStatus() == 500) {
                    snackBarLong(context, view);
                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<MemberListResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("ApartmentsDetail", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void AddFlat(final Context context, final JsonObject jsonObject, final int type, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<AddFlatResponse> call = pointInterface.AddFlat(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<AddFlatResponse>() {
            @Override
            public void onResponse(Call<AddFlatResponse> call, Response<AddFlatResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                if (response.body() != null && response.body().getStatus()) {

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("uid", UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getUid());
                    jsonObject.addProperty("token", UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());

                    UtilsMethods.INSTANCE.ApartmentsDetail(context, jsonObject, view, 0,null);

                } else {
                    if (type == 1){
                        snackBar(response.body().getMsg(), view);
                    }else {
                        snackBar("Office Already Added!", view);
                    }
                }
            }

            @Override
            public void onFailure(Call<AddFlatResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void RequestAction(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ResidentsResponse> call = pointInterface.RequestAction(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ResidentsResponse>() {
            @Override
            public void onResponse(Call<ResidentsResponse> call, Response<ResidentsResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }

                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200")) {

//                    snackBar(response.body().getMsg(), view);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((NotificationActivity)context).finish();
                            } catch (Exception ignored) {
                            }
                        }
                    }, 2000);
                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<ResidentsResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("Signresponse", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void AddRequest(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ResidentsResponse> call = pointInterface.AddRequest(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ResidentsResponse>() {
            @Override
            public void onResponse(Call<ResidentsResponse> call, Response<ResidentsResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("AddFlat", "response : " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200")) {

                    snackBar(response.body().getMsg(), view);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((CreateRequest)context).onBackPressed();
                        }
                    }, 1000);

                }else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("500")) {
                    snackBarLong(context, view);
                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<ResidentsResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("Signresponse", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void AddServices(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ResidentsResponse> call = pointInterface.AddServiceRequest(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ResidentsResponse>() {
            @Override
            public void onResponse(Call<ResidentsResponse> call, Response<ResidentsResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("AddFlat", "response : " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200")) {

                    snackBar(response.body().getMsg(), view);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((LocalServices)context).onBackPressed();
                        }
                    }, 1000);

                }else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("404")) {
                    snackBar(response.body().getMsg(), view);
                }else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("500")) {
                    snackBarLong(context, view);
                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<ResidentsResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("Signresponse", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void DeleteActivity(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ApartmentsResponse> call = pointInterface.DeleteActivity(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ApartmentsResponse>() {
            @Override
            public void onResponse(Call<ApartmentsResponse> call, Response<ApartmentsResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }

                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200")  ) {

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("flat_id", UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class).getFlat_id());
                    jsonObject.addProperty("token", UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());

                    snackBar(response.body().getMsg(), view);

                    UtilsMethods.INSTANCE.getRequest(context, jsonObject, view, loader);

                } else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("500")) {
                    snackBarLong(context, view);
                } else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<ApartmentsResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void addMember(final Context context, final String path,final String flatid,final String apartment_id, final String token,
                          final String name,final String mobile_no, final View view, final Loader loader) {

        int compressionRatio = 25; //1 == originalImage, 2 = 50% compression, 4=25% compress
        File file = new File(path);
        try {
            Bitmap bitmap = BitmapFactory.decodeFile (file.getPath());
            bitmap.compress (Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file));
        }
        catch (Throwable t) {
//            Log.e("ERROR", "Error compressing file." + t.toString ());
            t.printStackTrace ();
        }

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);

        RequestBody flatId = RequestBody.create(MediaType.parse("text/plain"), flatid);
        RequestBody apartmentId = RequestBody.create(MediaType.parse("text/plain"), apartment_id);
        RequestBody Token = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody Name = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody mobileNo = RequestBody.create(MediaType.parse("text/plain"), mobile_no);

        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<MemberListResponse> call = pointInterface.addMember(part, flatId, apartmentId, Token, Name, mobileNo);
        call.enqueue(new Callback<MemberListResponse>() {
            @Override
            public void onResponse(Call<MemberListResponse> call, Response<MemberListResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("ApartmentsDetail", "response : " + new Gson().toJson(response.body()));

                if (response.body()!=null && response.body().getStatus() == 200){
                    snackBar(response.body().getMsg(), view);
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("MemberAdd","");
                    GlobalBus.getBus().post(activityMessage);
                }else if (response.body() != null && response.body().getStatus() == 404) {
                    snackBar(response.body().getMsg(), view);
                }else if (response.body() != null && response.body().getStatus() == 500) {
                    snackBarLong(context, view);
                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<MemberListResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("ApartmentsDetail", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void uploadImage(final Context context, final String path,final String userId, final String token, final View view,
                            final Loader loader) {

        int compressionRatio = 25; //1 == originalImage, 2 = 50% compression, 4=25% compress
        File file = new File(path);
        try {
            Bitmap bitmap = BitmapFactory.decodeFile (file.getPath());
            bitmap.compress (Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file));
        }
        catch (Throwable t) {
//            Log.e("ERROR", "Error compressing file." + t.toString ());
            t.printStackTrace ();
        }

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);

        RequestBody Token = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody uid = RequestBody.create(MediaType.parse("text/plain"), userId);


        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<MemberListResponse> call = pointInterface.uploadImage(part, uid, Token);
        call.enqueue(new Callback<MemberListResponse>() {
            @Override
            public void onResponse(Call<MemberListResponse> call, Response<MemberListResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("uploadImageResponse", "response : " + new Gson().toJson(response.body()));

                if (response.body()!=null && response.body().getStatus() == 200){
                    snackBar(response.body().getMsg(), view);
                    save(context, ApplicationConstant.INSTANCE.profilePic, response.body().getData());
                }else if (response.body() != null && response.body().getStatus() == 404) {
                    snackBar(response.body().getMsg(), view);
                }else if (response.body() != null && response.body().getStatus() == 500) {
                    snackBarLong(context, view);
                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<MemberListResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("ApartmentsDetail", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void UpdateActivity(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {

        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);

        Call<ApartmentsResponse> call = pointInterface.UpdateActivity(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ApartmentsResponse>() {
            @Override
            public void onResponse(Call<ApartmentsResponse> call, Response<ApartmentsResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("UpdateActivity", "response : " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200")  ) {

                    RequestAdapter.edit = false;
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("flat_id", UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class).getFlat_id());
                    jsonObject.addProperty("token", UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());

                    snackBar(response.body().getMsg(), view);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((PopupActivity)context).finish();
                            } catch (Exception ignored) {
                            }
                        }
                    }, 2000);

                    UtilsMethods.INSTANCE.getRequest(context, jsonObject, view, loader);

                } else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("500")) {
                    snackBarLong(context, view);
                } else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<ApartmentsResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void RemoveServiceRequest(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ResidentsResponse> call = pointInterface.RemoveServiceRequest(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ResidentsResponse>() {
            @Override
            public void onResponse(Call<ResidentsResponse> call, Response<ResidentsResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("AddFlat", "response : " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200")) {

                    snackBar(response.body().getMsg(), view);
                    FragmentActivityMessage activityMessage = new FragmentActivityMessage("ServiceListUpdate","");
                    GlobalBus.getBus().post(activityMessage);
                }else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("404")) {
                    snackBar(response.body().getMsg(), view);
                }else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("500")) {
                    snackBarLong(context, view);
                }else {
                    snackBar(context.getResources().getString(R.string.error), view);
                }
            }

            @Override
            public void onFailure(Call<ResidentsResponse> call, Throwable t) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
//                Log.e("Signresponse", "error " + t.getMessage());
                snackBar(context.getResources().getString(R.string.error), view);
            }
        });
    }

    public void save(final Context context, String key, Object o) {
        context.getSharedPreferences(ApplicationConstant.INSTANCE.appPref, Context.MODE_PRIVATE).edit()
                .putString(key, new Gson().toJson(o))
                .apply();
    }

    public <T> T get(final Context context, String key, Class<T> returnType) {
        String json = context.getSharedPreferences(ApplicationConstant.INSTANCE.appPref, Context.MODE_PRIVATE).getString(key, null);
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, returnType);
    }

}
