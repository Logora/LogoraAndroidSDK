package com.logora.logora_android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LogoraApiClient {
    RequestQueue queue;
    private final String apiUrl = "https://staging.logora.fr/api/v1";
    private final String authUrl = "https://staging.logora.fr/oauth";
    private String userTokenKey = "logora_user_token";
    private String userSessionKey = "logora_session";
    private String applicationName = null;
    private String authAssertion = null;
    private String providerToken = null;
    private Context context = null;
    private static LogoraApiClient instance = null;

    public LogoraApiClient() {}

    public LogoraApiClient(String applicationName, String authAssertion, Context context) {
        this.queue = Volley.newRequestQueue(context);
        this.applicationName = applicationName;
        this.authAssertion = authAssertion;
        this.context = context;
    }

    public void setProviderToken(String providerToken) { this.providerToken = providerToken; }
    public String getProviderToken() { return this.providerToken; }

    public void setAuthAssertion(String authAssertion) { this.authAssertion = authAssertion; }
    public String getAuthAssertion() { return this.authAssertion; }

    public static LogoraApiClient getInstance(String applicationName, String authAssertion, Context context) {
        if(LogoraApiClient.instance == null) {
            LogoraApiClient.instance = new LogoraApiClient(applicationName, authAssertion, context);
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
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("shortname", this.applicationName);
        String route = "/settings";
        this.client_post(route, queryParams, null, listener, errorListener);
    }

    public void getList(Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener, String resourceName, String resourceType, Integer page,
                                   Integer perPage, String sort, Integer outset, String query,
                                    HashMap<String, String> extraArguments) {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("page", String.valueOf(page));
        queryParams.put("per_page", String.valueOf(perPage));
        queryParams.put("sort", sort);
        queryParams.put("outset", String.valueOf(outset));
        queryParams.put("query", String.valueOf(query));
        if(extraArguments != null) {
            queryParams.putAll(extraArguments);
        }
        String route = "/" + resourceName;
        if(resourceType.equals("USER")) {
            this.user_get(route, queryParams, listener, errorListener);
        } else {
            this.client_get(route, queryParams, listener, errorListener);
        }
    }

    public void getDebate(Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener, String slug) {
        HashMap<String, String> queryParams = new HashMap<>();
        String route = "/groups/" + slug;
        this.client_get(route, queryParams, listener, errorListener);
    }

    public void getUser(Response.Listener<JSONObject> listener,
                          Response.ErrorListener errorListener, String slug) {
        HashMap<String, String> queryParams = new HashMap<>();
        String route = "/users/" + slug;
        this.client_get(route, queryParams, listener, errorListener);
    }

    /* USER METHODS */
    public void getCurrentUser(Response.Listener<JSONObject> listener,
                               Response.ErrorListener errorListener) {
        HashMap<String, String> queryParams = new HashMap<>();
        String route = "/me";
        this.user_get(route, queryParams, listener, errorListener);
    }

    public void getGroupVote(Response.Listener<JSONObject> listener,
                                Response.ErrorListener errorListener, Integer groupId) {
        HashMap<String, String> queryParams = new HashMap<>();
        String route = "/users/group/" + groupId + "/vote";
        this.user_get(route, queryParams, listener, errorListener);
    }

    public void createVote(Response.Listener<JSONObject> listener,
                           Response.ErrorListener errorListener,
                           Integer voteableId, String voteableType, Integer positionId) {
        HashMap<String, String> queryParams = new HashMap<>();
        HashMap<String, String> bodyParams = new HashMap<>();
        bodyParams.put("voteable_id", String.valueOf(voteableId));
        bodyParams.put("voteable_type", voteableType);
        if (positionId != null) {
            bodyParams.put("position_id", String.valueOf(positionId));
        }
        String route = "/votes";
        this.user_post(route, queryParams, bodyParams, listener, errorListener);
    }

    public void updateVote(Response.Listener<JSONObject> listener,
                           Response.ErrorListener errorListener,
                           Integer voteId, Integer positionId) {
        HashMap<String, String> queryParams = new HashMap<>();
        HashMap<String, String> bodyParams = new HashMap<>();
        bodyParams.put("position_id", String.valueOf(positionId));
        String route = "/votes/" + voteId;
        this.user_patch(route, queryParams, bodyParams, listener, errorListener);
    }

    public void deleteVote(Response.Listener<JSONObject> listener,
               Response.ErrorListener errorListener, Integer voteId) {
        HashMap<String, String> queryParams = new HashMap<>();
        String route = "/votes/" + voteId;
        this.user_delete(route, queryParams, listener, errorListener);
    }

    public void getDebateFollow(Response.Listener<JSONObject> listener,
                               Response.ErrorListener errorListener, Integer groupId) {
        HashMap<String, String> queryParams = new HashMap<>();
        String route = "/group_followings/" + groupId;
        this.user_get(route, queryParams, listener, errorListener);
    }

    public void followDebate(Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener, String debateSlug) {
        HashMap<String, String> queryParams = new HashMap<>();
        String route = "/groups/" + debateSlug + "/follow";
        this.user_post(route, queryParams, null, listener, errorListener);
    }

    public void unfollowDebate(Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener, String debateSlug) {
        HashMap<String, String> queryParams = new HashMap<>();
        String route = "/groups/" + debateSlug + "/unfollow";
        this.user_post(route, queryParams, null, listener, errorListener);
    }

    public void createReport(Response.Listener<JSONObject> listener,
                           Response.ErrorListener errorListener,
                           Integer reportableId, String reportableType, String classification, String description) {
        HashMap<String, String> queryParams = new HashMap<>();
        HashMap<String, String> bodyParams = new HashMap<>();
        bodyParams.put("reportable_id", String.valueOf(reportableId));
        bodyParams.put("reportable_type", reportableType);
        bodyParams.put("classification", classification);
        bodyParams.put("description", description);
        String route = "/reports";
        this.user_post(route, queryParams, bodyParams, listener, errorListener);
    }

    /* AUTH METHODS */
    public void userAuth(Response.Listener<JSONObject> listener,
                         Response.ErrorListener errorListener) {
        HashMap<String, String> bodyParams = new HashMap<>();
        bodyParams.put("grant_type", "assertion");
        bodyParams.put("assertion", this.authAssertion);
        bodyParams.put("assertion_type", "signature_jwt");
        bodyParams.put("provider", this.applicationName);
        String requestUrl = this.authUrl + "/token";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                requestUrl, new JSONObject(bodyParams), listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        this.queue.add(request);
    }

    public void user_refresh(Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener) {
        HashMap<String, String> bodyParams = new HashMap<>();
        bodyParams.put("refresh_token", this.getUserRefreshToken());
        bodyParams.put("grant_type", "refresh_token");
        String requestUrl = this.authUrl + "/token";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, requestUrl,
                new JSONObject(bodyParams), listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        this.queue.add(request);
    }

    /* GENERIC REQUEST METHODS */

    /* CLIENT METHODS */
    private void client_get(String route, HashMap<String, String> queryParams,
                     Response.Listener<JSONObject> listener,
                     Response.ErrorListener errorListener) {
        queryParams.put("provider_token", this.providerToken);
        String paramsString = this.paramstoQueryString(queryParams);
        String requestUrl = this.apiUrl + route + paramsString;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
            requestUrl, null, listener, errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Origin", "https://logora.fr");
                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("data", new JSONObject(jsonString));
                    jsonResponse.put("headers", new JSONObject(response.headers));

                    return Response.success(jsonResponse, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        this.queue.add(request);
    }

    private void client_post(String route, HashMap<String, String> queryParams,
                            HashMap<String, String> bodyParams,
                            Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener) {
        String paramsString = this.paramstoQueryString(queryParams);
        String requestUrl = this.apiUrl + route + paramsString;
        JSONObject bodyJson = new JSONObject();
        if(bodyParams != null) {
            bodyJson = new JSONObject(bodyParams);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                requestUrl, bodyJson, listener, errorListener)  {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Origin", "https://logora.fr");
                return params;
            }
        };
        this.queue.add(request);
    }

    /* USER METHODS */
    private void user_get(String route, HashMap<String, String> params,
                            Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener) {
        String paramsString = this.paramstoQueryString(params);
        String requestUrl = this.apiUrl + route + paramsString;

        String userAuthorizationHeader = this.getUserAuthorizationHeader();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                requestUrl, null, listener, errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", userAuthorizationHeader);
                return params;
            }
        };

        this.addToQueueWithRefresh(request);
    }

    private void user_post(String route, HashMap<String, String> params,
                          HashMap<String, String> bodyParams,
                          Response.Listener<JSONObject> listener,
                          Response.ErrorListener errorListener) {
        String paramsString = this.paramstoQueryString(params);
        String requestUrl = this.apiUrl + route + paramsString;
        JSONObject bodyJson = new JSONObject();
        if(bodyParams != null) {
            bodyJson = new JSONObject(bodyParams);
        }

        String userAuthorizationHeader = this.getUserAuthorizationHeader();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                requestUrl, bodyJson, listener, errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", userAuthorizationHeader);
                return params;
            }
        };

        this.addToQueueWithRefresh(request);
    }

    private void user_patch(String route, HashMap<String, String> params,
                           HashMap<String, String> bodyParams,
                           Response.Listener<JSONObject> listener,
                           Response.ErrorListener errorListener) {
        String paramsString = this.paramstoQueryString(params);
        String requestUrl = this.apiUrl + route + paramsString;
        JSONObject bodyJson = new JSONObject();
        if(bodyParams != null) {
            bodyJson = new JSONObject(bodyParams);
        }

        String userAuthorizationHeader = this.getUserAuthorizationHeader();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                requestUrl, bodyJson, listener, errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", userAuthorizationHeader);
                return params;
            }
        };
        this.addToQueueWithRefresh(request);
    }

    private void user_delete(String route, HashMap<String, String> params,
                           Response.Listener<JSONObject> listener,
                           Response.ErrorListener errorListener) {
        String paramsString = this.paramstoQueryString(params);
        String requestUrl = this.apiUrl + route + paramsString;

        String userAuthorizationHeader = this.getUserAuthorizationHeader();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,
                requestUrl, null, listener, errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", userAuthorizationHeader);
                return params;
            }
        };
        this.addToQueueWithRefresh(request);
    }

    private void addToQueueWithRefresh(JsonObjectRequest request) {
        if(this.isTokenExpired()) {
            this.user_refresh(response -> {
                this.setUserToken(response);
                this.queue.add(request);
            }, error -> {});
        } else {
            this.queue.add(request);
        }
    }

    private String getUserAuthorizationHeader() {
        return "Bearer " + this.getUserToken();
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

    /* TOKEN FUNCTIONS */
    public void setUserToken(JSONObject token) {
        try {
            Integer expiresAt = Integer.parseInt(Objects.requireNonNull(token.getString("created_at"))) + Integer.parseInt(Objects.requireNonNull(token.getString("expires_in")));
            token.put("expires_at", String.valueOf(expiresAt));
            this.setStorageItem(this.userTokenKey, token);
        } catch(JSONException e) {
            Log.i("ERROR", "Token object parsing error.");
        }
    }

    public String getUserToken() {
        JSONObject tokenObject = this.getUserTokenObject();
        try {
            return tokenObject.getString("access_token");
        } catch(JSONException e) {
            return null;
        }
    }

    public String getUserRefreshToken() {
        JSONObject tokenObject = this.getUserTokenObject();
        try {
            return tokenObject.getString("refresh_token");
        } catch(JSONException e) {
            return null;
        }
    }

    public JSONObject getUserTokenObject() {
        return this.getStorageItem(this.userTokenKey);
    }

    public void deleteUserToken() {
        this.deleteStorageItem(this.userTokenKey);
    }

    public Boolean isTokenExpired() {
        JSONObject tokenObject = this.getUserTokenObject();
        try {
            int expiresAt = Integer.parseInt(tokenObject.getString("expires_at"));
            Date expiresDate = new Date((long) expiresAt * 1000);
            return expiresDate.before(new Date());
        } catch(JSONException e) {
            return true;
        }
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

    private void deleteStorageItem(String key) {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("logora_settings", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }
}
