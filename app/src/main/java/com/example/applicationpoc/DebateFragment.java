package com.example.applicationpoc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebateFragment extends Fragment {
    private String debateSlug;

    public DebateFragment() {
        super(R.layout.fragment_debate);
    }

    public DebateFragment(String debateSlug) {
        super(R.layout.fragment_debate);
        this.debateSlug = debateSlug;
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressBar spinner = view.findViewById(R.id.debate_loader);
        TextView textView = view.findViewById(R.id.debate_header);
        spinner.setVisibility(View.VISIBLE);
        DebateShowViewModel model = new DebateShowViewModel();
        model.getDebate(this.debateSlug).observe(getViewLifecycleOwner(), debate -> {
            textView.setText(debate.getName());
            spinner.setVisibility(View.GONE);
        });
    }
}