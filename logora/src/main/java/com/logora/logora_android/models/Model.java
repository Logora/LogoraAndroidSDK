package com.logora.logora_android.models;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Model {
    public static Model objectFromJson(JSONObject jsonObject) {
        return (Model) (new Object());
    };
}