package com.example.applicationpoc;

import android.media.tv.TvContract;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.applicationpoc.model.GroupBox;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment {
    private List<GroupBox> groupBoxList = new ArrayList<GroupBox>();

    public IndexFragment() {
        super(R.layout.fragment_index);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogoraApiClient apiClient = LogoraApiClient.getInstance();
        apiClient.getTrendingDebates(
                response -> {
                    Log.i("INFO", String.valueOf(response));
                },
                error -> {
                    Log.i("ERROR", String.valueOf(error));
                }, 1, 10, "-created_at", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_index, container, false);
    }
}