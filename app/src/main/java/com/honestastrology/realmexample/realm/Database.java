package com.honestastrology.realmexample.realm;

import android.content.Context;

public interface Database {
    
    static Database initAsync(Context context){
        return new AsyncDatabase(context);
    }
    
    static Database initSync(Context context){
        return new SyncDatabase(context);
    }
    
    void create();
    
    void read();
    
    void update();
    
    void delete();
    
    void close();
    
}
