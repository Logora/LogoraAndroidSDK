package com.logora.logora_android.views;

import android.content.Context;
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
        searchSubmit = this.findViewById(R.id.search_submit);
        searchInput = this.findViewById(R.id.search_input);
        backIconView = this.findViewById(R.id.search_back_icon);

        backIconView.setOnClickListener(v -> {
            Log.i("INFO", "BACK SEARCH");
            this.setVisibility(GONE);
        });

        searchSubmit.setOnClickListener(v -> {
            String searchQuery = searchInput.getText().toString();
            HashMap<String, String> queryParams = new HashMap<>();
            queryParams.put("q", searchQuery);
            router.setCurrentRoute(Router.getRoute("SEARCH"), null, queryParams);
        });
    }

    private void setStyles() {

    }
}