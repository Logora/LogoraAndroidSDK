package com.logora.logora_sdk.view_holders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.Argument;
import com.logora.logora_sdk.models.Debate;
import com.logora.logora_sdk.views.ArgumentBox;

public class UserMessagesViewHolder extends ListViewHolder {
    Context context;
    ArgumentBox argumentBox;
    Argument argument;
    TextView userMessageTitle;
    TextView argumentsCount;
    TextView participantsCount;
    TextView debateVote;
    LinearLayout userMessageContainer;

    public UserMessagesViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        userMessageContainer = itemView.findViewById(R.id.user_message_box_container);
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
            argument.setIsReply(false);
            userMessageTitle.setText(debate.getName());
            argumentBox.updateWithObject(object, debate, context);
            argumentsCount.setText(String.valueOf(debate.getArgumentsCount()));
            participantsCount.setText(String.valueOf(debate.getUsersCount()));
            String debateVoteText = debate.getVoteMaxPercentage() + " %" + " " + debate.getVoteMaxPosition();
            debateVote.setText(debateVoteText);

        }
    }
}
