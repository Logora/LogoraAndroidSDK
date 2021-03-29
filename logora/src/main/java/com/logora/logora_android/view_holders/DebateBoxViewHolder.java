package com.logora.logora_android.view_holders;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logora.logora_android.R;
import com.logora.logora_android.views.TextWrapper;
import com.logora.logora_android.adapters.UserIconListAdapter;
import com.logora.logora_android.models.DebateBox;

import org.json.JSONObject;

import java.util.List;

public class DebateBoxViewHolder extends ListViewHolder {
    DebateBox debateBox;
    TextView debateNameView;
    ImageView debateImageView;
    TextView debateVoteView;
    RecyclerView debateUserListView;
    TextWrapper debateUserListEmpty;

    public DebateBoxViewHolder(View itemView, Context context) {
        super(itemView);
        debateNameView = itemView.findViewById(R.id.debate_box_name);
        debateImageView = itemView.findViewById(R.id.debate_box_image);
        debateVoteView = itemView.findViewById(R.id.debate_vote);
        debateUserListView = itemView.findViewById(R.id.debate_user_list);
        debateUserListEmpty = itemView.findViewById(R.id.debate_user_list_empty);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        debateUserListView.setLayoutManager(layoutManager);
    }


    @Override
    public void updateWithObject(Object object) {
        DebateBox debateBox = (DebateBox) object;
        this.debateBox = debateBox;
        debateNameView.setText(debateBox.getName());
        Glide.with(debateImageView.getContext())
                .load(Uri.parse(debateBox.getImageUrl()))
                .into(debateImageView);

        String debateVote = debateBox.getVotePercentage() + " %" + " " + debateBox.getVotePosition();
        debateVoteView.setText(debateVote);

        UserIconListAdapter userListAdapter = new UserIconListAdapter();
        List<JSONObject> userList = debateBox.getUserList();
        if(userList.size() == 0) {
            debateUserListView.setVisibility(View.GONE);
            debateUserListEmpty.setVisibility(View.VISIBLE);
        } else {
            userListAdapter.setItems(debateBox.getUserList());
            debateUserListView.setAdapter(userListAdapter);
        }
    }
}