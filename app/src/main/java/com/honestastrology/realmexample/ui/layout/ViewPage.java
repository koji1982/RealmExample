package com.honestastrology.realmexample.ui.layout;

import java.util.Iterator;

import io.realm.RealmObject;

public interface ViewPage<E extends RealmObject> {
    
    public void showContent();
    
}
