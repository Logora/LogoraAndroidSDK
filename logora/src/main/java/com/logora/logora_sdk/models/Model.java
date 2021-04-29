package com.logora.logora_sdk.models;

import org.json.JSONObject;

public abstract class Model {
    public static Model objectFromJson(JSONObject jsonObject) {
        return (Model) (new Object());
    };
}