package com.honestastrology.realmexample.database;

import android.content.Context;
import android.util.Log;

import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.mongodb.sync.SyncConfiguration;

/**
 *
 * */

class BasicOperator implements DBOperator {
    
    private static final String SYNC_ERROR_MESSAGE  = "SYNC DB is not available.";
    private static final String ASYNC_ERROR_MESSAGE = "ASYNC DB is not available.";
    
    private DBAccessor        _asyncDB;
    private DBAccessor        _syncDB;
    
    private DBAccessor        _currentDBAccessor;
    private DBErrorCallback   _errorCallback;
    
    BasicOperator(Context         context,
                  String          asyncFileName,
                  String          syncId,
                  DBErrorCallback errorCallback ){
        
        Realm.init( context );
        _asyncDB   = DBAccessor.createAsync( asyncFileName );
        _syncDB    = DBAccessor.createSync( syncId, errorCallback );
        
        _errorCallback     = errorCallback;
        _currentDBAccessor = _syncDB.isNull() ?
                                           _asyncDB : _syncDB;
    }
    
    //In-MemoryでRealmインスタンスを作成する場合のコンストラクタ
    BasicOperator(Context context,
                  String  syncId,
                  DBErrorCallback errorCallback){
        Realm.init( context );
//        _currentDBAccessor = new InMemoryDBAccessor();
        _errorCallback     = errorCallback;
        
//        _asyncDB = DBAccessor.getNullInstance();
//        _syncDB  = DBAccessor.getNullInstance();
        
        _asyncDB = new AsyncAccessor( RealmConnectType.IN_MEMORY );
        _syncDB  = new SyncAccessor(
                        syncId, errorCallback, RealmConnectType.IN_MEMORY);
        _currentDBAccessor = _syncDB;
    }
    
    @Override
    public boolean isNull(){
        return _currentDBAccessor.isNull();
    }
    
    @Override
    public ConnectType getCurrentConnect(){
        return _currentDBAccessor.getConnectType();
    }
    
    @Override
    public void toSync(){
        if( _syncDB.isNull() ){
            _errorCallback.onError( SYNC_ERROR_MESSAGE );
            return;
        }
        _currentDBAccessor = _syncDB;
    }
    
    @Override
    public void toAsync(){
        if( _asyncDB.isNull() ){
            _errorCallback.onError( ASYNC_ERROR_MESSAGE );
            return;
        }
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
