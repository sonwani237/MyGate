package com.troology.mygate.dashboard.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.login_reg.model.ApartmentDetails;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

import java.util.Calendar;
import java.util.Objects;

public class CreateRequest extends AppCompatActivity implements View.OnClickListener {

    Loader loader;
    ApartmentDetails details;
    EditText ed_name, number, purpose;
    TextView date_select, date;
    Button submit;
    RelativeLayout parent;
    String schedule_date, schedule_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.create_req);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);

        parent = findViewById(R.id.parent);
        ed_name = findViewById(R.id.ed_name);
        number = findViewById(R.id.number);
        purpose = findViewById(R.id.purpose);
        submit = findViewById(R.id.submit);
        date_select = findViewById(R.id.date_select);
        date = findViewById(R.id.date);

        details = UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class);

        submit.setOnClickListener(this);
        date_select.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == submit){
            if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                JsonObject object = new JsonObject();
                object.addProperty("flat_id", details.getFlat_id());
                object.addProperty("apartment_id", details.getApartment_id());
                object.addProperty("token", UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());
                object.addProperty("request_by", "1");
                object.addProperty("name", ed_name.getText().toString());
                object.addProperty("mobile", number.getText().toString());
                object.addProperty("meeting_time", schedule_date + " " + schedule_time);
                object.addProperty("remarks", purpose.getText().toString());
                object.addProperty("status", "1");

                UtilsMethods.INSTANCE.AddRequest(this, object, parent, loader);
            } else {
                UtilsMethods.INSTANCE.snackBar("", parent);
            }
        }
        if (v ==  date_select){
            ScheduleDate();
        }
    }

    private void ScheduleDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        schedule_date = "" +year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        ScheduleTime();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void ScheduleTime() {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        schedule_time = hourOfDay + ":" + minute + ":00";
                        date_select.setText(UtilsMethods.INSTANCE.ScheduleTime(schedule_date + " " + schedule_time));
                        date.setVisibility(View.VISIBLE);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}
