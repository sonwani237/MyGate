package com.troology.mygate.dashboard.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.troology.mygate.R;
import com.troology.mygate.dashboard.model.UserMeetings;
import com.troology.mygate.dashboard.ui.RequestAdapter;
import com.troology.mygate.login_reg.model.ApartmentDetails;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.FragmentActivityMessage;
import com.troology.mygate.utils.GlobalBus;
import com.troology.mygate.utils.UtilsMethods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOnce extends Fragment {

    TextView tv_date, tv_starttime, tv_endtime;
    Button btn_submit;
    int mYear, mMonth, mDay;
    EditText et_remarks;
    ApartmentDetails details;
    String type;


    /*public FragmentOnce() {
        // Required empty public constructor
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_once, container, false);

        tv_date = view.findViewById(R.id.oncedatepicker);
        tv_starttime = view.findViewById(R.id.oncetimepicker1);
        tv_endtime = view.findViewById(R.id.oncetimepicker2);
        btn_submit = view.findViewById(R.id.btnsubmitOnce);
        et_remarks = view.findViewById(R.id.etremarks_once);
        details = UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class);
//        assert getArguments() != null;
//        type = getArguments().getString("type");

        //Toast.makeText(getActivity(), ""+type, Toast.LENGTH_SHORT).show();
       /* if (type.equals("3")){

        }*/

        click();
        return view;
    }

    private void click() {

        /*date picker*/
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
                tv_date.setText(mDay + "-" + mMonth + "-" + mYear);
            }
        });

        /*start time picker*/
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
                        tv_starttime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        /*end time picker*/
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
                            tv_endtime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        /*btn submit*/
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject object = new JsonObject();
                object.addProperty("token", UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());
                object.addProperty("apartment_id", UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class).getApartment_id());
                object.addProperty("flat_id", details.getFlat_id());
                if (type.equals("1")){
                    object.addProperty("member_type", "Cab");
                }
                if (type.equals("2")){
                    object.addProperty("member_type", "Delivery");
                }
                if (type.equals("3")){
                    object.addProperty("member_type", "Guest");
                }
                if (type.equals("1") || type.equals("2")){
                    object.addProperty("name", "");
                    object.addProperty("mobile","");
                }else {
                    object.addProperty("name", UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getName());
                    object.addProperty("mobile", UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getMobile());
                }
                object.addProperty("time_from",tv_starttime.getText().toString());
                object.addProperty("time_to", tv_endtime.getText().toString());
                object.addProperty("stay_days",0);
                object.addProperty("remarks",et_remarks.getText().toString());
                object.addProperty("activity_type", "1");

                UtilsMethods.INSTANCE.AddActivity(getActivity(), object,null, null );
            }
        });

    }

    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage fragmentActivityMessage) {
        if (fragmentActivityMessage.getMessage().equalsIgnoreCase("type")){
            type = fragmentActivityMessage.getFrom();

            Log.e("type >>> "," >>> "+type);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }


}
