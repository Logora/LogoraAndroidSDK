package com.logora.logora_sdk.utils;

import androidx.fragment.app.Fragment;

import com.logora.logora_sdk.DebateFragment;
import com.logora.logora_sdk.IndexFragment;
import com.logora.logora_sdk.NotificationFragment;
import com.logora.logora_sdk.SearchFragment;
import com.logora.logora_sdk.UserFragment;

import java.util.HashMap;

public class Router {
    private static final HashMap<String, Route> routes = new HashMap<>();
    private static Router instance = null;
    private RouteListener routeListener;
    private Route currentRoute;

    private static void defineRoutes() {
        Route indexRoute = new Route("INDEX", "/", null);
        Router.routes.put(indexRoute.getName(), indexRoute);

        HashMap<String, String> debateRouteParamDef = new HashMap<>();
        debateRouteParamDef.put("debateSlug", "");
        Route debateRoute = new Route("DEBATE", "/debat/:debateSlug", debateRouteParamDef);
        Router.routes.put(debateRoute.getName(), debateRoute);

        HashMap<String, String> userRouteParamDef = new HashMap<>();
        debateRouteParamDef.put("userSlug", "");
        Route userRoute = new Route("USER", "/utilisateur/:userSlug", userRouteParamDef);
        Router.routes.put(userRoute.getName(), userRoute);

        HashMap<String, String> searchRouteQueryParamDef = new HashMap<>();
        searchRouteQueryParamDef.put("q", "");
        Route searchRoute = new Route("SEARCH", "/recherche", searchRouteQueryParamDef);
        Router.routes.put(searchRoute.getName(), searchRoute);

        Route notificationRoute = new Route("NOTIFICATIONS", "/notifications", null);
        Router.routes.put(notificationRoute.getName(), notificationRoute);
    }

    public static Route getRoute(String routeName) {
        return routes.get(routeName);
    }

    public void setCurrentRoute(Route currentRoute, HashMap<String, String> params) {
        Route previousRoute = this.currentRoute;
        currentRoute.setParams(params);
        this.currentRoute = currentRoute;
        if (routeListener != null) routeListener.onRouteChange(previousRoute, this.currentRoute);
    }

    public Route getCurrentRoute() {
        return this.currentRoute;
    }

    public static Route parseRoute(String routeName, String routeParam) {
        Route route = Router.getRoute(routeName);
        HashMap<String, String> routeParams = new HashMap<>();
        switch (route.getName()) {
            case "DEBATE":
                routeParams.put("debateSlug", routeParam);
                break;
            case "USER":
                routeParams.put("userSlug", routeParam);
                break;
            case "SEARCH":
                routeParams.put("q", routeParam);
                break;
        }
        route.setParams(routeParams);
        return route;
    }

    public static Fragment getRouteFragment(Route route) {
        switch (route.getName()) {
            case "DEBATE":
                return new DebateFragment(route.getParams().get("debateSlug"));
            case "USER":
                return new UserFragment(route.getParams().get("userSlug"));
            case "SEARCH":
                return new SearchFragment(route.getParams().get("q"));
            case "NOTIFICATIONS":
                return new NotificationFragment();
            default:
                return new IndexFragment();
        }
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
        void onRouteChange(Route previousRoute, Route currentRoute);
    }
}
