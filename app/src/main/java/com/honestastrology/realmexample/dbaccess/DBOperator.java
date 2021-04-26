package com.honestastrology.realmexample.dbaccess;

import android.content.Context;

import java.util.Iterator;

import io.realm.RealmObject;

public interface DBOperator {
    
    public static DBOperator createSimpleOperator(Context context){
        return new CRUDOperator(context);
    }
    
    public void toAsync();
    
    public void toSync();
    
    public <E extends RealmObject>
    Number getMaxPrimaryNumber(Class<E> clazz, String primaryFieldName);
    
    //新しいRealmObjectを作る場合は一意の@PrimaryKeyを保持していなければならない
    public void create(RealmObject realmObject);
    
    public <E extends RealmObject> Iterator<E> readAll(Class<E> clazz);
    
    public void update(RealmObject realmObject);
    
    public void delete(RealmObject realmObject);
    
    public void closeAll();
    
}
