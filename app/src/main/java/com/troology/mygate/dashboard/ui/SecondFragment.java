package com.troology.mygate.dashboard.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.troology.mygate.R;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.UtilsMethods;

public class SecondFragment extends Fragment {

    TextView name;
    UserDetails details;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        InItView(view);
        return view;
    }

    private void InItView(View view) {
        name = view.findViewById(R.id.name);

        details = UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class);
        if (details!=null){
            name.setText(details.getName());
        }

    }

}
