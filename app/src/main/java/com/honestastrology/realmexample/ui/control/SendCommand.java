package com.honestastrology.realmexample.ui.control;

import com.honestastrology.realmexample.database.DBOperator;

import io.realm.RealmObject;

public interface SendCommand<E extends RealmObject> {
    
    public void execute(DBOperator operator, E realmObject);
    
}
