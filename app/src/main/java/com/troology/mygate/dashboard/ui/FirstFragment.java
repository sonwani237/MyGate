package com.troology.mygate.dashboard.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.troology.mygate.R;

public class FirstFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        inItView(view);

        return view;
    }

    private void inItView(View view) {
        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == fab){
            startActivity(new Intent(getActivity(), CreateRequest.class));
        }
    }
}
