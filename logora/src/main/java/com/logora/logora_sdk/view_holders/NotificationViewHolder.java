package com.logora.logora_sdk.view_holders;

import android.content.res.Resources;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.BadgeNotification;
import com.logora.logora_sdk.models.GetVoteNotification;
import com.logora.logora_sdk.models.GroupFollowArgumentNotification;
import com.logora.logora_sdk.models.GroupReplyNotification;
import com.logora.logora_sdk.models.LevelUnlockNotification;
import com.logora.logora_sdk.models.Notification;
import com.logora.logora_sdk.utils.DateUtil;
import com.logora.logora_sdk.utils.LogoraApiClient;
import com.logora.logora_sdk.utils.Router;
import com.logora.logora_sdk.utils.Settings;

import org.json.JSONException;

import java.util.HashMap;

public class NotificationViewHolder extends ListViewHolder {
    private final Settings settings = Settings.getInstance();
    private final Router router = Router.getInstance();
    private final LogoraApiClient apiClient = LogoraApiClient.getInstance();
    TextView notificationContent;
    TextView notificationDate;
    ImageView notificationImage;
    RelativeLayout notificationContainer;

    public NotificationViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
        notificationContainer = view.findViewById(R.id.notification_container);
        notificationContent = view.findViewById(R.id.notification_main_content);
        notificationImage = view.findViewById(R.id.notification_image);
        notificationDate = view.findViewById(R.id.notification_date);
    }

    @Override
    public void updateWithObject(Object object) {
        Resources res = this.itemView.getContext().getResources();
        Notification notification = (Notification) object;
        Spanned content;
        if (!notification.getIsOpened()) {
            notificationContainer.setBackgroundColor(res.getColor(R.color.text_tertiary));
        }
        notificationDate.setText(DateUtil.getTimeAgo(notification.getPublishedDate()));
        switch (notification.getNotifyType()) {
            case "get_badge":
                BadgeNotification badgeNotification = (BadgeNotification) object;
                content = Html.fromHtml(res.getString(R.string.notifications_new_badge) + " " + "<b>" + badgeNotification.getSecondTarget().getTitle() + "</b>");
                notificationContent.setText(content);
                Glide.with(notificationImage.getContext())
                        .load(Uri.parse(badgeNotification.getImageUrl()))
                        .into(notificationImage);
                this.itemView.setOnClickListener(v -> {
                    this.apiClient.readNotification(response -> {
                        try {
                            boolean success = response.getBoolean("success");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace, notification.getId());
                    HashMap<String, String> routeParams = new HashMap<>();
                    routeParams.put("userSlug", badgeNotification.getActor().getHashId());
                    router.navigate(Router.getRoute("USER"), routeParams);
                });
                break;
            case "group_reply":
                GroupReplyNotification groupReplyNotification = (GroupReplyNotification) object;
                if (notification.getActorCount() > 1) {
                    content = Html.fromHtml("<b>" + groupReplyNotification.getActor().getFullName() + "</b>" + " et " + (notification.getActorCount() - 1) + " " + res.getString(R.string.notifications_multiple_answers) + " " + res.getString(R.string.notifications_subject) + " " + "<b>" + groupReplyNotification.getSecondTarget().getName() + "</b>");
                } else {
                    content = Html.fromHtml("<b>" + groupReplyNotification.getActor().getFullName() + "</b>" + " " + res.getString(R.string.notifications_answer) + " " + res.getString(R.string.notifications_subject) + " " + "<b>" + groupReplyNotification.getSecondTarget().getName() + "</b>");
                }
                notificationContent.setText(content);
                Glide.with(notificationImage.getContext())
                        .load(Uri.parse(groupReplyNotification.getImageUrl()))
                        .into(notificationImage);
                this.itemView.setOnClickListener(v -> {
                    HashMap<String, String> queryParams = new HashMap<>();
                    this.apiClient.readNotification(response -> {
                        try {
                            boolean success = response.getBoolean("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace, notification.getId());
                    HashMap<String, String> routeParams = new HashMap<>();
                    routeParams.put("debateSlug", groupReplyNotification.getSecondTarget().getSlug());
                    router.navigate(Router.getRoute("DEBATE"), routeParams);
                });
                break;
            case "level_unlock":
                LevelUnlockNotification levelUnlockNotification = (LevelUnlockNotification) object;
                Glide.with(notificationImage.getContext())
                        .load(Uri.parse(levelUnlockNotification.getImageUrl()))
                        .into(notificationImage);
                this.itemView.setOnClickListener(v -> {
                    HashMap<String, String> queryParams = new HashMap<>();
                    this.apiClient.readNotification(response -> {
                        try {
                            boolean success = response.getBoolean("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace, notification.getId());
                    HashMap<String, String> routeParams = new HashMap<>();
                    routeParams.put("userSlug", levelUnlockNotification.getTarget().getHashId());
                    router.navigate(Router.getRoute("USER"), routeParams);
                });
                break;
            case "get_vote":
                GetVoteNotification getVoteNotification = (GetVoteNotification) object;
                if (notification.getActorCount() == 2) {
                    content = Html.fromHtml("<b>" + getVoteNotification.getActor().getFullName() + "</b>" + " et " + (notification.getActorCount() - 1) + " " + res.getString(R.string.notifications_one_support) + " " + res.getString(R.string.notifications_subject_argument));

                } else if (notification.getActorCount() > 1) {
                    content = Html.fromHtml("<b>" + getVoteNotification.getActor().getFullName() + "</b>" + " et " + (notification.getActorCount() - 1) + " " + res.getString(R.string.notifications_multiple_support) + " " + res.getString(R.string.notifications_subject_argument));
                } else {
                    content = Html.fromHtml("<b>" + getVoteNotification.getActor().getFullName() + "</b>" + " " + res.getString(R.string.notifications_support) + " " + res.getString(R.string.notifications_subject_argument));
                }
                notificationContent.setText(content);
                Glide.with(notificationImage.getContext())
                        .load(Uri.parse(getVoteNotification.getImageUrl()))
                        .into(notificationImage);
                this.itemView.setOnClickListener(v -> {
                    HashMap<String, String> queryParams = new HashMap<>();
                    this.apiClient.readNotification(response -> {
                        try {
                            boolean success = response.getBoolean("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace, notification.getId());
                    HashMap<String, String> routeParams = new HashMap<>();
                    routeParams.put("debateSlug", getVoteNotification.getSecondTarget().getSlug());
                    router.navigate(Router.getRoute("DEBATE"), routeParams);
                });
                break;
            case "group_follow_argument":
                GroupFollowArgumentNotification groupFollowArgumentNotification = (GroupFollowArgumentNotification) object;
                if (notification.getActorCount() > 1) {
                    content = Html.fromHtml("<b>" + groupFollowArgumentNotification.getActor().getFullName() + "</b>" + " " + res.getString(R.string.notifications_participation) + " " + res.getString(R.string.notifications_subject_debate) + " " + "<b>" + groupFollowArgumentNotification.getSecondTarget().getName() + "</b>");
                } else {
                    content = Html.fromHtml("<b>" + groupFollowArgumentNotification.getActor().getFullName() + "</b>" + " et " + (notification.getActorCount() - 1) + " " + res.getString(R.string.notifications_multiple_participations) + " " + res.getString(R.string.notifications_subject_debate) + " " + "<b>" + groupFollowArgumentNotification.getSecondTarget().getName() + "</b>");
                }
                notificationContent.setText(content);
                this.itemView.setOnClickListener(v -> {
                    HashMap<String, String> queryParams = new HashMap<>();
                    this.apiClient.readNotification(response -> {
                        try {
                            boolean success = response.getBoolean("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace, notification.getId());
                    HashMap<String, String> routeParams = new HashMap<>();
                    routeParams.put("debateSlug", groupFollowArgumentNotification.getSecondTarget().getSlug());
                    router.navigate(Router.getRoute("DEBATE"), routeParams);
                });
                break;
            case "user_follow_level_unlock":
                this.itemView.setOnClickListener(v -> {
                    HashMap<String, String> queryParams = new HashMap<>();
                    this.apiClient.readNotification(response -> {
                        try {
                            boolean success = response.getBoolean("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace, notification.getId());
                    HashMap<String, String> routeParams = new HashMap<>();
                    routeParams.put("userSlug", notification.getActor().getHashId());
                    router.navigate(Router.getRoute("USER"), routeParams);
                });
                break;
            case "followed":
                content = Html.fromHtml("<b>" + notification.getActor().getFullName() + "</b>" + " " + res.getString(R.string.notifications_followed));
                notificationContent.setText(content);
                Glide.with(notificationImage.getContext())
                        .load(Uri.parse(notification.getActor().getImageUrl()))
                        .into(notificationImage);
                this.itemView.setOnClickListener(v -> {
                    HashMap<String, String> queryParams = new HashMap<>();
                    this.apiClient.readNotification(response -> {
                        try {
                            boolean success = response.getBoolean("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace, notification.getId());
                    HashMap<String, String> routeParams = new HashMap<>();
                    routeParams.put("userSlug", notification.getActor().getHashId());
                    router.navigate(Router.getRoute("USER"), routeParams);
                });
                break;
            case "group_invitation_new":
                this.itemView.setVisibility(View.GONE);
            default:
                content = Html.fromHtml("default");
                notificationContent.setText(content);
                break;
        }
    }
}