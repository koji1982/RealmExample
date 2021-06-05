package com.honestastrology.realmexample.database;

import android.content.Context;

import com.honestastrology.realmexample.ui.view.LayoutSwitcher;

import java.util.Iterator;

import io.realm.RealmObject;

public interface DBAccessor {
    
    public static DBAccessor createAsync(LayoutSwitcher entryViewCallback,
                                         String         asyncFileName){
        return new AsyncAccessor( entryViewCallback, asyncFileName );
    }
    
    public static DBAccessor createSync(Context        context,
                                        LayoutSwitcher entryViewCallback,
                                        String         syncId){
        return new SyncAccessor( context, entryViewCallback, syncId );
    }
    
    public boolean isValid();
    
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
