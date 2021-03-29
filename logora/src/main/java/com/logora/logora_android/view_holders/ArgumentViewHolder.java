package com.logora.logora_android.view_holders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.logora.logora_android.R;
import com.logora.logora_android.models.Argument;

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