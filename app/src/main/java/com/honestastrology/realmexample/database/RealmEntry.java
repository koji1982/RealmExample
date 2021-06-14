package com.honestastrology.realmexample.database;

import java.util.Iterator;

import io.realm.RealmObject;

public interface RealmEntry {
    
    public boolean isIterable();
    
    public <E extends RealmObject> Iterator<E> getIterator();
    
}
