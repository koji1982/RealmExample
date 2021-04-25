package com.honestastrology.realmexample.ui.content;

import android.widget.ListAdapter;

import java.util.Iterator;

import io.realm.RealmObject;

public interface ListConverter<E extends RealmObject> {
    
    public static <E extends RealmObject>
    ListConverter<E> create(Class<E> clazz){
        return 
    }
    
    public ListAdapter convert(Iterator<E> iterator);
    
}
