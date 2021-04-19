package com.honestastrology.realmexample.realm;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

class AsyncDatabase implements Database {
    
    private App   _app;
    private Realm _asyncRealm;
    private Realm _syncRealm;
    
    AsyncDatabase(Context context){
        Realm.init(context);
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
        
        String realmName = "My Project";
        RealmConfiguration config = new RealmConfiguration.Builder().name(realmName).build();
//      
        _asyncRealm = Realm.getInstance(config);
        addChangeListenerToRealm(_asyncRealm);
        FutureTask<String> Task = new FutureTask(new ExampleTask(), "test");
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(Task);
    }
    
    @Override
    public void create(){
        
    }
    
    @Override
    public void read(){
        
    }
    
    @Override
    public void update(){
        
    }
    
    @Override
    public void delete(){
        
    }
    
    @Override
    public void close(){
        _asyncRealm.close();
    }
    
    public void connectAsync(){
        String realmName = "My Project";
        RealmConfiguration config = new RealmConfiguration.Builder().name(realmName).build();
        Realm backgroundThreadRealm = Realm.getInstance(config);
    }
    
    public void connectSync(){
        SyncConfiguration config = new SyncConfiguration.Builder(_app.currentUser(), "|")
                                           .allowQueriesOnUiThread(true)
                                           .allowWritesOnUiThread(true)
                                           .build();
        Realm.getInstanceAsync(config, new Realm.Callback() {
            @Override
            public void onSuccess(Realm realm) {
                Log.v(
                        "EXAMPLE",
                        "Successfully opened a realm with reads and writes allowed on the UI thread."
                );
            }
        });
    }
    
    private void addChangeListenerToRealm(Realm realm) {
        // all Tasks in the realm
        RealmResults<Document> Tasks = _asyncRealm.where(Document.class).findAllAsync();
        Tasks.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Document>>() {
            @Override
            public void onChange(RealmResults<Document> collection, OrderedCollectionChangeSet changeSet) {
                // process deletions in reverse order if maintaining parallel data structures so indices don't change as you iterate
                OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                for (OrderedCollectionChangeSet.Range range : deletions) {
                    Log.v("QUICKSTART", "Deleted range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));
                }
                OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                for (OrderedCollectionChangeSet.Range range : insertions) {
                    Log.v("QUICKSTART", "Inserted range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));                            }
                OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                for (OrderedCollectionChangeSet.Range range : modifications) {
                    Log.v("QUICKSTART", "Updated range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));                            }
            }
        });
    }
    
}
