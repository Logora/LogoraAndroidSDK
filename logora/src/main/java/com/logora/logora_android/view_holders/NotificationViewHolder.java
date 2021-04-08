package com.logora.logora_android.view_holders;

import android.content.res.Resources;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.logora.logora_android.R;
import com.logora.logora_android.models.BadgeBox;
import com.logora.logora_android.models.BadgeNotification;
import com.logora.logora_android.models.GetVoteNotification;
import com.logora.logora_android.models.GroupFollowArgumentNotification;
import com.logora.logora_android.models.GroupReplyNotification;
import com.logora.logora_android.models.LevelUnlockNotification;
import com.logora.logora_android.models.Notification;

import java.util.logging.Level;

public class NotificationViewHolder extends ListViewHolder {
    TextView notificationContent;
    ImageView notificationImage;

    public NotificationViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view){
        notificationContent = view.findViewById(R.id.notification_main_content);
        notificationImage = view.findViewById(R.id.notification_image);
    }

    @Override
    public void updateWithObject(Object object) {
        Resources res = this.itemView.getContext().getResources();
        Notification notification = (Notification) object;
        Spanned content;

        switch(notification.getNotifyType()){
            case "get_badge":
                BadgeNotification badgeNotification = (BadgeNotification) object;
                content = Html.fromHtml(res.getString(R.string.notifications_new_badge) + " " + "<b>" + badgeNotification.getSecondTarget().getTitle() + "</b>");
                notificationContent.setText(content);
                Glide.with(notificationImage.getContext())
                        .load(Uri.parse(badgeNotification.getImageUrl()))
                        .into(notificationImage);
                break;
            case "group_reply":
                GroupReplyNotification groupReplyNotification = (GroupReplyNotification) object;
                if(notification.getActorCount() > 1) {
                    content = Html.fromHtml("<b>" + groupReplyNotification.getActor().getFullName() + "</b>" + " et " + String.valueOf(notification.getActorCount() - 1) + " " + res.getString(R.string.notifications_multiple_answers) + " " + res.getString(R.string.notifications_subject) + " " + "<b>" + groupReplyNotification.getSecondTarget().getName() + "</b>");
                } else {
                    content = Html.fromHtml(res.getString(R.string.notifications_answer) + res.getString(R.string.notifications_subject) + " " + "<b>" +groupReplyNotification.getSecondTarget().getName() + "</b>");
                }
                notificationContent.setText(content);
                Glide.with(notificationImage.getContext())
                        .load(Uri.parse(groupReplyNotification.getImageUrl()))
                        .into(notificationImage);
                break;
            case "level_unlock":
                LevelUnlockNotification levelUnlockNotification = (LevelUnlockNotification) object;
                content = Html.fromHtml("level_unlock");
                notificationContent.setText(content);
                break;
            case "get_vote":
                GetVoteNotification getVoteNotification = (GetVoteNotification) object;
                content = Html.fromHtml("get_vote");
                notificationContent.setText(content);
                break;
            case "group_follow_argument":
                GroupFollowArgumentNotification groupFollowArgumentNotification = (GroupFollowArgumentNotification) object;
                content = Html.fromHtml("group_follow_argument");
                notificationContent.setText(content);
                break;
            default:
                content = Html.fromHtml("default");
                notificationContent.setText(content);
                break;
        }
    }
}