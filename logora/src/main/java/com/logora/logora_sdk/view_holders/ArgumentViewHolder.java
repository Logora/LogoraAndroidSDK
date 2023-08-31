package com.logora.logora_sdk.view_holders;

import android.content.Context;
import android.view.View;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.Argument;
import com.logora.logora_sdk.models.Debate;
import com.logora.logora_sdk.utils.Auth;
import com.logora.logora_sdk.views.ArgumentBox;

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
            argumentBox.setVisibility(View.GONE);
            if(authClient.getIsLoggedIn()) {
                if(argument.getAuthor().getId().intValue() == authClient.getCurrentUser().getId().intValue()) {
                    argumentBox.updateWithObject(object, debate, context);
                    argumentBox.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}