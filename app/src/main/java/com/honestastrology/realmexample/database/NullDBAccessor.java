package com.honestastrology.realmexample.database;

import java.util.Collections;
import java.util.Iterator;

import io.realm.RealmObject;

enum NullDBAccessor implements DBAccessor {
    
    INSTANCE;
    
    static DBAccessor getInstance(){
        return INSTANCE;
    }
    
    public boolean isNull() {
        return true;
    }
    
    public ConnectType getConnectType(){
        return null;
    }
    
    public <E extends RealmObject>
    Number getMaxPrimaryNumber(Class<E> clazz, String primaryKeyField){
        return 0;
    }
    //新しいRealmObjectを作る場合は一意の@PrimaryKeyを保持していなければならない
    public void create(RealmObject realmObject){
        
    }
    
    //Primary keyがint値であることを前提としている
    //そうでない場合は別でgetメソッドを作る必要がある
    public <E extends RealmObject> E getRealmObject(Class<E> clazz, String fieldName, int primaryId){
        return null;
    }
    
    public <E extends RealmObject> Iterator<E> readAll(Class<E> clazz){
        return Collections.emptyIterator();
    }
    
    public void update(RealmObject realmObject){
    }
    
    public void delete(RealmObject realmObject){
    }
    
    public void close(){
    }
}
