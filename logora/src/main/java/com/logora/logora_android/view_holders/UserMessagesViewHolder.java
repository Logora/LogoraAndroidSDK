package com.logora.logora_android.view_holders;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.logora.logora_android.R;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.models.UserBox;
import com.logora.logora_android.views.ArgumentBox;

public class UserMessagesViewHolder extends ListViewHolder {
    Context context;
    ArgumentBox argumentBox;
    Argument argument;
    TextView userMessageTitle;
    TextView argumentsCount;
    TextView participantsCount;
    TextView debateVote;

    public UserMessagesViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        userMessageTitle = itemView.findViewById(R.id.user_message_group_title);
        argumentBox = itemView.findViewById(R.id.argument_box_container);
        argumentsCount = itemView.findViewById(R.id.user_messages_participants_count);
        participantsCount = itemView.findViewById(R.id.user_messages_messages_count);
        debateVote = itemView.findViewById(R.id.user_messages_debate_vote);
    }

    public void updateWithObject(Object object) {
        argument = (Argument) object;
        Debate debate = argument.getDebate();
        if (debate != null) {
            userMessageTitle.setText(debate.getName());
            argumentBox.updateWithObject(object, debate, context);
            argumentsCount.setText(String.valueOf(debate.getArgumentsCount()));
            participantsCount.setText(String.valueOf(debate.getUsersCount()));
            String debateVoteText = debate.getVotePercentage() + " %" + " " + debate.getVotePosition();
            debateVote.setText(debateVoteText);
        }
    }
}
