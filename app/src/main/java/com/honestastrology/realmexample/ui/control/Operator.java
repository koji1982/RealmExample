package com.honestastrology.realmexample.ui.control;

import android.content.Context;

import java.util.Iterator;

import io.realm.RealmObject;

public interface Operator {
    
    static Operator createOperator(Context context){
        return new DBOperator(context);
    }
    
    void toAsync();
    
    void toSync();
    
    void create(RealmObject realmObject);
    
    <E extends RealmObject> Iterator<E> readAll(Class<E> clazz);
    
    void update(RealmObject realmObject);
    
    void delete(RealmObject realmObject);
    
    void closeAll();
    
}
