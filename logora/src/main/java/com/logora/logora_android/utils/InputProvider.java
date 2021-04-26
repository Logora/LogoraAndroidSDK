package com.logora.logora_android.utils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class InputProvider {
    private static InputProvider instance = null;
    public static Map<Integer, Integer> userPositions;
    static {
        userPositions = new HashMap<>();
    }

    public static InputProvider getInstance() {
        if(InputProvider.instance == null) {
            InputProvider.instance = new InputProvider();
        }
        return InputProvider.instance;
    }

    public void addUserPosition(Integer debateId, Integer positionId) {
        this.userPositions.put(debateId, positionId);
    }

    public void removeUserPosition(Integer debateId) {
        this.userPositions.remove(debateId);
    }

    public Map<Integer, Integer> getUserPositions() {
        return this.userPositions;
    }
}
