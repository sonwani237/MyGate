package com.troology.mygate.dashboard.fragment;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.login_reg.model.ApartmentDetails;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.FragmentActivityMessage;
import com.troology.mygate.utils.GlobalBus;
import com.troology.mygate.utils.UtilsMethods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFrequent extends Fragment {
    TextView  tv_starttime, tv_endtime;
    Button btn_submit;
    EditText et_remarks,et_name,et_mobile;
    Spinner tv_validity;
    String days;
    ApartmentDetails details;
    String type;
    LinearLayout ll_name,ll_timestart,ll_timeend;


   /* public FragmentFrequent() {
        // Required empty public constructor
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_frequent, container, false);

        tv_validity = view.findViewById(R.id.tvselectvalidity);
        tv_starttime = view.findViewById(R.id.starttime_frequent);
        tv_endtime = view.findViewById(R.id.endtimefrequent);
        btn_submit = view.findViewById(R.id.btnsubmitFrequent);
        et_remarks = view.findViewById(R.id.etRemarks_frequent);
        ll_name = view.findViewById(R.id.linlayname);
        ll_timestart = view.findViewById(R.id.linlaytime1);
        ll_timeend = view.findViewById(R.id.linlay2);
        et_name = view.findViewById(R.id.namefrequent);
        et_mobile = view.findViewById(R.id.mobilefrequent);
//        int type = getArguments().getInt("type");
        if (type.equals("3")){
            ll_name.setVisibility(View.VISIBLE);
            ll_timestart.setVisibility(View.GONE);
            ll_timeend.setVisibility(View.GONE);
        }

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.days_of_months, R.layout.spinner_item);
        tv_validity.setAdapter(adapter);

        details = UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class);

        //days = tv_validity.getSelectedItem().toString();
        //Toast.makeText(getActivity(), ""+days, Toast.LENGTH_SHORT).show();

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
                        tv_starttime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
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
                            tv_endtime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonObject object = new JsonObject();
                object.addProperty("token", UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());
                object.addProperty("apartment_id", UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class).getApartment_id());
                object.addProperty("flat_id", details.getFlat_id());
              //  object.addProperty("member_type", details.get);

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
                    object.addProperty("name",et_name.getText().toString());
                    object.addProperty("mobile",et_mobile.getText().toString());
                }
                object.addProperty("name", UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getName());
                object.addProperty("mobile", UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getMobile());
                object.addProperty("time_from",tv_starttime.getText().toString());
                object.addProperty("time_to", tv_endtime.getText().toString());
                object.addProperty("stay_days","spinner value");
                object.addProperty("remarks",et_remarks.getText().toString());
                object.addProperty("activity_type","2");



                UtilsMethods.INSTANCE.AddActivity(getActivity(), object,null, null );
            }
        });

    }

    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage fragmentActivityMessage) {
        if (fragmentActivityMessage.getMessage().equalsIgnoreCase("type")){
            type = fragmentActivityMessage.getFrom();
            Log.e("Frequent"," >>> "+type);
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
