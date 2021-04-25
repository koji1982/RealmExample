package com.honestastrology.realmexample.realm;

import android.content.Context;

import java.util.Iterator;

import io.realm.RealmObject;

public interface Database {
    
    public static Database createAsync(Context context){
        return new AsyncDatabase(context);
    }
    
    public static Database createSync(Context context, AppHolder appHolder){
        return new SyncDatabase(context, appHolder);
    }
    
    public void create(RealmObject realmObject);
    
    public <E extends RealmObject> Iterator<E> readAll(Class<E> clazz);
    
    public void update(RealmObject realmObject);
    
    public void delete(RealmObject realmObject);
    
    public void close();
    
}
