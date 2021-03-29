package com.logora.logora_android.view_holders;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logora.logora_android.R;
import com.logora.logora_android.TextWrapper;
import com.logora.logora_android.adapters.UserIconListAdapter;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.models.DebateBox;

import org.json.JSONObject;

import java.util.List;

public class ArgumentViewHolder extends ListViewHolder {
    ImageView shareButtonView;


    public ArgumentViewHolder(View itemView, Context context) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
        shareButtonView = view.findViewById(R.id.argument_share_button);
    }


    @Override
    public void updateWithObject(Object object) {
        Argument argument = (Argument) object;
        shareButtonView.setOnClickListener(v -> {
            Log.i("INFO", "Clicked on share argument");
        });
    }
}