package com.honestastrology.realmexample.realm;

import io.realm.mongodb.App;

public interface AppHolder {
    
    static AppHolder createAppHolder(){
        return new DBAppHolder();
    }
    
    App getApp();
    
}
