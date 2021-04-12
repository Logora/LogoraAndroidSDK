package com.logora.logora_android.utils;

import java.util.HashMap;
import java.util.Map;

public class Route {
    private String name;
    private String path;
    private HashMap<String, String> paramDefs;
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

    public HashMap<String,String> getParams() { return this.params; }

    public void setParams(HashMap<String,String> params) { this.params = params; }
}
