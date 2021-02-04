package com.example.applicationpoc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.airbnb.paris.Paris;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavbarFragment extends Fragment {

    private Router router = Router.getInstance();
    public NavbarFragment() {
        super(R.layout.fragment_navbar);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_navbar, container, false);

        setNavbarTitle(fragmentView, getShortname());

        Button navbarButtonView = fragmentView.findViewById(R.id.navbar_button);
        Paris.styleBuilder(navbarButtonView)
                .textColor(Color.parseColor(getPrimaryColor()))
                .apply();

        navbarButtonView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                router.setCurrentRoute("myRoute");
            }
        });
        return fragmentView;
    }

    private void setNavbarTitle(View view, String navbarTitle) {
        TextView navbarTitleView = (TextView) view.findViewById(R.id.navbar_title);
        navbarTitleView.setText(navbarTitle);
    }

    private String getShortname() {
        SharedPreferences sharedPreferences =  this.getActivity().getSharedPreferences("logora_settings", 0);
        return sharedPreferences.getString("shortname", "default");
    }

    private String getPrimaryColor() {
        SharedPreferences sharedPreferences =  this.getActivity().getSharedPreferences("logora_settings", 0);
        return sharedPreferences.getString("callPrimaryColor", "default");
    }
}