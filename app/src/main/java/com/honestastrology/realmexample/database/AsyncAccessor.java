package com.honestastrology.realmexample.database;

import java.util.Iterator;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

/**
 * 端末の記憶領域を使用した非同期DBアクセスを行うクラス。
 * In-Memoryで生成した場合、In-Memory中に保存された記録は
 * アプリ終了時に破棄される。　*/
class AsyncAccessor implements RealmAccessor {
    
    private Realm _asyncRealm;
    
    AsyncAccessor(String asyncFileName){
        RealmConfiguration config = new RealmConfiguration.Builder()
                                            .name( asyncFileName )
                                            .allowQueriesOnUiThread(true)
                                            .allowWritesOnUiThread(true)
                                            .build();
        _asyncRealm = Realm.getInstance( config );
    }
    
    AsyncAccessor(Persistence persistence){
        if( persistence != Persistence.TEMPORARY ){
            throw new IllegalArgumentException();
        }
        RealmConfiguration config = new RealmConfiguration.Builder()
                                            .inMemory()
                                            .name( IN_MEMORY_FILE_NAME )
                                            .allowQueriesOnUiThread(true)
                                            .allowWritesOnUiThread(true)
                                            .build();
        _asyncRealm = Realm.getInstance( config );
    }
    
    @Override
    public boolean isNull(){
        return ( _asyncRealm == null );
    }
    
    @Override
    public ConnectType getConnectType(){
        return RealmConnectType.ASYNC;
    }
    
    @Override
    public <E extends RealmObject>
    Number getMaxPrimaryNumber(Class<E> clazz, String primaryKeyField){
        return _asyncRealm.where( clazz ).max( primaryKeyField );
    }
    
    @Override
    public void create(RealmObject realmObject){
        _asyncRealm.executeTransaction (
                realmTransaction -> realmTransaction.insert( realmObject ) );
    }
    
    @Override
    public <E extends RealmObject> E getRealmObject(Class<E> clazz,
                                                    String   fieldName,
                                                    int      id){
        return _asyncRealm.where( clazz ).equalTo( fieldName, id ).findFirst();
    }
    
    @Override
    public <E extends RealmObject> Iterator<E> readAll(Class<E> clazz){
        return _asyncRealm.where( clazz ).findAll().iterator();
    }
    
    @Override
    public void update(RealmObject realmObject){
        _asyncRealm.executeTransaction(
                realmTransaction -> realmTransaction.insertOrUpdate( realmObject ) );
    }
    
    @Override
    public void delete(RealmObject realmObject){
        _asyncRealm.executeTransaction(
                realmTransaction -> realmObject.deleteFromRealm() );
    }
    
    @Override
    public void close(){
        _asyncRealm.close();
    }
}
