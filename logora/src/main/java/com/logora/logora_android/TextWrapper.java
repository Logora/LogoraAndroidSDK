package com.logora.logora_android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.logora.logora_android.utils.Settings;

public class TextWrapper extends androidx.appcompat.widget.AppCompatTextView {
    Settings settings = Settings.getInstance();

    public TextWrapper(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextWrapper);

        String textKey = a.getString(R.styleable.TextWrapper_textKey);
        String textValue = settings.get("layout." + textKey);
        if(textValue != null) {
            this.setText(textValue);
        }
        a.recycle();
    }
}
