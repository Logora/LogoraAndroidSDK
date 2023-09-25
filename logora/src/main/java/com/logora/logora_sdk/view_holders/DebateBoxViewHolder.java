package com.logora.logora_sdk.view_holders;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.views.TextWrapper;
import com.logora.logora_sdk.adapters.UserIconListAdapter;
import com.logora.logora_sdk.models.DebateBox;

import org.json.JSONObject;

import java.util.List;

public class DebateBoxViewHolder extends ListViewHolder {
    private TextView debateNameView;
    private ImageView debateImageView;
    private TextView debateVoteView;
    private RecyclerView debateUserListView;
    private TextWrapper debateUserListEmpty;

    public DebateBoxViewHolder(View itemView, Context context) {
        super(itemView);
        findViews(itemView);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        debateUserListView.setLayoutManager(layoutManager);
    }

    private void findViews(View view) {
        debateNameView = view.findViewById(R.id.debate_box_name);
        debateImageView = view.findViewById(R.id.debate_box_image);
        debateVoteView = view.findViewById(R.id.debate_vote);
        debateUserListView = view.findViewById(R.id.debate_user_list);
        debateUserListEmpty = view.findViewById(R.id.debate_user_list_empty);
    }

    @Override
    public void updateWithObject(Object object) {
        DebateBox debateBox = (DebateBox) object;
        debateNameView.setText(debateBox.getName());
        Glide.with(debateImageView.getContext())
                .load(Uri.parse(debateBox.getImageUrl()))
                .into(debateImageView);
        debateImageView.setClipToOutline(true);
        String debateVote = debateBox.getVotePercentage() + " %" + " " + debateBox.getVotePosition();
        debateVoteView.setText(debateVote);
        List<JSONObject> userList = debateBox.getUserList();
        UserIconListAdapter userIconListAdapter = new UserIconListAdapter(userList);
        debateUserListView.setAdapter(userIconListAdapter);
        if (userList.size() == 0) {
            debateUserListView.setVisibility(View.GONE);
            debateUserListEmpty.setVisibility(View.VISIBLE);
        }
    }
}