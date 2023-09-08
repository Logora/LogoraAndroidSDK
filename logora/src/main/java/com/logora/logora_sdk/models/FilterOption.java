package com.logora.logora_sdk.models;

public class FilterOption extends Model {
    private final String name;
    private final String apiName;
    private final String value;
    private final String textKey;

    public FilterOption(final String name, final String apiName, final String value, final String textKey) {
        this.name = name;
        this.apiName = apiName;
        this.value = value;
        this.textKey = textKey;
    }

    public String getName() {
        return this.name;
    }

    public String getApiName() {
        return this.apiName;
    }

    public String getValue() {
        return this.value;
    }

    public String getTextKey() {
        return this.textKey;
    }
}