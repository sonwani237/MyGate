package com.troology.mygate.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.troology.mygate.dashboard.model.MemberListResponse;
import com.troology.mygate.dashboard.model.ResidentsResponse;
import com.troology.mygate.dashboard.ui.CreateRequest;
import com.troology.mygate.dashboard.ui.Dashboard;
import com.troology.mygate.login_reg.model.ApartmentsResponse;
import com.troology.mygate.login_reg.model.ApiResponse;
import com.troology.mygate.login_reg.model.UserDetails;
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
        Call<ApiResponse> call = pointInterface.sendOTP(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("SignUpRequest", "Sign_up response : " + new Gson().toJson(response.body()));
                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200")) {
                    Toast.makeText(context, ""+response.body().getOtp().getPhone_otp(), Toast.LENGTH_LONG).show();

                    Intent i = new Intent(context, OTPVerification.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("mobile", jsonObject.get("mobile").getAsString());
                    context.startActivity(i);

//                    context.startActivity(new Intent(context, OTPVerification.class)
//                            .putExtra("mobile", jsonObject.get("mobile").getAsString()));
                } else if (response.body() != null && response.body().getStatus().equalsIgnoreCase("404")) {
                    Intent i = new Intent(context, RegisterScreen.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("mobile", jsonObject.get("mobile").getAsString());
                    context.startActivity(i);

//                    context.startActivity(new Intent(context, RegisterScreen.class)
//                            .putExtra("mobile", jsonObject.get("mobile").getAsString()));
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

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("uid", UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getUid());
                    jsonObject.addProperty("token", UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());

                    ApartmentsDetail(context, jsonObject, view, null);
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
                Log.e("register", "Signup response : " + response.body() + "   " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200")) {

                    JsonObject object = new JsonObject();
                    object.addProperty("mobile", jsonObject.get("mobile").getAsString());

                    sendOTP(context, object, view, null);

//                    context.startActivity(new Intent(context, Dashboard.class));
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
                Log.e("getCity", "response : " + response.body() + "   " + new Gson().toJson(response.body()));

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
                Log.e("getCity", "response : " + response.body() + "   " + new Gson().toJson(response.body()));

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
                Log.e("getCity", "response : " + response.body() + "   " + new Gson().toJson(response.body()));

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
                Log.e("getApartment", "response : " + new Gson().toJson(response.body()));

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

    public void getFlat(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<AddFlatResponse> call = pointInterface.getFlat(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<AddFlatResponse>() {
            @Override
            public void onResponse(Call<AddFlatResponse> call, Response<AddFlatResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("getApartment", "response : " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus() && response.body().getFlats().size() > 0) {
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("FlatList", new Gson().toJson(response.body().getFlats()));
                    GlobalBus.getBus().post(activityMessage);
                } else if (response.body() != null && !response.body().getStatus()) {
                    snackBar(response.body().getMsg(), view);
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

    public void getResidents(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ResidentsResponse> call = pointInterface.getResidents(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ResidentsResponse>() {
            @Override
            public void onResponse(Call<ResidentsResponse> call, Response<ResidentsResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("getResidents", "response : " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("200") && response.body().getResidentsData().size() > 0) {
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("ResidentsList", new Gson().toJson(response.body().getResidentsData()));
                    GlobalBus.getBus().post(activityMessage);
                } else if (response.body() != null && !response.body().getStatus().equalsIgnoreCase("200")) {
                    snackBar(response.body().getMsg(), view);
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("ResidentsList", "");
                    GlobalBus.getBus().post(activityMessage);
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

    public void ApartmentsDetail(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<ApartmentsResponse> call = pointInterface.ApartmentsDetail(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<ApartmentsResponse>() {
            @Override
            public void onResponse(Call<ApartmentsResponse> call, Response<ApartmentsResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("ApartmentsDetail", "response : " + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getStatus() && response.body().getApartment_details().size() > 0) {
                    save(context, ApplicationConstant.INSTANCE.flatPerf, response.body().getApartment_details().get(0));
                    Intent intent = new Intent(context, Dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("EXIT", true);
                    intent.putExtra("approval_status", response.body().getApartment_details().get(0).getApproval_status());
                    context.startActivity(intent);
                } else if (response.body() != null && !response.body().getStatus()) {
                    Intent intent = new Intent(context, AddFlat.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("EXIT", true);
                    context.startActivity(intent);
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

    public void addMember(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<MemberListResponse> call = pointInterface.addMember(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<MemberListResponse>() {
            @Override
            public void onResponse(Call<MemberListResponse> call, Response<MemberListResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("ApartmentsDetail", "response : " + new Gson().toJson(response.body()));

                if (response.body()!=null && response.body().getStatus()== 200){
                    snackBar(response.body().getMsg(), view);
                    ActivityActivityMessage activityMessage =
                            new ActivityActivityMessage("MemberAdd","");
                    GlobalBus.getBus().post(activityMessage);
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

    public void viewMember(final Context context, final JsonObject jsonObject, final View view, final Loader loader) {
        EndPointInterface pointInterface = ApiClient.getClient().create(EndPointInterface.class);
        Call<MemberListResponse> call = pointInterface.viewMember(ApplicationConstant.INSTANCE.contentType, jsonObject);
        call.enqueue(new Callback<MemberListResponse>() {
            @Override
            public void onResponse(Call<MemberListResponse> call, Response<MemberListResponse> response) {
                if (loader != null && loader.isShowing()) {
                    loader.dismiss();
                }
                Log.e("ApartmentsDetail", "response : " + new Gson().toJson(response.body()));
                if (response.body()!=null && response.body().getMemberData()!=null){
                    FragmentActivityMessage fragmentActivityMessage =
                            new FragmentActivityMessage("MemberList", new Gson().toJson(response.body().getMemberData()));
                    GlobalBus.getBus().post(fragmentActivityMessage);
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

                if (response.body() != null && response.body().getStatus()) {

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("uid", UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getUid());
                    jsonObject.addProperty("token", UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());

                    UtilsMethods.INSTANCE.ApartmentsDetail(context, jsonObject, view, null);

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
                            Intent intent = new Intent(context, Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("EXIT", true);
                            context.startActivity(intent);
                        }
                    }, 1000);

                } else {
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
