package com.honestastrology.realmexample.database;

import androidx.annotation.NonNull;

/** エラー発生時にエラーメッセージを受け取るためのコールバック */
public interface DBErrorCallback {
    
    public void onError(@NonNull String message);
    
}
