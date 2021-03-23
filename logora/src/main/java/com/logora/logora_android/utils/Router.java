package com.logora.logora_android.utils;

import android.net.Uri;

import java.util.HashMap;

public class Router {
    private static final HashMap<String, Route> routes = new HashMap<>();
    private static Router instance = null;
    private RouteListener routeListener;
    private Route currentRoute;

    private static void defineRoutes() {
        Route indexRoute = new Route("INDEX", "/", null, null);
        Router.routes.put(indexRoute.getName(), indexRoute);

        HashMap<String, String> debateRouteParamDef = new HashMap<>();
        debateRouteParamDef.put("debateSlug", "");
        Route debateRoute = new Route("DEBATE", "/debat/:debateSlug", debateRouteParamDef, null);
        Router.routes.put(debateRoute.getName(), debateRoute);

        HashMap<String, String> userRouteParamDef = new HashMap<>();
        debateRouteParamDef.put("userSlug", "");
        Route userRoute = new Route("USER", "/utilisateur/:userSlug", userRouteParamDef, null);
        Router.routes.put(userRoute.getName(), userRoute);

        HashMap<String, String> searchRouteQueryParamDef = new HashMap<>();
        searchRouteQueryParamDef.put("q", "");
        Route searchRoute = new Route("SEARCH", "/recherche", null, searchRouteQueryParamDef);
        Router.routes.put(searchRoute.getName(), searchRoute);
    }

    public static Route getRoute(String routeName) {
        return routes.get(routeName);
    }

    public void setCurrentRoute(Route currentRoute, HashMap<String, String> params, HashMap<String, String> queryParams) {
        Route previousRoute = this.currentRoute;
        this.currentRoute = currentRoute;
        if (routeListener != null) routeListener.onRouteChange(previousRoute, this.currentRoute, params, queryParams);
    }

    public Route getCurrentRoute() {
        return this.currentRoute;
    }

    public static Router getInstance() {
        if(Router.instance == null) {
            Router.instance = new Router();
            Router.defineRoutes();
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
        void onRouteChange(Route previousRoute, Route currentRoute, HashMap<String, String> params, HashMap<String, String> queryParams);
    }
}
