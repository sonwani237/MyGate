package com.troology.mygate.add_flat.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.troology.mygate.add_flat.model.LocationList;
import com.troology.mygate.login_reg.model.UserDetails;
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

    Spinner city, location, building, building_no;
    RadioButton owner, rental;
    Button submit;
    Loader loader;
    RelativeLayout parent;
    RadioGroup radio_grp;

    ArrayList<CityList> city_list = new ArrayList<>();
    ArrayList<String> cityList = new ArrayList<>();

    ArrayList<LocationList> location_list = new ArrayList<>();
    ArrayList<String> locationList;

    ArrayList<BuildingList> building_list = new ArrayList<>();
    ArrayList<String> buildingList;

    ArrayList<BuildingList> building_no_list = new ArrayList<>();
    ArrayList<String> buildingNo;

    String user_type = "1", city_id = "", location_id = "", apartment_no_id = "", building_id = "";
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flat);

        InItView();
        userDetails = UtilsMethods.INSTANCE.get(getApplicationContext(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class);

    }

    private void InItView() {

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
        parent = findViewById(R.id.parent);
        city = findViewById(R.id.city);
        location = findViewById(R.id.location);
        building = findViewById(R.id.building);
        building_no = findViewById(R.id.building_no);
        owner = findViewById(R.id.owner);
        rental = findViewById(R.id.rental);
        submit = findViewById(R.id.submit);
        radio_grp = findViewById(R.id.radio_grp);

        getCity();

        owner.setOnClickListener(this);
        rental.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void getCity() {
        if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilsMethods.INSTANCE.getCity(getApplicationContext(), parent, loader);
        } else {
            UtilsMethods.INSTANCE.snackBar("", parent);
        }
    }

    @Subscribe
    public void onActivityActivityMessage(ActivityActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("CityList")) {
            LoadCityData(activityFragmentMessage.getFrom());
        }
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("LocationList")) {
            LoadLocation(activityFragmentMessage.getFrom());
        }
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("BuildingList")) {
            LoadBuilding(activityFragmentMessage.getFrom());
        }
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("ApartmentList")) {
            LoadApartment(activityFragmentMessage.getFrom());
        }
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("NoApartment")) {
            building_no.setVisibility(View.GONE);
            radio_grp.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
        }
    }

    private void LoadApartment(String from) {
        building_no_list = new Gson().fromJson(from, new TypeToken<List<BuildingList>>() {
        }.getType());
        buildingNo = new ArrayList<>();
        if (building_no_list != null && building_no_list.size() > 0) {
            building_no.setVisibility(View.VISIBLE);
            buildingNo.add("Select Building No");
            for (int i = 0; i < building_no_list.size(); i++) {
                buildingNo.add(building_no_list.get(i).getName());
            }
        } else {
            buildingNo.clear();
            submit.setVisibility(View.GONE);
            building_no.setVisibility(View.GONE);
        }

        building_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals("Select Building No")) {
                    apartment_no_id = building_list.get(position - 1).getBuilding_id();
                    radio_grp.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> buildingNoAdapter;
        buildingNoAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, buildingNo);
        building_no.setAdapter(buildingNoAdapter);
    }

    private void LoadBuilding(String from) {
        building_list = new Gson().fromJson(from, new TypeToken<List<BuildingList>>() {
        }.getType());
        buildingList = new ArrayList<>();
        building.setVisibility(View.GONE);
        building_no.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        if (building_list != null && building_list.size() > 0) {
            building.setVisibility(View.VISIBLE);
            buildingList.add("Select Building");
            for (int i = 0; i < building_list.size(); i++) {
                buildingList.add(building_list.get(i).getName());
            }
        } else {
            buildingList.clear();
            building.setVisibility(View.GONE);
        }

        building.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals("Select Building")) {
                    building_id = building_list.get(position - 1).getBuilding_id();
                    if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {

                        loader.show();
                        loader.setCancelable(false);
                        loader.setCanceledOnTouchOutside(false);

                        JsonObject object = new JsonObject();
                        object.addProperty("building_id", building_list.get(position - 1).getBuilding_id());

                        UtilsMethods.INSTANCE.getApartment(getApplicationContext(), object, parent, loader);
                    } else {
                        UtilsMethods.INSTANCE.snackBar("", parent);
                    }
                }/* else {
                 *//* *//*

                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> buildingAdapter;
        buildingAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, buildingList);
        building.setAdapter(buildingAdapter);
    }

    private void LoadLocation(String from) {
        location_list = new Gson().fromJson(from, new TypeToken<List<LocationList>>() {
        }.getType());
        locationList = new ArrayList<>();
        location.setVisibility(View.GONE);
        building.setVisibility(View.GONE);
        building_no.setVisibility(View.GONE);
        radio_grp.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        if (location_list != null && location_list.size() > 0) {
            location.setVisibility(View.VISIBLE);
            locationList.add("Select Location");
            for (int i = 0; i < location_list.size(); i++) {
                locationList.add(location_list.get(i).getName());
            }
        } else {
            locationList.clear();
            location.setVisibility(View.GONE);
        }

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals("Select Location")) {
                    location_id = location_list.get(position - 1).getLocation_id();
                    if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {

                        loader.show();
                        loader.setCancelable(false);
                        loader.setCanceledOnTouchOutside(false);

                        JsonObject object = new JsonObject();
                        object.addProperty("location_id", location_list.get(position - 1).getLocation_id());

                        UtilsMethods.INSTANCE.getBuilding(getApplicationContext(), object, parent, loader);
                    } else {
                        UtilsMethods.INSTANCE.snackBar("", parent);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> locationAdapter;
        locationAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, locationList);
        location.setAdapter(locationAdapter);
    }

    private void LoadCityData(String from) {
        city_list = new Gson().fromJson(from, new TypeToken<List<CityList>>() {
        }.getType());
        if (city_list != null && city_list.size() > 0) {
            cityList.add("Select City");
            for (int i = 0; i < city_list.size(); i++) {
                cityList.add(city_list.get(i).getName());
            }
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

                        UtilsMethods.INSTANCE.getLocation(getApplicationContext(), object, parent, loader);
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
            case R.id.rental:
                rental.setChecked(true);
                owner.setChecked(false);
                user_type = "2";
                break;
            case R.id.owner:
                rental.setChecked(false);
                owner.setChecked(true);
                user_type = "1";
                break;
            case R.id.submit:
                if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {

                    loader.show();
                    loader.setCancelable(false);
                    loader.setCanceledOnTouchOutside(false);

                    JsonObject object = new JsonObject();
                    object.addProperty("mobile",userDetails.getMobile());
                    object.addProperty("city_id",city_id);
                    object.addProperty("location_id",location_id);
                    object.addProperty("apartment_no_id",apartment_no_id);
                    object.addProperty("building_id",building_id);
                    object.addProperty("rental_owner",user_type);

                    UtilsMethods.INSTANCE.AddFlat(getApplicationContext(), object, parent, loader);
                } else {
                    UtilsMethods.INSTANCE.snackBar("", parent);
                }
                break;
        }
    }

}
