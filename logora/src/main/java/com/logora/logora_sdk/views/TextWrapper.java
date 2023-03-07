package com.logora.logora_sdk.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.logora.logora_sdk.R;
import com.logora.logora_sdk.utils.Settings;

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
