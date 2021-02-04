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

        Button navbarButtonView = fragmentView.findViewById(R.id.index_button);
        TextView loginLinkView = fragmentView.findViewById(R.id.login_link_button);

        navbarButtonView.setBackgroundColor(Color.parseColor(getPrimaryColor()));
        loginLinkView.setTextColor(Color.parseColor(getPrimaryColor()));

        navbarButtonView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                router.setCurrentRoute("myRoute");
            }
        });
        return fragmentView;
    }

    private String getPrimaryColor() {
        SharedPreferences sharedPreferences =  this.getActivity().getSharedPreferences("logora_settings", 0);
        return sharedPreferences.getString("callPrimaryColor", "default");
    }
}