package com.logora.logora_android;

import java.util.HashMap;
import java.util.Map;

public class Route {
    private String name;
    private String path;
    private HashMap<String, String> paramDefs;
    private HashMap<String, String> queryParamDefs;

    public Route(String name, String path, HashMap<String, String> paramDefs, HashMap<String, String> queryParamDefs) {
        this.name = name;
        this.path = path;
        this.paramDefs = paramDefs;
        this.queryParamDefs = queryParamDefs;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public String toUrl(HashMap<String, String> params) {
        String queryString = this.getQueryString(params);
        return this.path + queryString;
    }

    private String getQueryString(HashMap<String, String> queryParams) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (Map.Entry<String, String> stringEntry : queryParamDefs.entrySet()) {
            if(index == 0) {
                sb.append('?');
            } else {
                sb.append('&');
            }
            sb.append(stringEntry.getKey()).append("=").append(queryParams.get(stringEntry.getKey()));
            index ++;
        }
        return sb.toString();
    }
}
