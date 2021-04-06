package com.logora.logora_android.view_holders;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.logora.logora_android.R;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.utils.Auth;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Settings;
import com.logora.logora_android.views.ArgumentBox;
import com.logora.logora_android.views.ArgumentVote;
import com.logora.logora_android.views.PrimaryButton;

import org.json.JSONException;
import org.json.JSONObject;

public class ArgumentViewHolder extends ListViewHolder {
    Context context;
    Debate debate;
    ArgumentBox argumentBox;

    public ArgumentViewHolder(View itemView, Context context, Debate debate) {
        super(itemView);
        this.context = context;
        this.debate = debate;
        argumentBox = itemView.findViewById(R.id.argument_box_container);
    }

    @Override
    public void updateWithObject(Object object){
        argumentBox.updateWithObject(object, debate, context);
    }
}