package com.honestastrology.realmexample.dbaccess;

import android.content.Context;

import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmObject;

class CRUDOperator implements DBOperator {
    
    private AppHolder  _appHolder;
    private Database   _asyncDB;
    private Database   _syncDB;
    
    private Database   _currentDatabase;
    
    CRUDOperator(Context context){
        Realm.init(context);
        _appHolder = AppHolder.createAppHolder();
        _asyncDB   = Database.createAsync(context);
//        _syncDB          = Database.initSync(context, _appHolder);
        
        _currentDatabase = _asyncDB;
    }
    
    @Override
    public void toAsync(){
        _currentDatabase = _asyncDB;
    }
    
    @Override
    public void toSync(){
        _currentDatabase = _syncDB;
    }
    
    @Override
    public void closeAll(){
        _asyncDB.close();
//        _syncDB.close();
    }
    
    @Override
    public <E extends RealmObject>
    Number getMaxPrimaryNumber(Class<E> clazz, String primaryKeyField){
        return _currentDatabase.getMaxPrimaryNumber(clazz, primaryKeyField);
    }
    
    @Override
    public void create(RealmObject realmObject){
        _currentDatabase.create(realmObject);
    }
    
    @Override
    public <E extends RealmObject> Iterator<E> readAll(Class<E> clazz){
        return _currentDatabase.readAll(clazz);
    }
    
    @Override
    public void update(RealmObject realmObject){
        _currentDatabase.update(realmObject);
    }
    
    @Override
    public void delete(RealmObject realmObject){
        _currentDatabase.delete(realmObject);
    }
    
}
