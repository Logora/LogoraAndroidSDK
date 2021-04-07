package com.logora.logora_android.view_holders;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.logora.logora_android.R;
import com.logora.logora_android.models.BadgeBox;
import com.logora.logora_android.models.Notification;

public class NotificationViewHolder extends ListViewHolder {
    TextView actorFullName;
    TextView notificationContent;
    Notification notification;

    public NotificationViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view){
        actorFullName = view.findViewById(R.id.notification_actor_full_name);
        notificationContent = view.findViewById(R.id.notification_content);
    }

    @Override
    public void updateWithObject(Object object) {
        Notification notification = (Notification) object;
        actorFullName.setText(notification.getActor().getFullName());
        // Switch content according to notify_type
    }
}