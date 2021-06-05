package com.honestastrology.realmexample.ui.view;

import io.realm.RealmObject;

public interface ViewPage<E extends RealmObject> {
    
    public void showContent();
    
    public void updateContent();
    
}
