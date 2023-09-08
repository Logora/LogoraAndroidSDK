package com.logora.logora_sdk.utils;

import java.util.HashMap;

public class Route {
    private final String name;
    private final String path;
    private final HashMap<String, String> paramDefs;
    private HashMap<String, String> params;

    public Route(String name, String path, HashMap<String, String> paramDefs) {
        this.name = name;
        this.path = path;
        this.paramDefs = paramDefs;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public HashMap<String, String> getParams() {
        return this.params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }
}
