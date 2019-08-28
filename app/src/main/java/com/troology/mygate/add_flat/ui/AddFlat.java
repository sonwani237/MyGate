package com.troology.mygate.add_flat.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.troology.mygate.R;
import com.troology.mygate.add_flat.model.BuildingList;
import com.troology.mygate.add_flat.model.CityList;
import com.troology.mygate.add_flat.model.CountryList;
import com.troology.mygate.add_flat.model.FlatList;
import com.troology.mygate.add_flat.model.LocationList;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.login_reg.ui.LoginScreen;
import com.troology.mygate.splash.ui.SplashActivity;
import com.troology.mygate.utils.ActivityActivityMessage;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.GlobalBus;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class AddFlat extends AppCompatActivity implements View.OnClickListener {

    Spinner officeType, country, state, city, apartment, flatNo;
    Button submit;
    Loader loader;
    RelativeLayout parent;
    ImageView iv_addflat, iv_logout;

    ArrayList<CountryList> country_list = new ArrayList<>();
    ArrayList<String> countryList;

    ArrayList<LocationList> state_list = new ArrayList<>();
    ArrayList<String> stateList;

    ArrayList<CityList> city_list = new ArrayList<>();
    ArrayList<String> cityList;

    ArrayList<BuildingList> apartment_list = new ArrayList<>();
    ArrayList<String> apartmentList;

    ArrayList<FlatList> flat_list = new ArrayList<>();
    ArrayList<String> flatList;

    String country_id = "", state_id = "", city_id = "", apartment_id = "", flat_id = "", flat_no = "";
    UserDetails userDetails;
    CheckBox owner;
    String own = "1";
    int type;
    String addType = "Select Office";
    String selectFlat = "Select flat";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        userDetails = UtilsMethods.INSTANCE.get(getApplicationContext(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class);
        InItView();


    }

    private void InItView() {

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
        parent = findViewById(R.id.parent);
        country = findViewById(R.id.country);
        officeType = findViewById(R.id.officeType);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        apartment = findViewById(R.id.apartment);
        flatNo = findViewById(R.id.flatNo);
        iv_logout = findViewById(R.id.iv_logout);
        iv_addflat = findViewById(R.id.ivaddflat);
        iv_addflat.setAlpha(100);

        submit = findViewById(R.id.submit);
        owner = findViewById(R.id.owner);

//        getCountry();
        submit.setOnClickListener(this);
        owner.setOnClickListener(this);
        iv_logout.setOnClickListener(this);


        getType();

    }

    private void getType() {
        List<String> categories = new ArrayList<String>();
        categories.add("Select Type");
        categories.add("Office");
        categories.add("Resident");

        officeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!parent.getItemAtPosition(position).toString().equals("Select Type")) {
                    country.setVisibility(View.VISIBLE);
                    getCountry();
                    if (parent.getItemAtPosition(position).toString().equals("Office")) {
                        type = 2;
                        addType = "Select Office";
                        selectFlat = "Select Branch";
                    } else {
                        type = 1;
                        addType = "Select Apartment";
                        selectFlat = "Select Flat";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        officeType.setAdapter(dataAdapter);
    }


    private void getCountry() {
        if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            JsonObject object = new JsonObject();
            object.addProperty("token", userDetails.getToken());

            UtilsMethods.INSTANCE.getCountry(getApplicationContext(), object, parent, loader);
        } else {
            UtilsMethods.INSTANCE.snackBar(getResources().getString(R.string.network_error), parent);
        }
    }

    @Subscribe
    public void onActivityActivityMessage(ActivityActivityMessage activityFragmentMessage) {


        if (activityFragmentMessage.getMessage().equalsIgnoreCase("CountryList")) {
            LoadCountryData(activityFragmentMessage.getFrom());
        }
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("CityList")) {
            LoadCityData(activityFragmentMessage.getFrom());
        }
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("LocationList")) {
            LoadState(activityFragmentMessage.getFrom());
        }
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("ApartmentList")) {
            LoadApartment(activityFragmentMessage.getFrom());
        }
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("FlatList")) {
            LoadFlat(activityFragmentMessage.getFrom());
        }
    }

    private void LoadCountryData(String from) {
        country_list = new Gson().fromJson(from, new TypeToken<List<CountryList>>() {
        }.getType());
        countryList = new ArrayList<>();
        state.setVisibility(View.GONE);
        city.setVisibility(View.GONE);
        apartment.setVisibility(View.GONE);
        flatNo.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        owner.setVisibility(View.GONE);

        if (country_list != null && country_list.size() > 0) {
            countryList.add("Select Country");
            for (int i = 0; i < country_list.size(); i++) {
                countryList.add(country_list.get(i).getName());
            }
        }

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals("Select Country")) {
                    country_id = country_list.get(position - 1).getCountry_id();
                    if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {

                        loader.show();
                        loader.setCancelable(false);
                        loader.setCanceledOnTouchOutside(false);

                        JsonObject object = new JsonObject();
                        object.addProperty("country_id", country_list.get(position - 1).getCountry_id());
                        object.addProperty("token", userDetails.getToken());

                        UtilsMethods.INSTANCE.getState(getApplicationContext(), object, parent, loader);
                    } else {
                        UtilsMethods.INSTANCE.snackBar(getResources().getString(R.string.network_error), parent);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> countryAdapter;
        countryAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, countryList);
        country.setAdapter(countryAdapter);
    }

    private void LoadState(String from) {
        state_list = new Gson().fromJson(from, new TypeToken<List<LocationList>>() {
        }.getType());
        stateList = new ArrayList<>();
        state.setVisibility(View.GONE);
        city.setVisibility(View.GONE);
        apartment.setVisibility(View.GONE);
        flatNo.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        owner.setVisibility(View.GONE);
        if (state_list != null && state_list.size() > 0) {
            state.setVisibility(View.VISIBLE);
            stateList.add("Select State");
            for (int i = 0; i < state_list.size(); i++) {
                stateList.add(state_list.get(i).getName());
            }
        } else {
            stateList.clear();
            state.setVisibility(View.GONE);
        }

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals("Select State")) {
                    state_id = state_list.get(position - 1).getLocation_id();
                    if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {

                        loader.show();
                        loader.setCancelable(false);
                        loader.setCanceledOnTouchOutside(false);

                        JsonObject object = new JsonObject();
                        object.addProperty("state_id", state_list.get(position - 1).getLocation_id());
                        object.addProperty("token", userDetails.getToken());

                        UtilsMethods.INSTANCE.getCity(getApplicationContext(), object, parent, loader);
                    } else {
                        UtilsMethods.INSTANCE.snackBar(getResources().getString(R.string.network_error), parent);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> stateAdapter;
        stateAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, stateList);
        state.setAdapter(stateAdapter);
    }

    private void LoadCityData(String from) {
        city_list = new Gson().fromJson(from, new TypeToken<List<CityList>>() {
        }.getType());
        cityList = new ArrayList<>();
        city.setVisibility(View.GONE);
        apartment.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        owner.setVisibility(View.GONE);
        flatNo.setVisibility(View.GONE);
        flatNo.setVisibility(View.GONE);
        if (city_list != null && city_list.size() > 0) {
            city.setVisibility(View.VISIBLE);
            cityList.add("Select City");
            for (int i = 0; i < city_list.size(); i++) {
                cityList.add(city_list.get(i).getName());
            }
        } else {
            cityList.clear();
            city.setVisibility(View.GONE);
        }

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals("Select City")) {
                    city_id = city_list.get(position - 1).getId();
                    if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {

                        loader.show();
                        loader.setCancelable(false);
                        loader.setCanceledOnTouchOutside(false);

                        JsonObject object = new JsonObject();
                        object.addProperty("city_id", city_list.get(position - 1).getId());
                        object.addProperty("token", userDetails.getToken());
                        object.addProperty("type", type);

                        UtilsMethods.INSTANCE.getApartment(getApplicationContext(), object, parent, loader);
                    } else {
                        UtilsMethods.INSTANCE.snackBar("", parent);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> cityAdapter;
        cityAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, cityList);
        city.setAdapter(cityAdapter);
    }

    private void LoadApartment(String from) {
        apartment_list = new Gson().fromJson(from, new TypeToken<List<BuildingList>>() {
        }.getType());
        apartmentList = new ArrayList<>();
        apartment.setVisibility(View.GONE);
        flatNo.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        owner.setVisibility(View.GONE);
        if (apartment_list != null && apartment_list.size() > 0) {
            apartment.setVisibility(View.VISIBLE);
            apartmentList.add(addType);
            for (int i = 0; i < apartment_list.size(); i++) {
                apartmentList.add(apartment_list.get(i).getApartment_no());
            }
        } else {
            apartmentList.clear();
            apartment.setVisibility(View.GONE);
        }

        apartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals(addType)) {
                    apartment_id = apartment_list.get(position - 1).getApartment_id();

                    if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {

                        loader.show();
                        loader.setCancelable(false);
                        loader.setCanceledOnTouchOutside(false);

                        JsonObject object = new JsonObject();
                        object.addProperty("apartment_id", apartment_id);
                        object.addProperty("token", userDetails.getToken());

                        UtilsMethods.INSTANCE.getFlat(getApplicationContext(), object, parent, type, loader);
                    } else {
                        UtilsMethods.INSTANCE.snackBar(getResources().getString(R.string.network_error), parent);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> apartmentAdapter;
        apartmentAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, apartmentList);
        apartment.setAdapter(apartmentAdapter);
    }

    private void LoadFlat(String from) {
        flat_list = new Gson().fromJson(from, new TypeToken<List<FlatList>>() {
        }.getType());
        flatList = new ArrayList<>();
        flatNo.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        owner.setVisibility(View.GONE);
        if (flat_list != null && flat_list.size() > 0) {
            flatNo.setVisibility(View.VISIBLE);
            flatList.add(selectFlat);
            for (int i = 0; i < flat_list.size(); i++) {
                flatList.add(flat_list.get(i).getFlat_no());
            }
        } else {
            flatList.clear();
            flatNo.setVisibility(View.GONE);
        }

        flatNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals(selectFlat)) {
                    flat_id = flat_list.get(position - 1).getFlat_id();
                    flat_no = flat_list.get(position - 1).getFlat_no();
                    submit.setVisibility(View.VISIBLE);
                    if (type == 2){
                        owner.setVisibility(View.GONE);
                    }
                    else {
                        owner.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> flatAdapter;
        flatAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, flatList);
        flatNo.setAdapter(flatAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {

                    loader.show();
                    loader.setCancelable(false);
                    loader.setCanceledOnTouchOutside(false);

                    JsonObject object = new JsonObject();
                    object.addProperty("token", userDetails.getToken());
                    object.addProperty("uid", userDetails.getUid());
                    object.addProperty("flat_id", flat_id);
                    object.addProperty("flat_no", flat_no);
                    object.addProperty("apartment_id", apartment_id);
                    object.addProperty("city_id", city_id);
                    object.addProperty("state_id", state_id);
                    object.addProperty("country_id", country_id);
                    object.addProperty("isowner", own);
                    object.addProperty("type", ""+type);
//                    Log.e("Req "," >>> "+new Gson().toJson(object));

                    UtilsMethods.INSTANCE.AddFlat(AddFlat.this, object, type, parent, loader);
                } else {
                    UtilsMethods.INSTANCE.snackBar(getResources().getString(R.string.network_error), parent);
                }
                break;
            case R.id.owner:
                if (owner.isChecked()) {
                    own = "1";
                } else {
                    own = "0";
                }
                break;

            case R.id.iv_logout:
                UtilsMethods.INSTANCE.save(this, ApplicationConstant.INSTANCE.userToken, "");
                Intent intent = new Intent(AddFlat.this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                startActivity(intent);
        }
    }

}
