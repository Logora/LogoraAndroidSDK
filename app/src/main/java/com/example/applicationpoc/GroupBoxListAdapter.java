package com.example.applicationpoc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.applicationpoc.model.GroupBox;

import java.util.List;

public class GroupBoxListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<GroupBox> groupBoxItems;

    public GroupBoxListAdapter(Activity activity, List<GroupBox> groupBoxItems) {
        this.activity = activity;
        this.groupBoxItems = groupBoxItems;
    }

    @Override
    public int getCount() {
        return groupBoxItems.size();
    }

    @Override
    public GroupBox getItem(int location) {
        return groupBoxItems.get(location);
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

        // getting movie data for the row
        GroupBox groupBox = groupBoxItems.get(position);

        Glide.with(this.activity.getApplicationContext())
                .load(Uri.parse(groupBox.getImageUrl()))
                .into(image);
        name.setText(groupBox.getName());
        return convertView;
    }
}
