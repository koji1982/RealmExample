package com.honestastrology.realmexample.database;

import java.util.Iterator;

import io.realm.RealmObject;

public interface DBAccessor {
    
    public static final String IN_MEMORY_FILE_NAME = "in_memory_async";
    
    public static DBAccessor createAsync(String asyncFileName){
        return new AsyncAccessor( asyncFileName );
    }
    
    public static DBAccessor createSync(String          syncId,
                                        DBErrorCallback errorCallback){
        return new SyncAccessor( syncId, errorCallback );
    }
    
    public boolean isNull();
    
    public ConnectType getConnectType();
    
    public <E extends RealmObject>
    Number getMaxPrimaryNumber(Class<E> clazz, String primaryKeyField);
    //新しいRealmObjectを作る場合は一意の@PrimaryKeyを保持していなければならない
    public void create(RealmObject realmObject);
    
    //Primary keyがint値であることを前提としている
    //そうでない場合は別でgetメソッドを作る必要がある
    public <E extends RealmObject> E getRealmObject(Class<E> clazz, String fieldName, int primaryId);
    
    public <E extends RealmObject> Iterator<E> readAll(Class<E> clazz);
    
    public void update(RealmObject realmObject);
    
    public void delete(RealmObject realmObject);
    
    public void close();
    
}
