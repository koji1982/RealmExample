package com.honestastrology.realmexample.dbaccess;

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
    
    public <E extends RealmObject>
    Number getMaxPrimaryNumber(Class<E> clazz, String primaryKeyField);
    //新しいRealmObjectを作る場合は一意の@PrimaryKeyを保持していなければならない
    public void create(RealmObject realmObject);
    
    public <E extends RealmObject> Iterator<E> readAll(Class<E> clazz);
    
    public void update(RealmObject realmObject);
    
    public void delete(RealmObject realmObject);
    
    public void close();
    
}
