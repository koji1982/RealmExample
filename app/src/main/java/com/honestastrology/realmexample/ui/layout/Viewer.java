package com.honestastrology.realmexample.ui.layout;

import java.util.Iterator;

import io.realm.RealmObject;

public interface Viewer<E extends RealmObject> {
    
    public void setContents(Iterator<E> contents);
    
    public E getSelectedContent();
    
}
