package com.honestastrology.realmexample.database;

import io.realm.mongodb.sync.SyncConfiguration;

interface ConnectionCallback {
    
    void setConnectedConfig(SyncConfiguration config);
    
}
