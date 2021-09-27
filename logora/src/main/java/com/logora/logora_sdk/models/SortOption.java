package com.logora.logora_sdk.models;

public class SortOption extends Model {
    private String name;
    private String value;
    private String textKey;

    public SortOption(final String name, final String value, final String textKey) {
        this.name = name;
        this.value = value;
        this.textKey = textKey;
    }

    public String getName() { return this.name; }
    public String getValue() { return this.value; }
    public String getTextKey() { return this.textKey; }
}