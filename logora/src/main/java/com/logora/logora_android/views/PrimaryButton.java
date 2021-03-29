package com.logora.logora_android.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.logora.logora_android.R;
import com.logora.logora_android.utils.Settings;

public class PrimaryButton extends androidx.appcompat.widget.AppCompatButton {
    Settings settings = Settings.getInstance();
    private Context context;

    public PrimaryButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PrimaryButton);

        String textKey = a.getString(R.styleable.PrimaryButton_buttonTextKey);
        String textValue = settings.get("layout." + textKey);
        if(textValue != null) {
            this.setText(textValue);
        }
        a.recycle();

        setStyles();
    }

    @SuppressLint("ResourceType")
    private void setStyles() {
        String primaryColor = settings.get("theme.callPrimaryColor");
        this.setTextColor(Color.WHITE);

        LayerDrawable shape = (LayerDrawable) ContextCompat.getDrawable(this.context, R.drawable.button_primary_background);
        GradientDrawable gradientDrawable = (GradientDrawable) shape.findDrawableByLayerId(R.id.shape);
        gradientDrawable.setColor(Color.parseColor(primaryColor));
        this.setBackground(shape);
    }
}
