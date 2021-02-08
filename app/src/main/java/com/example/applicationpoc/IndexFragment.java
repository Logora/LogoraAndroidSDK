package com.example.applicationpoc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.example.applicationpoc.model.DebateBox;

import java.util.HashMap;

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
        DebateBoxListViewModel model = new DebateBoxListViewModel();
        model.getGroupBoxList().observe(getViewLifecycleOwner(), groupBoxList -> {
            DebateBoxListAdapter adapter = new DebateBoxListAdapter(this.getActivity(), groupBoxList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView,
                                        View view, int position, long id) {
                    DebateBox debateBox = (DebateBox) adapterView.getItemAtPosition(position);
                    Log.i("INFO", debateBox.getName());
                    HashMap<String, String> routeParams = new HashMap<>();
                    routeParams.put("debateSlug", debateBox.getSlug());
                    router.setCurrentRoute(Router.getRoute("DEBATE"), routeParams, null);
                }
            });

            spinner.setVisibility(View.GONE);
        });
    }
}