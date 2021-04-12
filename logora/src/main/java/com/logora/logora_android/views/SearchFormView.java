package com.logora.logora_android.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.logora.logora_android.R;
import com.logora.logora_android.utils.Router;

import java.util.HashMap;

public class SearchFormView extends RelativeLayout {
    private final Router router = Router.getInstance();
    private ImageView backIconView;
    private EditText searchInput;
    private ImageView searchSubmit;

    public SearchFormView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public SearchFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchFormView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.search_form, this);
        findViews();

        backIconView.setOnClickListener(v -> {
            this.setVisibility(GONE);
        });

        searchSubmit.setOnClickListener(v -> {
            String searchQuery = searchInput.getText().toString();
            HashMap<String, String> routeParams = new HashMap<>();
            routeParams.put("q", searchQuery);
            router.setCurrentRoute(Router.getRoute("SEARCH"), routeParams);
        });
    }

    private void findViews() {
        searchSubmit = this.findViewById(R.id.search_submit);
        searchInput = this.findViewById(R.id.search_input);
        backIconView = this.findViewById(R.id.search_back_icon);
    }
}