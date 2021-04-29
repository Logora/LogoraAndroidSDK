package com.logora.logora_sdk.utils;

import com.logora.logora_sdk.models.Argument;

import java.util.HashMap;
import java.util.Map;

public class InputProvider {
    private static InputProvider instance = null;
    public static Map<Integer, Integer> userPositions;
    static {
        userPositions = new HashMap<>();
    }
    private InputProviderUpdateListener listener;
    public Argument updateArgument;
    public Argument removeArgument;

    public static InputProvider getInstance() {
        if(InputProvider.instance == null) {
            InputProvider.instance = new InputProvider();
        }
        return InputProvider.instance;
    }

    public void setListener(InputProviderUpdateListener listener) { this.listener = listener; }

    public void addUserPosition(Integer debateId, Integer positionId) { this.userPositions.put(debateId, positionId); }

    public void setUpdateArgument(Argument argument) {
        this.updateArgument = argument;
        listener.onInputProviderUpdate();
    }

    public Argument getUpdateArgument() { return this.updateArgument; }

    public void removeUpdateArgument() { this.updateArgument = null; }

    public void setRemoveArgument(Argument argument) {
        this.removeArgument = argument;
        listener.onInputProviderUpdate();
    }

    public Argument getRemoveArgument() { return this.removeArgument; }

    public void removeRemoveArgument() { this.removeArgument = null; }

    public void removeUserPosition(Integer debateId) {
        this.userPositions.remove(debateId);
    }

    public Map<Integer, Integer> getUserPositions() {
        return this.userPositions;
    }

    public interface InputProviderUpdateListener {
        void onInputProviderUpdate();
    }
}
