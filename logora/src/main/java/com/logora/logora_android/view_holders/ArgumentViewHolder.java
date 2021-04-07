package com.logora.logora_android.view_holders;

import android.content.Context;
import android.view.View;
import com.logora.logora_android.R;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.views.ArgumentBox;

public class ArgumentViewHolder extends ListViewHolder {
    Context context;
    Debate debate;
    ArgumentBox argumentBox;

    public ArgumentViewHolder(View itemView, Context context, Debate debate) {
        super(itemView);
        this.context = context;
        this.debate = debate;
        argumentBox = itemView.findViewById(R.id.argument_box_container);
    }

    public void updateWithObject(Object object) {
        argumentBox.updateWithObject(object, debate, context);
    }
}