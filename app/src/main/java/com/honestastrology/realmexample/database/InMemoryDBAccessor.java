package com.honestastrology.realmexample.database;

import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

class InMemoryDBAccessor implements DBAccessor {
    
    private Realm _inMemoryRealm;
    
    InMemoryDBAccessor(){
        RealmConfiguration config = new RealmConfiguration.Builder()
                                            .inMemory()
                                            .name("test-realm")
                                            .allowQueriesOnUiThread( true )
                                            .allowWritesOnUiThread( true )
                                            .build();
        _inMemoryRealm = Realm.getInstance( config );
    }
    
    @Override
    public boolean isNull(){
        return ( _inMemoryRealm == null );
    }
    
    @Override
    public ConnectType getConnectType(){
        return RealmConnectType.IN_MEMORY;
    }
    
    @Override
    public <E extends RealmObject>
    Number getMaxPrimaryNumber(Class<E> clazz, String primaryKeyField){
        return _inMemoryRealm.where( clazz ).max( primaryKeyField );
    }
    
    @Override
    public void create(RealmObject realmObject){
        _inMemoryRealm.executeTransaction (
                realmTransaction -> realmTransaction.insert( realmObject ) );
    }
    
    @Override
    public <E extends RealmObject> E getRealmObject(Class<E> clazz,
                                                    String   fieldName,
                                                    int      id){
        return _inMemoryRealm.where( clazz ).equalTo( fieldName, id ).findFirst();
    }
    
    @Override
    public <E extends RealmObject> Iterator<E> readAll(Class<E> clazz){
        return _inMemoryRealm.where( clazz ).findAll().iterator();
    }
    
    @Override
    public void update(RealmObject realmObject){
        _inMemoryRealm.executeTransaction(
                realmTransaction -> realmTransaction.insertOrUpdate( realmObject) );
    }
    
    @Override
    public void delete(RealmObject realmObject){
        _inMemoryRealm.executeTransaction(
                realmTransaction -> realmObject.deleteFromRealm() );
    }
    
    @Override
    public void close(){
        _inMemoryRealm.close();
    }
}
