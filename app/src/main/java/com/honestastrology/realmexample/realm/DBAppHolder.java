package com.honestastrology.realmexample.realm;

import android.util.Log;

import io.realm.BuildConfig;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

class DBAppHolder implements AppHolder{
    
    private static final String appID = "realmexample-hfplt";
    
    private App    _app;
    
    DBAppHolder(){
        
        AppConfiguration appConfiguration 
                = new AppConfiguration.Builder(appID)
                          .appName(BuildConfig.VERSION_NAME)
                          .appVersion(Integer.toString(BuildConfig.VERSION_CODE))
                          .build();
    
        App _app = new App(appConfiguration);
        
        Credentials credentials = Credentials.anonymous();
        
        App.Callback<User> callback = result -> {
            if (result.isSuccess()) {
                Log.v("QUICKSTART", "Successfully authenticated anonymously.");
                User user = _app.currentUser();
                // interact with realm using your user object here
            } else {
                Log.e("QUICKSTART", "Failed to log in. Error: " + result.getError());
            }
        };
        
    }
    
    @Override
    public App getApp(){
        return _app;
    }
    
}
