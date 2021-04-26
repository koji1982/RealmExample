package com.honestastrology.realmexample.dbaccess;

import io.realm.mongodb.App;

public interface AppHolder {
    
    static AppHolder createAppHolder(){
        return new DBAppHolder();
    }
    
    App getApp();
    
}
