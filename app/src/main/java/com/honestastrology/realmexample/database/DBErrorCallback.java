package com.honestastrology.realmexample.database;

import androidx.annotation.NonNull;

public interface DBErrorCallback {
    
    public void onError(@NonNull String message);
    
}
