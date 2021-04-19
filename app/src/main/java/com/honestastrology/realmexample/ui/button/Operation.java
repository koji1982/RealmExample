package com.honestastrology.realmexample.ui.button;

import android.content.Context;

import com.honestastrology.realmexample.realm.Database;

public interface Operation {
    
    static void init(Context context){
        Database.initAsync(context);
    }
    
    static void close(){
        Database.close();
    }
    
    void sendRequest();
    
}
