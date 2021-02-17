package com.logora.logora_android;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.logora.logora_android.view_models.DebateShowViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * A {@link Fragment} subclass containing the debate space search page.
 */
public class SearchFragment extends Fragment {
    private String debateSlug;

    public SearchFragment() {
        super(R.layout.fragment_debate);
    }

    public SearchFragment(String debateSlug) {
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