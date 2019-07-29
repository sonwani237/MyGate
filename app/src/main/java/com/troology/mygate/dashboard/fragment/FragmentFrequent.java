package com.troology.mygate.dashboard.fragment;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.dashboard.ui.Dashboard;
import com.troology.mygate.login_reg.model.ApartmentDetails;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.FragmentActivityMessage;
import com.troology.mygate.utils.GlobalBus;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class FragmentFrequent extends Fragment {
    TextView tv_starttime, tv_endtime, tRemark;
    Button btn_submit;
    EditText et_remarks, et_name, et_mobile;
    Spinner tv_validity;
    String days;
    ApartmentDetails details;
    String type = "";
    LinearLayout ll_name, time_layout;
    RelativeLayout parent;
    Loader loader;
    String date = "2019-1-1", start_time = "", end_time = "";
    long startTimestamp, endTime;
    Calendar c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_frequent, container, false);

        loader = new Loader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        tv_validity = view.findViewById(R.id.tvselectvalidity);
        tv_starttime = view.findViewById(R.id.starttime_frequent);
        tv_endtime = view.findViewById(R.id.endtimefrequent);
        btn_submit = view.findViewById(R.id.btnsubmitFrequent);
        et_remarks = view.findViewById(R.id.etRemarks_frequent);
        ll_name = view.findViewById(R.id.linlayname);
        time_layout = view.findViewById(R.id.time_layout);
        et_name = view.findViewById(R.id.namefrequent);
        et_mobile = view.findViewById(R.id.mobilefrequent);
        parent = view.findViewById(R.id.parent);
        tRemark = view.findViewById(R.id.tRemark);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.days_of_months, R.layout.spinner_item);
        tv_validity.setAdapter(adapter);

        /*tv_validity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/

        details = UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class);

        type = Dashboard.type;
        if (type.equals("3")) {
            ll_name.setVisibility(View.VISIBLE);
            time_layout.setVisibility(View.GONE);
        }

        if (type.equals("1")) {
            tRemark.setText("Cab Number");
            et_remarks.setHint("Cab Number");
        }

        startTimestamp =  ( Calendar.getInstance(TimeZone.getTimeZone("UTC"))).getTimeInMillis();
        c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        click();
        return view;
    }

    private void click() {

        tv_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        start_time = selectedHour + ":" + selectedMinute + ":00";
                        tv_endtime.setText("Select");
                        tv_starttime.setText(UtilsMethods.INSTANCE.ShortTime(date + " " + start_time));
                    }
                }, hour, minute, false);
                mTimePicker.show();
            }
        });

        tv_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!tv_starttime.getText().toString().contains(":")) {
                    Toast.makeText(getActivity(), "Please select start time first", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            end_time = selectedHour + ":" + selectedMinute + ":00";
                            if (checkTimings(start_time, end_time)){
                                tv_endtime.setText(UtilsMethods.INSTANCE.ShortTime(date + " " + end_time));
                            }else {
                                tv_endtime.setText("Select");
                                UtilsMethods.INSTANCE.snackBar(getResources().getString(R.string.time_error), parent);
                            }

                        }
                    }, hour, minute, false);
                    mTimePicker.show();
                }
            }

        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValid()){
                    if (UtilsMethods.INSTANCE.isNetworkAvailable(getActivity())) {
                        btn_submit.setEnabled(false);
                        loader.show();
                        loader.setCancelable(false);
                        loader.setCanceledOnTouchOutside(false);

                        JsonObject object = new JsonObject();
                        object.addProperty("token", UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());
                        object.addProperty("apartment_id", UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class).getApartment_id());
                        object.addProperty("flat_id", details.getFlat_id());

                        if (type.equals("1")) {
                            object.addProperty("member_type", "Cab");

                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            String formattedDate = df.format(c.getTime());
                            int day = Integer.parseInt(tv_validity.getSelectedItem().toString().toLowerCase().replace(" days","").trim());
                            c.add(Calendar.DAY_OF_YEAR, +day);
                            endTime = c.getTimeInMillis();

                            object.addProperty("time_from", formattedDate +" "+start_time);
                            object.addProperty("time_to", UtilsMethods.INSTANCE.endDate(""+endTime) +" "+end_time);
                        }
                        if (type.equals("2")) {
                            object.addProperty("member_type", "Delivery");
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            String formattedDate = df.format(c.getTime());
                            int day = Integer.parseInt(tv_validity.getSelectedItem().toString().toLowerCase().replace(" days","").trim());
                            c.add(Calendar.DAY_OF_YEAR, +day);
                            endTime = c.getTimeInMillis();

                            object.addProperty("time_from", formattedDate +" "+start_time);
                            object.addProperty("time_to", UtilsMethods.INSTANCE.endDate(""+endTime) +" "+end_time);
                        }
                        if (type.equals("3")) {
                            object.addProperty("member_type", "Guest");
                            int day = Integer.parseInt(tv_validity.getSelectedItem().toString().toLowerCase().replace(" days","").trim());
                            c.add(Calendar.DAY_OF_YEAR, +day);
                            endTime = c.getTimeInMillis();
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formattedDate = df.format(calendar.getTime());
                            object.addProperty("time_from", formattedDate);
                            object.addProperty("time_to", UtilsMethods.INSTANCE.ActiveTime(""+endTime));
                        }
                        if (type.equals("1") || type.equals("2")) {
                            object.addProperty("name", "");
                            object.addProperty("mobile", "");
                        } else {
                            object.addProperty("name", et_name.getText().toString());
                            object.addProperty("mobile", et_mobile.getText().toString());
                        }

                        object.addProperty("stay_days", tv_validity.getSelectedItem().toString());
                        object.addProperty("remarks", et_remarks.getText().toString());
                        object.addProperty("activity_type", "2");
                        object.addProperty("request_by", "1");

                        UtilsMethods.INSTANCE.AddActivity(getActivity(), object, parent, loader);
                    } else {
                        UtilsMethods.INSTANCE.snackBar(getResources().getString(R.string.network_error), parent);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_submit.setEnabled(true);
                        }
                    }, 1500);
                }
            }
        });

    }

    public boolean isValid() {

        if (type.equalsIgnoreCase("3")){
            if (et_name.getText().toString().equalsIgnoreCase("")) {
                UtilsMethods.INSTANCE.snackBar("Name cannot be blank!", parent);
                return false;
            }

            if (et_mobile.getText().toString().length() != 10) {
                UtilsMethods.INSTANCE.snackBar("Please enter valid Mobile Number!", parent);
                return false;
            }
        }else {
            if (tv_starttime.getText().toString().equalsIgnoreCase("Select")) {
                UtilsMethods.INSTANCE.snackBar("Please select start time.", parent);
                return false;
            }

            if (tv_endtime.getText().toString().equalsIgnoreCase("Select")) {
                UtilsMethods.INSTANCE.snackBar("Please select end time.", parent);
                return false;
            }
            if (type.equalsIgnoreCase("1")){
                if(et_remarks.getText().toString().equalsIgnoreCase("")){
                    UtilsMethods.INSTANCE.snackBar("Cab no cannot be blank!", parent);
                    return false;
                }
            }
            if (type.equalsIgnoreCase("2")){
                if(et_remarks.getText().toString().equalsIgnoreCase("")){
                    UtilsMethods.INSTANCE.snackBar("Remark cannot be blank!", parent);
                    return false;
                }
            }

        }

        return true;
    }

    private boolean checkTimings(String time, String endtime) {

        String pattern = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            return date1.before(date2);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }

}
