package com.logora.logora_android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.logora.logora_android.utils.Settings;

public class PrimaryButton extends androidx.appcompat.widget.AppCompatButton {
    Settings settings = Settings.getInstance();

    public PrimaryButton(@NonNull Context context) {
        super(context);
    }

    public PrimaryButton(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrimaryButton(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }
}
