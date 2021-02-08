package com.example.applicationpoc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.applicationpoc.model.DebateBox;

import java.util.List;

public class DebateBoxListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DebateBox> debateBoxItems;

    public DebateBoxListAdapter(Activity activity, List<DebateBox> debateBoxItems) {
        this.activity = activity;
        this.debateBoxItems = debateBoxItems;
    }

    @Override
    public int getCount() {
        return debateBoxItems.size();
    }

    @Override
    public DebateBox getItem(int location) {
        return debateBoxItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.group_box, null);

        ImageView image = (ImageView) convertView.findViewById(R.id.group_box_image);
        TextView name = (TextView) convertView.findViewById(R.id.group_box_name);

        DebateBox debateBox = debateBoxItems.get(position);

        Glide.with(this.activity.getApplicationContext())
                .load(Uri.parse(debateBox.getImageUrl()))
                .into(image);
        name.setText(debateBox.getName());
        return convertView;
    }
}
