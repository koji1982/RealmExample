package com.honestastrology.realmexample.database;

/** エラー発生時にエラーメッセージを受け取るためのコールバック */
public interface DBErrorCallback {
    
    public void onError(String message);
    
}
