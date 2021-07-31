package com.honestastrology.realmexample.database;

import android.content.Context;

import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmObject;

/** シンプルなデータベース操作(データの生成、読込、更新、削除)を行うクラス */
class BasicOperator implements DBOperator {
    
    private static final String SYNC_ERROR_MESSAGE  = "SYNC DB is not available.";
    private static final String ASYNC_ERROR_MESSAGE = "ASYNC DB is not available.";
    
    private RealmAccessor _asyncDB;
    private RealmAccessor _syncDB;
    
    private RealmAccessor _currentRealmAccessor;
    private DBErrorCallback   _errorCallback;
    
    BasicOperator(Context         context,
                  String          asyncFileName,
                  String          syncId,
                  DBErrorCallback errorCallback ){
        
        Realm.init( context );
        _syncDB    = RealmAccessor.createSync( syncId, errorCallback );
        _asyncDB   = RealmAccessor.createAsync( asyncFileName );
        
        _errorCallback     = errorCallback;
        _currentRealmAccessor = _syncDB.isNull() ?
                                           _asyncDB : _syncDB;
    }
    
    //In-MemoryでcurrentにAsyncAccessorを入れる場合のコンストラクタ
    BasicOperator(Context     context,
                  Persistence persistence){
        Realm.init( context );
        
        _asyncDB = new AsyncAccessor( persistence );
        _syncDB  = new SyncAccessor( persistence );
        _currentRealmAccessor = _asyncDB;
    }
    
    @Override
    public boolean isNull(){
        return _currentRealmAccessor.isNull();
    }
    
    @Override
    public ConnectType getCurrentConnect(){
        return _currentRealmAccessor.getConnectType();
    }
    
    @Override
    public void toSync(){
        if( _syncDB.isNull() ){
            _errorCallback.onError( SYNC_ERROR_MESSAGE );
            return;
        }
        _currentRealmAccessor = _syncDB;
    }
    
    @Override
    public void toAsync(){
        if( _asyncDB.isNull() ){
            _errorCallback.onError( ASYNC_ERROR_MESSAGE );
            return;
        }
        _currentRealmAccessor = _asyncDB;
    }
    
    @Override
    public void closeAll(){
        _asyncDB.close();
        _syncDB.close();
    }
    
    @Override
    public <E extends RealmObject>
    Number getMaxPrimaryNumber(Class<E> clazz, String primaryKeyField){
        return _currentRealmAccessor.getMaxPrimaryNumber(clazz, primaryKeyField);
    }
    
    @Override
    public void create(RealmObject realmObject){
        _currentRealmAccessor.create(realmObject);
    }
    
    @Override
    public <E extends RealmObject> E getRealmObject(Class<E> clazz,
                                                    String   fieldName,
                                                    int      id){
        return _currentRealmAccessor.getRealmObject(clazz, fieldName, id);
    }
    
    @Override
    public <E extends RealmObject> Iterator<E> readAll(Class<E> clazz){
        return _currentRealmAccessor.readAll(clazz);
    }
    
    @Override
    public void update(RealmObject realmObject){
        _currentRealmAccessor.update(realmObject);
    }
    
    @Override
    public void delete(RealmObject realmObject){
        _currentRealmAccessor.delete(realmObject);
    }
}
