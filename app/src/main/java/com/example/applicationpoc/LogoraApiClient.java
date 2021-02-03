package com.example.applicationpoc;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LogoraApiClient {
    RequestQueue queue;
    String apiUrl = "https://app.logora.fr/api/v1";
    String applicationName;
    String providerToken;

    public LogoraApiClient(String applicationName, Context context) {
        this.queue = Volley.newRequestQueue(context);
        this.applicationName = applicationName;
    }

    public void setProviderToken(String providerToken) {
        this.providerToken = providerToken;
    }

    public void getSettings(Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener) {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("shortname", this.applicationName);
        String route = "/settings";
        this.client_post(route, queryParams, null, listener, errorListener);
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
                Map<String, String>  params = new HashMap<String, String>();
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
                Map<String, String>  params = new HashMap<String, String>();
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
                Map<String, String>  params = new HashMap<String, String>();
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

    public String paramstoQueryString(HashMap<String, String> params) {
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
}
