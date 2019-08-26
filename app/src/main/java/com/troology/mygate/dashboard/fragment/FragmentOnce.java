package com.troology.mygate.dashboard.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.troology.mygate.R;
import com.troology.mygate.dashboard.model.UserMeetings;
import com.troology.mygate.dashboard.ui.Dashboard;
import com.troology.mygate.dashboard.ui.PopupActivity;
import com.troology.mygate.dashboard.ui.RequestAdapter;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOnce extends Fragment {

    TextView tv_date, tv_starttime, tv_endtime, tRemark;
    Button btn_submit;
    EditText et_remarks, name, mobile;
    ApartmentDetails details;
    String type;
    RelativeLayout parent;
    Loader loader;
    LinearLayout ll_name, time_layout;
    String start_time = "", end_time = "", formattedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_once, container, false);

        loader = new Loader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        tv_date = view.findViewById(R.id.oncedatepicker);
        tv_starttime = view.findViewById(R.id.oncetimepicker1);
        tv_endtime = view.findViewById(R.id.oncetimepicker2);
        btn_submit = view.findViewById(R.id.btnsubmitOnce);
        et_remarks = view.findViewById(R.id.etremarks_once);
        name = view.findViewById(R.id.name);
        mobile = view.findViewById(R.id.mobile);
        ll_name = view.findViewById(R.id.linlayname);
        time_layout = view.findViewById(R.id.time_layout);
        parent = view.findViewById(R.id.parent);
        tRemark = view.findViewById(R.id.tRemark);
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

        click();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(calendar.getTime());

        return view;
    }

    private void click() {

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        formattedDate = UtilsMethods.INSTANCE.mDate("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        tv_date.setText(UtilsMethods.INSTANCE.Date("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });

        /*start time picker*/
        tv_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        start_time = hourOfDay + ":" + minute + ":00";
                        tv_endtime.setText("Select");
                        tv_starttime.setText(UtilsMethods.INSTANCE.ShortTime(formattedDate + " " + start_time));

                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
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
                            end_time = selectedHour + ":" + selectedMinute + ":00";
                            if (checkTimings(start_time, end_time)) {
                                tv_endtime.setText(UtilsMethods.INSTANCE.ShortTime(formattedDate + " " + end_time));
                            } else {
                                tv_endtime.setText("Select");
                                UtilsMethods.INSTANCE.snackBar(getResources().getString(R.string.time_error), parent);
                            }
                        }
                    }, hour, minute, false);
                    mTimePicker.show();
                }
            }
        });


        if (RequestAdapter.edit) {

            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isValid()) {
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
                                object.addProperty("time_from", UtilsMethods.INSTANCE.DateTime(formattedDate + " " + start_time));
                                object.addProperty("time_to", UtilsMethods.INSTANCE.DateTime(formattedDate + " " + end_time));
                            }
                            if (type.equals("2")) {
                                object.addProperty("member_type", "Delivery");
                                object.addProperty("time_from", UtilsMethods.INSTANCE.DateTime(formattedDate + " " + start_time));
                                object.addProperty("time_to", UtilsMethods.INSTANCE.DateTime(formattedDate + " " + end_time));
                            }
                            if (type.equals("3")) {
                                object.addProperty("member_type", "Guest");
                                object.addProperty("time_from", "");
                                object.addProperty("time_to", formattedDate);
                            }
                            if (type.equals("1") || type.equals("2")) {
                                object.addProperty("name", "");
                                object.addProperty("mobile", "");
                            } else
                                {
                                object.addProperty("name", name.getText().toString().trim());
                                object.addProperty("mobile", mobile.getText().toString().trim());
                            }
                            object.addProperty("stay_days", "");
                            object.addProperty("remarks", et_remarks.getText().toString());
                            object.addProperty("activity_type", "1");
                            object.addProperty("request_by", "1");
                            object.addProperty("record_id", "" + PopupActivity.record_id);
                            object.addProperty("email", "");
                            object.addProperty("contact_per_uid", "");

                            UtilsMethods.INSTANCE.UpdateActivity(getActivity(), object, parent, loader);

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


        } else {

            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isValid()) {
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
                                object.addProperty("time_from", UtilsMethods.INSTANCE.DateTime(formattedDate + " " + start_time));
                                object.addProperty("time_to", UtilsMethods.INSTANCE.DateTime(formattedDate + " " + end_time));
                            }
                            if (type.equals("2")) {
                                object.addProperty("member_type", "Delivery");
                                object.addProperty("time_from", UtilsMethods.INSTANCE.DateTime(formattedDate + " " + start_time));
                                object.addProperty("time_to", UtilsMethods.INSTANCE.DateTime(formattedDate + " " + end_time));
                            }
                            if (type.equals("3")) {
                                object.addProperty("member_type", "Guest");
                                object.addProperty("time_from", "");
                                object.addProperty("time_to", formattedDate);
                            }
                            if (type.equals("1") || type.equals("2")) {
                                object.addProperty("name", "");
                                object.addProperty("mobile", "");
                            } else {
                                object.addProperty("name", name.getText().toString().trim());
                                object.addProperty("mobile", mobile.getText().toString().trim());
                            }
                            object.addProperty("stay_days", "");
                            object.addProperty("remarks", et_remarks.getText().toString());
                            object.addProperty("activity_type", "1");
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

    }

    private boolean checkTimings(String time, String endtime) {

        String pattern = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            return date1.before(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isValid() {

        if (type.equalsIgnoreCase("3")) {
            if (name.getText().toString().equalsIgnoreCase("")) {
                UtilsMethods.INSTANCE.snackBar("Name cannot be blank!", parent);
                return false;
            }

            if (mobile.getText().toString().length() != 10) {
                UtilsMethods.INSTANCE.snackBar("Please enter valid Mobile Number!", parent);
                return false;
            }
        } else {
            if (tv_starttime.getText().toString().equalsIgnoreCase("Select")) {
                UtilsMethods.INSTANCE.snackBar("Please select start time.", parent);
                return false;
            }

            if (tv_endtime.getText().toString().equalsIgnoreCase("Select")) {
                UtilsMethods.INSTANCE.snackBar("Please select end time.", parent);
                return false;
            }
            if (type.equalsIgnoreCase("1")) {
                if (et_remarks.getText().toString().equalsIgnoreCase("")) {
                    UtilsMethods.INSTANCE.snackBar("Cab no cannot be blank!", parent);
                    return false;
                }
            }
            if (type.equalsIgnoreCase("2")) {
                if (et_remarks.getText().toString().equalsIgnoreCase("")) {
                    UtilsMethods.INSTANCE.snackBar("Remark cannot be blank!", parent);
                    return false;
                }
            }

        }
        return true;
    }

}
