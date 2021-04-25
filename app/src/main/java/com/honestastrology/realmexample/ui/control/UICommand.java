package com.honestastrology.realmexample.ui.control;

import com.honestastrology.realmexample.ui.content.Viewer;

import io.realm.RealmObject;

public interface UICommand<E extends RealmObject> {
    
    public int getUIId();
    
    public void execute(Viewer<E> viewer, Operator _dbOperator);
    
}
