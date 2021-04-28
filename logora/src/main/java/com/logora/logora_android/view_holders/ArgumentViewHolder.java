package com.logora.logora_android.view_holders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import com.logora.logora_android.R;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.utils.Auth;
import com.logora.logora_android.views.ArgumentBox;

public class ArgumentViewHolder extends ListViewHolder {
    Context context;
    Debate debate;
    int depth;
    private final Auth authClient = Auth.getInstance();
    ArgumentBox argumentBox;

    public ArgumentViewHolder(View itemView, Context context, Debate debate, int depth) {
        super(itemView);
        this.context = context;
        this.debate = debate;
        this.depth = depth;
        argumentBox = itemView.findViewById(R.id.argument_box_container);
    }

    public void updateWithObject(Object object) {
        Argument argument = (Argument) object;
        String status = argument.getStatus();
        argumentBox.setDepth(this.depth);
        if(status.equals("accepted")) {
            argumentBox.updateWithObject(object, debate, context);
        } else if(status.equals("rejected") || status.equals("pending")) {
            if(authClient.getIsLoggedIn()) {
                if(argument.getAuthor().getId().intValue() == authClient.getCurrentUser().getId().intValue()) {
                    argumentBox.updateWithObject(object, debate, context);
                }
            } else {
                argumentBox.setVisibility(View.GONE);
            }
        }
    }
}