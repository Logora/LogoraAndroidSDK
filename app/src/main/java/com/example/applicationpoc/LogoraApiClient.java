package com.example.applicationpoc;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.tv.TvContract;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LogoraApiClient {
    RequestQueue queue;
    String apiUrl = "https://app.logora.fr/api/v1";
    String authUrl = "https://app.logora.fr/oauth";
    String userTokenKey = "logora_user_token";
    String userSessionKey = "logora_session";
    private String applicationName = null;
    private String providerToken;
    private Context context = null;
    private static LogoraApiClient instance = null;

    public LogoraApiClient() {}

    public LogoraApiClient(String applicationName, Context context) {
        this.queue = Volley.newRequestQueue(context);
        this.applicationName = applicationName;
        this.context = context;
    }

    public void setProviderToken(String providerToken) { this.providerToken = providerToken; }
    public String getProviderToken() { return this.providerToken; }

    public static LogoraApiClient getInstance(String applicationName, Context context) {
        if(LogoraApiClient.instance == null) {
            LogoraApiClient.instance = new LogoraApiClient(applicationName, context);
        }
        return LogoraApiClient.instance;
    }

    public static LogoraApiClient getInstance() {
        if(LogoraApiClient.instance == null) {
            LogoraApiClient.instance = new LogoraApiClient();
        }
        return LogoraApiClient.instance;
    }

    /* CLIENT METHODS */
    public void getSettings(Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener) {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("shortname", this.applicationName);
        String route = "/settings";
        this.client_post(route, queryParams, null, listener, errorListener);
    }

    /* AUTH METHODS */
    public void userAuth() {
        HashMap<String, String> bodyParams = new HashMap<>();
        bodyParams.put("grant_type", "assertion");
        bodyParams.put("assertion", "assertion");
        bodyParams.put("assertion_type", "oauth2");
        bodyParams.put("provider", this.applicationName);
        String requestUrl = this.authUrl + "/token";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                requestUrl, null,
                response -> {
                    Log.i("INFO", "LOGGING User");
                },
                error -> {
                    Log.i("ERROR", String.valueOf(error));
                })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            protected Map<String,String> getParams() {
                return bodyParams;
            }
        };
        this.queue.add(request);
    }

    public void userRefresh() {
        return;
    }

    private void setUserToken(HashMap<String, String> token) {
        Integer expiresAt = Integer.parseInt(Objects.requireNonNull(token.get("created_at"))) + Integer.parseInt(Objects.requireNonNull(token.get("expires_in")));
        token.put("expires_at", String.valueOf(expiresAt));
        this.setStorageItem(this.userTokenKey, new JSONObject(token));
    }

    private String getUserToken() {
        return "token";
    }

    /* GENERIC REQUEST METHODS */

    /* CLIENT METHODS */

    /* Asynchrounous GET method */
    private void client_get(String route, HashMap<String, String> params,
                     Response.Listener<JSONObject> listener,
                     Response.ErrorListener errorListener) {
        String paramsString = this.paramstoQueryString(params);
        String requestUrl = this.apiUrl + route + paramsString;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
            requestUrl, null, listener, errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        this.queue.add(request);
    }

    /* Asynchrounous POST method */
    private void client_post(String route, HashMap<String, String> queryParams,
                            HashMap<String, String> bodyParams,
                            Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener) {
        String paramsString = this.paramstoQueryString(queryParams);
        String requestUrl = this.apiUrl + route + paramsString;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                requestUrl, null, listener, errorListener)  {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            protected Map<String,String> getParams() {
                return bodyParams;
            }
        };
        this.queue.add(request);
    }

    /* Asynchrounous PATCH method */
    private void client_patch(String route,
                              HashMap<String, String> queryParams,
                              HashMap<String, String> bodyParams,
                              Response.Listener<JSONObject> listener,
                              Response.ErrorListener errorListener) {
        String paramsString = this.paramstoQueryString(queryParams);
        String requestUrl = this.apiUrl + route + paramsString;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH,
                requestUrl, null, listener, errorListener)  {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            protected Map<String,String> getParams() {
                return bodyParams;
            }
        };
        this.queue.add(request);
    }

    /* Asynchrounous DELETE method */
    private void client_delete(String route, HashMap<String, String> queryParams,
                        Response.Listener<JSONObject> listener,
                        Response.ErrorListener errorListener) {
        String paramsString = this.paramstoQueryString(queryParams);
        String requestUrl = this.apiUrl + route + paramsString;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,
                requestUrl, null, listener, errorListener)  {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        this.queue.add(request);
    }

    private HashMap<String, String> getUserAuthorizationHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + this.getUserToken());
        return headers;
    }

    private String paramstoQueryString(HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (Map.Entry<String, String> stringEntry : params.entrySet()) {
            if(index == 0) {
                sb.append('?');
            } else {
                sb.append('&');
            }
            sb.append(stringEntry.getKey()).append("=").append(stringEntry.getValue());
            index ++;
        }
        return sb.toString();
    }

    /* STORAGE FUNCTIONS */
    private void setStorageItem(String key, JSONObject value) {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("logora_settings", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value.toString());
        editor.apply();
    }

    private JSONObject getStorageItem(String key) {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("logora_settings", 0);
        String item = sharedPreferences.getString(key, "");
        try {
            JSONObject value = new JSONObject(item);
            return value;
        } catch (JSONException e) {
            return null;
        }
    }
}
