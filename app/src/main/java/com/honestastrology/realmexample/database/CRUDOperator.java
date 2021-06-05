package com.honestastrology.realmexample.database;

import android.content.Context;

import com.honestastrology.realmexample.ui.view.LayoutSwitcher;

import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.mongodb.sync.SyncConfiguration;

/**
 *
 * */

class CRUDOperator implements DBOperator, ConnectionCallback {
    
    private DBAccessor        _asyncDB;
    private DBAccessor        _syncDB;
    private SyncConfiguration _connectedConfig;
    
    private DBAccessor _currentDBAccessor;
    
    CRUDOperator(Context        context,
                 LayoutSwitcher entryViewCallback,
                 String         asyncFileName,
                 String         syncId              ){
        
        Realm.init( context );
        _asyncDB   = DBAccessor.createAsync( entryViewCallback, asyncFileName );
        _syncDB    = DBAccessor.createSync( context,
                                            entryViewCallback,
                                            syncId );
        
        _currentDBAccessor = _syncDB;
    }
    
    @Override
    public void setConnectedConfig(SyncConfiguration config){
        this._connectedConfig = config;
    }
    
    @Override
    public boolean isNull(){
        return _currentDBAccessor.isValid();
    }
    
    @Override
    public ConnectType getCurrentConnect(){
        return _currentDBAccessor.getConnectType();
    }
    
    @Override
    public void toSync(){
        _currentDBAccessor = _syncDB;
    }
    
    @Override
    public void toAsync(){
        _currentDBAccessor = _asyncDB;
    }
    
    @Override
    public void closeAll(){
        _asyncDB.close();
        _syncDB.close();
    }
    
    @Override
    public <E extends RealmObject>
    Number getMaxPrimaryNumber(Class<E> clazz, String primaryKeyField){
        return _currentDBAccessor.getMaxPrimaryNumber(clazz, primaryKeyField);
    }
    
    @Override
    public void create(RealmObject realmObject){
        _currentDBAccessor.create(realmObject);
    }
    
    @Override
    public <E extends RealmObject> E getRealmObject(Class<E> clazz,
                                                    String   fieldName,
                                                    int      id){
        return _currentDBAccessor.getRealmObject(clazz, fieldName, id);
    }
    
    @Override
    public <E extends RealmObject> Iterator<E> readAll(Class<E> clazz){
        return _currentDBAccessor.readAll(clazz);
    }
    
    @Override
    public void update(RealmObject realmObject){
        _currentDBAccessor.update(realmObject);
    }
    
    @Override
    public void delete(RealmObject realmObject){
        _currentDBAccessor.delete(realmObject);
    }
    
}
