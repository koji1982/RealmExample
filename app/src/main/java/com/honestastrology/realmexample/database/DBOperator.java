package com.honestastrology.realmexample.database;

import android.content.Context;

import com.honestastrology.realmexample.ui.view.LayoutSwitcher;

import java.util.Iterator;

import io.realm.RealmObject;

public interface DBOperator {
    
    public static DBOperator createSimpleOperator(Context context,
                                                  LayoutSwitcher entryViewCallback,
                                                  String  asyncFileName,
                                                  String  syncId){
        return new CRUDOperator(context, entryViewCallback, asyncFileName, syncId);
    }
    
    public boolean isNull();
    
    public ConnectType getCurrentConnect();
    
    public void toSync();
    
    public void toAsync();
    
    public <E extends RealmObject>
    Number getMaxPrimaryNumber(Class<E> clazz, String primaryFieldName);
    
    //新しいRealmObjectを作る場合は一意の@PrimaryKeyを保持していなければならない
    public void create(RealmObject realmObject);
    
    public <E extends RealmObject> E getRealmObject(Class<E> clazz, String fieldName, int id); 
    
    public <E extends RealmObject> Iterator<E> readAll(Class<E> clazz);
    
    public void update(RealmObject realmObject);
    
    public void delete(RealmObject realmObject);
    
    public void closeAll();
    
}
