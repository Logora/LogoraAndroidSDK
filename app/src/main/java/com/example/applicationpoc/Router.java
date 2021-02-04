package com.example.applicationpoc;

import android.util.Log;

public class Router {
    public enum Routes {INDEX, DEBATE};

    private static Router instance = null;
    private RouteListener routeListener;
    private String currentRoute;

    public void setCurrentRoute(String currentRoute) {
        this.currentRoute = currentRoute;
        if (routeListener != null) routeListener.onRouteChange(currentRoute, currentRoute);
    }

    public String getCurrentRoute() {
        return this.currentRoute;
    }

    public static Router getInstance() {
        if(Router.instance == null) {
            Router.instance = new Router();
        }
        return Router.instance;
    }

    public RouteListener getListener() {
        return routeListener;
    }

    public void setListener(RouteListener routeListener) {
        this.routeListener = routeListener;
    }

    public interface RouteListener {
        void onRouteChange(String previousRoute, String currentRoute);
    }
}
