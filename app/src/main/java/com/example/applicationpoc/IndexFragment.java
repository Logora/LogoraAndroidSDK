package com.example.applicationpoc;

import android.os.Bundle;
import android.telephony.RadioAccessSpecifier;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.applicationpoc.model.GroupBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment {
    private Router router = Router.getInstance();
    private ListView listView;

    public IndexFragment() {
        super(R.layout.fragment_index);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.trending_debates_list);
        ProgressBar spinner = view.findViewById(R.id.trending_debates_loader);
        spinner.setVisibility(View.VISIBLE);
        GroupBoxListViewModel model = new GroupBoxListViewModel();
        model.getGroupBoxList().observe(getViewLifecycleOwner(), groupBoxList -> {
            GroupBoxListAdapter adapter = new GroupBoxListAdapter(this.getActivity(), groupBoxList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView,
                                        View view, int position, long id) {
                    GroupBox groupBox = (GroupBox) adapterView.getItemAtPosition(position);
                    Log.i("INFO", groupBox.getName());
                    HashMap<String, String> routeParams = new HashMap<>();
                    routeParams.put("debateSlug", groupBox.getSlug());
                    router.setCurrentRoute(Router.getRoute("DEBATE"), routeParams, null);
                }
            });

            spinner.setVisibility(View.GONE);
        });
    }
}