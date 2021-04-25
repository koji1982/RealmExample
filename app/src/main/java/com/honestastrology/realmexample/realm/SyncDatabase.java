package com.honestastrology.realmexample.realm;

import android.content.Context;
import android.util.Log;

import io.realm.RealmObject;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

class SyncDatabase implements Database {
    
    private App _app;
    
    SyncDatabase(Context context, AppHolder appHolder){
        
        String appID = "realmexample-hfplt";
        _app = new App(new AppConfiguration.Builder(appID).build());
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
        
        SyncConfiguration config = new SyncConfiguration.Builder(_app.currentUser(), ":")
                                           .allowQueriesOnUiThread(true)
                                           .allowWritesOnUiThread(true)
                                           .build();
    }
    
    @Override
    public void create(RealmObject realmObject){
        
    }
    
    @Override
    public void readAll(){
        
    }
    
    @Override
    public void update(RealmObject realmObject){
        
    }
    
    @Override
    public void delete(RealmObject realmObject){
        
    }
    
    @Override
    public void close(){
        
    }
}
