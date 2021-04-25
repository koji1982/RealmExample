package com.honestastrology.realmexample.ui.content;

import android.app.Activity;

import java.util.Iterator;

import io.realm.RealmObject;

public interface Viewer<E extends RealmObject> {
    
    public static <E extends RealmObject>
    Viewer<E> create(Activity activity, Class<E> clazz){
        return new DBContentsViewer<>(activity, clazz);
    }
    
    public void setContents(Iterator<E> contents);
    
    public E getSelectedContent();
    
}
