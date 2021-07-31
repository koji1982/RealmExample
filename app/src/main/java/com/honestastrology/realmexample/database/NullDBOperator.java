package com.honestastrology.realmexample.database;

import java.util.Collections;
import java.util.Iterator;

import io.realm.RealmObject;

enum NullDBOperator implements DBOperator {
    
    INSTANCE;
    
    static DBOperator getInstance(){
        return INSTANCE;
    }
    
    @Override
    public boolean isNull(){
        return true;
    }
    
    @Override
    public ConnectType getCurrentConnect(){
        return RealmConnectType.NULL;
    }
    
    @Override
    public void toSync(){
        
    }
    
    @Override
    public void toAsync(){
        
    }
    
    @Override
    public <E extends RealmObject>
    Number getMaxPrimaryNumber(Class<E> clazz, String primaryFieldName){
        return null;
    }
    
    @Override
    public void create(RealmObject realmObject){
        
    }
    
    @Override
    public <E extends RealmObject> E getRealmObject(Class<E> clazz, String fieldName, int id){
        return null;
    }
    
    @Override
    public <E extends RealmObject> Iterator<E> readAll(Class<E> clazz){
        return Collections.emptyIterator();
    }
    
    @Override
    public void update(RealmObject realmObject){
        
    }
    
    @Override
    public void delete(RealmObject realmObject){
        
    }
    
    @Override
    public void closeAll(){
        
    }
}
